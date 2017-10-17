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

package org.eclipse.agail.recommenderandconfigurator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.agail.recommenderandconfigurator.configurator.StaticServiceConfiguration;
import org.eclipse.agail.recommenderandconfigurator.recommendermodels.ListOfApps;
import org.eclipse.agail.recommenderandconfigurator.recommendermodels.ListOfClouds;
import org.eclipse.agail.recommenderandconfigurator.recommendermodels.ListOfDevices;
import org.eclipse.agail.recommenderandconfigurator.recommendermodels.ListOfWFs;

public class LoadConfigurations {
	
	public static StaticServiceConfiguration loadConfigurationProperties(){
		
		StaticServiceConfiguration conf = new StaticServiceConfiguration();
		Properties prop = new Properties();
		InputStream input = null;
	
		
		// LOAD PROPERTIES
		try {
	
			String filename = "configuration.properties";
			input = LoadConfigurations.class.getClassLoader().getResourceAsStream(filename);
			if(input==null){
		            System.out.println("Sorry, unable to find " + filename);
		            return conf;
			}
	
			//load a properties file from class path, inside static method
			prop.load(input);
	
	        //get the property value and print it out
			conf.recommenderServiceForDevelopmentUIActive = Boolean.valueOf(prop.getProperty("recommenderServiceForDevelopmentUIActive"));
			conf.recommenderServiceForDeviceManagementUIActive = Boolean.valueOf(prop.getProperty("recommenderServiceForDeviceManagementUIActive"));
			conf.recommenderServiceForAppManagementUIActive = Boolean.valueOf(prop.getProperty("recommenderServiceForAppManagementUIActive"));
			conf.allowRecommenderServerToUseGatewayProfile = Boolean.valueOf(prop.getProperty("allowRecommenderServerToUseGatewayProfile"));
			conf.recommenderServerIP =prop.getProperty("recommenderServerIP");
		
			
		} catch (IOException ex) {
			ex.printStackTrace();
	    } finally{
	    	if(input!=null){
	    		try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	}    
	   }
		return  conf;
	}

	public static org.eclipse.agail.recommenderandconfigurator.recommendermodels.GatewayProfile loadGatewayProfile_ForRecom_Properties(){
		
		org.eclipse.agail.recommenderandconfigurator.recommendermodels.GatewayProfile profile = new org.eclipse.agail.recommenderandconfigurator.recommendermodels.GatewayProfile();
		
		
		Properties prop = new Properties();
		InputStream input = null;
	
		try {
	
			String filename = "gateway_recommender.properties";
			input = LoadConfigurations.class.getClassLoader().getResourceAsStream(filename);
			if(input==null){
		            System.out.println("Sorry, unable to find " + filename);
			    return profile;
			}
	
			//load a properties file from class path, inside static method
			prop.load(input);
	
	        //get the property value and print it out
			String[] installedApps = prop.getProperty("installedApps").split(";");
			String[] pluggedDevs = prop.getProperty("pluggedDevs").split(";");
			String[] installedWFs = prop.getProperty("installedWFs").split(";");
			String[] usedClouds = prop.getProperty("usedClouds").split(";");
			String location = prop.getProperty("location");
			String pricingPreferences = prop.getProperty("pricingPreferences");
			
			profile.location =prop.getProperty("location");
			profile.pricingPreferences =prop.getProperty("pricingPreferences");
			
			
			List<org.eclipse.agail.recommenderandconfigurator.recommendermodels.App> appList = new ArrayList<org.eclipse.agail.recommenderandconfigurator.recommendermodels.App>();
			for(int i=0; i<installedApps.length;i++){
				String [] elements= installedApps[i].split(",");
				org.eclipse.agail.recommenderandconfigurator.recommendermodels.App newapp = new org.eclipse.agail.recommenderandconfigurator.recommendermodels.App(elements[0],elements[1],Integer.valueOf(elements[2]),Integer.valueOf(elements[3]));
				//System.out.println(cloudToBeAdded.getTitle());
				appList.add(newapp);
			}
			ListOfApps la = new ListOfApps();
			la.setAppList(appList);
			profile.setApps(la);
			
			
			List<org.eclipse.agail.recommenderandconfigurator.recommendermodels.Device> devList = new ArrayList<org.eclipse.agail.recommenderandconfigurator.recommendermodels.Device>();
			for(int i=0; i<pluggedDevs.length;i++){
				String [] elements= pluggedDevs[i].split(",");
				org.eclipse.agail.recommenderandconfigurator.recommendermodels.Device newdev = new org.eclipse.agail.recommenderandconfigurator.recommendermodels.Device(elements[0],elements[1]);
				//System.out.println(cloudToBeAdded.getTitle());
				devList.add(newdev);
			}
			ListOfDevices ld = new ListOfDevices();
			ld.setDeviceList(devList);
			profile.setDevices(ld);
			
			List<org.eclipse.agail.recommenderandconfigurator.recommendermodels.Workflow> wfList = new ArrayList<org.eclipse.agail.recommenderandconfigurator.recommendermodels.Workflow>();
			for(int i=0; i<installedWFs.length;i++){
				String [] elements= installedWFs[i].split(",");
				org.eclipse.agail.recommenderandconfigurator.recommendermodels.Workflow newwf = new org.eclipse.agail.recommenderandconfigurator.recommendermodels.Workflow(elements[0],elements[1],elements[2],elements[3]);
				//System.out.println(cloudToBeAdded.getTitle());
				wfList.add(newwf);
			}
			ListOfWFs lw = new ListOfWFs();
			lw.setWfList(wfList);
			profile.setWfs(lw);
			
			List<org.eclipse.agail.recommenderandconfigurator.recommendermodels.Cloud> clList = new ArrayList<org.eclipse.agail.recommenderandconfigurator.recommendermodels.Cloud>();
			for(int i=0; i<usedClouds.length;i++){
				String [] elements= usedClouds[i].split(",");
				org.eclipse.agail.recommenderandconfigurator.recommendermodels.Cloud newcl = new org.eclipse.agail.recommenderandconfigurator.recommendermodels.Cloud(elements[0],elements[1],null,null,null,null,null,null,null);
				//System.out.println(cloudToBeAdded.getTitle());
				clList.add(newcl);
			}
			ListOfClouds lc = new ListOfClouds();
			lc.setCloudList(clList);
			profile.setClouds(lc);
			
			
		} catch (IOException ex) {
			ex.printStackTrace();
	    } finally{
	    	if(input!=null){
	    		try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	}    
	   }
	
		return profile;
	}

	public static org.eclipse.agail.recommenderandconfigurator.configuratormodels.GatewayProfile loadGatewayProfile_ForConf_Properties(){
		
		org.eclipse.agail.recommenderandconfigurator.configuratormodels.GatewayProfile profile = new org.eclipse.agail.recommenderandconfigurator.configuratormodels.GatewayProfile();
		
		
		Properties prop = new Properties();
		InputStream input = null;
	
		try {
	
			String filename = "gateway_configurator.properties";
			input = LoadConfigurations.class.getClassLoader().getResourceAsStream(filename);
			if(input==null){
		            System.out.println("Sorry, unable to find " + filename);
			    return profile;
			}
	
			//load a properties file from class path, inside static method
			prop.load(input);
	
	        //get the property value and print it out
			String[] installedApps = prop.getProperty("installedApps").split(";");
			String[] supportedDataEncodingProtocolsOfGateway = prop.getProperty("supportedDataEncodingProtocolsOfGateway").split(",");
			String[] supportedConnectivityProtocolsOfGateway = prop.getProperty("supportedConnectivityProtocolsOfGateway").split(",");
			int [] s1= new int[supportedDataEncodingProtocolsOfGateway.length];
			int [] s2= new int[supportedConnectivityProtocolsOfGateway.length];
			
			for(int p=0;p<s1.length;p++)
				s1[p]=Integer.valueOf(supportedDataEncodingProtocolsOfGateway[p]);
			for(int p=0;p<s2.length;p++)
				s2[p]=Integer.valueOf(supportedConnectivityProtocolsOfGateway[p]);
			profile.setSupportedDataEncodingProtocolsOfGateway(s1);
			profile.setSupportedConnectivityProtocolsOfGateway(s2);
			
			profile.setUserRequirementWeight_Performance(Integer.valueOf(prop.getProperty("userRequirementWeight_Performance")));
			profile.setUserRequirementWeight_Reliability(Integer.valueOf(prop.getProperty("userRequirementWeight_Reliability")));
			profile.setUserRequirementWeight_Cost(Integer.valueOf(prop.getProperty("userRequirementWeight_Cost")));
			
			
			org.eclipse.agail.recommenderandconfigurator.configuratormodels.App [] appList = new org.eclipse.agail.recommenderandconfigurator.configuratormodels.App [installedApps.length];
			for(int i=0; i<installedApps.length;i++){
				String [] elements= installedApps[i].split(",");
				String []protocols1 = elements[4].split(" ");
				String []protocols2 = elements[5].split(" ");
				
				int [] p1= new int[protocols1.length];
				int [] p2= new int[protocols2.length];
				
				for(int p=0;p<protocols1.length;p++)
					p1[p]=Integer.valueOf(protocols1[p]);
				for(int p=0;p<protocols2.length;p++)
					p2[p]=Integer.valueOf(protocols2[p]);
				
				org.eclipse.agail.recommenderandconfigurator.configuratormodels.App newapp = new org.eclipse.agail.recommenderandconfigurator.configuratormodels.App();
				newapp.setName(elements[0]);
				newapp.setUrl(elements[1]);
				newapp.setInUse_DataEncodingProtocol(Integer.valueOf(elements[2]));
				newapp.setInUse_ConnectivitiyProtocol(Integer.valueOf(elements[3]));
				newapp.setSupportedDataEncodingProtocolsOfApp(p1);
				newapp.setSupportedConnectivitiyProtocolsOfApp(p2);
				
				appList[i]=newapp;
			}
			
			profile.setInstalledApps(appList);
			
			
		} catch (IOException ex) {
			ex.printStackTrace();
	    } finally{
	    	if(input!=null){
	    		try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	}    
	   }
	
		return profile;
	}

	
}
