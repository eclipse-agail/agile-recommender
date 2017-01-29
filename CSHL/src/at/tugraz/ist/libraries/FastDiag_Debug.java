package at.tugraz.ist.libraries;


import java.util.List;
import org.chocosolver.solver.constraints.Constraint;

/**
 *
 * @author Michael Jeran
 */
public class FastDiag_Debug {
    
    private static final Boolean debug = false;
    
    public static void debug(String text) {
        if (debug) {
            System.out.println("=> " + text);
        }
    }
    public static void debug(Constraint[] constraintList) {
        if (debug) {
            for (Constraint c : constraintList) {
                System.out.println("im solver: " + c.getName());
            }
        }
    }
    
    public static void debug(List<Constraint> D, List<Constraint> C, List<Constraint> AC) {
        
        if (debug) {
            System.out.println("D:  ------------");

            for (Constraint c : D) {
                System.out.println("D name: " + c.getName());            
            }

            System.out.println("C:  ------------");

            for (Constraint c : C) {
                System.out.println("C name: " + c.getName());            
            }

            System.out.println("AC: ------------");

            for (Constraint c : AC) {
                System.out.println("AC name: " + c.getName());            
            }

            System.out.println("------------");            
        }        
    }
}