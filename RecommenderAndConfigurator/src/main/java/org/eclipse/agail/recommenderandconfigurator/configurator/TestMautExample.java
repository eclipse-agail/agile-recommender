/*******************************************************************************
 * Copyright (C) 2017 TUGraz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     TUGraz - initial API and implementation
 ******************************************************************************/

package org.eclipse.agail.recommenderandconfigurator.configurator;
import java.util.Arrays;
import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

public class TestMautExample {
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Decide on 1 configuration which is best in total
		/////////////////////////////////////////////////
		// MODEL ////////////////////////////////////////
		/////////////////////////////////////////////////
        Model model = new Model("calculateUtility");
		
       
        /////////////////////////////////////////////////
        // VARIABLES ////////////////////////////////////
        /////////////////////////////////////////////////
        
        // USER WEIGHTS FOR 3 DIMENSIONS OF CONFIGURATIONS
        // ONLY IN JAVA
        int user_weight[][] = new int[][]{
        	{1,2,3}, // u1
        	{3,1,1}}; // u2
        
		// UTILITY VALUES OF 3 DIMENSIONS OF CONFIGURATION
		// IN JAVA
		int[][] configurations = new int[][]{
            {9,5,8}, // conf1 ->  Utility for user-0=9*1+5*2+8*3=43 and  user-1=40 -> TOTAL=83 -> BEST CONF in TOTAL
            {5,8,3}}; // conf2 ->  Utility for user-0=30 and  user-1=26 -> TOTAL=56
        // IN CHOCO to be able to use it in scalar function
        IntVar[][] configurations_choco = new IntVar [2][3];
        for (int j = 0; j < 2; j++) {
        	for (int k = 0; k < 3; k++) 
            configurations_choco[j][k] = model.intVar("configurations_choco"+j+k, configurations[j][k]);
        }	
        
        IntVar selectedConfigurationID = model.intVar("selectedConfiguration",0,1) ;
        
        // Calculate utilities of configuration
        IntVar total_utility = model.intVar("total_utility",0,100) ;
        IntVar [] utilityOfConfiguration = new IntVar[2]; // number of configurations
        IntVar [][] utilityOfUser = new IntVar[2][2]; // number of configurations * number of users 
   
        for(int i=0;i<2;i++){
        	
        	for(int j=0;j<2;j++){
        		// define user-j utility IntVar for configuration-i
        		utilityOfUser[i][j] = model.intVar("utilityOfUser_"+j+"_for_conf_"+i, 0,100);
        		// calculate the utility value
        		model.scalar(configurations_choco[i],user_weight[j], "=", utilityOfUser[i][j]).post();
            }
        	utilityOfConfiguration[i] = model.intVar("utilityOfConfiguration_"+i,0,100);
        	model.sum(utilityOfUser[i],"=",utilityOfConfiguration[i]).post();
        	
        	/////////////////////////////////////////////////
        	// CONSTRAINTS //////////////////////////////////
        	/////////////////////////////////////////////////
        	// if configuration-x is selected then the total utility will be the calculated utility of conf-x
        	model.ifThen(
        			   model.arithm(selectedConfigurationID,"=",i),
        			   // update the domain values of total_utility
        			   model.sum(ArrayUtils.toArray(utilityOfConfiguration[i]),"=",total_utility)
        	);
       	
        }
      
        /////////////////////////////////////////////////
        // SOLVER ///////////////////////////////////////
        /////////////////////////////////////////////////
		Solution sol = model.getSolver().findOptimalSolution(total_utility, true);
		System.out.println(sol);
	}

	
}
