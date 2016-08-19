package at.tugraz.ist.configurator.CSHL;

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
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.solver.variables.Variable;
import org.chocosolver.util.criteria.Criterion;

import com.google.common.base.Stopwatch;

import at.tugraz.ist.configurator.CSHL.SimpleGa.Algorithm;
import at.tugraz.ist.configurator.CSHL.SimpleGa.FitnessCalc;
import at.tugraz.ist.configurator.CSHL.SimpleGa.Individual;
import at.tugraz.ist.configurator.CSHL.SimpleGa.Population;

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
	
	 public static IntVar[][] vars;
	 public static int numberOfmodels = 10; 
	 public static int numberOfvars = 5;
	 public static int numberOfclusters = 4;
	 public static int [][] clusters;
	 public static List <Model> modelsOfTheSameProblem;
	 
	 // variable order for each cluster
	 public static List<int[]> ordersOfVariables;
	 
	 // value order for each domain for each cluster
	 public static List<int[][]> ordersOfValues;
	 
	 
	 public static VariableSelector variableOrderingHeuristic;
	 public static IntValueSelector valueOrderingHeuristic;
	
	 public static String modelsName = "";
	 
	 public static void main(String []args){
		 //testFZNfiles();
		 //testKMeans();
		 //testKMeans2();
		 
		 testHeuristic();
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
		  System.out.println("in testPopulationOverCluster: Cluster#"+clusterIndex);
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
					 Model model = modelsOfTheSameProblem.get(modelIndex);
					 Solver solver = model.getSolver();
			    	 
					 // getHeuristics
					 solver = getHeuristics(modelIndex, solver,-1,variableOrder);
					 
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
					 Model model = new Model();
					 model = modelsOfTheSameProblem.get(modelIndex);
					 Solver solver = model.getSolver();
			    	 
					 // getHeuristics
					 solver = getHeuristics(modelIndex, solver,-1,variableOrder);
					 
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
			  System.out.println("GENE: "+geneStr+", Fitness +:"+fitness);
			  return fitness;
		
	 }
	 
	 public static void getOrders(){
		 System.out.println("in getOrders");
		 //Find best variable ordering
		 int sizeOfGene = numberOfvars;
		 int sizeOfPopulation = 120;
		 ordersOfVariables = new ArrayList<int[]>(numberOfclusters);
		 for(int v=0;v<numberOfclusters;v++){
			 ordersOfVariables.add(new int[numberOfvars]);
		 }
		 
		 // set target time (CSP running time)
		 // 0.015 ms
		 FitnessCalc.setTarget(1);
		 
		 // FIND VARIABLE AND VALUE ORDERING FOR EACH CLUSTER
		 // CLUSTER
		 for (int cl=0;cl<numberOfclusters;cl++){
			 
			 System.out.println("CLUSTER #"+cl);
			 
			 if(clusters[cl].length<2){
				 continue;
			 }
			 // create population for each cluster
			 
			 
			 Population myPop = new Population(sizeOfPopulation,sizeOfGene,true,cl);
			 
			 // testPopulationOverCluster(myPop,cl);
			  
			 int generationCount = 0; 
			 
			 //while(myPop.getFittest().getFitness() > FitnessCalc.getMaxFitness()){ 
			 while(generationCount<0){
			   generationCount++; 
			   System.out.println("Generation: "+generationCount+" Fittest: "+myPop.getFittest().getFitness()); 
			   
			   System.out.println("Start evolvePopulation");
			   // generate new population for better results
			   myPop = Algorithm.evolvePopulation(myPop,cl); 
			   System.out.println("End evolvePopulation");
			   // apply new test over new population
			   // testPopulationOverCluster(myPop,cl);
			 } 
			 
			 // find solution for this cluster
			 System.out.println("Ordering Found: Cluster#"+cl);
			 System.out.println("Solution found!"); 
			 System.out.println("Generation: "+generationCount); 
			 System.out.println("Genes:"); 
			 System.out.println(myPop.getFittest()); 
			 
			 int [] varOrder = myPop.getFittest().getGenes();
			 // SET VARIABLE ORDER FOR THIS CLUSTER
			 //ordersOfVariables.set(cl, new int[numberOfvars]);
			 //ordersOfVariables.add(new int[numberOfvars]);
			 ordersOfVariables.set(cl, varOrder);
		 }
		 
	 }
	 
	 public static void testHeuristic(){
		
		 vars = new IntVar[numberOfmodels][];
		
		 int maxDomainSize = 50;
		 
		 // getModelsforProblem(int numberOfVars,int numberOfModels, int maxDomainSize)
		 modelsOfTheSameProblem = getModelsforProblem(numberOfvars, numberOfmodels, maxDomainSize);
		 
		 // apply clustering
		 testKMeans2();
		 
		 // get clusters
		 getClusters();
		 System.out.println("Clusters are calculated with sizes"+clusters[0].length+clusters[1].length+clusters[2].length+clusters[3].length);
		 
		 // get var and value orders for each cluster
		 getOrders();
		 System.out.println("Orders are calculated");
		 
		 // test clusters: without heuristic, with fixed heuristic, with learned heuristic	 
		 for (int k=0;k<numberOfclusters;k++){
			 
			 System.out.println("#######################################");
			 System.out.println("#########    CLUSTER   "+k+"     ########");
			 System.out.println("#######################################");
			 
			 int numberOfModelsInTheCluster = clusters[k].length;
			 
			 for (int i=0; i<numberOfModelsInTheCluster;i++){
				 
				 int modelIndex = clusters[k][i];
				 Model model = modelsOfTheSameProblem.get(i);
				 Model model2 = modelsOfTheSameProblem.get(i);
				 Model model3 = modelsOfTheSameProblem.get(i);
				 
				 Solver solver = model.getSolver();
				 //solver.setSearch(minDomLBSearch(vars));
				 System.out.println("#######################################");
				 System.out.println("#########    MODEL   "+modelIndex+"     ########");
				 System.out.println("#######################################");
				 
				 for(int j=0;j<numberOfvars;j++){
					 System.out.println(vars[modelIndex][j]);
				 }
				 System.out.println(model.getCstrs()[0].toString());
				 
				 System.out.println("#######################################");
			     System.out.println("SOLVER with Heuristic: <select variable: FirstFail> <select value:IntDomainMin>  ");
			     solver.setSearch(intVarSearch(
		                 
						 // selects the variable of smallest domain size
		                 new FirstFail(model2),
			    		 //new AntiFirstFail(model),
		                 
		                 // selects the smallest domain value (lower bound)
		                 new IntDomainMin(),
		                 //new IntDomainMax(),
		                
		                 // variables to branch on
		                 vars[modelIndex][0],vars[modelIndex][1], vars[i][2], vars[modelIndex][3], vars[modelIndex][4]
				  ));
				
			     // System.out.println("Solutions for Model #"+i);
			     //List<Solution> solns = model.getSolver().findAllSolutions(null);
	//		     System.out.println("number of solutions: "+solns.size());
	//		     
	//		     for(int k=0;k<solns.size();k++){
	//		    	 System.out.println("Solutions #"+k);
	//		    	 System.out.println("v0: "+solns.get(k).getIntVal(vars[0])
	//		    			 + " v1: "+solns.get(k).getIntVal(vars[1])
	//		    			 + " v2: "+solns.get(k).getIntVal(vars[2])
	//		    			 + " v3: "+solns.get(k).getIntVal(vars[3])
	//		    			 + " v4: "+solns.get(k).getIntVal(vars[4])
	//		    			 );
	//		    
	//		     }
			     
			     solver.solve();
			     solver.printStatistics();
			   
			     System.out.println("#######################################");
			     System.out.println("SOLVER without Heuristic");
			     Solver solver2 = model2.getSolver();
				 //List<Solution> solns2 = solver2.findAllSolutions(null);
				 //System.out.println("number of solutions: "+solns2.size());
			     solver2.solve();
			     solver2.printStatistics();
			     
			     System.out.println("#######################################");
			     
			     
			     // APPLY GENETIC ALGORITHM FOR LAST USER
			     if (i==numberOfModelsInTheCluster-1){
			    	 System.out.println("#######################################");
				     System.out.println("SOLVER without specific Heuristic based on Cluster");
				     Solver solver3 = model3.getSolver();
			    	 // getHeuristics
					 solver3 = getHeuristics(modelIndex,solver3,k,null);
					 solver3.solve();
				     solver3.printStatistics();
				     System.out.println("#######################################");
			     }
			     
				 
			 }
			 
			 System.out.println("#######################################");
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

	 public static List<Model> getModelsforProblem(int numberOfVars,int numberOfModels, int maxDomainSize){
		 
		 List <Model> models = new ArrayList<Model>();
		 Random rand = new Random();
		 
		 int[] DomainUpperValues = new int[numberOfVars];
		 
		 for (int i =0;i<numberOfVars;i++){
			 DomainUpperValues[i] = rand.nextInt(maxDomainSize+1);
		 }
		 // System.out.println("#######################################");
		 //System.out.println("Problem is generated with "+numberOfVars+" variables, "+numberOfModels+" different user models");
		 
		 
		 for (int i =0;i<numberOfModels;i++){
			 Model model = new Model("Model#"+ i);
			// System.out.println("##########");
			// System.out.println("Model#"+ i);
			 vars[i] = new IntVar[numberOfVars];
			 
			 
			// System.out.println("SET VARIABLES and VALUES of the Models");
			 // SET VARIABLES and VALUES of the Model
			 for(int j=0; j<numberOfVars;j++){
				 // if variable is set by user
				 if(rand.nextBoolean()) {
					 // set a value for this variable
					 vars[i][j] =  model.intVar("v"+j, rand.nextInt(DomainUpperValues[j]+1));
					
				 } 
				 else{
					 // set the domain of this variable
					 vars[i][j] =  model.intVar("v"+j, 0, DomainUpperValues[j]);
					 
				 }
				// System.out.println(vars[i][j]);
			 }
			 
			
			//System.out.println("SET constraints of the this Problem");
			// SET constraints of the this problem
			if(numberOfVars>3){
				 model.ifThen(
						   model.arithm(vars[i][0],"<",vars[i][1]),
						   model.arithm(vars[i][0],"=",vars[i][2])
				 );
				// System.out.println(model.getCstrs()[0].toString());
			}
			
			models.add(model);
			//System.out.println("##########");
		 }
		 //System.out.println("#######################################");
		 java.util.Date date= new java.util.Date();
		 long time = date.getTime();
		 modelsName = "SedasTestModels-"+time;
		 writeToFile();
		 return models;
	 }
	 
	 public static void writeToFile(){
		    List<String> lines = new ArrayList<String>();
		    String str = "";
		    int val = -1;
		    int size = -1;
		    
		    for (int i=0;i<numberOfmodels;i++){
		    	str = "";
		   		for(int j=0;j<numberOfvars;j++){
		   			size = vars[i][j].getDomainSize();
		   			if(size==1)
		   				val = vars[i][j].getValue();
		   			else
		   				val = -1;
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

	 public static Solver getHeuristics(int modelIndex, Solver solver, int ClusterIndex, int[] varOrder){
		 
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
					 	for(int i =0;i<vars[modelIndex].length;i++){
			                varIndex = ord[i];
			                return vars[modelIndex][i];
			            }
			            return null;
			 };
		 }
		 else{
			 varSelector = new FirstFail(modelsOfTheSameProblem.get(modelIndex));
		 }
		 
		 solver.setSearch(intVarSearch(
                 
				 varSelector,
                 
                 // selects the smallest domain value (lower bound)
                 new IntDomainMin(),
                 //new IntDomainMax(),
                
                 // variables to branch on
                 vars[modelIndex][0],vars[modelIndex][1], vars[modelIndex][2], vars[modelIndex][3], vars[modelIndex][4]
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
