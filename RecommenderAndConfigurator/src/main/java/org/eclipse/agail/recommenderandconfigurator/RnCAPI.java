/*********************************************************************
 * Copyright (C) 2017 TUGraz.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 **********************************************************************/
package org.eclipse.agail.recommenderandconfigurator;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.agail.recommenderandconfigurator.configurator.Optimizer;
import org.eclipse.agail.recommenderandconfigurator.configurator.StaticServiceConfiguration;
import org.eclipse.agail.recommenderandconfigurator.devAPImodels.AgileDevice;
import org.eclipse.agail.recommenderandconfigurator.devAPImodels.AgileWorkflowModel;
import org.eclipse.agail.recommenderandconfigurator.devAPImodels.DataModel;
import org.eclipse.agail.recommenderandconfigurator.devAPImodels.TokenModel;
import org.eclipse.agail.recommenderandconfigurator.recommendermodels.Device;
import org.eclipse.agail.recommenderandconfigurator.recommendermodels.ListOfApps;
import org.eclipse.agail.recommenderandconfigurator.recommendermodels.ListOfClouds;
import org.eclipse.agail.recommenderandconfigurator.recommendermodels.ListOfDevices;
import org.eclipse.agail.recommenderandconfigurator.recommendermodels.ListOfWFs;
import org.eclipse.agail.recommenderandconfigurator.recommendermodels.Workflow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class RnCAPI {
	
	public static StaticServiceConfiguration conf = new StaticServiceConfiguration();
	//public static String recommenderIP = "http://ec2-54-201-143-18.us-west-2.compute.amazonaws.com:8080/Recommender/";
	static org.eclipse.agail.recommenderandconfigurator.recommendermodels.GatewayProfile recommenderProfile = new org.eclipse.agail.recommenderandconfigurator.recommendermodels.GatewayProfile(); 
	static org.eclipse.agail.recommenderandconfigurator.configuratormodels.GatewayProfile configurationProfile = new org.eclipse.agail.recommenderandconfigurator.configuratormodels.GatewayProfile();
	 
   
	public static void main(String[] args) throws Exception {
	    SpringApplication.run(RnCAPI.class, args);
	    conf = new StaticServiceConfiguration();
	    conf.loadProperties();
	    // recommenderProfile = LoadConfigurations.loadGatewayProfile_ForRecom_Properties();
	    configurationProfile= LoadConfigurations.loadGatewayProfile_ForConf_Properties();
	   
	}
	
//	@ResponseBody @RequestMapping("/testDataModel")
//    public ListOfWFs testDataModel (@RequestBody AgileDevice [] devices) {
//		int dn= devices.length;
//		try{
//			for(int i=0;i<devices.length;i++){
//		    	Device dev = new Device();
//		    	String devTitle="";
//		    	devTitle += devices[i].getName().trim();
//		    	for(int j=0;j<devices[i].getStreams().length;j++){
//		    		devTitle += " ";
//		    		devTitle += devices[i].getStreams()[j].getId().trim();
//		    	} 
//		    	dev.setTitle(devTitle);
//		    	recommenderProfile.devices = new ListOfDevices();
//		    	recommenderProfile.devices.addDevice(dev);
//		    	
//		    	System.out.println("Devices in the Profile: " + devTitle);
//		    	
//			}
//		}catch(Exception e){
//			System.out.println("Exception in devices api: " + e.getMessage());
//		}
//		
//		return getWorkflowRecommendation();
//    }
	
	public static void updateProfile(){
	
		System.out.println("This part adds devices and workflows into the profile");
		try{
			// DEVICES
			RestTemplate restTemplate = new RestTemplate();
		    final String uri = "http://172.18.0.1:8080/api/devices";
		    AgileDevice [] devices = restTemplate.getForObject(uri, AgileDevice[].class);
		    
		    System.out.println("devices are taken");
		    System.out.println("devices lenght: "+devices.length);
		    System.out.println("first device's name: "+devices[0].getName());
		    
	    	
		    for(int i=0;i<devices.length;i++){
		    	Device dev = new Device();
		    	String devTitle="";
		    	devTitle += devices[i].getName().trim();
		    	for(int j=0;j<devices[i].getStreams().length;j++){
		    		devTitle += " ";
		    		devTitle += devices[i].getStreams()[j].getId().trim();
		    	} 
		    	
		    	dev.setTitle(devTitle);
		    	recommenderProfile.devices = new ListOfDevices();
		    	recommenderProfile.devices.addDevice(dev);
		    	
		    	System.out.println("Devices in the Profile: " + devTitle);
	    }
		}catch(Exception e){
			System.out.println("Exception in devices api: " + e.getMessage());
		}
		
		try{
			// WORKFLOW
			RestTemplate restTemplate = new RestTemplate();
			
			// 1- GET TOKEN

			//  curl --data "client_id=node-red-admin&grant_type=password&scope=*&username=admin&password=password" http://172.18.0.1:1880/red/auth/token
			System.out.println("will call get token");
			
			String uri2 = "http://172.18.0.1:1880/red/auth/token";

			//  curl --data "client_id=node-red-admin&grant_type=password&scope=*&username=admin&password=password" http://agile-nodered:1880/red/auth/token
		    //String uri2 = "http://agile-nodered:1880/red/auth/token";

		    DataModel data = new DataModel();
		    TokenModel token = restTemplate.postForObject(uri2, data, TokenModel.class);
		    System.out.println("get token succeeded");
		    
		    // 2- SET TOKEN
		    // curl -H "Authorization: Bearer AEqPo4CKKr7j1CMUeqou7EuzjceeI6n4YPGcRd6XIQ3PJmBsXhyHjgX873z9J7ZoRjwU5YWPA7NBTdbGJNSWzt64K1z1nepPThS4EOFZZAZYBXX2aD4HvPjIJjlrr210" http://172.18.0.6:1880/red/settings

		    // System.out.println("will call settings");
		    
		    String uri3 = "http://172.18.0.1:1880/red/settings";

		    //String uri3 = "http://agile-nodered:1880/red/settings";

		    restTemplate = new RestTemplate();
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.set("Authorization", "Bearer "+token.getAccess_token() );
		    
		    HttpEntity<?> entity = new HttpEntity(headers);
		    // String result = restTemplate.postForObject(uri3, entity, String.class);
		    // System.out.println("settings succeeded");
		    
		    // 3- GET FLOWS

		    System.out.println("will call flows");
		    String uri4 = "http://172.18.0.1:1880/red/flows";

		    //String uri4 = "http://agile-nodered:1880/red/flows";

		    AgileWorkflowModel wfs = restTemplate.postForObject(uri4, entity, AgileWorkflowModel.class);
		    System.out.println("flows succeeded");
		    
		    
		    List<Workflow> wfList = new ArrayList<Workflow>();
	    	
		    for(int i=0;i<wfs.getV1().length;i++){
		    	Workflow wf = new Workflow();
		    	String title = wfs.getV1()[i].getType();
		    	if(checkTitle(title)){
			    	wf.setDatatag(title);
			    	wfList.add(wf);
		    	}
		    }
		    for(int i=0;i<wfs.getV2().getFlows().length;i++){
		    	Workflow wf = new Workflow();
		    	String title = wfs.getV2().getFlows()[i].getType();
		    	if(checkTitle(title)){
			    	wf.setDatatag(title);
			    	wfList.add(wf);
			    	System.out.println("Workflow in the Profile: " + title);
		    	}
		    }
		    
		    recommenderProfile.wfs = new ListOfWFs();
		    recommenderProfile.wfs.setWfList(wfList);
		    
		}catch(Exception e){
			System.out.println("Exception in nodered api: " + e.getMessage());
		}
		
		System.out.println("End of adding devices and workflows into the profile");
		
	}
	
	
	static boolean checkTitle(String title){
		boolean flag = false;
		
		// remove commen nodes in search
		if (title.contains("function") || 
				title.contains("switch") || 
				title.contains("debug") || 
				title.contains("template") || 
				title.contains("inject") ||
				title.contains("catch") ||
				title.contains("status") ||
				title.contains("delay") ||
				title.contains("trigger") ||
				title.contains("comment") ||
				title.contains("trigger") 
				){
			
			flag = true;
		}
		return flag;
	}

	
	@ModelAttribute
	public void setResponseHeader(HttpServletResponse response) {
	    response.addHeader("Access-Control-Allow-Origin", "*");
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
    public org.eclipse.agail.recommenderandconfigurator.configuratormodels.GatewayProfile getResourceOptimization() {
    	
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
    	
    	updateProfile();
    	
    	ListOfApps recommendedApps = new ListOfApps();
    	
    	if(conf.allowRecommenderServerToUseGatewayProfile==true && conf.recommenderServiceForAppManagementUIActive==true ){
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	
			final String uri = conf.recommenderServerIP+"getAppRecommendation";
			 
			recommendedApps = restTemplate.postForObject(uri, recommenderProfile, ListOfApps.class);
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
     * @apiSuccess {String} ListOfWFs.description
     * @apiSuccess {String} ListOfWFs.installCommand
     * @apiSuccess {String} ListOfWFs.javascriptCode
     * 
     * 
     * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 200 OK
     * 
     * {
     *     "wfList": [
     *         {
     *             "type": "node",
     *             "datatag": "node-red,ads-b,dump1090,ibm",
     *             "dataowner": "Neil Kolban",
     *             "href": "https://flows.nodered.org/node/node-red-contrib-ads-b",
     *             "description": "low-description\">A Node-Red ADS-B decoded node",
     *             "installCommand": "npm install node-red-contrib-ads-b",
     *             "javascriptCode": null
     *         },
     *         {
     *             "type": "node",
     *             "datatag": "pm,pm2.5,pm10,sensor,air,node-red",
     *             "dataowner": "Jannik Becher",
     *             "href": "https://flows.nodered.org/node/node-red-contrib-sds011-sensor",
     *             "description": "low-description\">This is a Node Red node to manage connection to the SDS011 sensor on a Raspberry Pi. It allows you to specify the variables that define the connections to the sensor. This node is added to the Raspberry Pi section.",
     *             "installCommand": "npm install node-red-contrib-sds011-sensor",
     *             "javascriptCode": null
     *         },
     *         {
     *             "type": "node",
     *             "datatag": "pcap,Packet Capture,ARP,node-red",
     *             "dataowner": "Nicholas Humfrey",
     *             "href": "https://flows.nodered.org/node/node-red-contrib-pcap",
     *             "description": "low-description\">Network packet capture for Node-RED",
     *             "installCommand": "npm install node-red-contrib-pcap",
     *             "javascriptCode": null
     *         },
     *         {
     *             "type": "node",
     *             "datatag": "node-red,lego,boost,move,hub,robotics,ble,bluetooth",
     *             "dataowner": "Sebastian Raff",
     *             "href": "https://flows.nodered.org/node/node-red-contrib-movehub",
     *             "description": "low-description\">Node-RED Nodes to control the Lego Boost Move Hub",
     *             "installCommand": "npm install node-red-contrib-movehub",
     *             "javascriptCode": null
     *         },
     *         {
     *             "type": "node",
     *             "datatag": "node-red,aws-sdk",
     *             "dataowner": "high-u",
     *             "href": "https://flows.nodered.org/node/node-red-contrib-aws-sdk-anything",
     *             "description": "low-description\">node-red aws sdk anything",
     *             "installCommand": "npm install node-red-contrib-aws-sdk-anything",
     *             "javascriptCode": null
     *         },
     *         {
     *             "type": "node",
     *             "datatag": "node-red,fritzbox,fritz,router,tr064,presence,avm,callmonitor,phonebook",
     *             "dataowner": "Jochen Scheib",
     *             "href": "https://flows.nodered.org/node/node-red-contrib-fritz",
     *             "description": "low-description\">This node gives access to the fritzbox tr064 api",
     *             "installCommand": "npm install node-red-contrib-fritz",
     *             "javascriptCode": null
     *         },
     *         {
     *             "type": "node",
     *             "datatag": "node-red,Yamaha,AVR,RX-777,RX-677,RX-477,RX-A740,HTR-4065,TSR-5790",
     *             "dataowner": "Sebastian Krauskopf",
     *             "href": "https://flows.nodered.org/node/node-red-contrib-avr-yamaha",
     *             "description": "low-description\">Node-RED node to connect to Yamaha Audio Video Receivers (e.g. Yamaha AVR RX-677)",
     *             "installCommand": "npm install node-red-contrib-avr-yamaha",
     *             "javascriptCode": null
     *         },
     *         {
     *             "type": "node",
     *             "datatag": "node-red,db2,ibm,ibmi,os400,ibm i,iseries",
     *             "dataowner": "Benoit Marolleau",
     *             "href": "https://flows.nodered.org/node/node-red-contrib-db2-for-i",
     *             "description": "low-description\">A Node-RED node to use a IBM DB2 for i database",
     *             "installCommand": "npm install node-red-contrib-db2-for-i",
     *             "javascriptCode": null
     *         },
     *         {
     *             "type": "flow",
     *             "datatag": "slack,bot,sdk",
     *             "dataowner": "joshendriks",
     *             "href": "https://flows.nodered.org/flow/51f68bd87a897caa5c3148457cc084c0",
     *             "description": "e-red-contrib-slackbotsdk</h1>\n          <p>This package implements a slackbot for <a href=\"https://slackapi.github.io/node-slack-sdk/\">node-red</a> using the official <a href=\"https://slackapi.github.io/node-slack-sdk/\">Slack Developer Kit for Node.js</a>",
     *             "installCommand": null,
     *             "javascriptCode": "[{\"id\":\"1d4829a6.6aa586\",\"type\":\"tab\",\"label\":\"Flow 1\",\"disabled\":false},{\"id\":\"baf3ec3e.50bc8\",\"type\":\"slackbot in\",\"z\":\"1d4829a6.6aa586\",\"name\":\"\",\"token\":\"a7e55e6.54507a\",\"x\":720,\"y\":120,\"wires\":[[\"bf5d7d2f.2fb\"]]},{\"id\":\"bf5d7d2f.2fb\",\"type\":\"change\",\"z\":\"1d4829a6.6aa586\",\"name\":\"Echo\",\"rules\":[{\"t\":\"set\",\"p\":\"payload\",\"pt\":\"msg\",\"to\":\"This is a test reply\",\"tot\":\"str\"}],\"action\":\"\",\"property\":\"\",\"from\":\"\",\"to\":\"\",\"reg\":false,\"x\":610,\"y\":380,\"wires\":[[\"baf3ec3e.50bc8\"]]},{\"id\":\"895ba886.b35758\",\"type\":\"inject\",\"z\":\"1d4829a6.6aa586\",\"name\":\"\",\"topic\":\"\",\"payload\":\"node-red slackbot is here\",\"payloadType\":\"str\",\"repeat\":\"\",\"crontab\":\"\",\"once\":false,\"x\":170,\"y\":139,\"wires\":[[\"fb623394.7c9ac\"]]},{\"id\":\"fb623394.7c9ac\",\"type\":\"change\",\"z\":\"1d4829a6.6aa586\",\"name\":\"\",\"rules\":[{\"t\":\"set\",\"p\":\"channel\",\"pt\":\"msg\",\"to\":\"channelID\",\"tot\":\"str\"}],\"action\":\"\",\"property\":\"\",\"from\":\"\",\"to\":\"\",\"reg\":false,\"x\":460,\"y\":100,\"wires\":[[\"baf3ec3e.50bc8\"]]},{\"id\":\"a7e55e6.54507a\",\"type\":\"slackbot-token\",\"z\":\"\",\"name\":\"Testbot\",\"token\":\"configure token here\"}]"
     *         },
     *         {
     *            "type": "flow",
     *             "datatag": "ibmi,iseries,db2",
     *             "dataowner": "bmarolleau",
     *             "href": "https://flows.nodered.org/flow/b255f32b8e07a5cc0c17e654fd338354",
     *             "description": "e-red-contrib-db2-for-i  basic flow</h1>\n          <h2 id=\"description\">Description</h2>\n<p>A basic flow for reading or writing to a DB2 for i db using Node.js v6 / Node-RED on IBM i",
     *             "installCommand": null,
     *             "javascriptCode": "[{\"id\":\"f336e0ee.99fdf8\",\"type\":\"inject\",\"z\":\"e5e26fd6.5797\",\"name\":\"SQL Query\",\"topic\":\"database\",\"payload\":\"SELECT * FROM ACMEDB.MYTABLE\",\"payloadType\":\"str\",\"repeat\":\"\",\"crontab\":\"\",\"once\":false,\"x\":144,\"y\":470,\"wires\":[[\"8c1463e2.299be8\"]]},{\"id\":\"994e12fc.b6d598\",\"type\":\"debug\",\"z\":\"e5e26fd6.5797\",\"name\":\"\",\"active\":true,\"console\":\"false\",\"complete\":\"false\",\"x\":846,\"y\":472,\"wires\":[]},{\"id\":\"8c1463e2.299be8\",\"type\":\"template\",\"z\":\"e5e26fd6.5797\",\"name\":\"ACMEDB-Connection\",\"field\":\"database\",\"fieldType\":\"msg\",\"format\":\"handlebars\",\"syntax\":\"mustache\",\"template\":\"*LOCAL\",\"output\":\"str\",\"x\":390,\"y\":472,\"wires\":[[\"6800df26.7aed4\"]]},{\"id\":\"6800df26.7aed4\",\"type\":\"DB2 for i\",\"z\":\"e5e26fd6.5797\",\"mydb\":\"4d3a95ef.d7e97c\",\"name\":\"\",\"x\":603,\"y\":470,\"wires\":[[\"994e12fc.b6d598\"]]},{\"id\":\"4d3a95ef.d7e97c\",\"type\":\"DB2 for i Config\",\"z\":\"\",\"db\":\"*LOCAL\"}]"
     *         }
     *     ]
     * }
     *
     */
    @ResponseBody @RequestMapping("/getWorkflowRecommendation")
    public ListOfWFs getWorkflowRecommendation () {
    	
    	System.out.println("getWorkflowRecommendation is started");
    	System.out.println("1- update profile is started");
    	updateProfile();
    	System.out.println("1- update profile is done");
    	ListOfWFs result = new ListOfWFs();
    	
    	if(conf.allowRecommenderServerToUseGatewayProfile==true && conf.recommenderServiceForDevelopmentUIActive==true ){
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	final String uri = conf.recommenderServerIP+"getWorkflowRecommendation";
			
	    	System.out.println("2- call the service");
	    	System.out.println("with recommenderProfile with dev size: "+recommenderProfile.getDevices().getDeviceList().size());
	    	if(recommenderProfile.getDevices().getDeviceList().size()>0 && recommenderProfile.getDevices().getDeviceList().get(0).getTitle()!=null)
	    	System.out.println("with recommenderProfile with dev title: "+recommenderProfile.getDevices().getDeviceList().get(0).getTitle());
	    	
	    	ObjectMapper mapper = new ObjectMapper();
	    	
	    	try{
	    		System.out.println(mapper.writeValueAsString(recommenderProfile));	
	    		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
	    		result = restTemplate.postForObject(uri, recommenderProfile, ListOfWFs.class);
	    		System.out.println("2- call the service done");
	    	}catch(Exception e){
	    		System.out.println("2- call the service failed: "+e.getMessage());
	    	}
	    	
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
    	
    	updateProfile();
    	ListOfClouds result = new ListOfClouds();
    	
    	if(conf.allowRecommenderServerToUseGatewayProfile==true && conf.recommenderServiceForDevelopmentUIActive==true ){
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	final String uri = conf.recommenderServerIP+"getCloudRecommendation";
			 
	    	result = restTemplate.postForObject(uri, recommenderProfile, ListOfClouds.class);
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
    	
    	updateProfile();
    	ListOfDevices result = new ListOfDevices();
    	
    	if(conf.allowRecommenderServerToUseGatewayProfile==true && conf.recommenderServiceForDeviceManagementUIActive==true ){
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	final String uri = conf.recommenderServerIP+"getDeviceRecommendation";
			 
	    	result = restTemplate.postForObject(uri, recommenderProfile, ListOfDevices.class);
    	}
		return result;
      
    }

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
    public org.eclipse.agail.recommenderandconfigurator.recommendermodels.GatewayProfile getGatewayProfileForRecommender () {
    	
		return recommenderProfile;
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
    public void setGatewayProfileForRecommender (@RequestBody  org.eclipse.agail.recommenderandconfigurator.recommendermodels.GatewayProfile prof) {
		this.recommenderProfile = prof;
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
    public org.eclipse.agail.recommenderandconfigurator.configuratormodels.GatewayProfile getGatewayProfileForConfigurator () {
    	
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
    public void setGatewayProfileForConfigurator (@RequestBody  org.eclipse.agail.recommenderandconfigurator.configuratormodels.GatewayProfile prof) {
		this.configurationProfile = prof;
    }
  
    
//  
//  /**
//   * @api {get} /updateRepositories UpdateRepositories 
//   * @apiName UpdateRepositories
//   * @apiVersion 1.0.0
//   * @apiGroup 1-Recommender
//   * @apiDescription Updates the local repositories by getting new items from Amazon, Docker.hub and Node.Red websites.
//   *
//   *
//   * @apiSuccess {Number} success is 0, error is -1
//   *
//   */
//  @ResponseBody @RequestMapping("/updateRepositories")
//  public int updateRepositories () {
//  	int res = -1;
//  	
//  	if(conf.allowRecommenderServerToUseGatewayProfile==true){
//	    	RestTemplate restTemplate = new RestTemplate();
//	    	final String uri = conf.recommenderServerIP+"updateRepositories";
//			 
//	    	String str = restTemplate.getForObject(uri, String.class);
//	    	res=0;
//  	}
//			
//		return res;
//    
//  }
  
 
//  @ResponseBody @RequestMapping("/updateGatewayProfile")
//  public int updateGatewayProfile () {
//  	
//  	profile = new at.tugraz.ist.agile.recommendermodels.GatewayProfile(); 
//  	configurationProfile = new at.tugraz.ist.agile.configuratormodels.GatewayProfile();
//  	
//  	Properties prop = new Properties();
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
//     return 0;
//    
//  }
   
}



