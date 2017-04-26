package at.tugraz.ist.configurator.Learning;

import static org.chocosolver.solver.search.strategy.Search.intVarSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
import at.tugraz.ist.configurator.FastDiag.FastDiag;
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
	 public static int type = 0; 
	 // 0: diagnoseByFastDiag_BestPrecision_withCSH,  
	 // 1: diagnoseByFastDiag_Fastest_withCSH
	 // 3: diagnoseByGeneticAlgorithm_BestPrecision
	 // 4: diagnoseByGeneticAlgorithm_Fastest
	 
	 /// UPDATE 
	 static int sizeOfPopulation = 10; // for 10 variables, it can be max: 10!= 3628800
	 static int maxNumberOfGeneration = 2;
	 static int generationTimeOut = 100;
	 ///
	 
	 public static float averageFitness=0;
	 public static int count = 0;
	
	 public static List<int []> learnHeuristicsForClusters (int vars, int [][]clus, List<CSP> csps){
		 
		 System.out.println("####################################");
		 System.out.println("GENETIC ALGORITHM - USED PARAMETERS:");
		 System.out.println("targetValueOfFitness: "+targetValueOfFitness);
		 System.out.println("sizeOfPopulation: "+sizeOfPopulation);
		 System.out.println("maxNumberOfGeneration: "+maxNumberOfGeneration);
		 System.out.println("generationTimeOut: "+generationTimeOut);
		 System.out.println("####################################");
		 
		 numberOfVariables = vars;
		 //sizeOfPopulation = vars*vars;
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
		 float fit =0;
		 float totalmodels=0;
		 
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
			   myPop = Algorithm.evolvePopulation(myPop,cl); 
			   //System.out.println("End evolvePopulation");
			   // apply new test over new population
			   // testPopulationOverCluster(myPop,cl);
			   currentTime = System.nanoTime();
//			   if((currentTime-startTime)>generationTimeOut)
//				   break;
			 } 
			 
			 int [] varOrder = myPop.getFittest().getGenes();
			 // SET VARIABLE ORDER FOR THIS CLUSTER
			 //ordersOfVariables.set(cl, new int[numberOfvars]);
			 //ordersOfVariables.add(new int[numberOfvars]);
			 System.out.println("Best Fitness value of Cluster-"+cl+" is "+myPop.getFittest().getFitness());
			 ordersOfVariables.set(cl, varOrder);
			 
			 
			 fit += myPop.getFittest().getFitness()*clusters[cl].length;
			 totalmodels += clusters[cl].length;
		 }
		 averageFitness = (float)(fit/totalmodels);
		 
		 System.out.println("Avg Fitness value: "+(float)(fit/totalmodels));
		 
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
	 
	 // returns execution time of Choco
	 public static float solveCSPwithChoco(CSP model, int [] variableOrder){
		 
		 long startTime = System.nanoTime();
		 
		 Solver solver = model.chocoModel.getSolver();
    	 
		 // getHeuristics
		 if(variableOrder!=null)
			 solver = getCorrespondingHeuristicForCSP(model, solver,-1, variableOrder);
		 
		 long endTime = System.nanoTime();
		 
		 return endTime-startTime;
	 }
	 
	 // returns precision btw 0..1 and time in negative value
	 // Genetic Algorithm for Diagnosis
	 public static float[] diagnoseCSP_GA4D(CSP model, int [] variableOrder){
			 	 
				 float precision = (float) 0.0;
			 	 float time = (float)0;
			 	 int [] valuesOfVariables = new int[variableOrder.length];
			 	 
			 	 for(int v=0;v<variableOrder.length;v++){
			 		int varIndex = variableOrder[v];
			 		int constrID = model.constraint_IDs_user.get(varIndex);
			 		valuesOfVariables[varIndex] = Constraints_Singleton.getInstance().getConstraintList_extension__UserRequirements().get(constrID).getValue_1();
			 	 }
			 	 
				 List<Integer> userDiagnosis = new ArrayList<Integer>();
				 userDiagnosis = model.getDiagnoseOfUser();

				 List<Integer> GADiagnosis  = new ArrayList<Integer>();
				 
				 CSP testModel = null;
				 
				 boolean isConsistent = false;
				 long startTime = 0;
				 long endTime = 0;
				 int count = 0;
				 
				 while(!isConsistent && valuesOfVariables.length>count){
					 
					 int varIndex = variableOrder[count];
					 
					 // when varIndex =0, this represents v1 not the v0, v0 is the selectedproduct
					 GADiagnosis.add(varIndex+1);
					 
					 // -1 means do not add this var as a user constraint
					 valuesOfVariables[varIndex] = -1;
					 // public CSP (int type, int[][]productTable, CSP originalCSP, int[] variables, int userID, int prodID){
					 testModel = new CSP(1, null,Constraints_Singleton.getInstance().getOriginalCSP(),valuesOfVariables,-1, model.selectedProductID,model.weightedProducts_of_User);
					 
					 Solver solver = testModel.chocoModel.getSolver();
					 
					 startTime = System.nanoTime();
					 isConsistent = solver.solve();
					 endTime = System.nanoTime();
					 time +=  (float)(startTime - endTime);
					 
					 count++;
				 }
				
				 
				 // IS DIAGNOSIS EQUAL TO USER DIAGNOSIS
				 if (GADiagnosis!=null && userDiagnosis!=null && GADiagnosis.size()==userDiagnosis.size()){	
	 		
					 	for (int k=0;k<GADiagnosis.size();k++)
						{
							for (int u=0;u<GADiagnosis.size();u++){
								int varID_userDiagnosis =  userDiagnosis.get(u);
								int varID_ofDiagnoseAlgorithmArray =  GADiagnosis.get(k);
							  	
								if (varID_userDiagnosis == varID_ofDiagnoseAlgorithmArray){
									precision ++;
									break; // SEARCH FOR OTHER VARIABLES IN USER DIAG
								}
							
							}
						} 
					 	
						if(precision==GADiagnosis.size())
							precision = 1;
						else
							precision = 0;
				 	}
				
				 float[] resp = {precision,time};
				 return resp;
			 }

	 // returns precision btw 0..1 and time in negative value
	 public static float[] diagnoseCSP_FastDiag(CSP model, int [] variableOrder){
		 	 
			 //System.out.println("Diagnose with fastdiag for variable order"+variableOrder[0]+variableOrder[1]); 
		 	 float precision = (float) 0.0;
		 	 float time = (float)0;
		 	 
			 List<Constraint> orderedUserConstraints = new ArrayList<Constraint>();
			 List<Integer> userDiagnosis = new ArrayList<Integer>();
			 userDiagnosis = model.getDiagnoseOfUser();

			 List<Constraint> fastDiagDiagnosis  = new ArrayList<Constraint>();
			 
			 //System.out.println("Before FastDiag"); 
			 int prodConstSize = model.constraints_products.size();
			 
			 List<Constraint> AC = Arrays.asList(model.chocoModel.getCstrs());
			 //List<Constraint> C = Arrays.asList(model.chocoModel.getCstrs()).subList(prodConstSize,AC.size()-1);
			 
			 if(variableOrder!=null){
				 // SET THE ORDER OF USER CONSTRAINTS
				 for (int i=0;i<variableOrder.length;i++){
					 Constraint usercon = model.constraints_user.get(variableOrder[i]);
					 orderedUserConstraints.add(usercon);
//					 //System.out.println(usercon);
//					 for(int j=0;j<AC.size();j++){ 
//						 Constraint c1 = AC.get(j);
//						 //System.out.println(c1);
//						 if(c1.getName().equals(usercon.getName())){
//							 if(c1.getCidxInModel()==usercon.getCidxInModel()){
//								 orderedUserConstraints.add(c1);
//								 //System.out.println("User Constraint: "+usercon+"Model Constraint: "+c1);
//							 }
//						 }
//					 }
				 }
				 model.constraints_user = orderedUserConstraints;
			 }
				 
//			 // TEST
//		 		boolean res = model.chocoModel.getSolver().solve();
//		 		model.chocoModel.unpost(orderedUserConstraints.get(0));
//		 		model.chocoModel.getSolver().reset();
//		 		res = model.chocoModel.getSolver().solve();
//		 		model.chocoModel.unpost(orderedUserConstraints.get(1));
//		 		model.chocoModel.getSolver().reset();
//		 		res = model.chocoModel.getSolver().solve();
//		 		model.chocoModel.unpost(orderedUserConstraints.get(2));
//		 		model.chocoModel.getSolver().reset();
//		 		res = model.chocoModel.getSolver().solve();
//		 	 //
//			 
			 
			 // ADD FASTDIAG HERE
			 // fastDiagDiagnosis = FastDiag_Recursive.computeDiagnose(orderedUserConstraints, AC, model);
			 
			 //fastDiagDiagnosis = FastDiag.computeDiagnose(model,Constraints_Singleton.getInstance().getOriginalCSP());
			 long startTime = System.currentTimeMillis();
			 fastDiagDiagnosis = FastDiag.computeDiagnose(model,Constraints_Singleton.getInstance().getOriginalCSP());
			 long endTime = System.currentTimeMillis();
			 time =  (float)(startTime - endTime);
			  
			
			 if (fastDiagDiagnosis!=null && userDiagnosis!=null && fastDiagDiagnosis.size()==userDiagnosis.size()){	
//				 System.out.println("####################################");
//				 System.out.println("FAST DIAG ALGORITHM DIAGNOSIS EVALUATION");
//				 System.out.println("User Model ID: "+model.originalIndex);
//				 System.out.println("FastDiag Diagnosis Size: "+fastDiagDiagnosis.size());
//				 System.out.println("Users Diagnosis Size: "+userDiagnosis.size());
//			 		 
					 
			 		
				 	for (int k=0;k<fastDiagDiagnosis.size();k++)
					{
						for (int u=0;u<fastDiagDiagnosis.size();u++){
							int varID_userDiagnosis =  userDiagnosis.get(u);
							int varID_ofDiagnoseAlgorithmArray =  -1;
						    int constrID_ofDiagnoseAlgorithm = -1;
							try{
								constrID_ofDiagnoseAlgorithm = Integer.valueOf(fastDiagDiagnosis.get(k).getName());
								varID_ofDiagnoseAlgorithmArray = Constraints_Singleton.getInstance().getConstraintList_extension__UserRequirements().get(constrID_ofDiagnoseAlgorithm).getVar_1_ID();
										
								if (varID_userDiagnosis == varID_ofDiagnoseAlgorithmArray){
									precision ++;
									break; // SEARCH FOR OTHER VARIABLES IN USER DIAG
								}
										 
							}catch(Exception ex){}
									
						}
					} 
				 	
					if(precision==fastDiagDiagnosis.size())
						precision = 1;
					else
						precision = 0;
					
			 	}
			
//			 	System.out.println("User Diagnosis: ");
//			 	for(int u=0;u<userDiagnosis.size();u++){System.out.print(" var-"+userDiagnosis.get(u));}
//			 	System.out.println();
//			 	
//			 	System.out.println("FastDiag Diagnosis: ");
//			 	for(int f=0;f<varID_ofDiagnoseAlgorithmArray.length;f++){System.out.print(" var-"+varID_ofDiagnoseAlgorithmArray[f]);}
//			 	System.out.println();
//			     
//				System.out.println("Precision: "+precision);
//				System.out.println("####################################");
			
			 float[] resp = {precision,time};
			 return resp;
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
				     if(count==1000){
					     System.out.println("evaluateFitnessValueOfCluster: count#"+count);
					     System.out.println("clusterIndex: "+clusterIndex);
					     count=0;
				     }
				     count++;
				     
				  	 //long startTime = System.nanoTime();
				     int modelIndex = clusters[clusterIndex][md];
				     if(modelIndex==0)
				    	 modelIndex=0;
					 //CSP userModel = cspList.get(modelIndex);
				     CSP userModel = new CSP(2, null,Constraints_Singleton.getInstance().getCSPs_tobe_Clustered().get(modelIndex),null,0,0,null);
					 //System.out.println("evaluateFitnessValueOfCluster: Model#"+modelIndex);
					 float fitnessValueOfOrder = 0;
					 
					 switch(type){
					 	
					 	// RETURNS DIAGNOSIS PRECISION OF FASTDIAG WITH VAR ORDER (0..1)
					 	case 0:
					 		fitnessValueOfOrder = diagnoseCSP_FastDiag(userModel,variableOrder)[0];
					 		break;
					 		
					 	// RETURNS DIAGNOSIS PERFORMANCE OF FASTDIAG WITH VAR ORDER (negative value of the executiontime)
					 	case 1:
					 		fitnessValueOfOrder = diagnoseCSP_FastDiag(userModel,variableOrder)[1];
					 		break;
					 		
					 	// RETURNS DIAGNOSIS PRECISION OF GENETIC ALGORITHM WITH VAR ORDER (0..1)
					 	case 3:
					 		fitnessValueOfOrder = diagnoseCSP_GA4D(userModel,variableOrder)[0];
					 		break;
					 		
					 	// RETURNS DIAGNOSIS PERFORMANCE OF GENETIC ALGORITHM WITH VAR ORDER (negative value of the executiontime)
					 	case 4:
					 		fitnessValueOfOrder = diagnoseCSP_GA4D(userModel,variableOrder)[1];
					 		break;
					 		
					 	default:
					 		fitnessValueOfOrder = solveCSPwithChoco(userModel,variableOrder);
					 		break;
					 }
					
					 totalFitnessValueForCluster += (fitnessValueOfOrder);
			  }
			  
			  fitness = totalFitnessValueForCluster/(clusters[clusterIndex].length);
			
			  return fitness;
		
	 }
	 	
	 
	 public static boolean isPredictionCorrect(CSP userModel, List<Constraint> diagnosis){
		 
		 List<Integer> productIDList = new ArrayList<Integer>();
		 CSP cspAfterDiagnose = FastDiag.subtractConstraints(userModel, userModel.constraints_user, diagnosis);
		 cspAfterDiagnose.chocoModel.getSolver().reset();
		 
		 while(cspAfterDiagnose.chocoModel.getSolver().solve()){
			 productIDList.add(cspAfterDiagnose.chocoModel.getVar(0));
		 }
		 
		 
		 return false;
	 }

//	 // returns time in negative value
//	 public static float diagnoseCSPwithFastDiag_Time(CSP model, int [] variableOrder){
//			 
//			 	 
//				 //System.out.println("Diagnose with fastdiag for variable order"+variableOrder[0]+variableOrder[1]); 
//			 	 float time = (float)0;
//				 List<Constraint> orderedUserConstraints = new ArrayList<Constraint>();
//				 List<Integer> userDiagnosis = new ArrayList<Integer>();
//				 userDiagnosis = model.getDiagnoseOfUser();
//
//				 List<Constraint> fastDiagDiagnosis  = new ArrayList<Constraint>();
//				 
//				 //System.out.println("Before FastDiag"); 
//				 int prodConstSize = model.constraints_products.size();
//				 
//				 List<Constraint> AC = Arrays.asList(model.chocoModel.getCstrs());
//				 
//
//				 if(variableOrder!=null){
//					 // SET THE ORDER OF USER CONSTRAINTS
//					 for (int i=0;i<variableOrder.length;i++){
//						 Constraint usercon = model.constraints_user.get(variableOrder[i]);
//						 orderedUserConstraints.add(usercon);
////						 //System.out.println(usercon);
////						 for(int j=0;j<AC.size();j++){ 
////							 Constraint c1 = AC.get(j);
////							 //System.out.println(c1);
////							 if(c1.getName().equals(usercon.getName())){
////								 if(c1.getCidxInModel()==usercon.getCidxInModel()){
////									 orderedUserConstraints.add(c1);
////									 //System.out.println("User Constraint: "+usercon+"Model Constraint: "+c1);
////								 }
////							 }
////						 }
//					 }
//					
//					 // ADD FASTDIAG HERE
//					 // fastDiagDiagnosis = FastDiag_Recursive.computeDiagnose(orderedUserConstraints, AC, model);
//					 model.constraints_user = orderedUserConstraints;
//				 }
//				 
//				 long startTime = System.currentTimeMillis();
//				 fastDiagDiagnosis = FastDiag.computeDiagnose(model,Constraints_Singleton.getInstance().getOriginalCSP());
//				 long endTime = System.currentTimeMillis();
//				 time =  startTime - endTime;
//				
//				 
//				 return (float)time;
//			 }
//		 
}
