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
import at.tugraz.ist.configurator.CSH.fileOperations.*;

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
	
	 /////////////////////////////////////////////////////////////////////
	 ////////////// ALL VARIABLES OF THE TEST ARE HERE ///////////////////
	 /////////////////////////////////////////////////////////////////////
	 // name of tested model 
	 public static String modelsName = "";
	 public static int numberOfmodels = 100; 
	 public static int maxDomainSize = 1000;
	 public static int numberOfvars = 1000;
	 public static int numberOfclusters = 4;
	 public static int [][] clusters;
	 public static List <UserModel> modelsOfTheSameProblem;
	 public static List<String> heuristics; 
	 public static int sizeOfPopulation = 3;
	 // number of tests applied
	 public static int testSize = 100;
	 public static int currentTest = 0;
	 public static List<List<Long>> statisticsOfClusters;
	 
	 // variable order for each cluster
	 public static List<int[]> ordersOfVariables;
	 
	 // value order for each domain for each cluster
	 public static List<int[][]> ordersOfValues;
	 
	 public static VariableSelector variableOrderingHeuristic;
	 public static IntValueSelector valueOrderingHeuristic;
	 public static FileWriter writer =null;
 	/////////////////////////////////////////////////////////////////////
	////////////// ENF OF ALL VARIABLES OF THE TEST   ///////////////////
	/////////////////////////////////////////////////////////////////////
	 
	 
	 public static void main(String []args){
		
		 for(int i=0;i<testSize;i++){
			 testHeuristic();
			 currentTest++;
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
		 
		 SampleCSPs.getModelsforProblem(numberOfvars, numberOfmodels, maxDomainSize);
		 
		 // apply clustering
		 Clustering.applyKMeans();
		 
		 // get clusters
		 Clustering.getClusters();
		 //System.out.println("Clusters are calculated with sizes"+clusters[0].length+clusters[1].length+clusters[2].length+clusters[3].length);
		 
		 // get var orders for each cluster
		 LearningHeuristics.learnHeuristicsForClusters();
		
		 
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
					 solver3 = LearningHeuristics.getCorrespondingHeuristicForCSP(model3,solver3,k,null);
					 
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
			 WriteToFile.writeCSV(linetoCSV,lastline);
		 }
		 System.out.println("##############################################################################");
		 
	 }
	 
	 
}
