package at.tugraz.ist.configurator.CSD;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;
import org.chocosolver.solver.variables.IntVar;

import at.tugraz.ist.configurator.ChocoExtensions.CSP;
import at.tugraz.ist.configurator.ChocoExtensions.Constraints_Singleton;
import at.tugraz.ist.configurator.ChocoExtensions.FileToChocoModel;
import at.tugraz.ist.configurator.FastDiag.FastDiag;

public class Tests{
	
	
	 public static void main(String []args){
		 
		 //  evaluateVarOderForDiagnosis();
		 testFastDiag();
		 
	 }
	
	 
	 public static void testFastDiag(){
		 String productTableFile= "files\\inputs\\CSD_ProductPortfolio.data";
		 List<Constraint> fastDiagDiagnosis  = new ArrayList<Constraint>();
		 
		 // PROD TABLE
		 // int [][] varArray_productTable = {{20,50,80},{80,10,2000},{1000,40,20},{500,90,8000},{50,100,100}};
		 // CSP (int type, int[][]productTable, CSP originalCSP, int[] variables, int userID, int prodID,int[]weightedProducts)
		 // create original CSP, type =0
		 //CSP productTableCSP = new CSP(0, varArray_productTable, null,null,0, 0, null);
		 CSP productTableCSP = FileToChocoModel.createOriginalCSP(productTableFile);
		 Constraints_Singleton.getInstance().setOriginalCSP(productTableCSP);
		 boolean isConsistent = FastDiag.isConsistent(productTableCSP);
		 
		 // USER CONST
		 int selectedProductID_byUser = 0;
		 int [] varArray_userConstraints = {17,17,17,17,17,17,17,17,17,17};
		//  C: [24 ([5 = 0]), 33 ([6 = 0]), 38 ([4 = 0]), 40 ([9 = 700]), 85 ([10 = 749]), 60 ([2 = 30]), 56 ([8 = 50]), 26 ([7 = 0]), 20 ([1 = 208]), 22 ([3 = 1])]
		 // CSP (int type, int[][]productTable, CSP originalCSP, int[] variables, int userID, int prodID,int[]weightedProducts)
		 // create test CSP, type =1
		 CSP usermodelCSP  = new CSP(1,null,productTableCSP, varArray_userConstraints,0, selectedProductID_byUser, null);
		 isConsistent = FastDiag.isConsistent(usermodelCSP);
		 
		 List<Constraint> consts = usermodelCSP.constraints_user;
		 List<Constraint> reordered_consts = new ArrayList<Constraint>();
		 List<Constraint> test_diag = new ArrayList<Constraint>();
		
//	     reordered_consts.add(consts.get(4));
//	     reordered_consts.add(consts.get(5));
//	     reordered_consts.add(consts.get(3));
//	     reordered_consts.add(consts.get(8));
//	     reordered_consts.add(consts.get(9));
//	     reordered_consts.add(consts.get(1));
//	     reordered_consts.add(consts.get(7));
//	     reordered_consts.add(consts.get(6));
//	     reordered_consts.add(consts.get(0));
//	     reordered_consts.add(consts.get(2));
//	     
//	     
//	     test_diag.add(consts.get(4));
//	     test_diag.add(consts.get(5));
//	     test_diag.add(consts.get(3));
//	     test_diag.add(consts.get(8));
//	     test_diag.add(consts.get(9));
//	     test_diag.add(consts.get(1));
//	     test_diag.add(consts.get(7));
//	     test_diag.add(consts.get(6));
//	     test_diag.add(consts.get(0));
//	     
//	     usermodelCSP.constraints_user = reordered_consts;
		 
		 // TEST FATDIAG
		 fastDiagDiagnosis = FastDiag.computeDiagnose(usermodelCSP,productTableCSP);
		 
		 isPredictionCorrect(usermodelCSP,fastDiagDiagnosis);
		 
		 //isPredictionCorrect(usermodelCSP,test_diag);
	 }
	 
	 
	 
	 public static int isPredictionCorrect(CSP userModel, List<Constraint> diagnosis){
		 int res= 0;
		 List<Integer> productIDList = new ArrayList<Integer>();
		 CSP cspAfterDiagnose = FastDiag.subtractConstraints(userModel, userModel.constraints_user, diagnosis);
		 cspAfterDiagnose.chocoModel.getSolver().reset();
		 
		 while(cspAfterDiagnose.chocoModel.getSolver().solve()){
			 //System.out.println(cspAfterDiagnose.chocoModel.getVars()[0]);
			 IntVar temp = (IntVar) cspAfterDiagnose.chocoModel.getVars()[0];
			 productIDList.add(temp.getValue());
		 }
		 System.out.println("Prediction List: "+productIDList);
		 System.out.println("selectedProductID: "+userModel.selectedProductID);
		 for(int i=0;i<productIDList.size();i++){
			 if(productIDList.get(i)==userModel.selectedProductID ){
				 res = 1;
				 break;
			 }
		 }
		 System.out.println("Prediction is "+res);
		 return res;
	 }
	 
}
