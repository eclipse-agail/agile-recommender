package at.tugraz.ist.configurator.CSD;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import at.tugraz.ist.configurator.Clustering.Clustering;
import at.tugraz.ist.configurator.Learning.LearningHeuristics;
import at.tugraz.ist.libraries.ChocoExtensions.CSP;
import at.tugraz.ist.libraries.ChocoExtensions.Constraints_Singleton;
import at.tugraz.ist.libraries.ChocoExtensions.FileToChocoModel;

public class TestCSD {
	
	 static String modelsName = "CSD";
	 static FileWriter writer = null;
	 static String userConstraintsFile= "files\\inputs\\CSD_Model.data";
	 static String productTableFile= "files\\inputs\\CSD_ProductPortfolio.data";
	 static String outputFolder= "files\\outputs\\CSD_Model\\";
	 
	 static int numberOfVariables;
	 static int numberOfClusters = 4 ;
	 static int [][] clusters ;
	 
	 static List<int[]> variableOrders ;
	
	
	 public static void main(String []args){
		
		 evaluateVarOderForDiagnosis();
		
	 }
	
	 public static void evaluateVarOderForDiagnosis(){
		 
		
		 // STEP-1 : generate the original problem with products table
		 Constraints_Singleton.getInstance().setOriginalCSP(FileToChocoModel.createOriginalCSP(productTableFile));
		  numberOfVariables = Constraints_Singleton.getInstance().getIntVarList_extension__UserRequirements().size()-1;
		 // STEP-1 is DONE
		 
		 
		 
		 // STEP-2 : get user_constraints as models based on the original problem
		  Constraints_Singleton.getInstance().setCSPs_tobe_Clustered(FileToChocoModel.createUserModels(Constraints_Singleton.getInstance().getOriginalCSP(),userConstraintsFile));
		 // STEP-2 is DONE
		 
		 
		 // STEP-3 : apply clustering
		 Clustering.applyKMeans(userConstraintsFile,numberOfVariables,outputFolder);
		 clusters = Clustering.getClusters(numberOfClusters,outputFolder);
		 // STEP-3 is DONE
		 
		 
		 // STEP-4 : learn heuristics
		 variableOrders = LearningHeuristics.learnHeuristicsForClusters(numberOfVariables,clusters,Constraints_Singleton.getInstance().getCSPs_tobe_Clustered());
		// STEP-4 is DONE without FASTDIAG
		 
		 for(int i=0;i<variableOrders.size();i++){
			 System.out.println("VARIABLE ORDER FOR CLUSTER-"+i+":");
			 for(int v=0;v<numberOfVariables;v++){
				 System.out.print(" "+variableOrders.get(i)[v]);
			 }
			 System.out.println();
		 }
		 
		 System.out.println("completed");
		 
	 }
	 
	 
}
