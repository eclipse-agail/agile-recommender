package at.tugraz.ist.configurator.Learning;

import static org.chocosolver.solver.search.strategy.Search.intVarSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMin;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;
import org.chocosolver.solver.variables.IntVar;

import at.tugraz.ist.configurator.ChocoExtensions.CSP;
import at.tugraz.ist.configurator.ChocoExtensions.ChocoDuplications;
import at.tugraz.ist.configurator.ChocoExtensions.Constraints_Singleton;
import at.tugraz.ist.configurator.FastDiag.FastDiag_Recursive;
import at.tugraz.ist.configurator.GeneticAlgorithm.Algorithm;
import at.tugraz.ist.configurator.GeneticAlgorithm.FitnessCalc;
import at.tugraz.ist.configurator.GeneticAlgorithm.Individual;
import at.tugraz.ist.configurator.GeneticAlgorithm.Population;

public class LearningHeuristics {
	
	 public static long targetValueOfFitness = 1;
	 static int numberOfVariables = 0;
	 static int [][] clusters;
	 static List<CSP> cspList;
	 static List<int []>ordersOfVariables;
	 static int numberOfClusters=4;
	 static int type = 1; // FASTDIAG
	 
	 /// UPDATE 
	 static int sizeOfPopulation = 2;
	 static int maxNumberOfGeneration = 2;
	 static int maxDomainSize = 10;
	 static int generationTimeOut = 100000000;
	 ///
	 
	
	 public static List<int []> learnHeuristicsForClusters(int vars, int [][]clus, List<CSP> csps){
		 
		 System.out.println("####################################");
		 System.out.println("GENETIC ALGORITHM - USED PARAMETERS:");
		 System.out.println("targetValueOfFitness: "+targetValueOfFitness);
		 System.out.println("sizeOfPopulation: "+sizeOfPopulation);
		 System.out.println("maxNumberOfGeneration: "+maxNumberOfGeneration);
		 System.out.println("generationTimeOut: "+generationTimeOut);
		 System.out.println("####################################");
		 
		 numberOfVariables = vars;
		 clusters = clus;
		 cspList = csps;
		 numberOfClusters = clusters.length;
		 
		 //System.out.println("in getOrders");
		 //Find best variable ordering
		 int sizeOfGene = numberOfVariables;
		 ordersOfVariables = new ArrayList<int[]>(numberOfClusters);
		 for(int v=0;v<numberOfClusters;v++){
			 ordersOfVariables.add(new int[numberOfVariables]);
		 }
		 
		 // set target time (CSP running time)
		 // 0.015 ms
		 FitnessCalc.setTarget(targetValueOfFitness);
		 
		 // FIND VARIABLE AND VALUE ORDERING FOR EACH CLUSTER
		 // CLUSTER
		 for (int cl=0;cl<numberOfClusters;cl++){
			 
			 //System.out.println("CLUSTER #"+cl);
			 
			 if(clusters[cl].length<0){
				 continue;
			 }
			 // create population for each cluster
			 
			 Population myPop = new Population(sizeOfPopulation,sizeOfGene,true,cl);
			 
			 // testPopulationOverCluster(myPop,cl);
			  
			 int generationCount = 0; 
			 long startTime = System.nanoTime();
			 long currentTime = System.nanoTime();
			 
			 //while(myPop.getFittest().getFitness() > FitnessCalc.getMaxFitness()){ 
			 while(generationCount<maxNumberOfGeneration){
			   generationCount++; 
			   //System.out.println("Generation: "+generationCount+" Fittest: "+myPop.getFittest().getFitness()); 
			   
			   //System.out.println("Start evolvePopulation");
			   // generate new population for better results
			   myPop = Algorithm.evolvePopulation(myPop,cl,maxDomainSize); 
			   //System.out.println("End evolvePopulation");
			   // apply new test over new population
			   // testPopulationOverCluster(myPop,cl);
			   currentTime = System.nanoTime();
			   if((currentTime-startTime)>generationTimeOut)
				   break;
			 } 
			 
			 int [] varOrder = myPop.getFittest().getGenes();
			 // SET VARIABLE ORDER FOR THIS CLUSTER
			 //ordersOfVariables.set(cl, new int[numberOfvars]);
			 //ordersOfVariables.add(new int[numberOfvars]);
			 System.out.println("Heuristic precision of Cluster-"+cl+" is "+myPop.getFittest().getFitness());
			 ordersOfVariables.set(cl, varOrder);
		 }
		 return ordersOfVariables;
		 
	 }
	 
	 public static Solver getCorrespondingHeuristicForCSP(CSP userModel, Solver solver, int ClusterIndex, int[] varOrder){
		 
		 int [] variableOrder = null;
		 VariableSelector varSelector; 
		 
		 if(varOrder!=null || ClusterIndex!=-1){
			 if(varOrder==null){
				 variableOrder = ordersOfVariables.get(ClusterIndex);
			 }
			 else 
				 variableOrder = varOrder;
			  final int [] ord = variableOrder;
			  varSelector =(VariableSelector<IntVar>) variables -> {
					 	int varIndex = 0;
					 	for(int i =0;i<userModel.chocoModel.getNbVars();i++){
			                varIndex = ord[i];
			                return (IntVar) userModel.chocoModel.getVars()[varIndex];
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
                 (IntVar)userModel.chocoModel.getVar(0),(IntVar)userModel.chocoModel.getVar(1),(IntVar)userModel.chocoModel.getVar(2),(IntVar)userModel.chocoModel.getVar(3),(IntVar)userModel.chocoModel.getVar(4)
		));
		    
		return solver;
	 }
	 
	 // returns execution time
	 public static long solveCSPwithChoco(CSP model, int [] variableOrder){
		 
		 long startTime = System.nanoTime();
		 
		 Solver solver = model.chocoModel.getSolver();
    	 
		 // getHeuristics
		 if(variableOrder!=null)
			 solver = getCorrespondingHeuristicForCSP(model, solver,-1, variableOrder);
		 
		 
		 long endTime = System.nanoTime();
		 
		 
		 return endTime-startTime;
	 }
	 
	 // returns precision btw 0..1
	 public static long diagnoseCSPwithFastDiag(CSP model, int [] variableOrder){
			 //System.out.println("Diagnose with fastdiag for variable order"+variableOrder[0]+variableOrder[1]); 
			 long precision = (long) 0.0;
			 List<Constraint> orderedUserConstraints = new ArrayList<Constraint>();
			 List<Integer> userDiagnosis = new ArrayList<Integer>();
			 
			 List<Constraint> fastDiagDiagnosis  = new ArrayList<Constraint>();
			 
			 //System.out.println("Before FastDiag"); 
			 int prodConstSize = model.constraints_products.size();
			 
			 List<Constraint> AC = Arrays.asList(model.chocoModel.getCstrs());
			 //List<Constraint> C = Arrays.asList(model.chocoModel.getCstrs()).subList(prodConstSize,AC.size()-1);
			 
			// SET THE ORDER OF USER CONSTRAINTS
			 for (int i=0;i<variableOrder.length;i++){
				 Constraint usercon = model.constraints_user.get(variableOrder[i]);
				 //System.out.println(usercon);
				 for(int j=0;j<AC.size();j++){ 
					 Constraint c1 = AC.get(j);
					 //System.out.println(c1);
					 if(c1.getName().equals(usercon.getName())){
						 if(c1.getCidxInModel()==usercon.getCidxInModel()){
							 orderedUserConstraints.add(c1);
							 //System.out.println("User Constraint: "+usercon+"Model Constraint: "+c1);
						 }
					 }
				 }
			 }
			 
			 // ADD FASTDIAG HERE
			 fastDiagDiagnosis = FastDiag_Recursive.computeDiagnose(orderedUserConstraints, AC, model);
			 
			 //System.out.println("After FastDiag"); 
			 if (fastDiagDiagnosis!=null)
				 
				// changed variables
				 userDiagnosis = model.getDiagnoseOfUser();
			 	
			  	 if(userDiagnosis!=null)
					 for (int m=0;m<userDiagnosis.size();m++)
					 {
						 // SEARCH M in ALL DIAGNOSIS
						 if(fastDiagDiagnosis!=null)
							 for (int k=0;k<fastDiagDiagnosis.size();k++)
							 {
								 int varID_userDiagnosis =  userDiagnosis.get(m);
								 int varID_ofDiagnoseAlgorithm = -1;
								 int constrID_ofDiagnoseAlgorithm = -1;
								 try{
									 constrID_ofDiagnoseAlgorithm = Integer.valueOf(fastDiagDiagnosis.get(k).getName());
									 varID_ofDiagnoseAlgorithm = Constraints_Singleton.getInstance().getConstraintList_extension__UserRequirements().get(constrID_ofDiagnoseAlgorithm).getVar_1_ID();
									 
									 if (varID_userDiagnosis == varID_ofDiagnoseAlgorithm){
										 precision += 1/userDiagnosis.size();
										 if(precision==2)
											 precision =2;
										 break; // SEARCH FOR OTHER VARIABLE IN USER DIAG
									 }
									 
								 }catch(Exception ex){}
							 }
					 }
			
			 return precision;
		 }
	 
	 public static int[] geneToOrder(byte[]gene){
		 // input : 010 01 1 -> v0:2, v1:1, v2:3
		 // output: 102 -> order or variables
		 int [] result = new int[numberOfVariables];
		 int index=0;
		 int order=0;
		 boolean [] orders = new boolean[numberOfVariables];
		 Arrays.fill(orders, Boolean.FALSE);
		 
		 for(int i=0;i<numberOfVariables;i++){
			 // 5, 4 ,3, 2, 1
			 int readNumberOfBytes = numberOfVariables-i;
			
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

	 public static float evaluateFitnessValueOfCluster(Individual ind, int clusterIndex){
		 //System.out.println("evaluateFitnessValueOfCluster: Cluster#"+clusterIndex);

		 	if (clusters[clusterIndex].length<=0)
			  return 0;
		  
		  	float totalFitnessValueForCluster = 0;
		  	float fitness = 0;
		  	  // INDIVIDUAL
			  int [] variableOrder = ind.getGenes();
				 
			  // MODEL
			  // run CSP over the models except the last one and take avg time
			  //System.out.println("totalModels in cluster: #"+(clusters[clusterIndex].length-1));
			  for (int md=0;md<clusters[clusterIndex].length;md++){
				  	 	 
				  	 //long startTime = System.nanoTime();
				     int modelIndex = clusters[clusterIndex][md];
				     if(modelIndex==0)
				    	 modelIndex=0;
					 CSP userModel = cspList.get(modelIndex);
					 String newName = String.valueOf(Math.random());
					 
					 userModel = ChocoDuplications.duplicateModel(userModel, newName);
					 //System.out.println("evaluateFitnessValueOfCluster: Model#"+modelIndex);
					 float fitnessValueOfOrder = 0;
					 
					 switch(type){
					 	// RETURNS PERFORMANCE OF CHOCO (in milliseconds)
					 	case 0:
					 		fitnessValueOfOrder = solveCSPwithChoco(userModel,variableOrder);
					 		break;
					 	// RETURNS DIAGNOSIS PRECISION OF FASTDIAG WITH VAR ORDER (0..1)
					 	case 1:
					 		fitnessValueOfOrder = diagnoseCSPwithFastDiag(userModel,variableOrder);
					 		break;
					 	default:
					 		fitnessValueOfOrder = solveCSPwithChoco(userModel,variableOrder);
					 		break;
					 }
					 
					 
					 //System.out.println("fitness: "+fitnessValueOfOrder);
					 //Solver solver = userModel.chocoModel.getSolver();
			    	 
					 // getHeuristics
					 //solver = getCorrespondingHeuristicForCSP(userModel, solver,-1,variableOrder);
					 
					 // solver.solve();
					 // long endTime = System.nanoTime();
					 totalFitnessValueForCluster += (fitnessValueOfOrder);
				     //solver.printStatistics();
			  }
			  
			  // bu gen icin bu clusterda hesaplanan ortalama running time
			  fitness = totalFitnessValueForCluster/(clusters[clusterIndex].length);
			  //System.out.println("GENE #"+ind.getGenes()+" bu gen icin bu clusterda-"+clusterIndex+" hesaplanan fitness:"+fitness);
			  String geneStr ="";
			  for(int i=0;i<numberOfVariables;i++){
				  geneStr += ind.getGenes()[i];
			  }
			  //System.out.println("GENE: "+geneStr+", Fitness +:"+fitness);
			  return fitness;
		
	 }
	 	
}
