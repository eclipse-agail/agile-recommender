# AGILE Configurator

## Resource Optimizer
This configurator is a agile service that runs on the Gateway.
It opens an API for other AGILE services to be able to get optimum settings for the gateway.

Resource Optimizer is developed as a Spring Boot Application. 
Later it will be wrapped as Docker microservice and will run on the AGILE gateway.


## CSHL
Cluster-Specific Heuristic Learning algorithm is implemented to increase the performance of the CSP Solver.
It will be modified for AGILE specific domains to be able to integrate with Resource Optimizer.

CSH is developed as a java-maven application. 
Later it will be integrated into Resource Optimizer as a library.