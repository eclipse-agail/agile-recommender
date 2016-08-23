package at.tugraz.ist.configurator.CSHL;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public class UserModel {
	
	public IntVar[]vars;
	public String name;
	public Model chocoModel;
	public Constraint ifcont;
	public Constraint thencont;
	
	UserModel(String n,IntVar[] variables,Constraint c1,Constraint c2){
		vars = new IntVar[variables.length];
		ifcont = c1;
		thencont = c2;
		name = n;
		chocoModel = new Model(name);
		
		for(int i=0;i<variables.length;i++){
			vars[i]=variables[i];
			
			if(variables[i].getDomainSize()==1)
				chocoModel.intVar(variables[i].getName(),variables[i].getValue());
			else
				chocoModel.intVar(variables[i].getName(),0,variables[i].getDomainSize());
		}
		
		chocoModel.ifThen(ifcont,thencont);
	}

}
