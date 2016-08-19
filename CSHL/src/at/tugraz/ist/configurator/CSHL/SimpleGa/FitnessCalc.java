package at.tugraz.ist.configurator.CSHL.SimpleGa;

public class FitnessCalc {

    public static int target;
    
    public static void setTarget(int time){
    	target = time;
    } 

    /* Public methods */
    // Set a candidate solution as a byte array
//    public static void setSolution(byte[] newSolution) {
//        solution = newSolution;
//    }
//
//    // To make it easier we can use this method to set our candidate solution 
//    // with string of 0s and 1s
//    public static void setSolution(String newSolution) {
//        solution = new byte[newSolution.length()];
//        // Loop through each character of our string and save it in our byte 
//        // array
//        for (int i = 0; i < newSolution.length(); i++) {
//            String character = newSolution.substring(i, i + 1);
//            if (character.contains("0") || character.contains("1")) {
//                solution[i] = Byte.parseByte(character);
//            } else {
//                solution[i] = 0;
//            }
//        }
//    }

    // Calculate inidividuals fittness by comparing it to our candidate solution
    static int getFitness(Individual individual) {
        int fitness = 0;
        
        // TODO: apply CSP here and compare running time
        fitness = individual.getRunningTime();
        
        if (fitness<target)
        	fitness = 100;
        else{
        	fitness = ((fitness-target)/target)*100;
        }
        	
        return fitness;
    }
    
    // Get optimum fitness
    public static int getMaxFitness() {
        int maxFitness = 100;
        return maxFitness;
    }
}