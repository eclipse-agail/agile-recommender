# AGILE Configurator

## Resource Optimizer
This configurator is a agile service that runs on the Gateway.
It opens an API for other AGILE services to be able to get optimum settings for the gateway.

Resource Optimizer is developed as a Spring Boot Application. 
Later it will be wrapped as Docker microservice and will run on the AGILE gateway.

Provides 1 restful services: 
* http://localhost:8080/configurator/getResourceOptimization


Example Input and Output:

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

## Implemented Cases in the Resource Optimizer:

### Case-1: Optimize App Configurations
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