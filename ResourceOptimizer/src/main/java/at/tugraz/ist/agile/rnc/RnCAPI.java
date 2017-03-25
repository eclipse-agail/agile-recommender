package at.tugraz.ist.agile.rnc;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import at.tugraz.ist.agile.configurator.Optimizer;
import at.tugraz.ist.agile.configurator.StaticServiceConfiguration;
import at.tugraz.ist.agile.recommendermodels.ListOfApps;
import at.tugraz.ist.agile.recommendermodels.ListOfClouds;
import at.tugraz.ist.agile.recommendermodels.ListOfDevices;
import at.tugraz.ist.agile.recommendermodels.ListOfWFs;


@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class RnCAPI {
	
	public static StaticServiceConfiguration conf = new StaticServiceConfiguration();
	//public static String recommenderIP = "http://ec2-54-201-143-18.us-west-2.compute.amazonaws.com:8080/Recommender/";
	static at.tugraz.ist.agile.recommendermodels.GatewayProfile profile = new at.tugraz.ist.agile.recommendermodels.GatewayProfile(); 
	static at.tugraz.ist.agile.configuratormodels.GatewayProfile configurationProfile = new at.tugraz.ist.agile.configuratormodels.GatewayProfile();
	
	public static void main(String[] args) throws Exception {
	    SpringApplication.run(RnCAPI.class, args);
	    conf = new StaticServiceConfiguration();
	    conf.loadProperties();
	    profile = LoadConfigurations.loadGatewayProfile_ForRecom_Properties();
	    configurationProfile= LoadConfigurations.loadGatewayProfile_ForConf_Properties();
	    
	}

	/**
	 * @apiDescription AGILE Recommender and Configurator Docker Service
	 * @apiVersion 1.0.0
	 * 
	 */
    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "Agile Recommener and Configurator Service is running.";
    }
    
    /**
     * @api {get} /recommenderdockerservice/getResourceOptimization GetResourceOptimization 
     * @apiName GetResourceOptimization
     * @apiVersion 1.0.0
     * @apiGroup 2-Configurator
     * @apiDescription Returns Resource Optimized Configuration.
     *
     * @apiSuccess {Object} installedApps	Optimized Configuration of installed Apps.
     * @apiSuccess {Object} pluggedDevs		plugged devices on the gateway.
     * @apiSuccess {Object} installedWFs	installed nodes on the gateway.
     * @apiSuccess {String} [location]		location of the gateway as "EU", "NA", "SA", "AS", "OC" 
     * @apiSuccess {String} [pricingPreferences]	pricing preferences of the user as "metered", "fixed", "free"
     *
	 * @apiSampleRequest http://192.168.1.246:8090/recommenderdockerservice/getResourceOptimization/
     */
    @ResponseBody @RequestMapping("/getResourceOptimization")
    public at.tugraz.ist.agile.configuratormodels.GatewayProfile getResourceOptimization() {
    	
    	Optimizer optimizer = new Optimizer(configurationProfile);
    	return optimizer.getconfiguredProfile();
      
    }
    
    
    /**
     * @api {get} /recommenderdockerservice/getAppRecommendation GetAppRecommendation 
     * @apiName GetAppRecommendation
     * @apiVersion 1.0.0
     * @apiGroup 1-Recommender
     * @apiDescription Returns Recommended Apps.
     *
     *
     * @apiSuccess {Object} ListOfApps 	List of Recommended Apps with their title,href,stars,downloads
     *
	 * @apiSampleRequest http://192.168.1.246:8090/recommenderdockerservice/getAppRecommendation/
     */
    @ResponseBody @RequestMapping("/getAppRecommendation")
    public ListOfApps getAppRecommendation() {
    	ListOfApps recommendedApps = new ListOfApps();
    	
    	if(conf.allowRecommenderServerToUseGatewayProfile==true && conf.recommenderServiceForAppManagementUIActive==true ){
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	
			final String uri = conf.recommenderServerIP+"getAppRecommendation";
			 
			recommendedApps = restTemplate.postForObject(uri, profile, ListOfApps.class);
    	}
    	
    	return recommendedApps;
    	
    }
    
    /**
     * @api {get} /recommenderdockerservice/getWorkflowRecommendation GetWorkflowRecommendation 
     * @apiName GetWorkflowRecommendation
     * @apiVersion 1.0.0
     * @apiGroup 1-Recommender
     * @apiDescription Returns Recommended Workflows.
     *
     *
     * @apiSuccess {Object} ListOfWFs List of Recommended Workflows with their type,datatag,dataowner,href.
     *
	 * @apiSampleRequest http://192.168.1.246:8090/recommenderdockerservice/getWorkflowRecommendation/
     */
    @ResponseBody @RequestMapping("/getWorkflowRecommendation")
    public ListOfWFs getWorkflowRecommendation () {
    	ListOfWFs result = new ListOfWFs();
    	
    	if(conf.allowRecommenderServerToUseGatewayProfile==true && conf.recommenderServiceForDevelopmentUIActive==true ){
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	final String uri = conf.recommenderServerIP+"getWorkflowRecommendation";
			 
	    	result = restTemplate.postForObject(uri, profile, ListOfWFs.class);
    	}
			
		return result;
      
    }
    
    
    /**
     * @api {get} /recommenderdockerservice/getCloudRecommendation GetCloudRecommendation 
     * @apiName GetCloudRecommendation
     * @apiVersion 1.0.0
     * @apiGroup 1-Recommender
     * @apiDescription Returns Recommended Cloud Servers.
     *
     *
     * @apiSuccess {Object} RecommendedClouds List of Recommended Clouds with their title,link,accesstype,locations,middlewares,frameworks,runtimes,services,pricing.
     *
	 * @apiSampleRequest http://192.168.1.246:8090/recommenderdockerservice/getCloudRecommendation/
     */
    @ResponseBody @RequestMapping("/getCloudRecommendation")
    public ListOfClouds getCloudRecommendation () {
    	ListOfClouds result = new ListOfClouds();
    	
    	if(conf.allowRecommenderServerToUseGatewayProfile==true && conf.recommenderServiceForDevelopmentUIActive==true ){
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	final String uri = conf.recommenderServerIP+"getCloudRecommendation";
			 
	    	result = restTemplate.postForObject(uri, profile, ListOfClouds.class);
    	}
		return result;
      
    }
    
    /**
     * @api {get} /recommenderdockerservice/getDeviceRecommendation getDeviceRecommendation 
     * @apiName getDeviceRecommendation
     * @apiVersion 1.0.0
     * @apiGroup 1-Recommender
     * @apiDescription Returns Recommended Devices.
     *
     *
     * @apiSuccess {Object} RecommendedDevices List of Recommended Devices with their title and link.
     *
	 * @apiSampleRequest http://192.168.1.246:8090/recommenderdockerservice/getDeviceRecommendation/
     */
    @ResponseBody @RequestMapping("/getDeviceRecommendation")
    public ListOfDevices getDeviceRecommendation () {
    	ListOfDevices result = new ListOfDevices();
    	
    	if(conf.allowRecommenderServerToUseGatewayProfile==true && conf.recommenderServiceForDeviceManagementUIActive==true ){
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	final String uri = conf.recommenderServerIP+"getDeviceRecommendation";
			 
	    	result = restTemplate.postForObject(uri, profile, ListOfDevices.class);
    	}
		return result;
      
    }
    
    /**
     * @api {get} /recommenderdockerservice/updateRepositories UpdateRepositories 
     * @apiName UpdateRepositories
     * @apiVersion 1.0.0
     * @apiGroup 1-Recommender
     * @apiDescription Updates the local repositories by getting new items from Amazon, Docker.hub and Node.Red websites.
     *
     *
     * @apiSuccess {Number} success is 0, error is -1
     *
	 * @apiSampleRequest http://192.168.1.246:8090/recommenderdockerservice/updateRepositories/
     */
    @ResponseBody @RequestMapping("/updateRepositories")
    public int updateRepositories () {
    	int res = -1;
    	
    	if(conf.allowRecommenderServerToUseGatewayProfile==true){
	    	RestTemplate restTemplate = new RestTemplate();
	    	final String uri = conf.recommenderServerIP+"updateRepositories";
			 
	    	String str = restTemplate.getForObject(uri, String.class);
	    	res=0;
    	}
			
		return res;
      
    }
    
    /**
     * @api {get} /recommenderdockerservice/updateGatewayProfile UpdateGatewayProfile 
     * @apiName UpdateGatewayProfile
     * @apiVersion 1.0.0
     * @apiGroup 1-Recommender
     * @apiDescription Takes the current the gateway profile.
     *
     *
     * @apiSuccess {Number} success is 0, error is -1
     *
	 * @apiSampleRequest http://192.168.1.246:8090/recommenderdockerservice/updateGatewayProfile/
     */
    @ResponseBody @RequestMapping("/updateGatewayProfile")
    public int updateGatewayProfile () {
    	
    	profile = new at.tugraz.ist.agile.recommendermodels.GatewayProfile(); 
    	configurationProfile = new at.tugraz.ist.agile.configuratormodels.GatewayProfile();
    	
    	Properties prop = new Properties();
		InputStream input = null;
	
		// LOAD GATEWAY PROFILE FOR RECoMMENDER
		try {
	
			String filename = "gateway_recommender.properties";
			input = at.tugraz.ist.agile.recommendermodels.GatewayProfile.class.getClassLoader().getResourceAsStream(filename);
			if(input==null){
		            System.out.println("Sorry, unable to find " + filename);
			    return -1;
			}
	
			//load a properties file from class path, inside static method
			prop.load(input);
	
	        //get the property value and print it out
			String[] installedApps = prop.getProperty("installedApps").split(";");
			String[] pluggedDevs = prop.getProperty("pluggedDevs").split(";");
			String[] installedWFs = prop.getProperty("installedWFs").split(";");
			String location = prop.getProperty("location");
			String pricingPreferences = prop.getProperty("pricingPreferences");
			
			for(int i=0; i<installedApps.length;i++){
				String[] elements= installedApps[i].split(",");
				profile.apps.getAppList().add(new at.tugraz.ist.agile.recommendermodels.App(elements[0],elements[1],Integer.valueOf(elements[2]),Integer.valueOf(elements[3])));
			}
			for(int i=0; i<pluggedDevs.length;i++){
				String[] elements= pluggedDevs[i].split(",");
				profile.devices.getDeviceList().add(new at.tugraz.ist.agile.recommendermodels.Device(elements[0],elements[1]));
			}
			for(int i=0; i<installedWFs.length;i++){
				String[] elements= installedWFs[i].split(",");
				profile.wfs.getWfList().add(new at.tugraz.ist.agile.recommendermodels.Workflow(elements[0],elements[1],elements[2],elements[3]));
			}
			
			profile.location = location;
			profile.pricingPreferences = pricingPreferences;
			
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
		
		// LOAD GATEWAY PROFILE FOR CONFIGURATOR
				try {
			
					String filename = "gateway_configurator.properties";
					input = at.tugraz.ist.agile.recommendermodels.GatewayProfile.class.getClassLoader().getResourceAsStream(filename);
					if(input==null){
				            System.out.println("Sorry, unable to find " + filename);
					    return -1;
					}
			
					//load a properties file from class path, inside static method
					prop.load(input);
			
			        //get the property value and print it out
					String[] supportedDataEncodingProtocolsOfGateway = prop.getProperty("supportedDataEncodingProtocolsOfGateway").split(",");
					String[] supportedConnectivityProtocolsOfGateway = prop.getProperty("supportedConnectivityProtocolsOfGateway").split(",");
					String userRequirementWeight_Performance = prop.getProperty("userRequirementWeight_Performance");
					String userRequirementWeight_Reliability = prop.getProperty("userRequirementWeight_Reliability");
					String userRequirementWeight_Cost = prop.getProperty("userRequirementWeight_Cost");
					String[] installedApps = prop.getProperty("installedApps").split(";");
					
					int [] supportedConnectivityProtocolsOfGateway_int = new int[supportedConnectivityProtocolsOfGateway.length];
					for(int i=0; i<supportedConnectivityProtocolsOfGateway.length;i++){
						supportedConnectivityProtocolsOfGateway_int[i]= Integer.valueOf(supportedDataEncodingProtocolsOfGateway[i]);
					}
					
					int [] supportedDataEncodingProtocolsOfGateway_int=new int[supportedDataEncodingProtocolsOfGateway.length];
					for(int i=0; i<supportedDataEncodingProtocolsOfGateway.length;i++){
						supportedDataEncodingProtocolsOfGateway_int[i]= Integer.valueOf(supportedDataEncodingProtocolsOfGateway[i]);
					}
					at.tugraz.ist.agile.configuratormodels.App [] apps = new at.tugraz.ist.agile.configuratormodels.App [installedApps.length];
					for(int i=0; i<installedApps.length;i++){
						String [] elements = installedApps[i].split(",");
						apps[i]= new at.tugraz.ist.agile.configuratormodels.App();
						apps[i].setName(elements[0]);
						apps[i].setUrl(elements[1]);
						apps[i].setInUse_DataEncodingProtocol(Integer.valueOf(elements[2]));
						apps[i].setInUse_ConnectivitiyProtocol(Integer.valueOf(elements[3]));
						
						String [] elements2 = elements[4].split(" ");
						int [] elements2_int=new int[elements2.length];
						for(int e=0; e<elements2.length;i++){
							elements2_int[e]= Integer.valueOf(elements2[e]);
						}
						
						String [] elements3 = elements[5].split(" ");
						int [] elements3_int=new int[elements3.length];
						for(int e=0; e<elements3.length;i++){
							elements3_int[e]= Integer.valueOf(elements3[e]);
						}
						 
						apps[i].setSupportedDataEncodingProtocolsOfApp(elements2_int);
						apps[i].setSupportedConnectivitiyProtocolsOfApp(elements3_int);
					}
					
					
					configurationProfile.setSupportedConnectivityProtocolsOfGateway(supportedConnectivityProtocolsOfGateway_int);
					configurationProfile.setSupportedDataEncodingProtocolsOfGateway(supportedDataEncodingProtocolsOfGateway_int);
					configurationProfile.setInstalledApps(apps);
					
					configurationProfile.setUserRequirementWeight_Performance(Integer.valueOf(userRequirementWeight_Performance));
					configurationProfile.setUserRequirementWeight_Reliability(Integer.valueOf(userRequirementWeight_Reliability));
					configurationProfile.setUserRequirementWeight_Cost(Integer.valueOf(userRequirementWeight_Cost));
					
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

    /**
     * @api {get} /recommenderdockerservice/getServiceConfiguration getServiceConfiguration 
     * @apiName getServiceConfiguration
     * @apiVersion 1.0.0
     * @apiGroup 3-Settings
     * @apiDescription Returns the settings of the service.
     *
     *
     * @apiSuccess {Object} StaticServiceConfiguration Current settings of the service.
     *
	 * @apiSampleRequest http://192.168.1.246:8090/
     */
    @ResponseBody @RequestMapping("/getServiceConfiguration")
    public StaticServiceConfiguration getServiceConfiguration () {
    	return this.conf;
    }
    
    /**
     * @api {put} /recommenderdockerservice/getDeviceRecommendation SetServiceConfiguration 
     * @apiName SetServiceConfiguration
     * @apiVersion 1.0.0
     * @apiGroup 3-Settings
     * @apiDescription Updates the settings of the service.
     *
     * @apiParam {Object} StaticServiceConfiguration	Settings of the service.
     *
     * @apiSuccess (200) settings are updated successfully.
     *
	 * @apiSampleRequest http://192.168.1.246:8090/recommenderdockerservice/setServiceConfiguration/
     */
    @RequestMapping("/setServiceConfiguration")
    public void setServiceConfiguration (@RequestBody StaticServiceConfiguration conf2) {
    	this.conf= conf2;
    	//conf.updateProperties(conf2);
    }

    /**
     * @api {get} /recommenderdockerservice/getServiceConfiguration getGatewayProfileForRecommender 
     * @apiName getGatewayProfileForRecommender
     * @apiVersion 1.0.0
     * @apiGroup 4-For Testing Purposes
     * @apiDescription Returns the gateway profile used by Recommender.
     *
     *
     * @apiSuccess {Object} GatewayProfile Current profile of the gateway.
     *
	 * @apiSampleRequest http://192.168.1.246:8090/recommenderdockerservice/getGatewayProfileForRecommender/
     */
    @ResponseBody @RequestMapping("/getGatewayProfileForRecommender")
    public at.tugraz.ist.agile.recommendermodels.GatewayProfile getGatewayProfileForRecommender () {
    	
		return profile;
    }
    
    /**
     * @api {put} /recommenderdockerservice/getServiceConfiguration setGatewayProfileForRecommender 
     * @apiName setGatewayProfileForRecommender
     * @apiVersion 1.0.0
     * @apiGroup 4-For Testing Purposes
     * @apiDescription Sets the gateway profile used by Recommender.
     *
     * @apiParam {Object} GatewayProfile	Profile of the gateway.
     *
	 * @apiSampleRequest http://192.168.1.246:8090/recommenderdockerservice/setGatewayProfileForRecommender/
     */
    @ResponseBody @RequestMapping("/setGatewayProfileForRecommender")
    public void setGatewayProfileForRecommender (@RequestBody  at.tugraz.ist.agile.recommendermodels.GatewayProfile prof) {
		this.profile = prof;
    }
  
    
    /**
     * @api {get} /recommenderdockerservice/getServiceConfiguration getGatewayProfileForConfigurator 
     * @apiName getGatewayProfileForConfigurator
     * @apiVersion 1.0.0
     * @apiGroup 4-For Testing Purposes
     * @apiDescription Returns the gateway profile used by Configurator.
     *
     *
     * @apiSuccess {Object} GatewayProfile Current profile of the gateway.
     *
	 * @apiSampleRequest http://192.168.1.246:8090/recommenderdockerservice/getGatewayProfileForConfigurator/
     */
    @ResponseBody @RequestMapping("/getGatewayProfileForConfigurator")
    public at.tugraz.ist.agile.configuratormodels.GatewayProfile getGatewayProfileForConfigurator () {
    	
		return configurationProfile;
    }
    
    /**
     * @api {put} /recommenderdockerservice/getServiceConfiguration setGatewayProfileForConfigurator 
     * @apiName setGatewayProfileForConfigurator
     * @apiVersion 1.0.0
     * @apiGroup 4-For Testing Purposes
     * @apiDescription Sets the gateway profile used by Configurator.
     *
     * @apiParam {Object} GatewayProfile	Profile of the gateway.
     *
     * @apiParamExample {json} Request-Example:
	 *     {
	 *       "id": 4711
	 *     }
     *
	 * @apiSampleRequest http://192.168.1.246:8090/recommenderdockerservice/setGatewayProfileForConfigurator/
     */
    @ResponseBody @RequestMapping("/setGatewayProfileForConfigurator")
    public void setGatewayProfileForConfigurator (@RequestBody  at.tugraz.ist.agile.configuratormodels.GatewayProfile prof) {
		this.configurationProfile = prof;
    }
  
   
}



