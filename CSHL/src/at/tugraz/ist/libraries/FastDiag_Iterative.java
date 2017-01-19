package at.tugraz.ist.libraries;

import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;

/**
 *
 * @author Michael Jeran
 */
public class FastDiag_Iterative {
    
    static enum STATE {CONSISTENT, SINGLETON, RUNTHROUGH};
    
    private static Solver masterSolver;
    
    public static List<Constraint> computeDiagnose(List<Constraint> C, List<Constraint> AC, Solver solver) {
        
        masterSolver = solver.duplicateModel();
        
        /*
         * if isEmpty(C) return 0;
         */        
        if (C.isEmpty()) { 
            return new ArrayList<>(); // empty list is equal to zero;            
        }
        
        /*
         * remove C from the solver
         */    
        for (Constraint c : C) {
            solver.unpost(c);
        }
        
        /*
         * if inconsistent(AC - C) return 0;
         */
        solver.getSearchLoop().reset();
        Boolean foundSolution = solver.findSolution();
        if (!foundSolution) {
            return new ArrayList<>(); // empty list is equal to zero;
        } 
                
        /*
         * Iterativ version
         */
        List<Constraint> c_1tmp = new ArrayList<>();
        List<Constraint> c_2tmp = new ArrayList<>();
        
        for (int i = 0; i < C.size() / 2; i++) {
            c_1tmp.add(C.get(i));
        }
        for (int i = C.size() / 2; i < C.size(); i++) {
            c_2tmp.add(C.get(i));
        }
        
        List<Constraint> ac_tmp = new ArrayList<>(AC);
        List<Constraint> c_tmp = new ArrayList<>(C);
        List<Constraint> delta = new ArrayList<>();
        
        Node initialNode = new Node(c_tmp, ac_tmp, c_1tmp, c_2tmp);
        
        List<Node> nodeList = new ArrayList<>();
        nodeList.add(initialNode);
        
        STATE rightState = STATE.RUNTHROUGH;
        while (!nodeList.isEmpty()) {
            
            if (rightState == STATE.RUNTHROUGH) {
                while (true) {
                    if (computeNodeLeft(delta, nodeList) != STATE.RUNTHROUGH) {
                        break;
                    }
                }
            }
            
            rightState = computeNodeRight(delta, nodeList);
            switch (rightState) {
                case RUNTHROUGH:
                    continue;
                default: {
                    nodeList.remove(nodeList.get(nodeList.size() - 1));
                }
            }
        }
        
        return delta;
    }
     
    private static STATE computeNodeLeft(List<Constraint> delta, List<Node> nodeList) {
        Node lastNode = nodeList.get(nodeList.size() - 1);
                
        List<Constraint> ac_tmpLocal = new ArrayList<>(lastNode.getAC());
        ac_tmpLocal.removeAll(lastNode.getC_1());

        List<Constraint> c_tmpLocal = new ArrayList<>(lastNode.getC_2());
        
        return computeNode(delta, nodeList, ac_tmpLocal, c_tmpLocal);
    }
    
    private static STATE computeNodeRight(List<Constraint> delta, List<Node> nodeList) {
        Node lastNode = nodeList.get(nodeList.size() - 1);
                
        List<Constraint> ac_tmpLocal = new ArrayList<>(lastNode.getAC());
        ac_tmpLocal.removeAll(delta);
        
        List<Constraint> c_tmpLocal = new ArrayList<>(lastNode.getC_1());

        return computeNode(delta, nodeList, ac_tmpLocal, c_tmpLocal);
    }
    
    private static STATE computeNode(List<Constraint> delta, List<Node> nodeList, List<Constraint> ac_tmpLocal, List<Constraint> c_tmpLocal) {
            
        Solver solver = masterSolver.duplicateModel();
        
        // solver must contains correct constraint set
        for (Constraint cSolver : solver.getCstrs()) {
            if (cSolver.getName().startsWith("c")) {
                boolean foundConstraint = false;
                for (Constraint cAC : ac_tmpLocal) {
                    if (cSolver.getName().equalsIgnoreCase(cAC.getName())) {
                        foundConstraint = true;
                        break;
                    }
                }
                if (!foundConstraint) {
                    solver.unpost(cSolver);
                }
            }
        }

        solver.getSearchLoop().reset();
        if (solver.findSolution()) {
            return STATE.CONSISTENT;
        }       

        if (c_tmpLocal.size() == 1) {
            delta.add(c_tmpLocal.get(0));
            return STATE.SINGLETON;
        }

        List<Constraint> c_1tmpLocal = new ArrayList<>();
        List<Constraint> c_2tmpLocal = new ArrayList<>();

        for (int i = 0; i < c_tmpLocal.size() / 2; i++) {
            c_1tmpLocal.add(c_tmpLocal.get(i));
        }
        for (int i = c_tmpLocal.size() / 2; i < c_tmpLocal.size(); i++) {
            c_2tmpLocal.add(c_tmpLocal.get(i));
        }

        // build node
        Node tmp = new Node(c_tmpLocal, ac_tmpLocal, c_1tmpLocal, c_2tmpLocal);
        nodeList.add(tmp);
        
        return STATE.RUNTHROUGH;
    }
}