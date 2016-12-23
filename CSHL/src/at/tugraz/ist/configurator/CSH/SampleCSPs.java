package at.tugraz.ist.configurator.CSH;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import at.tugraz.ist.configurator.CSH.chocoModels.UserModel;
import at.tugraz.ist.configurator.CSH.fileOperations.WriteToFile;

public class SampleCSPs {

	
	public static List<UserModel> getModelsforProblem(int numberOfVars,int numberOfModels, int maxDomainSize){
		 
		 Model testmodel = new Model();
		 
		 
		 Test.modelsOfTheSameProblem = new ArrayList<UserModel>(numberOfModels);
		 Random rand = new Random();
		 IntVar [] varstest = new IntVar[numberOfVars];
		 int[] DomainUpperValues = new int[numberOfVars];
		 
		 for (int i =0;i<numberOfVars;i++){
			 DomainUpperValues[i] = rand.nextInt(maxDomainSize+1);
		 }
		 // System.out.println("#######################################");
		 //System.out.println("Problem is generated with "+numberOfVars+" variables, "+numberOfModels+" different user models");
		 
		 
		 for (int i =0;i<numberOfModels;i++){
			 
			 //usermodel.chocoModel = new Model("Model#"+ i);
			 // System.out.println("##########");
			 // System.out.println("Model#"+ i);
			 //vars[i] = new IntVar[numberOfVars];
			 
			 testmodel = new Model("Model#"+ i);
			 
			 
			// System.out.println("SET VARIABLES and VALUES of the Models");
			 // SET VARIABLES and VALUES of the Model
			 for(int j=0; j<numberOfVars;j++){
				 // if variable is set by user
				 if(rand.nextBoolean()) {
					 // set a value for this variable
					 varstest[j] =  testmodel.intVar("v"+j, rand.nextInt(DomainUpperValues[j]+1));
					
				 } 
				 else{
					 // set the domain of this variable
					 varstest[j] =  testmodel.intVar("v"+j, 0, DomainUpperValues[j]);
					 
				 }
				// System.out.println(vars[i][j]);
			 }
			 
			
			//System.out.println("SET constraints of the this Problem");
			// SET constraints of the this problem
			 
			Constraint ifcont = testmodel.arithm(varstest[Test.numberOfvars-1],"<",varstest[Test.numberOfvars-2]);
			Constraint thencont = testmodel.arithm(varstest[Test.numberOfvars-1],"=",varstest[Test.numberOfvars-3]);
			
			UserModel usermodel = new UserModel("Model#"+ i,varstest,ifcont,thencont);
			
				// System.out.println(model.getCstrs()[0].toString());
			
			Test.modelsOfTheSameProblem.add(usermodel);
			//System.out.println("##########");
		 }
		 //System.out.println("#######################################");
		 java.util.Date date= new java.util.Date();
		 long time = date.getTime();
		 Test.modelsName = "SedasTestModels-"+time;
		 
		 WriteToFile.writeToFile();
		 return Test.modelsOfTheSameProblem;
	 }

}
