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
        
        int dimensionsOfProtocol = ProtocolsKnowledgeBase.dimensionsOfProtocol;
        
        int [] userweights = new int[dimensionsOfProtocol];
        // TODO Seda: update user weights by taking from profile
        Arrays.fill(userweights, 0, dimensionsOfProtocol, 1);
        userweights[2] = -1;
     
        // VARIABLES
        
        // Selected protocols of Apps
        IntVar [] dataEncodingProtocolOfApp = new IntVar[appArray.length]; 	
        IntVar [] connectivitiyProtocolOfApp = new IntVar[appArray.length]; 
        
        IntVar  [][] data_utilities = new IntVar[appArray.length][];
        IntVar  [][] conn_utilities = new IntVar[appArray.length][];
        
        
        // Utilities of Apps
        IntVar [] totalDataUtilitiesOfApps = model.intVarArray("totalDataUtilitiesOfApps",appArray.length,0,100);
        IntVar [] totalConnUtilitiesOfApps = model.intVarArray("totalConnUtilitiesOfApps",appArray.length,0,100);
        IntVar [] totalUtilitiesOfApps = model.intVarArray("totalUtilitiesOfApps",appArray.length,0,100);
        
      
        // SUM OF totalUtilityofApps 
        IntVar totalUtility= model.intVar("totalUtility",0,100) ;
        
        
        // CONSTRAINTS
        
        // calculate for each app
        for (int i=0;i<appArray.length;i++){
        	
        	// initialize CHOCO IntVar domain values
        	int dPdomainSize = appArray[i].getXorDataEncodingProtocols().length;
        	dataEncodingProtocolOfApp[i] = model.intVar("dataEncodingProtocolsOfApp-"+i,appArray[i].getXorDataEncodingProtocols());
        	int cPdomainSize = appArray[i].getXorConnectivitiyProtocols().length;
        	connectivitiyProtocolOfApp[i]=  model.intVar("connectivitiyProtocolsOfApps-"+i,appArray[i].getXorConnectivitiyProtocols());
        	// end 
        	
        	
        	// Create temp_totalDataUtil
        	data_utilities[i] =  new IntVar[dPdomainSize];
        	for(int p1=0;p1<dPdomainSize;p1++){
        		
        		int dpID = appArray[i].getXorDataEncodingProtocols()[p1];
        		int perf_dpID = ProtocolsKnowledgeBase.dataProtocolKnowledgeBase[dpID].getPerformance();
        		int rel_dpID = ProtocolsKnowledgeBase.dataProtocolKnowledgeBase[dpID].getReliability();
        		int cost_dpID = ProtocolsKnowledgeBase.dataProtocolKnowledgeBase[dpID].getCost();;
        		int tempDataUtil = perf_dpID*userweights[0] + rel_dpID*userweights[1] + cost_dpID*userweights[2];
        		data_utilities[i][p1] = model.intVar("data_utilities-"+p1,tempDataUtil);
        				
        		model.ifThen(
          			   model.arithm(dataEncodingProtocolOfApp[i],"=",dpID),
          			   // update the domain values of total_utility
          			   model.sum(ArrayUtils.toArray(data_utilities[i][p1]),"=",totalDataUtilitiesOfApps[i])
             			);
             	
        	}
        	// end of Create temp_totalDataUtil
        	
        	// Create temp_totalDataUtil
        	conn_utilities[i] = new IntVar[cPdomainSize];
        	for(int p1=0;p1<cPdomainSize;p1++){
        		
        		int cpID = appArray[i].getXorConnectivitiyProtocols()[p1];
        		int perf_cpID = ProtocolsKnowledgeBase.connetivityProtocolKnowledgeBase[cpID].getPerformance();
        		int rel_cpID = ProtocolsKnowledgeBase.connetivityProtocolKnowledgeBase[cpID].getReliability();
        		int cost_cpID = ProtocolsKnowledgeBase.connetivityProtocolKnowledgeBase[cpID].getCost();;
        		int tempConnUtil = perf_cpID*userweights[0] + rel_cpID*userweights[1] + cost_cpID*userweights[2];
        		conn_utilities[i][p1] = model.intVar("conn_utilities-"+p1,tempConnUtil);
        				
        		model.ifThen(
          			   model.arithm(connectivitiyProtocolOfApp[i],"=",cpID),
          			   // update the domain values of total_utility
          			   model.sum(ArrayUtils.toArray(conn_utilities[i][p1]),"=",totalConnUtilitiesOfApps[i])
             			);
             	
        	}
        	// end of Create temp_totalDataUtil
        	
        	// sum APP and CONN utilities of the app
        	model.sum(ArrayUtils.toArray(totalDataUtilitiesOfApps[i],totalConnUtilitiesOfApps[i]),"=",totalUtilitiesOfApps[i]).post();
        	
        } // end of calculate for each app
        
        
        // sum up utility values of  all apps into utilityValue
        model.sum(totalUtilitiesOfApps,"=",totalUtility).post();	
        
		/////////////////////////////////////////////////
		// SOLVER ///////////////////////////////////////
		/////////////////////////////////////////////////
		Solution sol = model.getSolver().findOptimalSolution(totalUtility, true);
		System.out.println(sol);
        
		for (int i=0;i<appArray.length;i++){
			this.configuredProfile.getInstalledApps()[i].setInUse_ConnectivitiyProtocol(sol.getIntVal(connectivitiyProtocolOfApp[i]));
			this.configuredProfile.getInstalledApps()[i].setInUse_DataEncodingProtocol(sol.getIntVal(dataEncodingProtocolOfApp[i]));
		}
	}
	
}
