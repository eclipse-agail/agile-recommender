# Recommender and Configurator Docker Service
Maintainer: Seda Polat Erdeniz, TUGraz

## This service provides an API to other AGILE Gateway services to access to the recommender and configurator service 



## API Document: http://54.186.105.109:8080/doc/

### Recommender Use Cases
#### Use Case 1 : App Recommendation
#### Use Case 2 : Workflow/Node Recommendation
#### Use Case 3 : Device Recommendation 
#### Use Case 4 : PaaS Provider (Cloud) Recommendation 
 
### Configurator Use Cases
#### Use Case 1 :
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
