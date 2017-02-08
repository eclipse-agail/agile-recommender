package at.tugraz.ist.configurator.ChocoExtensions;

import java.util.ArrayList;
import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public class ChocoDuplications {
	
	public static Constraints_Singleton seteOfVarsandCons = Constraints_Singleton.getInstance();
	//public static List<Constraint> ifconstraints = new ArrayList<Constraint>(); 
//	
//	public static Model duplicateModel(Model inputModel, String name){
//		
//		// public CSP (int type, int[][]productTable, CSP originalCSP, int[] variables, int userID, int prodID)
//		CSP originalCSP = null;
//		int userID = inputCSP.originalIndex;
//		
//		CSP copyCSP; 
//		
//		// TYPE = 0
//		if(inputCSP.isOriginalCSP)
//			originalCSP = Constraints_Singleton.getOriginalCSP();
//		
//		// TYPE = 1 
//		else if (inputCSP.isTestCSP){
//			originalCSP = Constraints_Singleton.getOriginalCSP();
//			copyCSP = new CSP(1, null, originalCSP,null, 0, 0);
//		}
//		
//		// TYPE = 2
//		else{
//			if(Constraints_Singleton.getCSPs_tobe_Clustered()!=null){
//				originalCSP = Constraints_Singleton.getCSPs_tobe_Clustered().get(userID);
//				originalCSP.originalIndex = userID;
//			}
//			else{
//				originalCSP = Constraints_Singleton.getOriginalCSP();
//				copyCSP.isOriginalCSP = false;
//				copyCSP.originalIndex = userID;
//			}
//			copyCSP = new CSP(2, null, originalCSP, null, 0, 0);
//		}
//		
//		
//		//Model copyModel = new Model(name);
//		copyCSP.chocoModel = new Model(name);
//		
//	
//		// ADD 
//		int numberOfVars = seteOfVarsandCons.getIntVarList_extension__UserRequirements().size();
//		// SET VARS
//		for(int i=0;i<numberOfVars;i++){
//			int varID = i;
//			copyCSP.chocoModel = addVariableToModel(copyCSP,varID);
//		}
//		
//		int cID = 0;
//		// int numberOfProductConstraints = inputCSP.constraint_IDs_products.size();
//		// SET PRODUCT CONSTR, should executed number of product vars
//		numberOfVars = originalCSP.constraint_IDs_products.get(0).length;
//		
//		
//		// int numberOfConstraints = inputCSP.constraint_IDs_products.size() * inputCSP.constraint_IDs_products.get(0).length;
//		int numberOfProducts = originalCSP.constraint_IDs_products.size();
//		for(int i=0;i<numberOfProducts;i++){
//			// add if-then constraints
//			for (int j=0;j<numberOfVars;j++){
//				cID = originalCSP.constraint_IDs_products.get(i)[j];
//				Constraint c = addProductConstraintToModel(copyCSP.chocoModel,cID,i);
//				copyCSP.constraints_products.add(c);
//			}
//			//cID += numberOfVars; // index = 3
//		}
//		
//		// SET USER CONSTR
//		for(int i=0;i<originalCSP.constraint_IDs_user.size();i++){
//			cID = originalCSP.constraint_IDs_user.get(i);
//			Constraint c = addUserConstraintToModel(copyCSP.chocoModel,cID);
//			c.post();
//			copyCSP.constraints_user.add(c);
//		}
//	
//		return copyCSP;
//	}
//	
	public static Model getChocoModelOfCreatedUser(int userIndex, String name){
		
			CSP originalCSP = Constraints_Singleton.getCSPs_tobe_Clustered().get(userIndex);
			Model copyModel = new Model(name);
			
	
			// ADD VARS
			int numberOfVars = seteOfVarsandCons.getIntVarList_extension__UserRequirements().size();
			for(int i=0;i<numberOfVars;i++){
				int varID = i;
				copyModel = addVariableToModel(copyModel,varID);
			}
		
			// ADD PRODUCT CONSTRAINTS
			int cID = 0;
			// int numberOfProductConstraints = inputCSP.constraint_IDs_products.size();
			// SET PRODUCT CONSTR, should executed number of product vars
			numberOfVars = originalCSP.constraint_IDs_products.get(0).length;
					
			// int numberOfConstraints = inputCSP.constraint_IDs_products.size() * inputCSP.constraint_IDs_products.get(0).length;
			int numberOfProducts = originalCSP.constraint_IDs_products.size();
			for(int i=0;i<numberOfProducts;i++){
				// add if-then constraints
				for (int j=0;j<numberOfVars;j++){
					cID = originalCSP.constraint_IDs_products.get(i)[j];
					Constraint c = addProductConstraintToModel(copyModel,cID,i);
				}
				//cID += numberOfVars; // index = 3
			}
		
			// ADD USER CONSTRAINTS
			for(int i=0;i<originalCSP.constraint_IDs_user.size();i++){
				cID = originalCSP.constraint_IDs_user.get(i);
				Constraint c = addUserConstraintToModel(copyModel,cID);
				c.post();
			}
	
			return copyModel;
	}
	
	public static Model getChocoModelOfOriginalCSP(String name){
		
		CSP originalCSP = Constraints_Singleton.getOriginalCSP();
		Model copyModel = new Model(name);
		

		// ADD VARS
		int numberOfVars = seteOfVarsandCons.getIntVarList_extension__UserRequirements().size();
		for(int i=0;i<numberOfVars;i++){
			int varID = i;
			copyModel = addVariableToModel(copyModel,varID);
		}
	
		// ADD PRODUCT CONSTRAINTS
		int cID = 0;
		// int numberOfProductConstraints = inputCSP.constraint_IDs_products.size();
		// SET PRODUCT CONSTR, should executed number of product vars
		numberOfVars = originalCSP.constraint_IDs_products.get(0).length;
				
		// int numberOfConstraints = inputCSP.constraint_IDs_products.size() * inputCSP.constraint_IDs_products.get(0).length;
		int numberOfProducts = originalCSP.constraint_IDs_products.size();
		for(int i=0;i<numberOfProducts;i++){
			// add if-then constraints
			for (int j=0;j<numberOfVars;j++){
				cID = originalCSP.constraint_IDs_products.get(i)[j];
				Constraint c = addProductConstraintToModel(copyModel,cID,i);
			}
			//cID += numberOfVars; // index = 3
		}
		return copyModel;
}

	
	public static Model addVariableToModel(Model model, int varID){
		
		IntVar_Extension var = seteOfVarsandCons.getIntVarList_extension__UserRequirements().get(varID);
		
		IntVar var_choco = null;
		
		switch (var.getType()){
			case 0:
				var_choco = model.intVar(String.valueOf(var.getID()),var.getLowerBound(),var.getUpperBound());
				break;
			case 1:
				var_choco = model.intVar(String.valueOf(var.getID()),var.getDomainArray());
				break;
			case 2:
				var_choco = model.intVar(String.valueOf(var.getID()),var.getValue());
				break;
			default:
				var_choco = model.intVar(String.valueOf(var.getID()),var.getValue());
				break;
		}
		
		//Constraints_Singleton.getInstance().getIntVarList_UserRequirements().add(var_choco);
		
		return model;
	}
	
	public static Constraint addUserConstraintToModel(Model model, int constID){
		
		Constraint_Extension constr = seteOfVarsandCons.getConstraintList_extension__UserRequirements().get(constID);
		int varID = constr.getVar_1_ID();
		
		Constraint returnConstr ;
		
		switch (constr.getType()){
			case 0:
				//IntVar var = instanceOfMasterModel.getIntVarList_UserRequirements().get(constr.getVar_1_ID());
				returnConstr = model.arithm((IntVar)model.getVar(varID), "=", constr.getValue_1());
				returnConstr.setName(String.valueOf(constr.getID()));
				break;
			default:
				//IntVar var2 = instanceOfMasterModel.getIntVarList_UserRequirements().get(constr.getVar_1_ID());
				returnConstr = model.arithm((IntVar)model.getVar(varID), "=", constr.getValue_1());
				returnConstr.setName(String.valueOf(constr.getID()));
				break;
		}
		return returnConstr;
	}
	
	public static Constraint addProductConstraintToModel(Model model, int constID, int prodID){
		
		Constraint_Extension constr = seteOfVarsandCons.getConstraintList_extension__UserRequirements().get(constID);
		//int varID = constr.getVar_1_ID();
	
		int ifConstID = prodID; 
		
		// constraint id of ifconstraint = constID
		Constraint ifconstr = ChocoDuplications.addUserConstraintToModel(model, ifConstID);
		Constraint thenconstr = ChocoDuplications.addUserConstraintToModel(model, constID);
		
		Constraint c;
		
		switch (constr.getType()){
			case 0:
				//IntVar var = instanceOfMasterModel.getIntVarList_UserRequirements().get(constr.getVar_1_ID());
				model.ifThen(ifconstr, thenconstr);
				c = model.getCstrs()[model.getCstrs().length-1];
				c.setName(String.valueOf(constID));
				//csp.constraints_products.add(c);
				break;
			default:
				model.ifThen(ifconstr, thenconstr);
				c = model.getCstrs()[model.getCstrs().length-1];
				c.setName(String.valueOf(constID));
				//csp.constraints_products.add(c);
				break;
		}
		
		
		return c;
		
	}

}
