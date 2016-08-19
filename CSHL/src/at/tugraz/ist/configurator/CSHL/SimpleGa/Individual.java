package at.tugraz.ist.configurator.CSHL.SimpleGa;

import java.util.Random;

public class Individual {

    static int defaultGeneLength = 30;
    private int[] genes ;
    // Cache
    private int fitness = 0;
    private int runningTime = 10000000;
    
    public void setRunningTime(int execTime){
    	runningTime = execTime;
    }
    public int getRunningTime(){
    	return runningTime;
    }
    
 
    // Create a random individual
    public void generateIndividual(int geneSize) {
    	defaultGeneLength = geneSize;
    	genes = new int[defaultGeneLength];
    	Random rand = new Random();
    	
    	boolean [] isUsed = new boolean[geneSize];
    	
        for (int i = 0; i < size(); i++) {
            int gene = rand.nextInt(geneSize);
            while(isUsed[gene]==true){
            	gene = rand.nextInt(geneSize);
            }
            genes[i] = gene;
            isUsed[gene]=true;
        }
    }

    /* Getters and setters */
    // Use this if you want to create individuals with different gene lengths
    public static void setDefaultGeneLength(int length) {
        defaultGeneLength = length;
    }
    
    public int getGene(int index) {
        return genes[index];
    }
    
    public int[] getGenes() {
        return genes;
    }

    public void setGene(int index, int value) {
        genes[index] = value;
        fitness = 0;
    }

    /* Public methods */
    public int size() {
        return genes.length;
    }

    public int getFitness() {
        if (fitness == 0) {
            fitness = FitnessCalc.getFitness(this);
        }
        return fitness;
    }

    @Override
    public String toString() {
        String geneString = "";
        for (int i = 0; i < size(); i++) {
            geneString += getGene(i);
        }
        return geneString;
    }
}