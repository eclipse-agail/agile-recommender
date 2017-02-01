package at.tugraz.ist.configurator.ChocoExtensions;

import java.util.ArrayList;
import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

public class CSP {
	
	public Model chocoModel;
	public CSP originalCSPOfTheUserCSP = null;
	public int selectedProductID = 0;
	boolean isOriginalCSP;	
	boolean isTestCSP = false;	
	public int originalIndex;
	public int numberOfProducts;
	public int numberOfVariables;
	
	public List<Integer> constraint_IDs_user = new ArrayList<Integer>(); 
	public List<int[]> constraint_IDs_products = new ArrayList<int[]>(); 
	
	public List<Constraint> constraints_user = new ArrayList<Constraint>(); 
	public List<Constraint> constraints_products = new ArrayList<Constraint>(); 
	
	
	public List<Integer> diagnoseOfUser_IDs = new ArrayList<Integer>(); 

	Constraints_Singleton setOfConstraintsAndVariables = Constraints_Singleton.getInstance();
	
	boolean allVarsAreCreated = false;
	
	
	public CSP (int type, int[][]productTable, CSP originalCSP, int[] variables, int userID, int prodID){
		
		if(type==0)
			createOriginalCSP(productTable);
		else if (type==1)
			createTestCSP(originalCSP,variables,userID, prodID);
		else
			createUserCSP(originalCSP,variables,userID, prodID);
	}
	

	private void createTestCSP (CSP originalCSP, int[] variables, int userID, int prodID){
		isTestCSP = true;
		numberOfVariables = originalCSP.numberOfVariables;
		numberOfProducts = originalCSP.numberOfProducts;
		originalIndex = userID;
		originalCSPOfTheUserCSP = originalCSP;
		selectedProductID = prodID;
		constraint_IDs_products = originalCSP.constraint_IDs_products;
		//constraint_IDs_user = originalCSP.constraint_IDs_user;
		
		// constraints_products = originalCSP.constraints_products;
		String newName;
		// COPY ORIGINAL CSP
		// INCLUDES ALL VARIABLES
		// INCLUDES PRODUCT TABLE (PRODUCT CONSTRAINTS)
		if(variables==null)
			newName = String.valueOf(Math.random());
		else
			newName = "TestCSP for UserID:"+userID;
		
		ChocoDuplications.duplicateModel(this,newName);
		
		if(variables!=null)
			// ADD ADDITIONAL USER CONSTRAINTS
			for(int i=0;i<variables.length;i++){
				if(variables[i]!=-1){
					int varID = i+1;
					int cID = createNewConstr(varID,variables[i]);
					constraint_IDs_user.add(cID);
					Constraint c = ChocoDuplications.addUserConstraintToModel(chocoModel, cID);
					c.post(); // ADD CONSTRAINTS
					constraints_user.add(c);
				}
		}
		
	}
	
	private void createUserCSP (CSP originalCSP, int[] variables, int userID, int prodID){
		numberOfVariables = originalCSP.numberOfVariables;
		numberOfProducts = originalCSP.numberOfProducts;
		originalIndex = userID;
		originalCSPOfTheUserCSP = originalCSP;
		selectedProductID = prodID;
		constraint_IDs_products = originalCSP.constraint_IDs_products;
		//constraint_IDs_user = originalCSP.constraint_IDs_user;
		
		// constraints_products = originalCSP.constraints_products;
		String newName;
		// COPY ORIGINAL CSP
		// INCLUDES ALL VARIABLES
		// INCLUDES PRODUCT TABLE (PRODUCT CONSTRAINTS)
		if(variables==null)
			newName = String.valueOf(Math.random());
		else
			newName = "UserID:"+userID;
		
		ChocoDuplications.duplicateModel(this,newName);
		
		if(variables!=null)
			// ADD USER CONSTRAINTS
			for(int i=0;i<variables.length;i++){
				int varID = i+1;
				int cID = createNewConstr(varID,variables[i]);
				constraint_IDs_user.add(cID);
				Constraint c = ChocoDuplications.addUserConstraintToModel(chocoModel, cID);
				c.post(); // ADD CONSTRAINTS
				constraints_user.add(c);
			}
		
	}
	
	private void createOriginalCSP (int[][] variables){
		//vars = new IntVar[variables.length];
		
		numberOfVariables = variables[0].length;
		chocoModel = new Model("OriginalCSP");
		numberOfProducts = variables.length;
		createNewVar(); // FIRST VAR is PRODUCT ID , varID = 0
		
		int varID =0;
		
		for(int i=0;i<variables[0].length;i++){
			createNewVar(); // ADD FEATURE SET OF CAMERAS
		}
		allVarsAreCreated = true;
		
		addProductConstraints(variables);
		
		
	}

	private int isConstrCreated_Type0(int varID, int value){
		
		List<Constraint_Extension> constraintsList = setOfConstraintsAndVariables.getConstraintList_extension__UserRequirements();
		int constrIndex = -1;
		
		for(int j=0;j<constraintsList.size();j++){
			 if(constraintsList.get(j).getType()==0 & constraintsList.get(j).getVar_1_ID()==varID & constraintsList.get(j).getValue_1()==value){
				 constrIndex = j;
				 break;
			 }
		}
		return constrIndex;
	}
	
	private void createNewVar(){
		
		if(!allVarsAreCreated){
			int index = Constraints_Singleton.getInstance().getIntVarList_extension__UserRequirements().size();
			IntVar_Extension var = new IntVar_Extension();
			var.setID(index);
			var.setType(0);
			var.setLowerBound(0);
			
			if(index==0){
				// THIS IS SELECTED PRODUCT ID
				// if numberofpoducts=5 then domain will be: 0..4
				var.setUpperBound(numberOfProducts-1);
			}
			else{
				var.setUpperBound(1000000);
			}
			setOfConstraintsAndVariables.addVar(var);
			this.chocoModel = ChocoDuplications.addVariableToModel(this, index);
		}
		
	}
	
	private int createNewConstr(int varIndex, int value){
		
		int constrIndex = isConstrCreated_Type0(varIndex,value);
		//Constraint c = null;
		
		if (constrIndex == -1){
			
					constrIndex = Constraints_Singleton.getInstance().getConstraintList_extension__UserRequirements().size();
					Constraint_Extension constr = new Constraint_Extension();
					constr.setID(constrIndex);
					constr.setType(0);
					constr.setVar_1_ID(varIndex);
					constr.setValue_1(value);
					Constraints_Singleton.getInstance().addConstr(constr);
		}
		
		
		return constrIndex;
	}

	public void addProductConstraints(int[][] variables){
		
		int varIndex =0;
		int cID = -1;
		List<Constraint> productID_if_cnstr_list = new ArrayList<Constraint>();
		
		
		for(int p=0;p<variables.length;p++){
			if(variables==null || variables.length==0)
				variables = null;
			
			// CREATE IF CONSTRs 
			cID = createNewConstr(varIndex,p);
			productID_if_cnstr_list.add(ChocoDuplications.addUserConstraintToModel(chocoModel, cID));
		}
		
		
		int numberOfProducts = variables.length;
		int numberOfVariables = variables[0].length;
		
		for(int p=0;p<numberOfProducts;p++){
			
			varIndex =1 ;
			Constraint[] thenConstraints = new Constraint[numberOfVariables];
			Constraint[] product_costraints = new Constraint[numberOfVariables];
			int [] constIDs = new int [numberOfVariables];
			
			// CREATE IF CONSTRs 
			//cID = createNewConstr(varIndex,p);
			//productID_if_cnstr_list.add(ChocoDuplications.addUserConstraintToModel(this.chocoModel, cID));
			// do not add this constr to const list.
			// do not post this constr to model
			
			for(int i=0; i<numberOfVariables;i++){
				
				// CREATE THEN CONSTR 
				cID = createNewConstr(varIndex,variables[p][i]);
				thenConstraints[i] = ChocoDuplications.addProductConstraintToModel(this.chocoModel, cID, p);
				// then constr is posted as if-then
				// add if then const to const list
				constraints_products.add(thenConstraints[i]);
				
				// CREATE IF-THEN CONSTR 
				//cID = createNewConstr(1,varIndex,variables[p][i],0,p);
				//this = ChocoDuplications.addProductConstraintToModel(this, cID, p);
				
				
				// POST CONSTRAINT
				// e.g. IF PRODUCT_ID = 1 THEN (ZOOM=10 & SCREEN=7)
//				chocoModel.ifThen(
//					productID_if_cnstr_list.get(p),
//					thenConstraints[i] 
//				);
				
				varIndex++;
//				
//				product_costraints[i] = chocoModel.getCstrs()[chocoModel.getCstrs().length-1];
//				product_costraints[i].setName(String.valueOf(cID));
//				constraints_products.add(product_costraints[i]);
				constIDs[i] = cID;
			}
			constraint_IDs_products.add(p,constIDs);
			
		}
	}
	
	
	
	public List<Integer> getDiagnoseOfUser(){
		
		// collect id of then constraints
		if(diagnoseOfUser_IDs.size()==0){
			int [] constraintIDsOfSelectedProduct = constraint_IDs_products.get(selectedProductID);
			
			for(int i=0;i<constraint_IDs_user.size();i++){
				
				int constIDprod = constraintIDsOfSelectedProduct[i];
				int constIDuser = constraint_IDs_user.get(i);
				
				int varIDuser = setOfConstraintsAndVariables.getConstraintList_extension__UserRequirements().get(constIDuser).getVar_1_ID();
				int varIDprod = setOfConstraintsAndVariables.getConstraintList_extension__UserRequirements().get(constIDprod).getVar_1_ID();
						
				// collect changed variables V1=0 , V2 = 5 && V1=1, V2=5 -> then diagnosis is V1
				if((constIDprod!=constIDuser) && (varIDuser == varIDprod))
					diagnoseOfUser_IDs.add(varIDuser);
			}
		}
		
		return diagnoseOfUser_IDs;
	}
		
	
}
