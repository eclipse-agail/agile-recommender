package at.tugraz.ist.configurator.ChocoExtensions;

import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import at.tugraz.ist.configurator.ChocoExtensions.ChocoDuplications;
import at.tugraz.ist.configurator.ChocoExtensions.Constraint_Extension;
import at.tugraz.ist.configurator.ChocoExtensions.IntVar_Extension;

public class Constraints_Singleton {

	
	   private static Constraints_Singleton instance = null;
	   // private static Model model_singleton = new Model("model_singleton");
	   
	   private static CSP originalCSP;
	   private static List<CSP> CSPs_tobe_Clustered; 
	   
	   //private List<IntVar> IntVarList_UserRequirements  = new ArrayList<IntVar>();
	   private List<IntVar_Extension> IntVarList_extension__Constraints = new ArrayList<IntVar_Extension>();
	   
	   //private List<Constraint> constraintList__UserRequirements  = new ArrayList<Constraint>();
	   private List<Constraint_Extension> constraintList_extension__Constraints  = new ArrayList<Constraint_Extension>();
	   
	   
	   protected Constraints_Singleton() {
	      // Exists only to defeat instantiation.
	   }
	   
	   public static Constraints_Singleton getInstance() {
	      if(instance == null) {
	         instance = new Constraints_Singleton();
	      }
	      return instance;
	   }
	
	 public void addVar(IntVar_Extension var_ext){
		   IntVarList_extension__Constraints.add(var_ext);
		   //model_singleton = ChocoDuplications.addVariableToModel(model_singleton, var_ext.getID());
	   }
	   
	 public void addConstr(Constraint_Extension const_ext){
		   constraintList_extension__Constraints.add(const_ext);
//		   Constraint c = ChocoDuplications.addConstraintToModel(model_singleton, const_ext.getID());
//		   c.post();
		   
	   }

//	public List<IntVar> getIntVarList_UserRequirements() {
//		return IntVarList_UserRequirements;
//	}
//
//	public void setIntVarList_UserRequirements(List<IntVar> intVarList_UserRequirements) {
//		IntVarList_UserRequirements = intVarList_UserRequirements;
//	}

	public List<IntVar_Extension> getIntVarList_extension__UserRequirements() {
		return IntVarList_extension__Constraints;
	}

	public void setIntVarList_extension__UserRequirements(List<IntVar_Extension> intVarList_extension__UserRequirements) {
		IntVarList_extension__Constraints = intVarList_extension__UserRequirements;
	}
//
//	public List<Constraint> getConstraintList__UserRequirements() {
//		return constraintList__UserRequirements;
//	}
//
//	public void setConstraintList__UserRequirements(List<Constraint> constraintList__UserRequirements) {
//		this.constraintList__UserRequirements = constraintList__UserRequirements;
//	}

	public List<Constraint_Extension> getConstraintList_extension__UserRequirements() {
		return constraintList_extension__Constraints;
	}

	public void setConstraintList_extension__UserRequirements2(
			List<Constraint_Extension> constraintList_extension__UserRequirements) {
		this.constraintList_extension__Constraints = constraintList_extension__UserRequirements;
	}

	public static CSP getOriginalCSP() {
		return originalCSP;
	}

	public static void setOriginalCSP(CSP originalCSP) {
		Constraints_Singleton.originalCSP = originalCSP;
	}

	public static List<CSP> getCSPs_tobe_Clustered() {
		return CSPs_tobe_Clustered;
	}

	public static void setCSPs_tobe_Clustered(List<CSP> cSPs_tobe_Clustered) {
		CSPs_tobe_Clustered = cSPs_tobe_Clustered;
	}


	   
}