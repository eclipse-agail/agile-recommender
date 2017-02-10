package at.tugraz.ist.configurator.fileOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NormalizeFileForClustering {
	
	
	public static void normalize(String fileDir, String productsFileDir, int numberOfvars, int numberOfWeights){

		// 1- GET MAXIMUM VALUES IN PRODUCT FILE
		int [] maximumvalues = new int[numberOfvars];
		List<String> productValues = ReadFile.readFile(productsFileDir);
		int numberOfProducts= productValues.size();
		
		int[][] allValues = new int[numberOfvars][numberOfProducts];
		
		for(int p=0;p<productValues.size();p++){
			String line = productValues.get(p);
			String[]values = line.split(",");
			for(int v=0;v<numberOfvars;v++){
				allValues[v][p] = Integer.valueOf(values[v]);
			}
		}
		
		int lastElementIndex = numberOfProducts-1;
		for(int v=0;v<numberOfvars;v++){
			Arrays.sort(allValues[v]);
			maximumvalues[v] = allValues[v][lastElementIndex];
		}
		
			
		// 2- READ USER FILE 
		List<String> unNormalizedLines = ReadFile.readFile(fileDir);
		List<String> normalizedLines = new ArrayList<String>(unNormalizedLines.size());
		
		
		// 3- NORMALIZE
		for(int i=0;i<unNormalizedLines.size();i++){
			
			String [] valuesOfLine = unNormalizedLines.get(i).split(",");
			int[] weights = new int[numberOfWeights];
			
			for(int w=0;w<numberOfWeights;w++){
				weights[w] = Integer.valueOf(valuesOfLine[numberOfvars+w]);
			}
			
			String normalizedLine = "";
			for(int v=0;v<numberOfvars;v++){
				if(v!=0)
					normalizedLine += ",";
				int normalizedVal = (int) ( (( (float)(Integer.valueOf(valuesOfLine[v]))   / (float) maximumvalues[v]  )) *100 );
				//int normalizedVal = Integer.valueOf(valuesOfLine[v]);
				// is this value weighted
				for(int w=0;w<numberOfWeights;w++){
					if(v==weights[w]){
						normalizedVal = normalizedVal *(15-5*w); // 15,10,5 
						break;
					}
				}
				normalizedLine += normalizedVal;
				if(v==(numberOfvars-1))
					normalizedLine += ","+i;
			} 
			normalizedLines.add(normalizedLine);
		}
		
		// 4- WRITE TO NEW USER FILE
		String newFileDir = fileDir+"_normalized";
		WriteToFile.writeFile (newFileDir,normalizedLines,true,true,false);
	}

}
