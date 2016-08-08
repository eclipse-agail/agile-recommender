package at.tugraz.ist.configurator.CSHL;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;

public class StaticProblemDefinition {
	 public static void ExampleOne()
	    {
	        Solver solver = new Solver("my first problem");
	        
	        // 2. Create variables through the variable factory
	        IntVar x1 = VariableFactory.bounded("X1", 0, 5, solver);
	        IntVar x2 = VariableFactory.bounded("X2", 0, 5, solver);
	        IntVar x3 = VariableFactory.bounded("X3", 0, 5, solver);
	        IntVar x4 = VariableFactory.bounded("X4", 0, 5, solver);
	        IntVar x5 = VariableFactory.bounded("X5", 0, 5, solver);
	        
	        // 3. Create and post constraints by using constraint factories
	        solver.post(IntConstraintFactory.arithm(x1, "+", x2, ">", 8));
	        
	        // 4. Define the search strategy
	        solver.set(IntStrategyFactory.lexico_LB(x1, x2));
	        
	        // 5. Indicates that all solutions should be print to the console
	        Chatterbox.showSolutions(solver);
	        
	        // 6. Launch the resolution process
	        solver.findSolution();

	        // 7. Finally, outputs the resolution statistics
	        Chatterbox.printStatistics(solver);
	         /* */
	    }
}
