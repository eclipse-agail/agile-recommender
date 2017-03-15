package at.tugraz.ist.agile.rnc;


import java.util.Arrays;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import at.tugraz.ist.agile.configurator.Optimizer;
import at.tugraz.ist.agile.configurator.StaticServiceConfiguration;
import at.tugraz.ist.agile.configuratormodels.GatewayProfile;
import at.tugraz.ist.agile.recommendermodels.RecommendedApps;
import at.tugraz.ist.agile.recommendermodels.RecommendedClouds;
import at.tugraz.ist.agile.recommendermodels.RecommendedDevices;
import at.tugraz.ist.agile.recommendermodels.RecommendedWFs;

@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class RnCAPI {
	
	StaticServiceConfiguration conf = new StaticServiceConfiguration();
	
	public static void main(String[] args) throws Exception {
	    SpringApplication.run(RnCAPI.class, args);
	}

    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "Agile Recommener and Configurator Service is running.";
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

    @ResponseBody @RequestMapping("/getServiceConfiguration")
    public StaticServiceConfiguration getConfiguration () {
    	
		return conf;
    }

    @RequestMapping("/setServiceConfiguration")
    public void setConfiguration (@RequestBody StaticServiceConfiguration conf) {
    	
    	this.conf = conf;
    }

   
}



