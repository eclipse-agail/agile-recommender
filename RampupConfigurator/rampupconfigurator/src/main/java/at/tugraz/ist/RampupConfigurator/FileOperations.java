package at.tugraz.ist.RampupConfigurator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperations {
	
	public static String readFile(String filename, long line) {

		BufferedReader br = null;

		String linestr = "";
		String fullFile = "";
		
		String response = "";
		try {

			String sCurrentLine;
			File file = new File("resources/"+filename);
			String absolutePath = file.getAbsolutePath();
			br = new BufferedReader(new FileReader(absolutePath));

			int i =1;
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				fullFile += sCurrentLine +"\n";
				if(i==line){
					linestr = sCurrentLine;
					response = linestr;
					break;
				}
				i++;
			}
			if(line<0)
				response = fullFile;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return response;
	}
	
	public static String getPath(String filename){
		
		File file = new File("resources/"+filename);
		String absolutePath = file.getAbsolutePath();
		//String absolutePath = "/home/ec2-user/resources/"+filename;
		return absolutePath;
	}
	
	public static boolean writeFile(String filename,String content) {

		boolean response = false;
		try {
			File file = new File("resources/"+filename);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile(),false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			System.out.println("Done");
			response = true;
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		return response;
	}

}
