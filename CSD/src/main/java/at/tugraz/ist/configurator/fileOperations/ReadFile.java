package at.tugraz.ist.configurator.fileOperations;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {

	public static List<String> readFile(String filename){
		
		List<String> lines = new ArrayList<String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				// System.out.println(sCurrentLine);
				lines.add(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}
}
