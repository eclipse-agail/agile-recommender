package at.tugraz.ist.configurator.CSH.Diagnosis;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;

import at.tugraz.ist.libraries.CSH.Clustering;
import at.tugraz.ist.libraries.CSH.LearningHeuristics;
import at.tugraz.ist.libraries.ChocoExtensions.CSP;

public class TestCSD {
	
	
	 public static void main(String []args){
		 
		 evaluateVarOderForDiagnosis();
		
	 }
	
	 public static void evaluateVarOderForDiagnosis(){
		 
		 
		 
		 // generate the original problem with products table
		 TestHeuristics_CommonVariables.originalCSP = FileToChocoModel.createOriginalCSP(TestHeuristics_CommonVariables.modelsName);
		 
		 
		 // get user_constraints as models based on the original problem
		 TestHeuristics_CommonVariables.CSPs_tobe_Clustered = FileToChocoModel.createUserModels(TestHeuristics_CommonVariables.originalCSP,TestHeuristics_CommonVariables.modelsName);
		 
		 
		 // apply clustering
		 Clustering.applyKMeans();
		 
		 // get clusters
		 Clustering.getClusters();
		 
		 // get var orders for each cluster
		 LearningHeuristics.learnHeuristicsForClusters();
		 
		 
	 }
	 
	 
}
