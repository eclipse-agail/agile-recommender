package at.tugraz.ist.agile.configurator;


import java.util.Arrays;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import at.tugraz.ist.agile.configuratormodels.GatewayProfile;
import at.tugraz.ist.agile.recommendermodels.RecommendedApps;
import at.tugraz.ist.agile.recommendermodels.RecommendedClouds;
import at.tugraz.ist.agile.recommendermodels.RecommendedDevices;
import at.tugraz.ist.agile.recommendermodels.RecommendedWFs;

@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class ConfiguratorAPI {
	
	
	public static void main(String[] args) throws Exception {
	    SpringApplication.run(ConfiguratorAPI.class, args);
	}

    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "Agile Configurator is running. You can access its restful post-service via /getResourceOptimization.";
    }
    
    @ResponseBody @RequestMapping("/getResourceOptimization")
    public GatewayProfile getResourceOptimization(@RequestBody GatewayProfile profile) {
    	
    	Optimizer optimizer = new Optimizer(profile);
    	return optimizer.getconfiguredProfile();
      
    }
    
    @ResponseBody @RequestMapping("/getAppRecommendation")
    public RecommendedApps getAppRecommendation(@RequestBody at.tugraz.ist.agile.recommendermodels.GatewayProfile profile) {
    	
    	RestTemplate restTemplate = new RestTemplate();
    	
		final String uri = "http://54.213.147.198:8080/Recommender/getAppRecommendation";
		 
		RecommendedApps recommendedApps = restTemplate.postForObject(uri, profile, RecommendedApps.class);
		
		return recommendedApps;
		
    }
    
    @ResponseBody @RequestMapping("/getWorkflowRecommendation")
    public RecommendedWFs getWorkflowRecommendation (@RequestBody at.tugraz.ist.agile.recommendermodels.GatewayProfile profile) {
    	
    	RestTemplate restTemplate = new RestTemplate();
    	final String uri = "http://54.213.147.198:8080/Recommender/getWorkflowRecommendation";
		 
    	RecommendedWFs result = restTemplate.postForObject(uri, profile, RecommendedWFs.class);
		
		return result;
      
    }
    
    @ResponseBody @RequestMapping("/getCloudRecommendation")
    
    public RecommendedClouds getCloudRecommendation (@RequestBody at.tugraz.ist.agile.recommendermodels.GatewayProfile profile) {
    	
    	RestTemplate restTemplate = new RestTemplate();
    	final String uri = "http://54.213.147.198:8080/Recommender/getCloudRecommendation";
		 
    	RecommendedClouds result = restTemplate.postForObject(uri, profile, RecommendedClouds.class);
		
		return result;
      
    }
    
    @ResponseBody @RequestMapping("/getDeviceRecommendation")
    public RecommendedDevices getDeviceRecommendation (@RequestBody at.tugraz.ist.agile.recommendermodels.GatewayProfile profile) {
    	
    	RestTemplate restTemplate = new RestTemplate();
    	final String uri = "http://54.213.147.198:8080/Recommender/getDeviceRecommendation";
		 
    	RecommendedDevices result = restTemplate.postForObject(uri, profile, RecommendedDevices.class);
		
		return result;
      
    }
    
    @ResponseBody @RequestMapping("/updateRepositories")
    public String getCloudRecommendation () {
    	
    	RestTemplate restTemplate = new RestTemplate();
    	final String uri = "http://54.213.147.198:8080/Recommender/updateRepositories";
		 
    	String str = restTemplate.getForObject(uri, String.class);
		
		return str;
      
    }

    @ResponseBody @RequestMapping("/getPluggedDevices")
    public RecommendedDevices getPluggedDevices () {
    	return StaticGatewayProfile.devices;
    }
    @ResponseBody @RequestMapping("/getinstalledApps")
    public RecommendedApps getinstalledApps () {
    	return StaticGatewayProfile.apps;
    }
    @ResponseBody @RequestMapping("/getinstalledWfs")
    public RecommendedWFs getinstalledWfs () {
    	return StaticGatewayProfile.wfs;
    }
    @ResponseBody @RequestMapping("/getResources")
    public String getResources () {
    	return StaticGatewayProfile.resources;
    }
    @ResponseBody @RequestMapping("/getLocation")
    public String getLocation () {
    	return StaticGatewayProfile.location;
    }
    @ResponseBody @RequestMapping("/getPricingPreferences")
    public String getPricingPreferences () {
    	return StaticGatewayProfile.pricingPreferences;
    }
    
    
    @ResponseBody @RequestMapping("/getsupportedDataEncodingProtocolsOfGateway")
    public int[] getsupportedDataEncodingProtocolsOfGateway () {
    	return StaticGatewayProfile.supportedDataEncodingProtocolsOfGateway;
    }
    @ResponseBody @RequestMapping("/getsupportedConnectivityProtocolsOfGateway")
    public int[] getsupportedConnectivityProtocolsOfGateway () {
    	return StaticGatewayProfile.supportedConnectivityProtocolsOfGateway;
    }
    @ResponseBody @RequestMapping("/getuserRequirementWeight_Performance")
    public int getuserRequirementWeight_Performance () {
    	return StaticGatewayProfile.userRequirementWeight_Performance;
    }
    @ResponseBody @RequestMapping("/getuserRequirementWeight_Reliability")
    public int getuserRequirementWeight_Reliability () {
    	return StaticGatewayProfile.userRequirementWeight_Reliability;
    }
    @ResponseBody @RequestMapping("/getuserRequirementWeight_Cost")
    public int getuserRequirementWeight_Cost () {
    	return StaticGatewayProfile.userRequirementWeight_Cost;
    }
    
   
    @RequestMapping("/setPluggedDevices")
    public void setPluggedDevices (@RequestBody RecommendedDevices devs) {
    	 StaticGatewayProfile.devices = devs;
    }
    @RequestMapping("/setinstalledApps")
    public void setinstalledApps (@RequestBody RecommendedApps apps) {
    	 StaticGatewayProfile.apps = apps;
    }
    @RequestMapping("/setinstalledWfs")
    public void setinstalledWfs (@RequestBody RecommendedWFs wfs) {
    	 StaticGatewayProfile.wfs = wfs;
    }
    @RequestMapping("/setResources")
    public void setResources (@RequestBody String res) {
    	 StaticGatewayProfile.resources = res;
    }
    @RequestMapping("/setLocation")
    public void setLocation (@RequestBody String loc) {
    	StaticGatewayProfile.location = loc;
    }
    @RequestMapping("/setPricingPreferences")
    public void setPricingPreferences (@RequestBody String pref) {
    	StaticGatewayProfile.pricingPreferences = pref;
    }
    
    
    @RequestMapping("/setsupportedDataEncodingProtocolsOfGateway")
    public void setsupportedDataEncodingProtocolsOfGateway (@RequestBody int[] prots ) {
    	 StaticGatewayProfile.supportedDataEncodingProtocolsOfGateway = prots;
    }
    @RequestMapping("/setsupportedConnectivityProtocolsOfGateway")
    public void setsupportedConnectivityProtocolsOfGateway (@RequestBody int[] prots ) {
    	 StaticGatewayProfile.supportedConnectivityProtocolsOfGateway = prots;
    }
    @RequestMapping("/setuserRequirementWeight_Performance")
    public void setuserRequirementWeight_Performance (@RequestBody int w ) {
    	 StaticGatewayProfile.userRequirementWeight_Performance = w;
    }
    @RequestMapping("/setuserRequirementWeight_Reliability")
    public void setuserRequirementWeight_Reliability (@RequestBody int w ) {
    	 StaticGatewayProfile.userRequirementWeight_Reliability = w;
    }
    @RequestMapping("/setuserRequirementWeight_Cost")
    public void setuserRequirementWeight_Cost (@RequestBody int w ) {
    	 StaticGatewayProfile.userRequirementWeight_Cost = w;
    }
    
    
}



