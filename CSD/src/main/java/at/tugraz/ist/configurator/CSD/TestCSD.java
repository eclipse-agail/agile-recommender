package at.tugraz.ist.configurator.CSD;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import at.tugraz.ist.configurator.ChocoExtensions.Constraint_Extension;
import at.tugraz.ist.configurator.ChocoExtensions.Constraints_Singleton;
import at.tugraz.ist.configurator.ChocoExtensions.FileToChocoModel;
import at.tugraz.ist.configurator.Clustering.KMeansClustering;
import at.tugraz.ist.configurator.Learning.LearningHeuristics;


public class TestCSD {
	
	 static String modelsName = "CSD";
	 static FileWriter writer = null;
	 static String userConstraintsFile= "files\\inputs\\CSD_Model.data";
	 static String productTableFile= "files\\inputs\\CSD_ProductPortfolio.data";
	 static String outputFolder= "files\\outputs\\CSD_Model\\";
	 
	 static int numberOfVariables;
	 static int numberOfClusters ;
	 static int numberOfUsers ;
	 static int [][] clusters ;
	 
	 static List<int[]> variableOrders ;
	
	
	 public static void main(String []args){
		 
		numberOfClusters = 4;
		System.out.println("####################################");
		System.out.println("TEST for NumberOfClusters: "+ numberOfClusters);
		System.out.println("####################################");
		
		evaluateVarOderForDiagnosis();
		System.out.println("####################################");
	 }
	
	 public static void evaluateVarOderForDiagnosis(){
		 
		
		 // STEP-1 : generate the original problem with products table
		 Constraints_Singleton.getInstance().setOriginalCSP(FileToChocoModel.createOriginalCSP(productTableFile));
		 numberOfVariables = Constraints_Singleton.getInstance().getIntVarList_extension__UserRequirements().size()-1;
		 
		 // print clustered user models
		 int numberOfProductConstraints = Constraints_Singleton.getInstance().getOriginalCSP().numberOfProducts;
		 int constrIndex = 0;
		 
		 System.out.println("####################################");
		 System.out.println("PRODUCT TABLE");
		 System.out.println("Number of Products: "+numberOfProductConstraints);
		 System.out.println("Number of Variables: "+numberOfVariables);
		 
		 for(int i=0;i<numberOfProductConstraints;i++){
			 System.out.print("PRODUCT ID-: "+i+" => ");
			 for(int m=0;m<numberOfVariables;m++){
				 int constID = Integer.valueOf(Constraints_Singleton.getInstance().getOriginalCSP().constraints_products.get(constrIndex).getName());
				 Constraint_Extension c = Constraints_Singleton.getInstance().getConstraintList_extension__UserRequirements().get(constID);
				 System.out.print(" ("+c.getVar_1_ID()+" = "+c.getValue_1()+" )");
				 constrIndex++;
			 }
			 System.out.println();
		 }
		 System.out.println("####################################");
		
		 // STEP-1 is DONE
		 
		 
		 // STEP-2 : get user_constraints as models based on the original problem
		 Constraints_Singleton.getInstance().setCSPs_tobe_Clustered(FileToChocoModel.createUserModels(Constraints_Singleton.getInstance().getOriginalCSP(),userConstraintsFile));
		 numberOfUsers = Constraints_Singleton.getInstance().getCSPs_tobe_Clustered().size();
		 // STEP-2 is DONE
		 
		 
		 // STEP-3 : apply clustering
		 KMeansClustering.applyKMeans(userConstraintsFile,numberOfVariables+1,numberOfClusters,outputFolder);
		 clusters = KMeansClustering.getClusters(numberOfClusters,outputFolder);
		 
		 
		 // print clustered user models
		 System.out.println("####################################");
		 System.out.println("USER MODELS");
		 System.out.println("Number of Users: "+numberOfUsers);
		 System.out.println("Number of Clusters: "+numberOfClusters);
		 
		 for(int i=0;i<clusters.length;i++){
			 
			 System.out.println("Cluster"+i+" size= "+clusters[i].length+" :");
			 // get Model
			 for(int v=0;v<clusters[i].length;v++){
				 int modelIndex = clusters[i][v];
				 System.out.print("ID of User Model: "+modelIndex+" => ");
				 for(int m=0;m<numberOfVariables;m++){
					 System.out.print(" "+Constraints_Singleton.getInstance().getCSPs_tobe_Clustered().get(modelIndex).constraints_user.get(m));
				 }
				 System.out.println();
			 }
			 System.out.println();
		 }
		 System.out.println("####################################");
		 // STEP-3 is DONE
		 
		 
		 // STEP-4 : learn heuristics
		 variableOrders = LearningHeuristics.learnHeuristicsForClusters(numberOfVariables,clusters,Constraints_Singleton.getInstance().getCSPs_tobe_Clustered());
		 for(int i=0;i<variableOrders.size();i++){
			 System.out.println("VARIABLE ORDER FOR CLUSTER-"+i+":");
			 for(int v=0;v<numberOfVariables;v++){
				 System.out.print(" "+variableOrders.get(i)[v]);
			 }
			 System.out.println();
		 }
		// STEP-4 is DONE
		 
		// STEP-5 : run only with FastDiag
		// STEP-5 is not DONE
		 
		// STEP-6 : run only with Genetic Algorithm
		// STEP-6 is not DONE
		
		 
		 System.out.println("CSD test is completed");
		 
	 }
	 
}
