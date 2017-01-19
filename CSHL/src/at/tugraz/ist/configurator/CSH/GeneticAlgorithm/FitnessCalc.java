package at.tugraz.ist.configurator.CSH.GeneticAlgorithm;

import at.tugraz.ist.configurator.CSH.LearningHeuristics;
import at.tugraz.ist.configurator.CSH.TestCSH;

public class FitnessCalc {

    public static long target;
    
    public static void setTarget(long time){
    	target = time;
    } 

    // Calculate inidividuals fittness by comparing it to our candidate solution
    static long getFitness(Individual individual) {
        
    	long fitness = LearningHeuristics.testIndividualOverCluster(individual, individual.clusterID);
  
        return fitness;
    }
    
    // Get optimum fitness
    public static long getMaxFitness() {
        //int maxFitness = 1;
        return target;
    }
}