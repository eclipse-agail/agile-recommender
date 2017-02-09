package at.tugraz.ist.configurator.ChocoExtensions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.tugraz.ist.configurator.ChocoExtensions.CSP;
import at.tugraz.ist.configurator.fileOperations.ReadFile;

public class FileToChocoModel {
	
    static int numberOfProducts = 0;
    static int numberOfUsers = 0;
	
	public static List<CSP> createUserModels(CSP orginalCSP, String filename){
		List<String> lines = new ArrayList<String>();
		List<CSP> users = new ArrayList<CSP>();
		// TODO Seda
		try {
			lines = ReadFile.readFile(filename);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(lines!=null){
			numberOfUsers = lines.size();
			users = new ArrayList<>(numberOfUsers);
			
			for(int i=0; i<numberOfUsers;i++){
				String [] values = lines.get(i).split(",");
				int numberOfVars = values.length-2;
				int[] variables = new int[numberOfVars];
				
				//  Product ID and User ID is skipped here 
				for(int j=0;j<numberOfVars;j++){
					variables[j] = Integer.valueOf(values[j]);
 				}
				// public CSP (boolean type, int[][]productTable, CSP originalCSP, int[] variables, int userID, int prodID)
				CSP userConstraints = new CSP(2,null,orginalCSP,variables,i,Integer.valueOf(values[numberOfVars]));
				users.add(userConstraints);
			}
		}
		
		return users ;
	}

	public static CSP createOriginalCSP(String fileDir){
		
		List<String> lines = new ArrayList<String>();
		CSP model = null;
		int[][] productTable = null;
		
		// TODO Seda
		try {
			lines = ReadFile.readFile(fileDir);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(lines!=null){
			numberOfProducts = lines.size();
			productTable = new int[numberOfProducts][];
			
			// number of products  = lines.size()
			for(int i=0; i<numberOfProducts;i++){
				String [] values = lines.get(i).split(",");
				productTable[i] = new int[values.length-1];
				// last one is ID
				for(int j=0;j<values.length-1;j++){
					productTable[i][j] = Integer.valueOf(values[j]);
 				}
				
			}
		}

		// public CSP (boolean isOriginalCSP, int[][]productTable, CSP originalCSP, int[] variables, int userID, int productID)
		model = new CSP(0,productTable, null, null, 0, -1);
		
		return model;
	}

	
}