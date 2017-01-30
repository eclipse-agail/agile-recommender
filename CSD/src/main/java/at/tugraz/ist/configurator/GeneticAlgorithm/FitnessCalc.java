package at.tugraz.ist.configurator.GeneticAlgorithm;

import at.tugraz.ist.configurator.Learning.LearningHeuristics;

public class FitnessCalc {

    public static long target;
    
    public static void setTarget(long time){
    	target = time;
    } 

    // Calculate inidividuals fittness by comparing it to our candidate solution
    static float getFitness(Individual individual) {
        
    	float fitness = LearningHeuristics.evaluateFitnessValueOfCluster(individual, individual.clusterID);
  
        return fitness;
    }
    
    // Get optimum fitness
    public static long getMaxFitness() {
        //int maxFitness = 1;
        return target;
    }
}