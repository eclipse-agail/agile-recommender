package at.tugraz.ist.configurator.CSHL.SimpleGa;

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
    public static Population evolvePopulation(Population pop, int clusterIndex) {
    	System.out.println("evolvePopulation");
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
        
        System.out.println("crossover");
        // Loop over the population size and create new individuals with
        // crossover
        for (int i = elitismOffset; i < pop.size(); i++) {
            Individual indiv1 = tournamentSelection(pop,clusterIndex);
            Individual indiv2 = tournamentSelection(pop,clusterIndex);
            Individual newIndiv = crossover(indiv1, indiv2, clusterIndex);
            newPopulation.saveIndividual(i, newIndiv);
        }
        System.out.println("endofcrossover");
        
        System.out.println("Mutate population");
        // Mutate population
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            mutate(newPopulation.getIndividual(i));
        }
        System.out.println("endof Mutate");

        return newPopulation;
    }

    // Crossover individuals
    private static Individual crossover(Individual indiv1, Individual indiv2,int clusterIndex) {
        
    	Individual newSol = new Individual(indiv1.size(),clusterIndex);
    	//newSol.generateIndividual(indiv1.size(),clusterIndex);
        boolean [] isIncluded = new boolean[indiv1.size()];
        // Loop through genes
        for (int i = 0; i < indiv1.size(); i++) {
            // Crossover
            if (Math.random() <= uniformRate) {
            	int index = i;
            	newSol.setGene(i, indiv1.getGene(index));
            	while(isIncluded[indiv1.getGene(index)]){
            		newSol.setGene(i, indiv1.getGene(index));
            		index++;
            		if(index==indiv1.size())
            			index=0;
            	}
            	isIncluded[indiv1.getGene(index)] = true;
            } else {
                int index = i;
                newSol.setGene(i, indiv2.getGene(index));
            	while(isIncluded[indiv2.getGene(index)]){
            		newSol.setGene(i, indiv2.getGene(index));
            		index++;
            		if(index==indiv1.size())
            			index=0;
            	}
            	isIncluded[indiv2.getGene(index)] = true;
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
                int gene = rand.nextInt(indiv.size());
                indiv.setGene(0, indiv.getGene(i));
                indiv.setGene(i, gene);
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