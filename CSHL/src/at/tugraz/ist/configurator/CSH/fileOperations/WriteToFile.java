package at.tugraz.ist.configurator.CSH.fileOperations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.ist.configurator.CSH.TestCSH;

public class WriteToFile {

	
	 
	 public static void writeToFile(){
		    List<String> lines = new ArrayList<String>();
		    String str = "";
		    int val = -1000000;
		    int size = -1;
		    
		    for (int i=0;i<TestCSH.numberOfmodels;i++){
		    	str = "";
		   		for(int j=0;j<TestCSH.numberOfvars;j++){
		   			size = TestCSH.modelsOfTheSameProblem.get(i).vars[j].getDomainSize();
		   			if(size==1)
		   				val = TestCSH.modelsOfTheSameProblem.get(i).vars[j].getValue();
		   			else
		   				val = -1000000;
		   			str += val+",";
		   		}
		   		str += i+"\n";
		   		lines.add(str);
		    }
			try {
				String basePath = new File("").getAbsolutePath();
				System.out.println(basePath);
				
				File file = new File("kmeans2\\seda\\inputs\\"+TestCSH.modelsName+".data");

				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file.getAbsolutePath());
				BufferedWriter bw = new BufferedWriter(fw);
				
				for (int i=0;i<lines.size();i++){
					 bw.append(lines.get(i));
				 }
				bw.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		
	 }

	 public static void writeCSV(List<String> line, boolean last){
		 try {
			 File file = new File("kmeans2\\seda\\outputs\\TestHeuristics.csv");
				// if file doesnt exists, then create it
			 if (!file.exists()) {
					file.createNewFile();
					
			 }
			 if(TestCSH.writer==null)
				 TestCSH.writer = new FileWriter(file.getPath());
			 
			 CSVUtils.writeLine(TestCSH.writer,line);

			 if(last){
				 TestCSH.writer.flush();
				 TestCSH.writer.close();
			 }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	 }
	 
}
