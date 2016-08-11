package at.tugraz.ist.configurator.CSHL;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.chocosolver.parser.flatzinc.BaseFlatzincListener;
import org.chocosolver.parser.flatzinc.Flatzinc;
import org.chocosolver.parser.flatzinc.FznSettings;
import org.chocosolver.parser.flatzinc.ast.Datas;
import org.chocosolver.solver.Solver;
import org.chocosolver.parser.flatzinc.layout.SolutionPrinter;


public class Test {
	 public static void main(String []args)
	    {
//	        XMLtestDataParser.parse("testData/normalized-dsjc-125-1-4-ext.xml");
//	        
//	        System.out.println("Var size: "+ProblemDefinition.variables.size());
//	        System.out.println("Domain size: "+ProblemDefinition.domains.size());
		 try {
			   Flatzinc fzn = new Flatzinc();
			   fzn.addListener(new BaseFlatzincListener(fzn));
			   String[] input = new String [1];
			   input[0] = "testData/arith.fzn";
			   fzn.parseParameters(input);
			   fzn.defineSettings(new FznSettings());
			   fzn.createSolver();
			  
			   fzn.parseInputFile();
				
			   fzn.configureSearch();

			   // now you can access your model/solver and add a few things (variables, constraints, monitors, etc.)

			   fzn.solve(); // you might want to skip this and trigger the solving process yourself from the Solver object.
		 	} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}
