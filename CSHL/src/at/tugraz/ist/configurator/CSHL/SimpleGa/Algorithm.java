package at.tugraz.ist.configurator.CSHL.SimpleGa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Algorithm {

    /* GA parameters */
    private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.015;
    private static final int tournamentSize = 5;
    private static final boolean elitism = true;
    
    public static int geneSize = 30;
    
    /* Public methods */
    
    // Evolve a population
    public static Population evolvePopulation(Population pop, int clusterIndex, int maxDomain) {
    	
    	geneSize = pop.getIndividual(0).size();
        Population newPopulation = new Population(pop.size(),geneSize ,false, clusterIndex);

        // Keep our best individual
        if (elitism) {
            newPopulation.saveIndividual(0, pop.getFittest());
        }

        // Crossover population
        int elitismOffset;
        if (elitism) {
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }
        
        //System.out.println("crossover");
        // Loop over the population size and create new individuals with
        // crossover
        for (int i = elitismOffset; i < pop.size(); i++) {
            Individual indiv1 = tournamentSelection(pop,clusterIndex);
            
            Individual indiv2 = tournamentSelection(pop,clusterIndex);
           
            Individual newIndiv = crossover(indiv1, indiv2, clusterIndex);
            newPopulation.saveIndividual(i, newIndiv);
        }
        //System.out.println("endofcrossover");
        
        //System.out.println("Mutate population");
        // Mutate population
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            mutate(newPopulation.getIndividual(i));
        }
        //System.out.println("endof Mutate");

        return newPopulation;
    }

    // Crossover individuals
    private static Individual crossover(Individual indiv1, Individual indiv2,int clusterIndex) {
        
    	Individual newSol = new Individual(indiv1.size(),clusterIndex);
    	//newSol.generateIndividual(indiv1.size(),clusterIndex);
        boolean[] usedValues = new boolean[indiv1.size()];
        int index =0;
        int next1=0;
        int next2=0;
        
        // Loop through genes
        while(index<indiv1.size()) {
        	
        	if(next1>indiv1.size()-1)
        		next1=0;
        	if(next2>indiv1.size()-1)
            	next2=0;
        
            // Crossover
            if (Math.random() <= uniformRate) {
            	if(usedValues[indiv1.getGene(next1)]){
            		next1++;
            	}
            	else{
	            	newSol.setGene(index, indiv1.getGene(next1));
	            	usedValues[indiv1.getGene(next1)] = true;
	            	index++;
            	}
            } else {
                if(usedValues[indiv2.getGene(next2)]){
            		next2++;
            	}
                else{
	            	newSol.setGene(index, indiv2.getGene(next2));
	            	usedValues[indiv2.getGene(next2)] = true;
	            	index++;
                }
            }
        }
        return newSol;
    }
    
    // Mutate an individual
    private static void mutate(Individual indiv) {
    	Random rand = new Random();
        // Loop through genes
        for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() <= mutationRate) {
                // change order among genes
                //int gene = rand.nextInt(indiv.size());
            	int nextIndex= i-1;
            	if(i==0)
            		nextIndex = i+1;
            	
            	int oldI = indiv.getGene(i);
            	
            	// nextIndex is copied to i
                indiv.setGene(i, indiv.getGene(nextIndex));
                
                // old i is placed to nextIndex
                indiv.setGene(nextIndex, oldI);
            }
        }
    }

    // Select individuals for crossover
    private static Individual tournamentSelection(Population pop,int clusterIndex) {
        // Create a tournament population
        Population tournament = new Population(tournamentSize, geneSize, true, clusterIndex);
        
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
        }
        // Get the fittest
        Individual fittest = tournament.getFittest();
        return fittest;
    }
}