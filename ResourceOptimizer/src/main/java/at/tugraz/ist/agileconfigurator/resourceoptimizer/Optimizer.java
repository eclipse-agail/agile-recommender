package at.tugraz.ist.agileconfigurator.resourceoptimizer;

import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.objective.ParetoOptimizer;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.criteria.Criterion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import at.tugraz.ist.agileconfigurator.resourceoptimizer.models.Results;
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
      
        
        // VARIABLE & DOMAIN
     
        IntVar [] dataEncodingProtocolsOfApps = model.intVarArray("dataEncodingProtocolsOfApps",profile.getInstalledApps().length,0,4);
        IntVar [] connectivitiyProtocolsOfApps = model.intVarArray("connectivitiyProtocolsOfApps",profile.getInstalledApps().length,0,4);
        IntVar utilityValue = model.intVar("utility", 0);
        
        // CONSTRAINTS
        // apps can have different data encoding or network protocols
        for (int i=0;i<profile.getInstalledApps().length;i++){
        	
        	//IntVar [] supported_DEP_OfApp = model.intVar("supported_DEP_OfApp_"+i,0,4);
        	
        	// Incompatibility
        	model.ifThen(
        		model.arithm(connectivitiyProtocolsOfApps[i],"=",2),
       			model.arithm(dataEncodingProtocolsOfApps[i],"=",3)
       		);
        	
       		utilityValue = model.intVar("utility", calculateUtility(dataEncodingProtocolsOfApps[i].getValue(), connectivitiyProtocolsOfApps[i].getValue()) );
        	
        }
        
      
    
        model.getSolver().solve();
	}
	
	public int calculateUtility(int dataEncodingID, int connectivitiyID){
		
		// APPLY UTILITY FUNCTION
		return 1;
	}

	
}
