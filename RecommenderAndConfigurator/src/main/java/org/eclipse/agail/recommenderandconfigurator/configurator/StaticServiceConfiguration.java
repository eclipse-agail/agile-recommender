package org.eclipse.agail.recommenderandconfigurator.configurator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;


public class StaticServiceConfiguration {
	
	public boolean recommenderServiceForDevelopmentUIActive;
	public boolean recommenderServiceForDeviceManagementUIActive;
	public boolean recommenderServiceForAppManagementUIActive;
	public boolean allowRecommenderServerToUseGatewayProfile;
	public String recommenderServerIP;
	
	public StaticServiceConfiguration(){
		
	}
	
	public int loadProperties(){
		
		Properties prop = new Properties();
		InputStream input = null;
	
		
		// LOAD PROPERTIES
		try {
	
			String filename = "configuration.properties";
			input = org.eclipse.agail.recommenderandconfigurator.recommendermodels.GatewayProfile.class.getClassLoader().getResourceAsStream(filename);
			if(input==null){
		            System.out.println("Sorry, unable to find " + filename);
		            return -1;
			}
	
			//load a properties file from class path, inside static method
			prop.load(input);
	
	        //get the property value and print it out
			recommenderServiceForDevelopmentUIActive = Boolean.valueOf(prop.getProperty("recommenderServiceForDevelopmentUIActive"));
			recommenderServiceForDeviceManagementUIActive = Boolean.valueOf(prop.getProperty("recommenderServiceForDeviceManagementUIActive"));
			recommenderServiceForAppManagementUIActive = Boolean.valueOf(prop.getProperty("recommenderServiceForAppManagementUIActive"));
			allowRecommenderServerToUseGatewayProfile = Boolean.valueOf(prop.getProperty("allowRecommenderServerToUseGatewayProfile"));
		    recommenderServerIP=prop.getProperty("recommenderServerIP");
		
			
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
		return 0;
	}

	/*public int updateProperties(StaticServiceConfiguration conf){
		
		FileOutputStream out;
		try {
			FileInputStream in = new FileInputStream("configuration.properties");
			Properties props = new Properties();
			props.load(in);
			in.close();
			
			out = new FileOutputStream("configuration.properties");
		
			props.setProperty("recommenderServiceForDevelopmentUIActive", String.valueOf(conf.recommenderServiceForDevelopmentUIActive));
			props.setProperty("recommenderServiceForDeviceManagementUIActive", String.valueOf(conf.recommenderServiceForDeviceManagementUIActive));
			props.setProperty("recommenderServiceForAppManagementUIActive", String.valueOf(conf.recommenderServiceForAppManagementUIActive));
			props.setProperty("allowRecommenderServerToUseGatewayProfile", String.valueOf(conf.allowRecommenderServerToUseGatewayProfile));
			props.setProperty("recommenderServerIP", String.valueOf(conf.recommenderServerIP));
			props.store(out, null);
			out.close();
		 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		return 0;
	} */

	public boolean isRecommenderServiceForDevelopmentUIActive() {
		return recommenderServiceForDevelopmentUIActive;
	}

	public void setRecommenderServiceForDevelopmentUIActive(boolean recommenderServiceForDevelopmentUIActive) {
		this.recommenderServiceForDevelopmentUIActive = recommenderServiceForDevelopmentUIActive;
	}

	public boolean isRecommenderServiceForDeviceManagementUIActive() {
		return recommenderServiceForDeviceManagementUIActive;
	}

	public void setRecommenderServiceForDeviceManagementUIActive(
			boolean recommenderServiceForDeviceManagementUIActive) {
		this.recommenderServiceForDeviceManagementUIActive = recommenderServiceForDeviceManagementUIActive;
	}

	public boolean isRecommenderServiceForAppManagementUIActive() {
		return recommenderServiceForAppManagementUIActive;
	}

	public void setRecommenderServiceForAppManagementUIActive(boolean recommenderServiceForAppManagementUIActive) {
		this.recommenderServiceForAppManagementUIActive = recommenderServiceForAppManagementUIActive;
	}

	public boolean isAllowRecommenderServerToUseGatewayProfile() {
		return allowRecommenderServerToUseGatewayProfile;
	}

	public  void setAllowRecommenderServerToUseGatewayProfile(boolean allowRecommenderServerToUseGatewayProfile) {
		this.allowRecommenderServerToUseGatewayProfile = allowRecommenderServerToUseGatewayProfile;
	}

	public  String getRecommenderServerIP() {
		return recommenderServerIP;
	}

	public  void setRecommenderServerIP(String recommenderServerIP) {
		this.recommenderServerIP = recommenderServerIP;
	}



}
