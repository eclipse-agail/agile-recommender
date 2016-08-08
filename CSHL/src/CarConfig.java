/**
 * Created by spolater on 3/8/16.
 */

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.constraints.LogicalConstraintFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;


/**
 * Hello world!
 *
 */
public class CarConfig
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        ExampleOne();
        //ExampleTwo();
        //ExampleThree();
        //ExampleFour();
        //ExampleFive();
        //ExampleSix();

        //CarConfiguration();
    }


    private static void ExampleSix()
    {
        Solver solver = new Solver("my second problem");

        IntVar x = VariableFactory.bounded("x_OUT", 0, 1, solver);
        IntVar y = VariableFactory.bounded("y_OUT", 0, 1, solver);

        Constraint a = IntConstraintFactory.arithm(x,"=",1);
        Constraint b = IntConstraintFactory.arithm(y,"=",1);
        LogicalConstraintFactory.ifThen(a,b);

        //solver.set(IntStrategyFactory.lexico_LB(x, y));

        Chatterbox.showSolutions(solver);
        solver.findAllSolutions();

    }



    public static void ExampleFive()
    {
        Solver solver = new Solver("my second problem");

        IntVar x = VariableFactory.bounded("x", 0, 10, solver);
        IntVar y = VariableFactory.bounded("x", 0, 10, solver);

        solver.post(IntConstraintFactory.arithm(x, "+", y, "<", 2));
        solver.post(IntConstraintFactory.arithm(x, "+", y, ">", 0));

        //solver.set(IntStrategyFactory.lexico_LB(x, y));

        Chatterbox.showSolutions(solver);
        solver.findAllSolutions();
    }




    public static void CarConfiguration()
    {
    	/*solver.post(IntConstraintFactory.arithm(fourWheel, "=", 1));
    	solver.post(IntConstraintFactory.arithm(fuel, "=", 6));
    	solver.post(IntConstraintFactory.arithm(type, "=", 0));
    	solver.post(IntConstraintFactory.arithm(skibag, "=", 1));
    	solver.post(IntConstraintFactory.arithm(pdc, "=", 1));
    	*/

    	/*Variables: type, fuel, skibag, 4-wheel, pdc
    	Domains:
    		Type:	city(0), limo(1), combi(2), xdrive(3)
    		fuel:	4l, 6l, 10l
    		skibag:	yes, no
    		4-wheel:yes,no
    		pdc:	yes, no
    	*/

        Solver solver = new Solver("Car configuration atas");

        IntVar type = VariableFactory.bounded("type", 0, 3, solver);
        IntVar fuel = VariableFactory.enumerated("fuel", new int[]{4,6,10}, solver);
        BoolVar skibag = VariableFactory.bool("skibag", solver);
        BoolVar fourWheel = VariableFactory.bool("fourWheel", solver);
        BoolVar pdc = VariableFactory.bool("pdc", solver);

        //c1
        LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(fourWheel,"=",1),IntConstraintFactory.arithm(type,"=",3));

        //c2
        LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(skibag,"=",1),IntConstraintFactory.arithm(type,"!=",0));

        //c3
        LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(fuel,"=",4),IntConstraintFactory.arithm(type,"=",0));

        //c4
        LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(fuel,"=",6),IntConstraintFactory.arithm(type,"!=",3));

        //c5
        LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(type,"=",0),IntConstraintFactory.arithm(fuel,"!=",10));

        Constraint c6 = IntConstraintFactory.arithm(fourWheel, "=", 1);
        Constraint c7 = IntConstraintFactory.arithm(fuel,"=",6);
        Constraint c8 = IntConstraintFactory.arithm(type,"=",0);
        Constraint c9 = IntConstraintFactory.arithm(skibag,"=",1);
        Constraint c10 = IntConstraintFactory.arithm(pdc,"=",1);
        Constraint z = LogicalConstraintFactory.and(c6, c7, c8, c9, c10);
        solver.post(z);

        Chatterbox.showSolutions(solver);
        solver.findAllSolutions();
        Chatterbox.printStatistics(solver);

        //type = 0 fuel = 6 skibag = 0 fourWheel = 0 pdc = 0
        //http://chocoteam.github.io/choco3/2_modelling.html
        //


    }


    public static void ExampleFour()
    {
        //Find all the different solutions
        Solver solver = new Solver("solver nr 4");

        IntVar W = VariableFactory.enumerated("W", 0,1,solver);
        IntVar X = VariableFactory.enumerated("X", -1,2,solver);
        IntVar Y = VariableFactory.enumerated("Y", 2,4,solver);
        IntVar Z = VariableFactory.enumerated("Z", 5,7,solver);

        solver.post(IntConstraintFactory.alldifferent(new IntVar[]{W, X, Y, Z}));
        Chatterbox.showSolutions(solver);
        if(solver.findSolution())
        {
            do
            {
                System.out.println("----------------------" );

            }while(solver.nextSolution());
        }

    }



    public static void ExampleThree()
    {
        //The absolute constraint involves two variables VAR1 and VAR2.
        //It ensures that VAR1 = | VAR2 | .

        Solver solver = new Solver();
        IntVar X = VariableFactory.enumerated("X", 0, 2, solver);
        IntVar Y = VariableFactory.enumerated("Y", -6, 1, solver);
        solver.post(IntConstraintFactory.absolute(X, Y));
        //solver.findAllSolutions();
        Chatterbox.showSolutions(solver);
        if(solver.findSolution())
        {
            do
            {
                System.out.println("----------------------" );

            }while(solver.nextSolution());
        }
    }

    public static void ExampleTwo()
    {
        Solver solver = new Solver("my second problem");

        IntVar x = VariableFactory.bounded("x", 0, 10, solver);
        IntVar y = VariableFactory.bounded("x", 0, 10, solver);

        solver.post(IntConstraintFactory.arithm(x, "+", y, "<", 2));
        solver.set(IntStrategyFactory.lexico_LB(x, y));

        //System.out.println("++++++++++++++++" );
        //Chatterbox.printSolutions(solver);
        //System.out.println("++++++++++++++++" );
        Chatterbox.showSolutions(solver);

        if(solver.findSolution())
        {
            do
            {
                //Chatterbox.showSolutions(solver);

                //Chatterbox.printSolutions(solver);
                //Chatterbox.printStatistics(solver);
                System.out.println("----------------------" );

            }while(solver.nextSolution());
        }
    }

    public static void ExampleOne()
    {
        Solver solver = new Solver("my first problem");
        // 2. Create variables through the variable factory
        IntVar x = VariableFactory.bounded("X", 0, 5, solver);
        IntVar y = VariableFactory.bounded("Y", 0, 5, solver);
        // 3. Create and post constraints by using constraint factories
        solver.post(IntConstraintFactory.arithm(x, "+", y, ">", 8));
        // 4. Define the search strategy
        solver.set(IntStrategyFactory.lexico_LB(x, y));
        // 5. Indicates that all solutions should be print to the console
        Chatterbox.showSolutions(solver);
        // 6. Launch the resolution process
        solver.findSolution();

        // 7. Finally, outputs the resolution statistics
        Chatterbox.printStatistics(solver);
         /* */
    }

}