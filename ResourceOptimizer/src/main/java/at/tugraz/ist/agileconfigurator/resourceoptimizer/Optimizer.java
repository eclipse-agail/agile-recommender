package at.tugraz.ist.agileconfigurator.resourceoptimizer;

import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.objective.ParetoOptimizer;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.criteria.Criterion;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import at.tugraz.ist.agileconfigurator.resourceoptimizer.models.Results;
import at.tugraz.ist.agileconfigurator.resourceoptimizer.models.App;
import at.tugraz.ist.agileconfigurator.resourceoptimizer.models.Enum_ConnectivityProtocols;
import at.tugraz.ist.agileconfigurator.resourceoptimizer.models.GatewayProfile;
import at.tugraz.ist.agileconfigurator.resourceoptimizer.models.Protocol;
import at.tugraz.ist.agileconfigurator.resourceoptimizer.models.ProtocolsKnowledgeBase;


public class Optimizer {
	
	public GatewayProfile profile;
	private GatewayProfile configuredProfile = new GatewayProfile();
	private ProtocolsKnowledgeBase kb = new ProtocolsKnowledgeBase();
	
	public Optimizer(GatewayProfile p) {
		this.profile = p;
		optimizeProtocols();
	}
	
	public GatewayProfile getconfiguredProfile() {
		return this.configuredProfile;
	}
	public void setconfiguredProfile(GatewayProfile configuredProfile) {
		this.configuredProfile = configuredProfile;
	}
	public void optimizeProtocols(){
		
        Model model = new Model("OptimizeProtocols");
        // TODO add these again
        // App [] appArray = profile.getInstalledApps();
        
        App [] appArray = new App[1]; 
        appArray[0]= new App(true);
        
        
        // VARIABLE & DOMAIN
     
        IntVar [] dataEncodingProtocolsOfApps = model.intVarArray("dataEncodingProtocolsOfApps",appArray.length,0,4);
        IntVar [] connectivitiyProtocolsOfApps = model.intVarArray("connectivitiyProtocolsOfApps",appArray.length,0,4);
        
        IntVar [] performanceValuesOfProtocols = model.intVarArray("performanceValuesOfProtocols",4,0,10);
        IntVar [] reliabilityValuesOfProtocols = model.intVarArray("reliabilityValuesOfProtocols",4,0,10);
        IntVar [] costValuesOfProtocols = model.intVarArray("costValuesOfProtocols",4,0,10);
        
        IntVar [] performanceOfDataProtocolsOfApps = model.intVarArray("performanceOfDataProtocolsOfApps",appArray.length,0, 1000);
        IntVar [] reliabilityOfDataProtocolsOfApps = model.intVarArray("reliabilityOfDataProtocolsOfApps",appArray.length,0, 1000);
        IntVar [] costOfDataProtocolsOfApps = model.intVarArray("costOfDataProtocolsOfApps",appArray.length,0,1000);
        
        IntVar [] performanceOfConnProtocolsOfApps = model.intVarArray("performanceOfConnProtocolsOfApps",appArray.length,0, 1000);
        IntVar [] reliabilityOfConnProtocolsOfApps = model.intVarArray("reliabilityOfConnProtocolsOfApps",appArray.length,0, 1000);
        IntVar [] costOfConnProtocolsOfApps = model.intVarArray("costOfConnProtocolsOfApps",appArray.length,0,1000);
        
        
        IntVar utilityValue ;// = model.intVar("utility", 10000);
        
        // CONSTRAINTS
        // apps can have different data encoding or network protocols
        
        // SAMPLE OPTIMIZE CODE:
//        _me().findOptimalSolution(objective, maximize, stop);
//        if (!_me().isStopCriterionMet()  &&
//             model.getSolver().getMeasures().getSolutionCount() > 0) {
//            int opt = _model.getSolver().getObjectiveManager().getBestSolutionValue().intValue();
//            model.getSolver().reset();
//            model.clearObjective();
//            model.arithm(objective, "=", opt).post();
//            return findAllSolutions();
//        } else {
//             return Collections.emptyList();
//        }
        
        int totalPerf = 0;
        int totalRel = 0;
        int totalCost = 0;
        
        for (int i=0;i<appArray.length;i++){
        	
        	dataEncodingProtocolsOfApps[i] = model.intVar("dataEncodingProtocolsOfApp-"+i,appArray[i].getXorDataEncodingProtocols());
        	connectivitiyProtocolsOfApps[i]=  model.intVar("connectivitiyProtocolsOfApps-"+i,appArray[i].getXorConnectivitiyProtocols());
        	
        	for(int p1=0;p1<4;p1++){
        		
        		// calculate data encoding protocol values
        		model.ifThen(
        				   model.arithm(dataEncodingProtocolsOfApps[i],"=",p1),
        				   model.arithm(performanceOfDataProtocolsOfApps[i],"=",performanceValuesOfProtocols[p1])
        		);
        		model.ifThen(
     				   model.arithm(dataEncodingProtocolsOfApps[i],"=",p1),
     				   model.arithm(reliabilityOfDataProtocolsOfApps[i],"=",reliabilityValuesOfProtocols[p1])
        				);
        		model.ifThen(
     				   model.arithm(dataEncodingProtocolsOfApps[i],"=",p1),
     				   model.arithm(costOfDataProtocolsOfApps[i],"=",costValuesOfProtocols[p1])
        				);
        		
        		// calculate conn protocol values
        		model.ifThen(
     				   model.arithm(connectivitiyProtocolsOfApps[i],"=",p1),
     				   model.arithm(performanceOfConnProtocolsOfApps[i],"=",performanceValuesOfProtocols[p1])
	     		);
	     		model.ifThen(
	  				   model.arithm(connectivitiyProtocolsOfApps[i],"=",p1),
	  				   model.arithm(reliabilityOfConnProtocolsOfApps[i],"=",reliabilityValuesOfProtocols[p1])
	     				);
	     		model.ifThen(
	  				   model.arithm(connectivitiyProtocolsOfApps[i],"=",p1),
	  				   model.arithm(costOfConnProtocolsOfApps[i],"=",costValuesOfProtocols[p1])
	     				);
	        }
			// THIS PART IS STATIC. CHOCO DOES NOT CALCULATE THIS PART DYNAMICALLY.
			//        	totalPerf  += ProtocolsKnowledgeBase.dataProtocolKnowledgeBase[dataEncodingProtocolsOfApps[i].getValue()].getPerformance();
			//        	totalRel  += ProtocolsKnowledgeBase.dataProtocolKnowledgeBase[dataEncodingProtocolsOfApps[i].getValue()].getReliability();
			//        	totalCost  += ProtocolsKnowledgeBase.dataProtocolKnowledgeBase[dataEncodingProtocolsOfApps[i].getValue()].getCost();
			//        	
			//        	totalPerf  += ProtocolsKnowledgeBase.connetivityProtocolKnowledgeBase[connectivitiyProtocolsOfApps[i].getValue()].getPerformance();
			//        	totalRel  += ProtocolsKnowledgeBase.connetivityProtocolKnowledgeBase[connectivitiyProtocolsOfApps[i].getValue()].getReliability();
			//        	totalCost  += ProtocolsKnowledgeBase.connetivityProtocolKnowledgeBase[connectivitiyProtocolsOfApps[i].getValue()].getCost();
        }
        
        // TODO SEDA: add user weights 
        
        utilityValue = model.intVar("utility", totalPerf+totalRel-totalCost);
        
        // maximize
        model.getSolver().findAllOptimalSolutions(utilityValue, true);
        int m =0;
	}
	

}
