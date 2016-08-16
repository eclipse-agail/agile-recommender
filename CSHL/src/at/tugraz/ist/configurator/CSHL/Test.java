package at.tugraz.ist.configurator.CSHL;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
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
import org.chocosolver.parser.flatzinc.layout.SolutionPrinter;

import org.chocosolver.solver.search.strategy.selectors.values.*;
import org.chocosolver.solver.search.strategy.selectors.variables.AntiFirstFail;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;

import static java.lang.System.out;
import static org.chocosolver.solver.search.strategy.Search.*;
import org.chocosolver.solver.search.strategy.assignments.DecisionOperator;
import org.chocosolver.solver.search.strategy.strategy.IntStrategy;

public class Test {
	
	 public static IntVar[][] vars;
	
	 public static void main(String []args){
		 //testFZNfiles();
		 testHeuristic();
	 }
	 
	 public static void testHeuristic(){
		 
		 int numberOfmodels = 10; 
		 // min 3
		 int numberOfvars = 5;
		 vars = new IntVar[numberOfmodels][];
		
		 int maxDomainSize = 50;
		 
		 // getModelsforProblem(int numberOfVars,int numberOfModels, int maxDomainSize)
		 List <Model> modelsOfTheSameProblem = getModelsforProblem(numberOfvars, numberOfmodels, maxDomainSize);
		 
		 
		 for (int i=0; i<numberOfmodels;i++){
			 Model model = modelsOfTheSameProblem.get(i);
			 Model model2 = modelsOfTheSameProblem.get(i);
			 
			 Solver solver = model.getSolver();
			 //solver.setSearch(minDomLBSearch(vars));
			 System.out.println("#######################################");
			 System.out.println("#########    MODEL   "+i+"     ########");
			 System.out.println("#######################################");
			 for(int j=0;j<numberOfvars;j++){
				 System.out.println(vars[i][j]);
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
	                 vars[i][0],vars[i][1], vars[i][2], vars[i][3], vars[i][4]
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
		 }
		
		
	 }
	 
	 public static void testFZNfiles(){
		 List <String> filenames = new ArrayList<String>();
		 //Logger log = Logger.getLogger(Test.class.getName());
		 long time1=0;
		 long time2=0;

		 // GET ALL TEST DATA FILES
		 try {
			Files.walk(Paths.get("testData\\mzn")).forEach(filePath -> {
				    if (Files.isRegularFile(filePath)) {
				    	filenames.add(filePath.toString());
				        System.out.println(filePath);
				    }
				});
			
		 } catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		 }
		 
		 for(int i=0;i<filenames.size();i++){
			 try {
				   //log.info(filenames.get(i));
				   Flatzinc fzn = new Flatzinc();
				   fzn.addListener(new BaseFlatzincListener(fzn));
				   String[] input = new String [1];
				   //log.info("TEST DATA : "+ filenames.get(i));
				   time1 = System.nanoTime();
				   input[0] = filenames.get(i);
				   fzn.parseParameters(input);
				   FznSettings settings = new FznSettings();
				 
				   fzn.defineSettings(new FznSettings());
				   fzn.createSolver();
				  
				   fzn.parseInputFile();
					
				   fzn.configureSearch();
	
				   // now you can access your model/solver and add a few things (variables, constraints, monitors, etc.)
	
				   fzn.solve(); // you might want to skip this and trigger the solving process yourself from the Solver object.
				   time2 = System.nanoTime();
				   //log.info("TEST DATA : "+ filenames.get(i)+" resulted in "+(time2-time1)+" nanoseconds");
			 } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		 return models;
	 }
}
