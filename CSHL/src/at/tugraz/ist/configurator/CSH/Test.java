package at.tugraz.ist.configurator.CSH;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.log4j.Logger;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;
import org.chocosolver.parser.flatzinc.BaseFlatzincListener;
import org.chocosolver.parser.flatzinc.Flatzinc;
import org.chocosolver.parser.flatzinc.FznSettings;
import org.chocosolver.parser.flatzinc.ast.Datas;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.solver.variables.Variable;
import org.chocosolver.util.criteria.Criterion;

import com.google.common.base.Stopwatch;

import at.tugraz.ist.configurator.CSH.GeneticAlgorithm.Algorithm;
import at.tugraz.ist.configurator.CSH.GeneticAlgorithm.FitnessCalc;
import at.tugraz.ist.configurator.CSH.GeneticAlgorithm.Individual;
import at.tugraz.ist.configurator.CSH.GeneticAlgorithm.Population;
import at.tugraz.ist.configurator.CSH.chocoModels.UserModel;
import at.tugraz.ist.configurator.CSH.fileOperations.CSVUtils;

import org.chocosolver.parser.flatzinc.layout.SolutionPrinter;

import org.chocosolver.solver.search.strategy.selectors.values.*;
import org.chocosolver.solver.search.strategy.selectors.variables.AntiFirstFail;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;

import static java.lang.System.out;
import static org.chocosolver.solver.search.strategy.Search.*;
import org.chocosolver.solver.search.strategy.assignments.DecisionOperator;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.search.strategy.strategy.IntStrategy;


import java.io.File;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.FileHandler;

public class Test {
	
	 //public static IntVar[][] vars;
	 public static int numberOfmodels = 100; 
	 public static int maxDomainSize = 1000;
	 public static int numberOfvars = 1000;
	 public static int numberOfclusters = 4;
	 public static int [][] clusters;
	 public static List <UserModel> modelsOfTheSameProblem;
	 public static List<String> heuristics; 
	 public static int sizeOfPopulation = 3;
	 public static int testSize = 100;
	 public static int currentTest = 0;
	 
	 
	 //public List<Long> statisticsOfModel;
	 public static List<List<Long>> statisticsOfClusters;
	 
	 
	 // variable order for each cluster
	 public static List<int[]> ordersOfVariables;
	 
	 // value order for each domain for each cluster
	 public static List<int[][]> ordersOfValues;
	 
	 
	 public static VariableSelector variableOrderingHeuristic;
	 public static IntValueSelector valueOrderingHeuristic;
	
	 public static String modelsName = "";
	 
	 public static FileWriter writer =null;
	 
	 public static void main(String []args){
		 //testFZNfiles();
		 //testKMeans();
		 //testKMeans2();
		 for(int i=0;i<testSize;i++){
			 testHeuristic();
			 currentTest++;
		 }
		 //getOrders();
	 }
	 
	 public static int[] geneToOrder(byte[]gene){
		 // input : 010 01 1 -> v0:2, v1:1, v2:3
		 // output: 102 -> order or variables
		 int [] result = new int[numberOfvars];
		 int index=0;
		 int order=0;
		 boolean [] orders = new boolean[numberOfvars];
		 Arrays.fill(orders, Boolean.FALSE);
		 
		 for(int i=0;i<numberOfvars;i++){
			 // 5, 4 ,3, 2, 1
			 int readNumberOfBytes = numberOfvars-i;
			
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
			  for (int md=0;md<clusters[clusterIndex].length-1;md++){
					 int modelIndex = clusters[clusterIndex][md];
					 UserModel model = modelsOfTheSameProblem.get(modelIndex);
					 Solver solver = model.chocoModel.getSolver();
			    	 
					 // getHeuristics
					 solver = getHeuristics(model, solver,-1,variableOrder);
					 
					 solver.solve();
					 totalRunningTimeForCluster += solver.getTimeCountInNanoSeconds();
				     //solver.printStatistics();
			  }
			  
			  // bu gen icin bu clusterda hesaplanan ortalama running time
			  if (clusters[clusterIndex].length-1>0){
				  myPop.getIndividual(i).setFitness(totalRunningTimeForCluster/(clusters[clusterIndex].length-1));
				  //System.out.println("GENE #"+indivdiaulIndex+" bu gen icin bu clusterda hesaplanan ortalama running time: "+clusterIndex+" :"+totalRunningTimeForCluster/(clusters[clusterIndex].length-1));
			  }
		 }
	 }

	 public static long testIndividualOverCluster(Individual ind, int clusterIndex){
		  //System.out.println("in testIndividualOverCluster: Cluster#"+clusterIndex);

		 if (clusters[clusterIndex].length-1<=0)
			  return 0;
		  
		  	long totalRunningTimeForCluster = 0;
		  	long fitness = 0;
		  // INDIVIDUAL
			  int [] variableOrder = ind.getGenes();
				 
			  // MODEL
			  // run CSP over the models except the last one and take avg time
			  for (int md=0;md<clusters[clusterIndex].length-1;md++){
				  	 long startTime = System.nanoTime();
				     int modelIndex = clusters[clusterIndex][md];
					 UserModel userModel = modelsOfTheSameProblem.get(modelIndex);
					 Solver solver = userModel.chocoModel.getSolver();
			    	 
					 // getHeuristics
					 solver = getHeuristics(userModel, solver,-1,variableOrder);
					 
					 solver.solve();
					 long endTime = System.nanoTime();
					 totalRunningTimeForCluster += (endTime - startTime);
				     //solver.printStatistics();
			  }
			  
			  // bu gen icin bu clusterda hesaplanan ortalama running time
			  fitness = totalRunningTimeForCluster/(clusters[clusterIndex].length-1);
				  //System.out.println("GENE #"+ind.getGenes()+" bu gen icin bu clusterda hesaplanan ortalama running time: "+clusterIndex+" :"+totalRunningTimeForCluster/(clusters[clusterIndex].length-1));
			  String geneStr ="";
			  for(int i=0;i<numberOfvars;i++){
				  geneStr += ind.getGenes()[i];
			  }
			  //System.out.println("GENE: "+geneStr+", Fitness +:"+fitness);
			  return fitness;
		
	 }
	 
	 public static void getOrders(){
		 //System.out.println("in getOrders");
		 //Find best variable ordering
		 int sizeOfGene = numberOfvars;
		 ordersOfVariables = new ArrayList<int[]>(numberOfclusters);
		 for(int v=0;v<numberOfclusters;v++){
			 ordersOfVariables.add(new int[numberOfvars]);
		 }
		 
		 // set target time (CSP running time)
		 // 0.015 ms
		 FitnessCalc.setTarget(10000);
		 
		 // FIND VARIABLE AND VALUE ORDERING FOR EACH CLUSTER
		 // CLUSTER
		 for (int cl=0;cl<numberOfclusters;cl++){
			 
			 //System.out.println("CLUSTER #"+cl);
			 
			 if(clusters[cl].length<2){
				 continue;
			 }
			 // create population for each cluster
			 
			 
			 Population myPop = new Population(sizeOfPopulation,sizeOfGene,true,cl);
			 
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
			   myPop = Algorithm.evolvePopulation(myPop,cl,maxDomainSize); 
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
			 ordersOfVariables.set(cl, varOrder);
		 }
		 
	 }
	 
	 public static void testHeuristic(){
		 
		 heuristics = new ArrayList<String>(6);
		 heuristics.add("No Heuristics");
		 heuristics.add("FirstFail and IntDomainMin");
		 heuristics.add("AntiFirstFail and IntDomainMin");
		 heuristics.add("FirstFail and IntDomainMax");
		 heuristics.add("AntiFirstFail and IntDomainMax");
		 heuristics.add("CHSL and IntDomainMin");
	     
		
		 // getModelsforProblem(int numberOfVars,int numberOfModels, int maxDomainSize)
		 modelsOfTheSameProblem = new ArrayList<UserModel>(numberOfmodels);
		 getModelsforProblem(numberOfvars, numberOfmodels, maxDomainSize);
		 
		 // apply clustering
		 testKMeans2();
		 
		 // get clusters
		 getClusters();
		 //System.out.println("Clusters are calculated with sizes"+clusters[0].length+clusters[1].length+clusters[2].length+clusters[3].length);
		 
		 // get var and value orders for each cluster
		 getOrders();
		 //System.out.println("Orders are calculated");
		 
		 long startTime = 0;
	     long endTime = 0;
		 long execTime = 0;
		 
		 statisticsOfClusters = new ArrayList<List<Long>>(numberOfclusters);
		 
		 // test clusters: without heuristic, with fixed heuristic, with learned heuristic	 
		 for (int k=0;k<numberOfclusters;k++){
			 
			
			 int numberOfModelsInTheCluster = clusters[k].length;
			 List<Long> statisticsOfModel = new ArrayList<Long>(heuristics.size());
			 
			 for (int i=0; i<numberOfModelsInTheCluster;i++){
				 
				 int modelIndex = clusters[k][i];
				 UserModel model = new UserModel(modelsOfTheSameProblem.get(i).name,modelsOfTheSameProblem.get(i).vars,modelsOfTheSameProblem.get(i).ifcont,modelsOfTheSameProblem.get(i).thencont);
				 UserModel model2 = new UserModel(modelsOfTheSameProblem.get(i).name,modelsOfTheSameProblem.get(i).vars,modelsOfTheSameProblem.get(i).ifcont,modelsOfTheSameProblem.get(i).thencont);
				 UserModel model3 = new UserModel(modelsOfTheSameProblem.get(i).name,modelsOfTheSameProblem.get(i).vars,modelsOfTheSameProblem.get(i).ifcont,modelsOfTheSameProblem.get(i).thencont);
				 UserModel model4 = new UserModel(modelsOfTheSameProblem.get(i).name,modelsOfTheSameProblem.get(i).vars,modelsOfTheSameProblem.get(i).ifcont,modelsOfTheSameProblem.get(i).thencont);
				 UserModel model5 = new UserModel(modelsOfTheSameProblem.get(i).name,modelsOfTheSameProblem.get(i).vars,modelsOfTheSameProblem.get(i).ifcont,modelsOfTheSameProblem.get(i).thencont);
				 UserModel model6 = new UserModel(modelsOfTheSameProblem.get(i).name,modelsOfTheSameProblem.get(i).vars,modelsOfTheSameProblem.get(i).ifcont,modelsOfTheSameProblem.get(i).thencont);
				 
				 
				 statisticsOfModel = new ArrayList<Long>(heuristics.size());
				 
				
				 
				 // 1- SOLVE WITHOUT HEURISTICS
				 Solver solver = model.chocoModel.getSolver();
			   
			     startTime = System.nanoTime();
			     solver.solve();
			     endTime = System.nanoTime();
				 execTime = endTime - startTime;
				 // record last user
				 if (i==numberOfModelsInTheCluster-1 && numberOfModelsInTheCluster>1){
					 statisticsOfModel.add(execTime);
					 String measures1 = solver.getMeasures().toOneLineString();
					 System.out.println("Cluster-"+k+", "+measures1);
				 }
				 
				 
				 // 2- SOLVE WITH HEURISTIC
				 Solver solver2 = model2.chocoModel.getSolver();
				 solver2.setSearch(intVarSearch(
		                 
						 // selects the variable of smallest domain size
		                 new FirstFail(model2.chocoModel),
			    		 //new AntiFirstFail(model),
		                 
		                 // selects the smallest domain value (lower bound)
		                 new IntDomainMin(),
		                 //new IntDomainMax(),
		                
		                 // variables to branch on
		                 model2.vars[0],model2.vars[1], model2.vars[2],model2.vars[3],model2.vars[4]
				  ));
				
			     
			     startTime = System.nanoTime();
			     solver2.solve();
			     endTime = System.nanoTime();
				 execTime = endTime - startTime;
				
				 // record last user
				 if (i==numberOfModelsInTheCluster-1 && numberOfModelsInTheCluster>1){
					 statisticsOfModel.add(execTime);
					 String measures1 = solver2.getMeasures().toOneLineString();
					 System.out.println("Cluster-"+k+", "+measures1);
				 }
				 
				 
				 
				// 3- SOLVE WITH HEURISTIC
				 Solver solver4 = model4.chocoModel.getSolver();
				 solver4.setSearch(intVarSearch(
		                 
						 // selects the variable of smallest domain size
		                 new AntiFirstFail(model4.chocoModel),
			    		 //new AntiFirstFail(model),
		                 
		                 // selects the smallest domain value (lower bound)
		                 new IntDomainMin(),
		                 //new IntDomainMax(),
		                
		                 // variables to branch on
		                 model4.vars[0],model4.vars[1], model4.vars[2],model4.vars[3],model4.vars[4]
				  ));
				
			     
			     startTime = System.nanoTime();
			     solver4.solve();
			     endTime = System.nanoTime();
				 execTime = endTime - startTime;
				
				 // record last user
				 if (i==numberOfModelsInTheCluster-1 && numberOfModelsInTheCluster>1){
					 statisticsOfModel.add(execTime);
					 String measures1 = solver4.getMeasures().toOneLineString();
					 System.out.println("Cluster-"+k+", "+measures1);
				 }
				 
				// 4- SOLVE WITH HEURISTIC
				 Solver solver5 = model5.chocoModel.getSolver();
				 solver5.setSearch(intVarSearch(
		                 
						 // selects the variable of smallest domain size
		                 new FirstFail(model5.chocoModel),
			    		 //new AntiFirstFail(model),
		                 
		                 // selects the smallest domain value (lower bound)
		                 //new IntDomainMin(),
		                 new IntDomainMax(),
		                
		                 // variables to branch on
		                 model5.vars[0],model5.vars[1], model5.vars[2],model5.vars[3],model5.vars[4]
				  ));
				
			     
			     startTime = System.nanoTime();
			     solver5.solve();
			     endTime = System.nanoTime();
				 execTime = endTime - startTime;
				
				 // record last user
				 if (i==numberOfModelsInTheCluster-1 && numberOfModelsInTheCluster>1){
					 statisticsOfModel.add(execTime);
					 String measures1 = solver5.getMeasures().toOneLineString();
					 System.out.println("Cluster-"+k+", "+measures1);
				 }
				 
				// 5- SOLVE WITH HEURISTIC
				 Solver solver6 = model6.chocoModel.getSolver();
				 solver6.setSearch(intVarSearch(
		                 
						 // selects the variable of smallest domain size
		                 new AntiFirstFail(model6.chocoModel),
			    		 //new AntiFirstFail(model),
		                 
		                 // selects the smallest domain value (lower bound)
		                 //new IntDomainMin(),
		                 new IntDomainMax(),
		                
		                 // variables to branch on
		                 model6.vars[0],model6.vars[1], model6.vars[2],model6.vars[3],model6.vars[4]
				  ));
				
			     
			     startTime = System.nanoTime();
			     solver6.solve();
			     endTime = System.nanoTime();
				 execTime = endTime - startTime;
				
				 // record last user
				 if (i==numberOfModelsInTheCluster-1 && numberOfModelsInTheCluster>1){
					 statisticsOfModel.add(execTime);
					 String measures1 = solver6.getMeasures().toOneLineString();
					 System.out.println("Cluster-"+k+", "+measures1);
				 }
				 
				 // LAST- SOLVE WITH GENETIC ALGORITHM FOR LAST USER
				 if (i==numberOfModelsInTheCluster-1 && numberOfModelsInTheCluster>1){
			    	 Solver solver3 = model3.chocoModel.getSolver();
			    	 // getHeuristics
					 solver3 = getHeuristics(model3,solver3,k,null);
					 
					 
					 startTime = System.nanoTime();
				     solver3.solve();
				     endTime = System.nanoTime();
					 execTime = endTime - startTime;
					 statisticsOfModel.add(execTime);
					 
					 
					 String measures1 = solver3.getMeasures().toOneLineString();
					 System.out.println("Cluster-"+k+", "+measures1);
			     }
				 
				
			 }
			 // statistics for cluster
		     statisticsOfClusters.add(statisticsOfModel);
			
		 }
		 
		 System.out.println("##############################################################################");
		 System.out.println("### Number Of Clusters\t\t: "+numberOfclusters);
		 for(int k =0;k<numberOfclusters;k++){
			 System.out.println("### Number Of Models in Cl-" +(k+1)+"\t: "+clusters[k].length);
		 }
		
		 System.out.println("### Number Of Models (Users) \t: "+numberOfmodels);
		 System.out.println("### Number Of Variables \t: "+numberOfvars);
		 System.out.println("### Max Domain Size \t\t: "+maxDomainSize);
		 System.out.println("### Number Of Heuristic \t:"+heuristics.size());
		 for(int h =0;h<heuristics.size();h++){
			 System.out.println("### Heuristic-" +(h+1)+"  \t\t: "+heuristics.get(h));
		 }
		 System.out.println("##############################################################################");
		 System.out.println("################################### RESULTS ##################################");
		
		 String firstLine = "################\t";
		 for(int h =0;h<heuristics.size();h++){
			 firstLine += (h+1) +"\t";
		 }
		 System.out.println(firstLine);
		 
		 for(int k =0;k<numberOfclusters;k++){
			 String toPrint = "### Cluster-"+(k+1)+"\t:\t";
			 List <String> linetoCSV = new ArrayList<>();
			 for(Long element:statisticsOfClusters.get(k)){
				 toPrint += element+"\t";
				 linetoCSV.add(element.toString());
			 }
			 System.out.println(toPrint);
			 
			 boolean lastline = false;
			 if(currentTest==(testSize-1)&& k==numberOfclusters-1)
				 lastline=true;
			 writeCSV(linetoCSV,lastline);
		 }
		 System.out.println("##############################################################################");
		 
	 }
	 public static void writeCSV(List<String> line, boolean last){
		 try {
			 File file = new File("kmeans2\\seda\\outputs\\TestHeuristics.csv");
				// if file doesnt exists, then create it
			 if (!file.exists()) {
					file.createNewFile();
					
			 }
			 if(writer==null)
				 writer = new FileWriter(file.getPath());
			 
			 CSVUtils.writeLine(writer,line);

			 if(last){
			     writer.flush();
			     writer.close();
			 }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	 }
	 
	 public static void testKMeans2(){
		 	try {
		        /* Load a dataset */
		        Dataset data;
				
				data = FileHandler.loadDataset(new File("kmeans2/seda/inputs/"+modelsName+".data"), numberOfvars, ",");
				
		        /*
		         * Create a new instance of the KMeans algorithm, with no options
		         * specified. By default this will generate 4 clusters.
		         */
		        Clusterer km = new KMeans();
		        /*
		         * Cluster the data, it will be returned as an array of data sets, with
		         * each dataset representing a cluster
		         */
		        Dataset[] clusters = km.cluster(data);
		        System.out.println("Cluster count: " + clusters.length);
		        for(int i=0;i<clusters.length;i++){
		        	
		        	boolean dir = new File("kmeans2/seda/outputs/"+modelsName).mkdir();
		        	File file = new File("kmeans2/seda/outputs/"+modelsName+"/Cluster"+i+".txt");

					// if file doesnt exists, then create it
					if (!file.exists()) {
						file.createNewFile();
					}

		        	FileHandler.exportDataset(clusters[i],file);
		        }
		        
		        
		 	} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }

	 public static List<UserModel> getModelsforProblem(int numberOfVars,int numberOfModels, int maxDomainSize){
		 
		 Model testmodel = new Model();
		 
		 
		 modelsOfTheSameProblem = new ArrayList<UserModel>(numberOfModels);
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
			 
			Constraint ifcont = testmodel.arithm(varstest[numberOfvars-1],"<",varstest[numberOfvars-2]);
			Constraint thencont = testmodel.arithm(varstest[numberOfvars-1],"=",varstest[numberOfvars-3]);
			
			UserModel usermodel = new UserModel("Model#"+ i,varstest,ifcont,thencont);
			
				// System.out.println(model.getCstrs()[0].toString());
			
			modelsOfTheSameProblem.add(usermodel);
			//System.out.println("##########");
		 }
		 //System.out.println("#######################################");
		 java.util.Date date= new java.util.Date();
		 long time = date.getTime();
		 modelsName = "SedasTestModels-"+time;
		 
		 writeToFile();
		 return modelsOfTheSameProblem;
	 }
	 
	 public static void writeToFile(){
		    List<String> lines = new ArrayList<String>();
		    String str = "";
		    int val = -1000000;
		    int size = -1;
		    
		    for (int i=0;i<numberOfmodels;i++){
		    	str = "";
		   		for(int j=0;j<numberOfvars;j++){
		   			size = modelsOfTheSameProblem.get(i).vars[j].getDomainSize();
		   			if(size==1)
		   				val = modelsOfTheSameProblem.get(i).vars[j].getValue();
		   			else
		   				val = -1000000;
		   			str += val+",";
		   		}
		   		str += i+"\n";
		   		lines.add(str);
		    }
			try {
				String basePath = new File("").getAbsolutePath();
				System.out.println(basePath);
				
				File file = new File("kmeans2\\seda\\inputs\\"+modelsName+".data");

				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file.getAbsolutePath());
				BufferedWriter bw = new BufferedWriter(fw);
				
				for (int i=0;i<lines.size();i++){
					 bw.append(lines.get(i));
				 }
				bw.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		
	 }

	 public static void getClusters(){
		 
		 // "kmeans2/seda/outputs/"+modelsName+"/Cluster"+i+".txt"
		 clusters = new int [numberOfclusters][];
		 
		 for (int i=0;i<numberOfclusters;i++){
			 List<Integer> indexes = new ArrayList<Integer>();
			 
			 try {
				 BufferedReader br = new BufferedReader(new FileReader("kmeans2/seda/outputs/"+modelsName+"/Cluster"+i+".txt"));
			     StringBuilder sb = new StringBuilder();
			     
			     String line = br.readLine();
	
			     while (line != null) {
			         sb.append(line);
			         sb.append(System.lineSeparator());
			         int val = Integer.valueOf(line.split("\t")[0]);
			         indexes.add(val);
			         
			         // read next string
			         line = br.readLine();
			     }
			     clusters[i]= new int[indexes.size()];
			     for(int m=0;m<indexes.size();m++){
			    	 clusters[i][m]=indexes.get(m);
			     }
			     String everything = sb.toString();
			     br.close();
			 }
			 catch(Exception e){
				 int z =0;
			 }
		 }
	 }

	 public static Solver getHeuristics(UserModel userModel, Solver solver, int ClusterIndex, int[] varOrder){
		 
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
	 
	 
	 //	 public static void generateSeqFile(){
//		 try {
//		    Configuration conf = new Configuration();
//		    Job job;
//			
//			job = new Job(conf);
//			
//		    job.setJobName("Convert Text");
//		    job.setJarByClass(Mapper.class);
//	
//		    job.setMapperClass(Mapper.class);
//		    job.setReducerClass(Reducer.class);
//	
//		    // increase if you need sorting or a special number of files
//		    job.setNumReduceTasks(0);
//	
//		    job.setOutputKeyClass(LongWritable.class);
//		    job.setOutputValueClass(Text.class);
//	
//		    job.setOutputFormatClass(SequenceFileOutputFormat.class);
//		    job.setInputFormatClass(TextInputFormat.class);
//	
//		  
//			TextInputFormat.addInputPath(job, new Path("kmeans\\preinput"));
//			SequenceFileOutputFormat.setOutputPath(job, new Path("kmeans\\input"));
//					
//			// submit and wait for completion
//			job.waitForCompletion(true);
//				    
//		 } catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		  
//	   
//	 }
//	 
//	 public static void testFZNfiles(){
//		 List <String> filenames = new ArrayList<String>();
//		 //Logger log = Logger.getLogger(Test.class.getName());
//		 long time1=0;
//		 long time2=0;
//
//		 // GET ALL TEST DATA FILES
//		 try {
//			Files.walk(Paths.get("testData\\mzn")).forEach(filePath -> {
//				    if (Files.isRegularFile(filePath)) {
//				    	filenames.add(filePath.toString());
//				        System.out.println(filePath);
//				    }
//				});
//			
//		 } catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//		 }
//		 
//		 for(int i=0;i<filenames.size();i++){
//			 try {
//				   //log.info(filenames.get(i));
//				   Flatzinc fzn = new Flatzinc();
//				   fzn.addListener(new BaseFlatzincListener(fzn));
//				   String[] input = new String [1];
//				   //log.info("TEST DATA : "+ filenames.get(i));
//				   time1 = System.nanoTime();
//				   input[0] = filenames.get(i);
//				   fzn.parseParameters(input);
//				   FznSettings settings = new FznSettings();
//				 
//				   fzn.defineSettings(new FznSettings());
//				   fzn.createSolver();
//				  
//				   fzn.parseInputFile();
//					
//				   fzn.configureSearch();
//	
//				   // now you can access your model/solver and add a few things (variables, constraints, monitors, etc.)
//	
//				   fzn.solve(); // you might want to skip this and trigger the solving process yourself from the Solver object.
//				   time2 = System.nanoTime();
//				   //log.info("TEST DATA : "+ filenames.get(i)+" resulted in "+(time2-time1)+" nanoseconds");
//			 } catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//	 }
//
//	 public static void testKMeans(){
//		 
//		 double convergenceDelta = 0.001;
//		 int maxIterations = 2;
//		 boolean runClustering = true;
//		 double  clusterClassificationThreshold = 0.5;
//		 boolean runSequential = true;
//		 Path inputPath = new Path("kmeans\\input");
//		 Path clustersInPath = new Path("kmeans\\clustersIn");
//		 Path outputPath = new Path("kmeans\\output");
//		 DistanceMeasure measure = (DistanceMeasure) new ManhattanDistanceMeasure();
//		 
//		 try {
//			 generateSeqFile();
//			 // run the CanopyDriver job
//			 CanopyDriver.run(inputPath, clustersInPath, measure, (double) 3.1, (double) 2.1, runClustering, clusterClassificationThreshold, runSequential);
//			 //run(inputPath,clustersInPath, ManhattanDistanceMeasure.class.getName(), (float) 3.1, (float) 2.1, false);
//			
//			 KMeansDriver.run(inputPath, clustersInPath, outputPath, convergenceDelta, maxIterations, runClustering, clusterClassificationThreshold, runSequential);
//		
//		    
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		 
//	 }
//	 

}
