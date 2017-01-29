package at.tugraz.ist.agileconfigurator.resourceoptimizer;


import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import at.tugraz.ist.agileconfigurator.resourceoptimizer.models.Results;
import at.tugraz.ist.agileconfigurator.resourceoptimizer.models.GatewayProfile;

@RestController
@EnableAutoConfiguration
public class ConfiguratorAPI {
	
	
	public static void main(String[] args) throws Exception {
	    SpringApplication.run(ConfiguratorAPI.class, args);
	}

    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "Hello World!";
    }
    
    
    @ResponseBody @RequestMapping("/configurator/getResourceOptimization")
    public GatewayProfile getResourceOptimization(@RequestBody GatewayProfile profile) {
    	
    	Optimizer optimizer = new Optimizer(profile);
    	return optimizer.getconfiguredProfile();
      
    }

   
}