/*******************************************************************************
 * Copyright (C) 2017 TU Graz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     TU Graz - initial API and implementation
 ******************************************************************************/
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
		this.configuredProfile.setErrorMessage("");
		optimizeProtocols();
	}
	
	public GatewayProfile getconfiguredProfile() {
		return this.configuredProfile;
	}
	public void setconfiguredProfile(GatewayProfile configuredProfile) {
		this.configuredProfile = configuredProfile;
	}
	public void optimizeProtocols(){
		
		App [] appArray = profile.getInstalledApps();
		int dimensionsOfProtocol = ProtocolsKnowledgeBase.dimensionsOfProtocol;
		 
        int [] userweights = new int[dimensionsOfProtocol];
        userweights[0] = profile.getUserRequirementWeight_Performance();
        userweights[1] = profile.getUserRequirementWeight_Reliability();
        userweights[2] = profile.getUserRequirementWeight_Cost();
     
        
		
		// calculate max utility for each app
        for (int i=0;i<appArray.length;i++){
			// add constraint: more conn protocols are activated, more energy is consumed
	        Model model = new Model("OptimizeProtocols");
	        
	        // VARIABLES
	        
	        // Selected protocols of Gateway
	        IntVar dataEncodingProtocolOfGateway = model.intVar("dataEncodingProtocolOfGateway",profile.getSupportedDataEncodingProtocolsOfGateway());
	        IntVar connectivitiyProtocolOfGateway = model.intVar("connectivitiyProtocolOfGateway",profile.getSupportedConnectivityProtocolsOfGateway()) ;
	        
	        
	        // Selected protocols of Apps
	        IntVar dataEncodingProtocolOfApp;	
	        IntVar connectivitiyProtocolOfApp; 
	//        
	//        // Utilities of Apps for each supported protocols
	//        IntVar  [][] data_utilities = new IntVar[appArray.length][3];
	//        IntVar  [][] conn_utilities = new IntVar[appArray.length][3];
	//        
	        // Utilities of Apps
	        IntVar totalDataUtilitiesOfApps = model.intVar("totalDataUtilitiesOfApps",0,100);
	        IntVar totalConnUtilitiesOfApps = model.intVar("totalConnUtilitiesOfApps",0,100);
	        IntVar totalUtilitiesOfApps = model.intVar("totalUtilitiesOfApps",0,100);
	        
	        // SUM OF totalUtilityofApps 
	        IntVar totalUtility= model.intVar("totalUtility",0,100) ;
	        
	        // CONSTRAINTS
        	// initialize CHOCO IntVar domain values
        	int dPdomainSize = appArray[i].getSupportedDataEncodingProtocolsOfApp().length;
        	dataEncodingProtocolOfApp = model.intVar("dataEncodingProtocolsOfApp",appArray[i].getSupportedDataEncodingProtocolsOfApp());
        	int cPdomainSize = appArray[i].getSupportedConnectivitiyProtocolsOfApp().length;
        	connectivitiyProtocolOfApp=  model.intVar("connectivitiyProtocolsOfApp",appArray[i].getSupportedConnectivitiyProtocolsOfApp());
        	// end 
        	
        	// Create temp_totalDataUtil
        	IntVar[] data_utilities=  new IntVar[dPdomainSize];
        	for(int p1=0;p1<dPdomainSize;p1++){
        		
        		int dpID = appArray[i].getSupportedDataEncodingProtocolsOfApp()[p1];
        		int perf_dpID = ProtocolsKnowledgeBase.dataProtocolKnowledgeBase[dpID].getPerformance();
        		int rel_dpID = ProtocolsKnowledgeBase.dataProtocolKnowledgeBase[dpID].getReliability();
        		int cost_dpID = ProtocolsKnowledgeBase.dataProtocolKnowledgeBase[dpID].getCost();;
        		int tempDataUtil = perf_dpID*userweights[0] + rel_dpID*userweights[1] + cost_dpID*userweights[2];
        		data_utilities[p1] = model.intVar("data_utilities-"+p1,tempDataUtil);
        				
        		model.ifThen(
          			   model.arithm(dataEncodingProtocolOfApp,"=",dpID),
          			   // update the domain values of total_utility
          			   model.sum(ArrayUtils.toArray(data_utilities[p1]),"=",totalDataUtilitiesOfApps)
             			);
        	
        		model.ifThen(
           			   model.arithm(dataEncodingProtocolOfApp,"=",dpID),
           			   // update the domain values of total_utility
           			   model.arithm(dataEncodingProtocolOfGateway,"=",dpID)
              			);
        		
        	}
        	
        	// end of Create temp_totalDataUtil
        	
        	// Create temp_totalDataUtil
        	IntVar[] conn_utilities = new IntVar[cPdomainSize];
        	for(int p1=0;p1<cPdomainSize;p1++){
        		
        		int cpID = appArray[i].getSupportedConnectivitiyProtocolsOfApp()[p1];
        		int perf_cpID = ProtocolsKnowledgeBase.connetivityProtocolKnowledgeBase[cpID].getPerformance();
        		int rel_cpID = ProtocolsKnowledgeBase.connetivityProtocolKnowledgeBase[cpID].getReliability();
        		int cost_cpID = ProtocolsKnowledgeBase.connetivityProtocolKnowledgeBase[cpID].getCost();;
        		int tempConnUtil = perf_cpID*userweights[0] + rel_cpID*userweights[1] + cost_cpID*userweights[2];
        		conn_utilities[p1] = model.intVar("conn_utilities-"+p1,tempConnUtil);
        				
        		model.ifThen(
          			   model.arithm(connectivitiyProtocolOfApp,"=",cpID),
          			   // update the domain values of total_utility
          			   model.sum(ArrayUtils.toArray(conn_utilities[p1]),"=",totalConnUtilitiesOfApps)
             			);
        		

        		model.ifThen(
           			   model.arithm(connectivitiyProtocolOfApp,"=",cpID),
           			   // update the domain values of total_utility
           			   model.arithm(connectivitiyProtocolOfGateway,"=",cpID)
              			);
             	
        	}
        	// end of Create temp_totalDataUtil
        	
        	// sum APP and CONN utilities of the app
        	model.sum(ArrayUtils.toArray(totalDataUtilitiesOfApps,totalConnUtilitiesOfApps),"=",totalUtilitiesOfApps).post();
        	
        	Solution sol = model.getSolver().findOptimalSolution(totalUtilitiesOfApps, true);
        	
        	if(sol!=null){
	    		System.out.println(sol);
	    		this.configuredProfile.getInstalledApps()[i].setInUse_ConnectivitiyProtocol(sol.getIntVal(connectivitiyProtocolOfApp));
	    		this.configuredProfile.getInstalledApps()[i].setInUse_DataEncodingProtocol(sol.getIntVal(dataEncodingProtocolOfApp));
        	}
        	else{
	    		this.configuredProfile.getInstalledApps()[i].setInUse_ConnectivitiyProtocol(-1);
	    		this.configuredProfile.getInstalledApps()[i].setInUse_DataEncodingProtocol(-1);
	    		this.configuredProfile.setErrorMessage(this.configuredProfile.getErrorMessage()+"Warning: App-"+i+" could not be configured. It requires a Connectivity or Data Encoding Protocol which is not supported by the Gateway. ");
        	}
    		
        	
        } // end of calculate for each app
        
        
        // sum up utility values of  all apps into utilityValue
        //model.sum(totalUtilitiesOfApps,"=",totalUtility).post();	
        
		/////////////////////////////////////////////////
		// SOLVER ///////////////////////////////////////
		/////////////////////////////////////////////////
		
//        Solution sol = model.getSolver().findOptimalSolution(totalUtility, true);
//		System.out.println(sol);
//        
//		for (int i=0;i<appArray.length;i++){
//			this.configuredProfile.getInstalledApps()[i].setInUse_ConnectivitiyProtocol(sol.getIntVal(connectivitiyProtocolOfApp[i]));
//			this.configuredProfile.getInstalledApps()[i].setInUse_DataEncodingProtocol(sol.getIntVal(dataEncodingProtocolOfApp[i]));
//		}
	}
	
}
