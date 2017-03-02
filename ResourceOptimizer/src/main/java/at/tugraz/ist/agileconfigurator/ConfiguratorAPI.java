package at.tugraz.ist.agileconfigurator;


import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import at.tugraz.ist.agileconfigurator.models.Results;
import at.tugraz.ist.agileconfigurator.models.GatewayProfile;

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

   
}