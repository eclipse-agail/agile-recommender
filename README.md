# Recommender and Configurator Docker Service

## This service provides 6 APIs in total:
localhost:8090/recommenderdockerservice/updateRepositories
localhost:8090/recommenderdockerservice/getAppRecommendation
localhost:8090/recommenderdockerservice/getWorkflowRecommendation
localhost:8090/recommenderdockerservice/getDeviceRecommendation
localhost:8090/recommenderdockerservice/getCloudRecommendation
localhost:8090/recommenderdockerservice/getResourceOptimization


Example Input and Output:
(Post Request to localhost:8090/recommenderdockerservice/getCloudRecommendation)

```
INPUT: 

{
	"userID":1,	
	"devices":"",
	"apps":"",
	"wfs":"" ,
	"resources":"",
	"location": "EU",
	"pricingPreferences": "free OR metered"
}
```

```
OUTPUT:

[
    {
        "title": "Heroku ",
        "link": "https://www.heroku.com/ ",
        "accesstype": "public , private ",
        "locations": "EU , NA ",
        "middlewares": "m ",
        "frameworks": "django , flask , grails , play , rails ",
        "runtimes": "clojue , go , groovyjava , node , php , python , ruby , scala ",
        "services": "postgresql , redis , TODO_ADD_MORE ",
        "pricing": "metered , monthly , free "
    },
    {
        "title": "OpenShift Online ",
        "link": "https://www.openshift.com/features/index.html ",
        "accesstype": "public , private ",
        "locations": "EU , NA ",
        "middlewares": "jboss , tomcat , zend server ",
        "frameworks": "django , drupal , flask , rails , switchyard , vert.x ",
        "runtimes": "java , node , perl , php , python , ruby ",
        "services": "jenkins , mongodb , mysql , openshift metrics , postgresql ",
        "pricing": "monthly , fixed , annually , free , hybrid "
    },
    {
        "title": "Bluemix ",
        "link": "https://console.ng.bluemix.net/ ",
        "accesstype": "public , private ",
        "locations": "EU , NA , OC ",
        "middlewares": "m ",
        "frameworks": "rails , sinatra ",
        "runtimes": "go , java , node , php , python , ruby ",
        "services": "advancedd mobile access ,  alchemyapi ,  api management ,  application security manager ,  appscan dynamis analyzer ,  appscan mobile analyzer ,  TODO_ADD_MORE ",
        "pricing": "metered , monthly "
    },
    {
        "title": "Microsoft Azure ",
        "link": "https://azure.microsoft.com/tr-tr/ ",
        "accesstype": "public ",
        "locations": "AS , EU , NA , OC , SA ",
        "middlewares": "tomcat ",
        "frameworks": "cakephp , django ",
        "runtimes": "dotnet , java , node , php , python , ruby ",
        "services": "TODO_ADD_MORE ",
        "pricing": "metered , monthly "
    },
    {
        "title": "Atos Cloud Foundry ",
        "link": "https://canopy-cloud.com/application-platforms/atos-cloud-foundry ",
        "accesstype": "public , private ",
        "locations": "AS , EU , NA , OC , SA ",
        "middlewares": "jboss , tomcat , tomee ",
        "frameworks": "django , grails , hhvm , play , rack , rails , sinatra , spring ",
        "runtimes": "clojure , dotnet , go , groovy , java , node , php , python , ruby , scala , swift ",
        "services": "neo4j , abacus , cassandra , couchdb , dingo-postgresql , elasticsearch , kafka , memcached , mongodb , mysql , postgresql , rabbitmq , redis , riakcs ,  TODO_ADD_MORE ",
        "pricing": "metered , monthly , fixed "
    }
]
```

Example Input and Output of Configurator API:

```
INPUT: 
{
 	"supportedDataEncodingProtocolsOfGateway":[1,3],
  	"supportedConnectivityProtocolsOfGateway":[0,1,2,3],
    "userRequirementWeight_Performance": 1,
    "userRequirementWeight_Reliability": 1,
    "userRequirementWeight_Cost": -1,
  	"installedApps":[ {
                        "name": "App-1",	
                        "url": null,
                        "supportedDataEncodingProtocolsOfApp":[0,2],
                        "supportedConnectivitiyProtocolsOfApp":[2,3],
                        "inUse_DataEncodingProtocol": null,
                   		"inUse_ConnectivitiyProtocol": null},
                     {
                        "name": "App-2",
                       	"url": null,
                        "supportedDataEncodingProtocolsOfApp":[3,2],
                        "supportedConnectivitiyProtocolsOfApp":[1,3],
                        "inUse_DataEncodingProtocol": null,
                   		"inUse_ConnectivitiyProtocol": null}
                    ],
   "errorMessage": null
}
```

```
OUTPUT:

{
    "supportedDataEncodingProtocolsOfGateway": [
        1,
        3
    ],
    "supportedConnectivityProtocolsOfGateway": [
        0,
        1,
        2,
        3
    ],
    "userRequirementWeight_Performance": 1,
    "userRequirementWeight_Reliability": 1,
    "userRequirementWeight_Cost": -1,
    "installedApps": [
        {
            "name": "App-1",
            "url": null,
            "inUse_DataEncodingProtocol": -1,
            "inUse_ConnectivitiyProtocol": -1,
            "supportedDataEncodingProtocolsOfApp": [
                0,
                2
            ],
            "supportedConnectivitiyProtocolsOfApp": [
                2,
                3
            ]
        },
        {
            "name": "App-2",
            "url": null,
            "inUse_DataEncodingProtocol": 3,
            "inUse_ConnectivitiyProtocol": 3,
            "supportedDataEncodingProtocolsOfApp": [
                3,
                2
            ],
            "supportedConnectivitiyProtocolsOfApp": [
                1,
                3
            ]
        }
    ],
    "errorMessage": "Warning: App-1 could not be configured. It requires a Connectivity or Data Encoding Protocol which is not supported by the Gateway. "
}
```

## Implemented Configuration Scenario::

### Optimize App Configurations
- Goal: Update the configuration of each app on the gateway based on utility maximization for each
- Apps support different Data encoding and Connectivity protocols,
- Gateway support different Data encoding and Connectivity protocols,
- App should be configured to a protocol that is also supported by the gateway
- Each protocol has 3 dimensions : performance, reliability and cost,
- Cost should be minimized where other dimensions should be maximized in total,
- User weights are taken in inputs for each dimension (weight of cost should be given as negative value),
- App should be configured to a protocol that has maximum utility based on user weights and the performance/reliability/cost values of the protocols
- Configurator returns the configured protocols for each app,
- If protocols of app are not supported by the gateway, configured protocol of the app is set to -1 and in the error message it is given as a warning.

## CSHL
Cluster-Specific Heuristic Learning algorithm is implemented to increase the performance of the CSP Solver.
It will be modified for AGILE specific domains to be able to integrate with Resource Optimizer.

CSH is developed as a java-maven application. 
Later it will be integrated into Resource Optimizer as a library.

## CSD
Cluster-Specific Heuristic Learning algorithm is applied to Diagnosis algorithms to increase the precision of the Diagnosis.


CSD is developed as a java-maven application. It also uses Java-ML libraries which have to be used locally in the project. 
Later it will be integrated into Resource Optimizer as a library.