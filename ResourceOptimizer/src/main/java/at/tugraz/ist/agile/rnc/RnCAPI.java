package at.tugraz.ist.agile.rnc;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

	
	@ModelAttribute
	public void setResponseHeader(HttpServletResponse response) {
	    response.setHeader("Access-Control-Allow-Origin", "*");
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
     * @api {get} /getResourceOptimization GetResourceOptimization 
     * @apiName GetResourceOptimization
     * @apiVersion 1.0.0
     * @apiGroup 2-Configurator
     * @apiDescription Returns Resource Optimized Configuration.
     *
     * @apiSuccess {Number[]} supportedDataEncodingProtocolsOfGateway
     * @apiSuccess {Number[]} supportedConnectivityProtocolsOfGateway
     * @apiSuccess {Number} userRequirementWeight_Performance
     * @apiSuccess {Number} userRequirementWeight_Reliability
     * @apiSuccess {Number} userRequirementWeight_Cost
     * @apiSuccess {Object[]} installedApps	Optimized Configuration of installed Apps.
     * @apiSuccess {String} installedApps.name
     * @apiSuccess {String} installedApps.url
     * @apiSuccess {Number} installedApps.inUse_DataEncodingProtocol 
     * @apiSuccess {Number} installedApps.inUse_ConnectivitiyProtocol 
     * @apiSuccess {String} errormessage
     * 
     * 
     *
     */
    @ResponseBody @RequestMapping("/getResourceOptimization")
    public at.tugraz.ist.agile.configuratormodels.GatewayProfile getResourceOptimization() {
    	
    	Optimizer optimizer = new Optimizer(configurationProfile);
    	return optimizer.getconfiguredProfile();
      
    }
    
    
    /**
     * @api {get} /getAppRecommendation GetAppRecommendation 
     * @apiName GetAppRecommendation
     * @apiVersion 1.0.0
     * @apiGroup 1-Recommender
     * @apiDescription Returns Recommended Apps.
     *
     *
     * @apiSuccess {Object[]} ListOfApps 	List of Recommended Apps 
     * @apiSuccess {String} ListOfApps.title
     * @apiSuccess {String} ListOfApps.href
     * @apiSuccess {Number} ListOfApps.stars
     * @apiSuccess {Number} ListOfApps.downloads
     * 
     * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 200 OK
     * 
     * {
     *     "appList": [
     *         {
     *             "title": "App",
     *             "href": "hebele",
     *             "stars": 0,
     *             "downloads": 0
     *         },
     *         {
     *             "title": "mgarciap/iot-sensor-simulator/",
     *             "href": "https://hub.docker.com/r/mgarciap/iot-sensor-simulator/",
     *             "stars": 0,
     *             "downloads": 0
     *         },
     *         {
     *             "title": "cuongdd1/sensor-remote-dashboard/",
     *             "href": "https://hub.docker.com/r/cuongdd1/sensor-remote-dashboard/",
     *             "stars": 0,
     *             "downloads": 0
     *         },
     *         {
     *             "title": "sensorlab6/videk/",
     *             "href": "https://hub.docker.com/r/sensorlab6/videk/",
     *             "stars": 0,
     *             "downloads": 0
     *         },
     *        {
     *             "title": "elliotsabitov/sensorsservernode/",
     *             "href": "https://hub.docker.com/r/elliotsabitov/sensorsservernode/",
     *             "stars": 0,
     *             "downloads": 0
     *         },
     *         {
     *             "title": "elliotsabitov/sensorsclientreact/",
     *             "href": "https://hub.docker.com/r/elliotsabitov/sensorsclientreact/",
     *             "stars": 0,
      *            "downloads": 0
     *         }
     *     ]
     * }
     *
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
    	
    	//return Response.ok(recommendedApps).header("Access-Control-Allow-Origin", "*").build();
    }
    
    /**
     * @api {get} /getWorkflowRecommendation GetWorkflowRecommendation 
     * @apiName GetWorkflowRecommendation
     * @apiVersion 1.0.0
     * @apiGroup 1-Recommender
     * @apiDescription Returns Recommended Workflows.
     *
     *
     * @apiSuccess {Object[]} ListOfWFs List of Recommended Workflows.
     * @apiSuccess {String} ListOfWFs.type
     * @apiSuccess {String} ListOfWFs.datatag
     * @apiSuccess {String} ListOfWFs.dataowner
     * @apiSuccess {String} ListOfWFs.href
     * 
     * 
     * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 200 OK
     * 
     * {
     *     "wfList": [
     *         {
     *             "type": "Workflow",
     *             "datatag": "Workflow",
     *             "dataowner": "Workflow",
     *             "href": "wflink1"
     *         },
     *         {
     *             "type": "Workflow",
     *             "datatag": "Workflow",
     *             "dataowner": "Workflow",
     *             "href": "wflink2"
     *         },
     *         {
     *             "type": "flow",
     *             "datatag": "Hive,Thermostat,Hot-water,boost,british,gas",
     *             "dataowner": "twonk",
     *             "href": "/flow/62e04c333b192f6119d9d5b72f675d5f"
     *         },
     *         {
     *             "type": "node",
     *             "datatag": "node-red,sensor,temperature,DS18B20",
     *             "dataowner": "Brendan Murray",
     *             "href": "/node/node-red-contrib-ds18b20-sensor"
     *         },
     *         {
     *             "type": "node",
     *             "datatag": "node-red,sensor,temperature,DS18B20",
     *             "dataowner": "Brendan Murray",
     *             "href": "/node/node-red-ds18b20-sensor"
     *         },
     *         {
     *             "type": "node",
     *             "datatag": "node-red,netatmo,camera,tags,sensors,weather,iot,ibm",
     *             "dataowner": "Guido Bellomo",
     *             "href": "/node/node-red-contrib-netatmo-dashboard"
     *         },
     *         {
     *             "type": "node",
     *             "datatag": "node-red,netatmo,camera,tags,sensors,weather,iot,ibm",
     *             "dataowner": "Sam Adams",
     *             "href": "/node/node-red-contrib-netatmo"
     *         },
     *         {
     *             "type": "flow",
     *             "datatag": "arduino,johnny-five,monitoring,research,R&amp;D,data,logging,temperature,thermistor,sensor,johnny5",
     *             "dataowner": "BradleyBock",
     *             "href": "/flow/4bceef5251eaa1f17a8f1fbeb5b6c34d"
     *         },
     *         {
     *             "type": "flow",
     *             "datatag": "Plant ,Monitoring ,emoncms,Arduino ,Rasberry ,sensors ,sensor,humidity ,temperature,soil ,moisture,light,water ,Graphic",
     *             "dataowner": "caasisaac",
     *             "href": "/flow/25bbcb5246633806463d"
     *         },
     *         {
     *             "type": "flow",
     *             "datatag": "test,abb,temperature",
     *             "dataowner": "pankeshlinux",
     *             "href": "/flow/976d93d4a017c3f46b6bac7433b3c143"
     *         },
     *         {
     *             "type": "flow",
     *             "datatag": "modbus,serial,temperature,humidity",
     *             "dataowner": "nygma2004",
     *             "href": "/flow/670ba131f74bd127d884aab2a580bffb"
     *         },
     *         {
     *             "type": "flow",
     *             "datatag": "rmap,relay,temperature",
     *             "dataowner": "pat1",
     *             "href": "/flow/6f03c1f31b1b494a79bd36b30ca86278"
     *         }
     *     ]
     * }
     *
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
     * @api {get} /getCloudRecommendation GetCloudRecommendation 
     * @apiName GetCloudRecommendation
     * @apiVersion 1.0.0
     * @apiGroup 1-Recommender
     * @apiDescription Returns Recommended Cloud Servers.
     *
     *
     * @apiSuccess {Object[]} ListOfClouds List of Recommended Clouds.
     * @apiSuccess {String} ListOfClouds.title
     * @apiSuccess {String} ListOfClouds.link
     * @apiSuccess {String} ListOfClouds.accesstype
     * @apiSuccess {String} ListOfClouds.locations
     * @apiSuccess {String} ListOfClouds.middlewares
     * @apiSuccess {String} ListOfClouds.runtimes
     * @apiSuccess {String} ListOfClouds.services
     * @apiSuccess {String} ListOfClouds.pricing
     * 
     * 
     * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 200 OK
     * 
     * {
     *     "cloudList": [
     *         {
     *             "title": "Cloud",
     *             "link": "hebele4",
     *             "accesstype": "",
     *             "locations": "",
     *             "middlewares": "",
     *             "frameworks": "",
     *             "runtimes": "",
     *             "services": "",
     *             "pricing": ""
     *         },
     *         {
     *             "title": "Heroku ",
     *             "link": "https://www.heroku.com/ ",
     *             "accesstype": "public , private ",
     *             "locations": "EU , NA ",
     *             "middlewares": "m ",
     *             "frameworks": "django , flask , grails , play , rails ",
     *             "runtimes": "clojue , go , groovyjava , node , php , python , ruby , scala ",
     *             "services": "postgresql , redis , TODO_ADD_MORE ",
     *             "pricing": "metered , monthly , free "
     *         },
     *         {
     *             "title": "OpenShift Online ",
     *             "link": "https://www.openshift.com/features/index.html ",
     *             "accesstype": "public , private ",
     *             "locations": "EU , NA ",
     *             "middlewares": "jboss , tomcat , zend server ",
     *             "frameworks": "django , drupal , flask , rails , switchyard , vert.x ",
     *             "runtimes": "java , node , perl , php , python , ruby ",
     *             "services": "jenkins , mongodb , mysql , openshift metrics , postgresql ",
     *             "pricing": "monthly , fixed , annually , free , hybrid "
     *         },
     *         {
     *             "title": "Bluemix ",
     *             "link": "https://console.ng.bluemix.net/ ",
     *             "accesstype": "public , private ",
     *             "locations": "EU , NA , OC ",
     *             "middlewares": "m ",
     *             "frameworks": "rails , sinatra ",
     *             "runtimes": "go , java , node , php , python , ruby ",
     *             "services": "advancedd mobile access ,  alchemyapi ,  api management ,  application security manager ,  appscan dynamis analyzer ,  appscan mobile analyzer ,  TODO_ADD_MORE ",
      *            "pricing": "metered , monthly "
     *         },
     *         {
     *             "title": "Microsoft Azure ",
     *             "link": "https://azure.microsoft.com/tr-tr/ ",
     *             "accesstype": "public ",
     *             "locations": "AS , EU , NA , OC , SA ",
     *             "middlewares": "tomcat ",
     *             "frameworks": "cakephp , django ",
     *             "runtimes": "dotnet , java , node , php , python , ruby ",
     *             "services": "TODO_ADD_MORE ",
     *             "pricing": "metered , monthly "
     *         },
     *         {
     *             "title": "Atos Cloud Foundry ",
     *             "link": "https://canopy-cloud.com/application-platforms/atos-cloud-foundry ",
     *             "accesstype": "public , private ",
     *             "locations": "AS , EU , NA , OC , SA ",
     *             "middlewares": "jboss , tomcat , tomee ",
     *             "frameworks": "django , grails , hhvm , play , rack , rails , sinatra , spring ",
     *             "runtimes": "clojure , dotnet , go , groovy , java , node , php , python , ruby , scala , swift ",
     *             "services": "neo4j , abacus , cassandra , couchdb , dingo-postgresql , elasticsearch , kafka , memcached , mongodb , mysql , postgresql , rabbitmq , redis , riakcs ,  TODO_ADD_MORE ",
     *             "pricing": "metered , monthly , fixed "
     *         }
     *     ]
     * }
     *
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
     * @api {get} /getDeviceRecommendation GetDeviceRecommendation 
     * @apiName GetDeviceRecommendation
     * @apiVersion 1.0.0
     * @apiGroup 1-Recommender
     * @apiDescription Returns Recommended Devices.
     *
     *
     * @apiSuccess {Object[]} ListOfDevices List of Recommended Devices.
     * @apiSuccess {String} ListOfDevices.title
     * @apiSuccess {String} ListOfDevices.link
     * 
     * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 200 OK
     * 
     * {
     *     "deviceList": [
     *         {
     *             "title": "Seeedstudio-Gas-Sensor-Socket",
     *             "href": "https://www.amazon.com/Seeedstudio-Gas-Sensor-Socket/dp/B01C5RTCF4"
     *         },
     *         {
     *             "title": "Seeedstudio-Grove-Gas-Sensor-MQ3",
     *             "href": "https://www.amazon.com/Seeedstudio-Grove-Gas-Sensor-MQ3/dp/B01C5RNWW8"
     *         },
     *         {
     *             "title": "Wavesahre-MQ-7-Semiconductor-Sensor-Gas",
     *             "href": "https://www.amazon.com/Wavesahre-MQ-7-Semiconductor-Sensor-Gas/dp/B00NJNYWDG"
     *         },
     *         {
     *            "title": "Wavesahre-MQ-2-Gas-Sensor-Detection",
     *             "href": "https://www.amazon.com/Wavesahre-MQ-2-Gas-Sensor-Detection/dp/B00NJOIB50"
     *         },
      *        {
      *            "title": "Waveshare-MQ-3-Gas-Sensor-Detection",
     *             "href": "https://www.amazon.com/Waveshare-MQ-3-Gas-Sensor-Detection/dp/B00NL3KEYK"
     *         },
     *         {
     *             "title": "MAUSAN-Temperature-Humidity-Sensor-Arduino",
     *             "href": "https://www.amazon.com/MAUSAN-Temperature-Humidity-Sensor-Arduino/dp/B06XT4WWKW"
     *         },
     *         {
     *             "title": "LM35-Temperature-Sensor-Component-pack",
     *             "href": "https://www.amazon.com/LM35-Temperature-Sensor-Component-pack/dp/B01ISMVA1E"
     *         },
     *         {
     *             "title": "WaveShare-Waveshare-DHT22-Temperature-Humidity-Sensor",
     *             "href": "https://www.amazon.com/WaveShare-Waveshare-DHT22-Temperature-Humidity-Sensor/dp/B01C1CTW2G"
     *         },
     *         {
     *             "title": "Venel-Has-Wavelength-760nm-1100nm-Light-Used-Fire-Fighting",
     *             "href": "https://www.amazon.com/Venel-Has-Wavelength-760nm-1100nm-Light-Used-Fire-Fighting/dp/B01HI410AY"
     *         }
     *     ]
     * }
     *
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
     * @api {get} /updateRepositories UpdateRepositories 
     * @apiName UpdateRepositories
     * @apiVersion 1.0.0
     * @apiGroup 1-Recommender
     * @apiDescription Updates the local repositories by getting new items from Amazon, Docker.hub and Node.Red websites.
     *
     *
     * @apiSuccess {Number} success is 0, error is -1
     *
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
    
   
//    @ResponseBody @RequestMapping("/updateGatewayProfile")
//    public int updateGatewayProfile () {
//    	
//    	profile = new at.tugraz.ist.agile.recommendermodels.GatewayProfile(); 
//    	configurationProfile = new at.tugraz.ist.agile.configuratormodels.GatewayProfile();
//    	
//    	Properties prop = new Properties();
//		InputStream input = null;
//	
//		// LOAD GATEWAY PROFILE FOR RECoMMENDER
//		try {
//	
//			String filename = "gateway_recommender.properties";
//			input = at.tugraz.ist.agile.recommendermodels.GatewayProfile.class.getClassLoader().getResourceAsStream(filename);
//			if(input==null){
//		            System.out.println("Sorry, unable to find " + filename);
//			    return -1;
//			}
//	
//			//load a properties file from class path, inside static method
//			prop.load(input);
//	
//	        //get the property value and print it out
//			String[] installedApps = prop.getProperty("installedApps").split(";");
//			String[] pluggedDevs = prop.getProperty("pluggedDevs").split(";");
//			String[] installedWFs = prop.getProperty("installedWFs").split(";");
//			String location = prop.getProperty("location");
//			String pricingPreferences = prop.getProperty("pricingPreferences");
//			
//			for(int i=0; i<installedApps.length;i++){
//				String[] elements= installedApps[i].split(",");
//				profile.apps.getAppList().add(new at.tugraz.ist.agile.recommendermodels.App(elements[0],elements[1],Integer.valueOf(elements[2]),Integer.valueOf(elements[3])));
//			}
//			for(int i=0; i<pluggedDevs.length;i++){
//				String[] elements= pluggedDevs[i].split(",");
//				profile.devices.getDeviceList().add(new at.tugraz.ist.agile.recommendermodels.Device(elements[0],elements[1]));
//			}
//			for(int i=0; i<installedWFs.length;i++){
//				String[] elements= installedWFs[i].split(",");
//				profile.wfs.getWfList().add(new at.tugraz.ist.agile.recommendermodels.Workflow(elements[0],elements[1],elements[2],elements[3]));
//			}
//			
//			profile.location = location;
//			profile.pricingPreferences = pricingPreferences;
//			
//		} catch (IOException ex) {
//			ex.printStackTrace();
//	    } finally{
//	    	if(input!=null){
//	    		try {
//				input.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//	    	}    
//	   }
//		
//		// LOAD GATEWAY PROFILE FOR CONFIGURATOR
//				try {
//			
//					String filename = "gateway_configurator.properties";
//					input = at.tugraz.ist.agile.recommendermodels.GatewayProfile.class.getClassLoader().getResourceAsStream(filename);
//					if(input==null){
//				            System.out.println("Sorry, unable to find " + filename);
//					    return -1;
//					}
//			
//					//load a properties file from class path, inside static method
//					prop.load(input);
//			
//			        //get the property value and print it out
//					String[] supportedDataEncodingProtocolsOfGateway = prop.getProperty("supportedDataEncodingProtocolsOfGateway").split(",");
//					String[] supportedConnectivityProtocolsOfGateway = prop.getProperty("supportedConnectivityProtocolsOfGateway").split(",");
//					String userRequirementWeight_Performance = prop.getProperty("userRequirementWeight_Performance");
//					String userRequirementWeight_Reliability = prop.getProperty("userRequirementWeight_Reliability");
//					String userRequirementWeight_Cost = prop.getProperty("userRequirementWeight_Cost");
//					String[] installedApps = prop.getProperty("installedApps").split(";");
//					
//					int [] supportedConnectivityProtocolsOfGateway_int = new int[supportedConnectivityProtocolsOfGateway.length];
//					for(int i=0; i<supportedConnectivityProtocolsOfGateway.length;i++){
//						supportedConnectivityProtocolsOfGateway_int[i]= Integer.valueOf(supportedDataEncodingProtocolsOfGateway[i]);
//					}
//					
//					int [] supportedDataEncodingProtocolsOfGateway_int=new int[supportedDataEncodingProtocolsOfGateway.length];
//					for(int i=0; i<supportedDataEncodingProtocolsOfGateway.length;i++){
//						supportedDataEncodingProtocolsOfGateway_int[i]= Integer.valueOf(supportedDataEncodingProtocolsOfGateway[i]);
//					}
//					at.tugraz.ist.agile.configuratormodels.App [] apps = new at.tugraz.ist.agile.configuratormodels.App [installedApps.length];
//					for(int i=0; i<installedApps.length;i++){
//						String [] elements = installedApps[i].split(",");
//						apps[i]= new at.tugraz.ist.agile.configuratormodels.App();
//						apps[i].setName(elements[0]);
//						apps[i].setUrl(elements[1]);
//						apps[i].setInUse_DataEncodingProtocol(Integer.valueOf(elements[2]));
//						apps[i].setInUse_ConnectivitiyProtocol(Integer.valueOf(elements[3]));
//						
//						String [] elements2 = elements[4].split(" ");
//						int [] elements2_int=new int[elements2.length];
//						for(int e=0; e<elements2.length;i++){
//							elements2_int[e]= Integer.valueOf(elements2[e]);
//						}
//						
//						String [] elements3 = elements[5].split(" ");
//						int [] elements3_int=new int[elements3.length];
//						for(int e=0; e<elements3.length;i++){
//							elements3_int[e]= Integer.valueOf(elements3[e]);
//						}
//						 
//						apps[i].setSupportedDataEncodingProtocolsOfApp(elements2_int);
//						apps[i].setSupportedConnectivitiyProtocolsOfApp(elements3_int);
//					}
//					
//					
//					configurationProfile.setSupportedConnectivityProtocolsOfGateway(supportedConnectivityProtocolsOfGateway_int);
//					configurationProfile.setSupportedDataEncodingProtocolsOfGateway(supportedDataEncodingProtocolsOfGateway_int);
//					configurationProfile.setInstalledApps(apps);
//					
//					configurationProfile.setUserRequirementWeight_Performance(Integer.valueOf(userRequirementWeight_Performance));
//					configurationProfile.setUserRequirementWeight_Reliability(Integer.valueOf(userRequirementWeight_Reliability));
//					configurationProfile.setUserRequirementWeight_Cost(Integer.valueOf(userRequirementWeight_Cost));
//					
//				} catch (IOException ex) {
//					ex.printStackTrace();
//			    } finally{
//			    	if(input!=null){
//			    		try {
//						input.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//			    	}    
//			   }		
//    	
//		
//    	
//       return 0;
//      
//    }

    /**
     * @api {get} /getServiceConfiguration GetServiceConfiguration 
     * @apiName GetServiceConfiguration
     * @apiVersion 1.0.0
     * @apiGroup 3-Settings
     * @apiDescription Returns the settings of the service.
     *
     * @apiSuccess {Object} staticServiceConfiguration       Current settings of the service.
     * @apiSuccess {Boolean} staticServiceConfiguration.recommenderServiceForDevelopmentUIActive 
     * @apiSuccess {Boolean} staticServiceConfiguration.recommenderServiceForDeviceManagementUIActive 
     * @apiSuccess {Boolean} staticServiceConfiguration.recommenderServiceForAppManagementUIActive 
     * @apiSuccess {Boolean} staticServiceConfiguration.allowRecommenderServerToUseGatewayProfile 
     * @apiSuccess {String} staticServiceConfiguration.recommenderServerIP 
     * 
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
    * "recommenderServiceForDevelopmentUIActive": false,
    * "recommenderServiceForDeviceManagementUIActive": true,
    * "recommenderServiceForAppManagementUIActive": true,
    * "allowRecommenderServerToUseGatewayProfile": true,
    * "recommenderServerIP": "http://ec2-54-201-143-18.us-west-2.compute.amazonaws.com:8080/Recommender/"
	* }
     *
     */
    @ResponseBody @RequestMapping("/getServiceConfiguration")
    public StaticServiceConfiguration getServiceConfiguration () {
    	return this.conf;
    }
    
    /**
     * @api {put} /setServiceConfiguration SetServiceConfiguration 
     * @apiName SetServiceConfiguration
     * @apiVersion 1.0.0
     * @apiGroup 3-Settings
     * @apiDescription Updates the settings of the service.
     *
     * @apiParam {Object} staticServiceConfiguration	Settings of the service.
     * @apiParam {Boolean} staticServiceConfiguration.recommenderServiceForDevelopmentUIActive 
     * @apiParam {Boolean} staticServiceConfiguration.recommenderServiceForDeviceManagementUIActive 
     * @apiParam {Boolean} staticServiceConfiguration.recommenderServiceForAppManagementUIActive 
     * @apiParam {Boolean} staticServiceConfiguration.allowRecommenderServerToUseGatewayProfile 
     * @apiParam {String} staticServiceConfiguration.recommenderServerIP 
     *
     * @apiSuccess (200) settings are updated successfully.
     *
     */
    @RequestMapping("/setServiceConfiguration")
    public void setServiceConfiguration (@RequestBody StaticServiceConfiguration conf2) {
    	this.conf= conf2;
    	//conf.updateProperties(conf2);
    }

    /**
     * @api {get} /getGatewayProfileForRecommender GetGatewayProfileForRecommender 
     * @apiName GetGatewayProfileForRecommender
     * @apiVersion 1.0.0
     * @apiGroup 4-For Testing Purposes
     * @apiDescription Returns the gateway profile used by Recommender.
     *
     *
     * @apiSuccess {Object[]} ListOfDevices
     * @apiSuccess {String} ListOfDevices.title
     * @apiSuccess {String} ListOfDevices.link
     * @apiSuccess {Object[]} ListOfApps
     * @apiSuccess {String} ListOfApps.title
     * @apiSuccess {String} ListOfApps.href
     * @apiSuccess {Number} ListOfApps.stars
     * @apiSuccess {Number} ListOfApps.downloads
     * @apiSuccess {Object[]} ListOfWFs
     * @apiSuccess {String} ListOfWFs.type
     * @apiSuccess {String} ListOfWFs.datatag
     * @apiSuccess {String} ListOfWFs.dataowner
     * @apiSuccess {String} ListOfWFs.href
     * @apiSuccess {Object[]} ListOfClouds
     * @apiSuccess {String} ListOfClouds.title
     * @apiSuccess {String} ListOfClouds.link
     * @apiSuccess {String} ListOfClouds.accesstype
     * @apiSuccess {String} ListOfClouds.locations
     * @apiSuccess {String} ListOfClouds.middlewares
     * @apiSuccess {String} ListOfClouds.runtimes
     * @apiSuccess {String} ListOfClouds.services
     * @apiSuccess {String} ListOfClouds.pricing
     * @apiSuccess {String} location
     * @apiSuccess {String} pricingPreferences
     * 
     * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 200 OK
	 * 
     * {
     *     "devices": {
     *        "deviceList": [
     *             {
     *                 "title": "camera",
     *                 "href": "link4"
     *             },
     *             {
     *                 "title": "gas sensor",
     *                 "href": "link5"
     *             },
     *             {
     *                 "title": "lcd",
     *                 "href": "link6"
     *             }
     *         ]
     *     },
     *     "apps": {
     *         "appList": [
     *             {
     *                 "title": "fire alarm",
     *                 "href": "link1",
     *                 "stars": 0,
     *                 "downloads": 0
     *             },
     *             {
     *                 "title": "temperature alarm",
     *                 "href": "link2",
     *                 "stars": 0,
     *                 "downloads": 0
     *             },
     *             {
     *                 "title": "gas alarm",
     *                 "href": "link3",
     *                 "stars": 0,
     *                 "downloads": 0
     *             }
     *         ]
     *     },
     *     "wfs": {
     *         "wfList": [
     *             {
     *                 "type": "node",
     *                "datatag": "datatag1",
     *                 "dataowner": "datawner1",
     *                 "href": "link7"
     *             },
     *             {
     *                 "type": "workflow",
     *                 "datatag": "datatag2",
     *                 "dataowner": "datawner2",
     *                 "href": "link8"
     *             },
     *             {
     *                 "type": "workflow",
     *                 "datatag": "datatag3",
     *                 "dataowner": "datawner3",
     *                 "href": "link9"
     *             }
     *         ]
     *     },
     *     "clouds": {
     *         "cloudList": [
      *            {
     *                 "title": "cloud1",
     *                 "link": "link10",
     *                 "accesstype": null,
     *                 "locations": null,
     *                 "middlewares": null,
     *                 "frameworks": null,
     *                 "runtimes": null,
     *                 "services": null,
     *                 "pricing": null
     *             },
     *             {
     *                 "title": "cloud2",
     *                 "link": "link11",
     *                 "accesstype": null,
     *                 "locations": null,
     *                 "middlewares": null,
     *                 "frameworks": null,
     *                 "runtimes": null,
     *                 "services": null,
     *                 "pricing": null
     *             }
     *         ]
     *    },
     *     "location": "EU",
     *     "pricingPreferences": "free , metered"
     * }
     * 
     *
     */
    @ResponseBody @RequestMapping("/getGatewayProfileForRecommender")
    public at.tugraz.ist.agile.recommendermodels.GatewayProfile getGatewayProfileForRecommender () {
    	
		return profile;
    }
    
    /**
     * @api {put} /setGatewayProfileForRecommender SetGatewayProfileForRecommender 
     * @apiName SetGatewayProfileForRecommender
     * @apiVersion 1.0.0
     * @apiGroup 4-For Testing Purposes
     * @apiDescription Sets the gateway profile used by Recommender.
     *
     * @apiParam {Object[]} ListOfDevices
     * @apiParam {String} ListOfDevices.title
     * @apiParam {String} ListOfDevices.link
     * @apiParam {Object[]} ListOfApps
     * @apiParam {String} ListOfApps.title
     * @apiParam {String} ListOfApps.href
     * @apiParam {Number} ListOfApps.stars
     * @apiParam {Number} ListOfApps.downloads
     * @apiParam {Object[]} ListOfWFs
     * @apiParam {String} ListOfWFs.type
     * @apiParam {String} ListOfWFs.datatag
     * @apiParam {String} ListOfWFs.dataowner
     * @apiParam {String} ListOfWFs.href
     * @apiParam {Object[]} ListOfClouds
     * @apiParam {String} ListOfClouds.title
     * @apiParam {String} ListOfClouds.link
     * @apiParam {String} ListOfClouds.accesstype
     * @apiParam {String} ListOfClouds.locations
     * @apiParam {String} ListOfClouds.middlewares
     * @apiParam {String} ListOfClouds.runtimes
     * @apiParam {String} ListOfClouds.services
     * @apiParam {String} ListOfClouds.pricing
     * @apiParam {String} location
     * @apiParam {String} pricingPreferences
     *
     */
    @ResponseBody @RequestMapping("/setGatewayProfileForRecommender")
    public void setGatewayProfileForRecommender (@RequestBody  at.tugraz.ist.agile.recommendermodels.GatewayProfile prof) {
		this.profile = prof;
    }
  
    
    /**
     * @api {get} /getGatewayProfileForConfigurator GetGatewayProfileForConfigurator 
     * @apiName GetGatewayProfileForConfigurator
     * @apiVersion 1.0.0
     * @apiGroup 4-For Testing Purposes
     * @apiDescription Returns the gateway profile used by Configurator.
     *
     * @apiSuccess {Number[]} supportedDataEncodingProtocolsOfGateway
     * @apiSuccess {Number[]} supportedConnectivityProtocolsOfGateway
     * @apiSuccess {Number} userRequirementWeight_Performance
     * @apiSuccess {Number} userRequirementWeight_Reliability
     * @apiSuccess {Number} userRequirementWeight_Cost
     * @apiSuccess {Object[]} installedApps	Optimized Configuration of installed Apps.
     * @apiSuccess {String} installedApps.name
     * @apiSuccess {String} installedApps.url
     * @apiSuccess {Number} installedApps.inUse_DataEncodingProtocol 
     * @apiSuccess {Number} installedApps.inUse_ConnectivitiyProtocol 
     * @apiSuccess {String} errormessage
     * 
     * 
     * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
     * {
     *     "supportedDataEncodingProtocolsOfGateway": [
     *         0,
     *         1,
     *         2
     *     ],
     *     "supportedConnectivityProtocolsOfGateway": [
     *         0,
     *         3,
     *         4
     *     ],
     *     "userRequirementWeight_Performance": 1,
     *     "userRequirementWeight_Reliability": 1,
     *     "userRequirementWeight_Cost": -1,
     *     "installedApps": [
     *         {
     *             "name": "fire alarm",
     *             "url": "link1",
     *             "inUse_DataEncodingProtocol": 0,
     *            "inUse_ConnectivitiyProtocol": 0,
     *             "supportedDataEncodingProtocolsOfApp": [
     *                 0,
     *                1
     *            ],
     *             "supportedConnectivitiyProtocolsOfApp": [
     *                 0,
     *                 2
     *             ]
     *         },
     *         {
     *             "name": "temperature alarm",
     *             "url": " link2",
     *             "inUse_DataEncodingProtocol": 0,
     *             "inUse_ConnectivitiyProtocol": 1,
     *             "supportedDataEncodingProtocolsOfApp": [
     *                0
     *             ],
     *             "supportedConnectivitiyProtocolsOfApp": [
     *                 1
     *             ]
     *         },
     *         {
     *             "name": "gas alarm",
     *             "url": "link3",
     *             "inUse_DataEncodingProtocol": 0,
     *             "inUse_ConnectivitiyProtocol": 0,
     *             "supportedDataEncodingProtocolsOfApp": [
     *                 0,
     *                 2
     *             ],
     *             "supportedConnectivitiyProtocolsOfApp": [
     *                 0
     *             ]
     *         }
     *     ],
     *     "errorMessage": null
     * }
     *
     */
    @ResponseBody @RequestMapping("/getGatewayProfileForConfigurator")
    public at.tugraz.ist.agile.configuratormodels.GatewayProfile getGatewayProfileForConfigurator () {
    	
		return configurationProfile;
    }
    
    /**
     * @api {put} /setGatewayProfileForConfigurator SetGatewayProfileForConfigurator 
     * @apiName setGatewayProfileForConfigurator
     * @apiVersion 1.0.0
     * @apiGroup 4-For Testing Purposes
     * @apiDescription Sets the gateway profile used by Configurator.
     *
     * @apiParam {Number[]} supportedDataEncodingProtocolsOfGateway
     * @apiParam {Number[]} supportedConnectivityProtocolsOfGateway
     * @apiParam {Number} userRequirementWeight_Performance
     * @apiParam {Number} userRequirementWeight_Reliability
     * @apiParam {Number} userRequirementWeight_Cost
     * @apiParam {Object[]} installedApps	Optimized Configuration of installed Apps.
     * @apiParam {String} installedApps.name
     * @apiParam {String} installedApps.url
     * @apiParam {Number} installedApps.inUse_DataEncodingProtocol 
     * @apiParam {Number} installedApps.inUse_ConnectivitiyProtocol 
     * @apiParam {String} errormessage
     *
     *
     */
    @ResponseBody @RequestMapping("/setGatewayProfileForConfigurator")
    public void setGatewayProfileForConfigurator (@RequestBody  at.tugraz.ist.agile.configuratormodels.GatewayProfile prof) {
		this.configurationProfile = prof;
    }
  
   
}



