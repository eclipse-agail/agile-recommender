package at.tugraz.ist.libraries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;

/**
 *
 * @author Michael Jeran
 */
public class FastDiag_Recursive {

    private static Solver masterSolver;
    
    /*
     * func FastDiag(C ⊆ AC, AC = {c_1 .. c_t}) : delta
     * @param List<Constraint> C contains all user requirement constraints
     * @param List<Constraint> AC contains all constraints
     * @param Solver solver contains all variables and AC constraints
     */
    public static List<Constraint> computeDiagnose(List<Constraint> C, List<Constraint> AC, Solver solver) {
                
        masterSolver = solver.getModel().getSolver();
        
        /*
         * if isEmpty(C) return 0;
         */        
        if (C.isEmpty()) { 
            return new ArrayList<Constraint>(); // empty list is equal to zero;            
        }
        
        /*
         * remove C from the solver
         */    
        for (Constraint c : C) {
            solver.getModel().unpost(c);
        }
        
        /*
         * if inconsistent(AC - C) return 0;
         */
        solver.reset();
        Boolean foundSolution = solver.solve();
        if (!foundSolution) {
            return new ArrayList<Constraint>(); // empty list is equal to zero;
        } else {
            /*
             * else return FD(0, C, AC);
             */  
            return FD(new ArrayList<Constraint>(), C, AC);
        }
    }
    
    /*
     * func FD(D, C = {c_1 .. c_q}, AC) : diagnosis delta
     * @param List<Constraint> D diagnosis 
     * @param List<Constraint> C constraint list
     * @param List<Constraint> AC constraint list
     * @param Solver solver contains everytime the actual AC
     */
    private static List<Constraint> FD(List<Constraint> D, List<Constraint> C, List<Constraint> AC) {
        
        FastDiag_Debug.debug("----------------------- FD CALLED -----------------------");
        
        FastDiag_Debug.debug(D, C, AC);    
            
        /*
         * if D != 0 and consistent(AC) return 0;
         */
        if (!D.isEmpty()) {
            //Solver solver = masterSolver.duplicateModel();
        	Solver solver = masterSolver.getModel().getSolver();
            for (Constraint cSolver : solver.getModel().getCstrs()) {
                if (cSolver.getName().startsWith("c")) {
                    boolean foundConstraint = false;
                    for (Constraint cAC : AC) {
                        if (cSolver.getName().equalsIgnoreCase(cAC.getName())) {
                            foundConstraint = true;
                            break;
                        }
                    }
                    if (!foundConstraint) {
                        solver.getModel().unpost(cSolver);
                    }
                }
            }
            FastDiag_Debug.debug(solver.getModel().getCstrs());
            
            solver.reset();
            FastDiag_Debug.debug(String.valueOf("starttime: " + System.currentTimeMillis()));
            
            long startTime = System.currentTimeMillis();
            while(true) {
                if (System.currentTimeMillis() >= startTime + 50) {
                    break;
                }
            }
            
            Boolean foundSolution = solver.solve();
            FastDiag_Debug.debug(String.valueOf("endtime:   " + System.currentTimeMillis()));
            FastDiag_Debug.debug(" foundSolution: " + foundSolution);
            if (foundSolution) {
            	FastDiag_Debug.debug("D != 0 && findSolution");
                return new ArrayList<Constraint>(); // empty list is equal to zero             
            }
        }
        
        /*
         * if singleton(C) return C;
         */
        if (C.size() == 1) {
            return C;            
        }
        
        /*
         * k = q/2;
         */        
        int k = C.size() / 2;
        
        /*
         * C_1 = {c_1 .. c_k}; 
         */
        List<Constraint> C_1 = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            C_1.add(C.get(i));            
        }
        
        /*
         * C_2 = {c_k+1 .. c_q};
         */
        List<Constraint> C_2 = new ArrayList<>();
        for (int i = k; i < C.size(); i++) {
            C_2.add(C.get(i));            
        }
        
        /*
         * compute AC - C_1
         */
        List<Constraint> ACminusC_1 = new ArrayList<>(AC);
        ACminusC_1.removeAll(C_1);
        
        /*
         * D_1 = FD(C_1, C_2, AC - C_1);
         */
        List<Constraint> D_1 = FD(C_1, C_2, ACminusC_1);
        
        /*
         * compute AC - D_1
         */
        List<Constraint> ACminusD_1 = new ArrayList<>(AC);
        ACminusD_1.removeAll(D_1);
        
        /*
         * D_2 = FD(D_1, C_1, AC - D_1);
         */
        List<Constraint> D_2 = FD(D_1, C_1, ACminusD_1);
        /*
         * compute D_1 u D_2
         */
        for (Constraint c : D_2) {
            if (!D_1.contains(c)) {
                D_1.add(c);                
            }            
        }
        
        /*
         * return(D_1 u D_2);
         */
        return D_1;
    }
}
    
    
    
    
    
   
 