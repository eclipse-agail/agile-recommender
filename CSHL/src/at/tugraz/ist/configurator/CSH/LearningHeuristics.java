package at.tugraz.ist.configurator.CSH;

import static org.chocosolver.solver.search.strategy.Search.intVarSearch;

import java.util.ArrayList;
import java.util.Arrays;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMin;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;
import org.chocosolver.solver.variables.IntVar;

import at.tugraz.ist.configurator.CSH.GeneticAlgorithm.Algorithm;
import at.tugraz.ist.configurator.CSH.GeneticAlgorithm.FitnessCalc;
import at.tugraz.ist.configurator.CSH.GeneticAlgorithm.Individual;
import at.tugraz.ist.configurator.CSH.GeneticAlgorithm.Population;
import at.tugraz.ist.configurator.CSH.chocoModels.UserModel;

public class LearningHeuristics {

	
	 public static int[] geneToOrder(byte[]gene){
		 // input : 010 01 1 -> v0:2, v1:1, v2:3
		 // output: 102 -> order or variables
		 int [] result = new int[TestCSH.numberOfvars];
		 int index=0;
		 int order=0;
		 boolean [] orders = new boolean[TestCSH.numberOfvars];
		 Arrays.fill(orders, Boolean.FALSE);
		 
		 for(int i=0;i<TestCSH.numberOfvars;i++){
			 // 5, 4 ,3, 2, 1
			 int readNumberOfBytes = TestCSH.numberOfvars-i;
			
			 for(int j=0;j<readNumberOfBytes;j++){
				 if(gene[index+j]==1){
					 order = j;
					 break;
				 }
			 }
			 while(orders[order]!=false){
				 order += 1;
			 }
			 orders[order] = true;
			 // i :1 ->(v1), order:0 -> (first var) 
			 result[order] = i;
			 index += readNumberOfBytes;
		 }
		 return result;
	 }
	 
	 public static void testPopulationOverCluster(Population myPop, int clusterIndex){
		  //System.out.println("in testPopulationOverCluster: Cluster#"+clusterIndex);
		  long totalRunningTimeForCluster = 0;
		  int indivdiaulIndex = 0;
		  int sizeOfPopulation = myPop.size();
		  // INDIVIDUAL
		  // run each individual for this model
		  for(int i=0;i<sizeOfPopulation;i++){
			  indivdiaulIndex = i;
			  int [] variableOrder = myPop.getIndividual(i).getGenes();
				 
			  // MODEL
			  // run CSP over the models except the last one and take avg time
			  for (int md=0;md<TestCSH.clusters[clusterIndex].length-1;md++){
					 int modelIndex = TestCSH.clusters[clusterIndex][md];
					 UserModel model = TestCSH.modelsOfTheSameProblem.get(modelIndex);
					 Solver solver = model.chocoModel.getSolver();
			    	 
					 // getHeuristics
					 solver = getCorrespondingHeuristicForCSP(model, solver,-1,variableOrder);
					 
					 solver.solve();
					 totalRunningTimeForCluster += solver.getTimeCountInNanoSeconds();
				     //solver.printStatistics();
			  }
			  
			  // bu gen icin bu clusterda hesaplanan ortalama running time
			  if (TestCSH.clusters[clusterIndex].length-1>0){
				  myPop.getIndividual(i).setFitness(totalRunningTimeForCluster/(TestCSH.clusters[clusterIndex].length-1));
				  //System.out.println("GENE #"+indivdiaulIndex+" bu gen icin bu clusterda hesaplanan ortalama running time: "+clusterIndex+" :"+totalRunningTimeForCluster/(clusters[clusterIndex].length-1));
			  }
		 }
	 }

	 public static long testIndividualOverCluster(Individual ind, int clusterIndex){
		  //System.out.println("in testIndividualOverCluster: Cluster#"+clusterIndex);

		 if (TestCSH.clusters[clusterIndex].length-1<=0)
			  return 0;
		  
		  	long totalRunningTimeForCluster = 0;
		  	long fitness = 0;
		  // INDIVIDUAL
			  int [] variableOrder = ind.getGenes();
				 
			  // MODEL
			  // run CSP over the models except the last one and take avg time
			  for (int md=0;md<TestCSH.clusters[clusterIndex].length-1;md++){
				  	 long startTime = System.nanoTime();
				     int modelIndex = TestCSH.clusters[clusterIndex][md];
					 UserModel userModel = TestCSH.modelsOfTheSameProblem.get(modelIndex);
					 Solver solver = userModel.chocoModel.getSolver();
			    	 
					 // getHeuristics
					 solver = getCorrespondingHeuristicForCSP(userModel, solver,-1,variableOrder);
					 
					 solver.solve();
					 long endTime = System.nanoTime();
					 totalRunningTimeForCluster += (endTime - startTime);
				     //solver.printStatistics();
			  }
			  
			  // bu gen icin bu clusterda hesaplanan ortalama running time
			  fitness = totalRunningTimeForCluster/(TestCSH.clusters[clusterIndex].length-1);
				  //System.out.println("GENE #"+ind.getGenes()+" bu gen icin bu clusterda hesaplanan ortalama running time: "+clusterIndex+" :"+totalRunningTimeForCluster/(clusters[clusterIndex].length-1));
			  String geneStr ="";
			  for(int i=0;i<TestCSH.numberOfvars;i++){
				  geneStr += ind.getGenes()[i];
			  }
			  //System.out.println("GENE: "+geneStr+", Fitness +:"+fitness);
			  return fitness;
		
	 }
	 
	 public static void learnHeuristicsForClusters(){
		 //System.out.println("in getOrders");
		 //Find best variable ordering
		 int sizeOfGene = TestCSH.numberOfvars;
		 TestCSH.ordersOfVariables = new ArrayList<int[]>(TestCSH.numberOfclusters);
		 for(int v=0;v<TestCSH.numberOfclusters;v++){
			 TestCSH.ordersOfVariables.add(new int[TestCSH.numberOfvars]);
		 }
		 
		 // set target time (CSP running time)
		 // 0.015 ms
		 FitnessCalc.setTarget(10000);
		 
		 // FIND VARIABLE AND VALUE ORDERING FOR EACH CLUSTER
		 // CLUSTER
		 for (int cl=0;cl<TestCSH.numberOfclusters;cl++){
			 
			 //System.out.println("CLUSTER #"+cl);
			 
			 if(TestCSH.clusters[cl].length<2){
				 continue;
			 }
			 // create population for each cluster
			 
			 
			 Population myPop = new Population(TestCSH.sizeOfPopulation,sizeOfGene,true,cl);
			 
			 // testPopulationOverCluster(myPop,cl);
			  
			 int generationCount = 0; 
			 long startTime = System.nanoTime();
			 long currentTime = System.nanoTime();
			 
			 while(myPop.getFittest().getFitness() > FitnessCalc.getMaxFitness()){ 
			 //while(generationCount<0){
			   generationCount++; 
			   System.out.println("Generation: "+generationCount+" Fittest: "+myPop.getFittest().getFitness()); 
			   
			   //System.out.println("Start evolvePopulation");
			   // generate new population for better results
			   myPop = Algorithm.evolvePopulation(myPop,cl,TestCSH.maxDomainSize); 
			   //System.out.println("End evolvePopulation");
			   // apply new test over new population
			   // testPopulationOverCluster(myPop,cl);
			   currentTime = System.nanoTime();
			   if((currentTime-startTime)>900000000)
				   break;
			 } 
			 
			 int [] varOrder = myPop.getFittest().getGenes();
			 // SET VARIABLE ORDER FOR THIS CLUSTER
			 //ordersOfVariables.set(cl, new int[numberOfvars]);
			 //ordersOfVariables.add(new int[numberOfvars]);
			 TestCSH.ordersOfVariables.set(cl, varOrder);
		 }
		 
	 }
	 
	 public static Solver getCorrespondingHeuristicForCSP(UserModel userModel, Solver solver, int ClusterIndex, int[] varOrder){
		 
		 int [] variableOrder = null;
		 VariableSelector varSelector; 
		 
		 if(varOrder!=null || ClusterIndex!=-1){
			 if(varOrder==null){
				 variableOrder = TestCSH.ordersOfVariables.get(ClusterIndex);
			 }
			 else 
				 variableOrder = varOrder;
			  final int [] ord = variableOrder;
			  varSelector =(VariableSelector<IntVar>) variables -> {
					 	int varIndex = 0;
					 	for(int i =0;i<userModel.vars.length;i++){
			                varIndex = ord[i];
			                return userModel.vars[i];
			            }
			            return null;
			 };
		 }
		 else{
			 varSelector = new FirstFail(userModel.chocoModel);
		 }
		 
		 solver.setSearch(intVarSearch(
                 
				 varSelector,
                 
                 // selects the smallest domain value (lower bound)
                 new IntDomainMin(),
                 //new IntDomainMax(),
                
                 // variables to branch on
                 userModel.vars[0],userModel.vars[1], userModel.vars[2], userModel.vars[3], userModel.vars[4]
		));
		    
		return solver;
	 }
	 
	 
}
