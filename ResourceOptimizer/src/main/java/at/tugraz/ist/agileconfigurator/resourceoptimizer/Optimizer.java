package at.tugraz.ist.agileconfigurator.resourceoptimizer;

import java.util.Arrays;
import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.Operator;
import org.chocosolver.solver.objective.ParetoOptimizer;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.criteria.Criterion;
import org.chocosolver.util.tools.ArrayUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import at.tugraz.ist.agileconfigurator.resourceoptimizer.models.Results;
import at.tugraz.ist.agileconfigurator.resourceoptimizer.models.App;
import at.tugraz.ist.agileconfigurator.resourceoptimizer.models.GatewayProfile;
import at.tugraz.ist.agileconfigurator.resourceoptimizer.models.Protocol;
import at.tugraz.ist.agileconfigurator.resourceoptimizer.models.ProtocolsKnowledgeBase;


public class Optimizer {
	
	public  GatewayProfile profile;
	private GatewayProfile configuredProfile = new GatewayProfile();
	private ProtocolsKnowledgeBase kb = new ProtocolsKnowledgeBase();
	
	public Optimizer(GatewayProfile p) {
		this.profile = p;
		this.configuredProfile = p;
		optimizeProtocols();
	}
	
	public GatewayProfile getconfiguredProfile() {
		return this.configuredProfile;
	}
	public void setconfiguredProfile(GatewayProfile configuredProfile) {
		this.configuredProfile = configuredProfile;
	}
	public void optimizeProtocols(){
		
		// add constraint: more conn protocols are activated, more energy is consumed
		
        Model model = new Model("OptimizeProtocols");
        App [] appArray = profile.getInstalledApps();
        
        //App [] appArray = new App[2]; 
        //appArray[0]= new App(true);
        //appArray[1]= new App(true);
        
        
        int numberOfDataEncofingProtocols = ProtocolsKnowledgeBase.numberOfDataEncodingProtocols;
        int numberOfConnectivityProtocols = ProtocolsKnowledgeBase.numberOfConnectivityProtocols;
        int dimensionsOfProtocol = ProtocolsKnowledgeBase.dimensionsOfProtocol;
        
        int [] userweights = new int[dimensionsOfProtocol];
        // TODO Seda: update user weights by taking from profile
        Arrays.fill(userweights, 0, dimensionsOfProtocol, 1);
        
        
        // # of protocols * 3 dimensions
        int[] coeffs = new int[(numberOfDataEncofingProtocols+numberOfConnectivityProtocols)*dimensionsOfProtocol];
        int index=0;
        for (int i=0;i<dimensionsOfProtocol;i++){
        	coeffs[index] = userweights[i];
        }
        
        // number of apps
        int[] noCoeffs_numberOfApps = new int[appArray.length];
        Arrays.fill(noCoeffs_numberOfApps, 0, appArray.length, 1);
        
        int[] noCoeffs_3 = new int[3];
        Arrays.fill(noCoeffs_3, 0, noCoeffs_3.length, 1);
        
        int[] noCoeffs_2 = new int[2];
        Arrays.fill(noCoeffs_2, 0, noCoeffs_2.length, 1);
        
        
        // VARIABLES
        IntVar [] dataEncodingProtocolOfApp = new IntVar[appArray.length]; 	
        IntVar [] connectivitiyProtocolOfApp = new IntVar[appArray.length]; 	
        
        
        // DATA values for each app
        IntVar [] performanceOfDataProtocolsOfApps = new IntVar[appArray.length]; 
        IntVar [] reliabilityOfDataProtocolsOfApps = new IntVar[appArray.length]; 
        IntVar [] costOfDataProtocolsOfApps = new IntVar[appArray.length]; 
        
        
        // CONN values for each app
        IntVar [] performanceOfConnProtocolsOfApps = new IntVar[appArray.length]; 
        IntVar [] reliabilityOfConnProtocolsOfApps = new IntVar[appArray.length];
        IntVar [] costOfConnProtocolsOfApps = new IntVar[appArray.length];
        
        
        // SUM OF DATA + CONN values for each app
        IntVar [] performanceValuesOfApps = new IntVar[appArray.length];  
        IntVar [] reliabilityValuesOfApps = new IntVar[appArray.length];  
        IntVar [] costValuesOfApps = new IntVar[appArray.length];         
        
        
        // SUM OF DATA performanceValuesOfApps, reliabilityValuesOfApps, costValuesOfApps (multiplied with user weights) for each app
        IntVar [] totalDPUtilityofApps = new IntVar[appArray.length]; model.intVarArray("totalUtilityofApps",appArray.length,0,100);
        
        
        // SUM OF CONN performanceValuesOfApps, reliabilityValuesOfApps, costValuesOfApps (multiplied with user weights) for each app
        IntVar [] totalCPUtilityofApps = new IntVar[appArray.length]; model.intVarArray("totalUtilityofApps",appArray.length,0,100);
        
        
        // SUM OF totalDPUtilityofApps and totalCPUtilityofApps for each app
        IntVar [] totalUtilityofApps = new IntVar[appArray.length]; model.intVarArray("totalUtilityofApps",appArray.length,0,100);
        
        
        // SUM OF totalUtilityofApps 
        IntVar utilityValue = null ;
        
        
        // CONSTRAINTS
        
        // calculate for each app
        for (int i=0;i<appArray.length;i++){
        	
        	// add domain of supported protocols
        	int dPdomainSize = appArray[i].getXorDataEncodingProtocols().length;
        	dataEncodingProtocolOfApp[i] = model.intVar("dataEncodingProtocolsOfApp-"+i,appArray[i].getXorDataEncodingProtocols());
        	int cPdomainSize = appArray[i].getXorConnectivitiyProtocols().length;
        	connectivitiyProtocolOfApp[i]=  model.intVar("connectivitiyProtocolsOfApps-"+i,appArray[i].getXorConnectivitiyProtocols());
        	
        	int data_utilities[] = new int [dPdomainSize];
        	
        	// DATA
        	for(int p1=0;p1<dPdomainSize;p1++){
        		
        		int dpID = appArray[i].getXorDataEncodingProtocols()[p1];
        		int perf_dpID = ProtocolsKnowledgeBase.dataProtocolKnowledgeBase[dpID].getPerformance();
        		int rel_dpID = ProtocolsKnowledgeBase.dataProtocolKnowledgeBase[dpID].getReliability();
        		int cost_dpID = ProtocolsKnowledgeBase.dataProtocolKnowledgeBase[dpID].getCost();;
        		data_utilities [p1] = perf_dpID*userweights[0] + rel_dpID*userweights[1] + cost_dpID*userweights[2];
        	}
        	
        	totalDPUtilityofApps[i] = model.intVar("totalDPUtilityofApps"+i,data_utilities);
        	
        	
        	int conn_utilities[] = new int [cPdomainSize];
        	// CONN
        	for(int p1=0;p1<cPdomainSize;p1++){
        		
        		int cpID = appArray[i].getXorConnectivitiyProtocols()[p1];
        		int perf_cpID = ProtocolsKnowledgeBase.connetivityProtocolKnowledgeBase[cpID].getPerformance();
        		int rel_cpID = ProtocolsKnowledgeBase.connetivityProtocolKnowledgeBase[cpID].getReliability();
        		int cost_cpID = ProtocolsKnowledgeBase.connetivityProtocolKnowledgeBase[cpID].getCost();
        		conn_utilities[p1] = perf_cpID*userweights[0] + rel_cpID*userweights[1] + cost_cpID*userweights[2];

        	}
        	totalCPUtilityofApps[i] = model.intVar("totalCPUtilityofApps"+i,conn_utilities);
        	
        	
        	// sum APP and CONN utilities of the app
        	totalUtilityofApps[i] = model.intVar("totalUtilityofApps"+i,0,100,true);
        	model.scalar(ArrayUtils.toArray(totalDPUtilityofApps[i], totalCPUtilityofApps[i]), noCoeffs_2, "=", totalUtilityofApps[i]).post();
    			
        		
        	// maximize totalUtilityofApps for each app
        	model.setObjective(true, totalUtilityofApps[i]);
        	
        } // end of calculate for each app
        
        // Calculate totalUtilityofApps for each app
        // model.getSolver().solve();	
        
        // sum up utility values of  all apps into utilityValue
        utilityValue = model.intVar("utilityValue",0,100,true);
        model.scalar(totalUtilityofApps, noCoeffs_numberOfApps, "=", utilityValue).post();
        
        Solver solver = model.getSolver();	
        model.getSolver().solve();
        
	}
	
}
