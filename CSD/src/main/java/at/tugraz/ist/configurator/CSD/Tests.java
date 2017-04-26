package at.tugraz.ist.configurator.CSD;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;

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
		 Constraints_Singleton.getInstance().setOriginalCSP(FileToChocoModel.createOriginalCSP(productTableFile));
		 int numberOfVariables = Constraints_Singleton.getInstance().getIntVarList_extension__UserRequirements().size()-1;
		 
		 List<Constraint> fastDiagDiagnosis  = new ArrayList<Constraint>();
		 
		 // PROD TABLE
		 int [][] varArray_productTable = {{20,50,80},{80,10,2000},{1000,40,20},{500,90,8000},{50,100,100}};
		 // CSP (int type, int[][]productTable, CSP originalCSP, int[] variables, int userID, int prodID,int[]weightedProducts)
		 // create original CSP, type =0
		 CSP productTableCSP = new CSP(0, varArray_productTable, null,null,0, 0, null);
		 
		 // USER CONST
		 int selectedProductID_byUser = 0;
		 int [] varArray_userConstraints = {1000,50,80};
		 // CSP (int type, int[][]productTable, CSP originalCSP, int[] variables, int userID, int prodID,int[]weightedProducts)
		 // create test CSP, type =1
		 CSP usermodelCSP  = new CSP(1,null,productTableCSP, varArray_userConstraints,0, selectedProductID_byUser, null);
		 
		 // TEST FATDIAG
		 fastDiagDiagnosis = FastDiag.computeDiagnose(usermodelCSP,productTableCSP);
		 
	 }
	 
	
}
