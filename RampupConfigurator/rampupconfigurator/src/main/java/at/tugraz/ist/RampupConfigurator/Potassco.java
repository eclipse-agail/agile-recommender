package at.tugraz.ist.RampupConfigurator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;

public class Potassco {
	
	public static String callClingo(){
		
		String result="";
		try {
			
			//Process process = new ProcessBuilder("C:\\Windows\\System32\\Clingo.exe",filePath).start();
			//Process process = new ProcessBuilder("wine64","/usr/share/tomcat7/resources/clingo.exe",filePath).start();
			//Process p = Runtime.getRuntime().exec("wine64 /usr/share/tomcat7/resources/clingo.exe "+filePath);
			//BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
	
			//Process process = new ProcessBuilder("su testuser").start();
			
			//ProcessBuilder ps = new ProcessBuilder( "su - testuser -c  'wine64 /usr/share/tomcat7/resources/clingo.exe /usr/share/tomcat7/resources/SmartHome'");
			Process ps = Runtime.getRuntime().exec("./new.sh");
			
			//ProcessBuilder ps = new ProcessBuilder("su - testuser -c  \'wine64 /usr/share/tomcat7/resources/clingo.exe /usr/share/tomcat7/resources/SmartHome > /usr/share/tomcat7/resources/output.txt \'");
			
			//ProcessBuilder ps = new ProcessBuilder("./rampup.sh");
			
			//ps.redirectErrorStream(true);
			
			//Process pr = ps.start();  
			
			ps.waitFor();

			result = FileOperations.readFile("output.txt", -1);
			
//clingo version 4.5.4
//Reading from /usr/share/tomcat7/resources/SmartHome
//Solving...
//Answer: 1
//room(5) kitchen_domain(2) living_room_domain(3) room_domain(2) room_domain(3) kitchen(2) living_r
//SATISFIABLE

			String [] res = result.split("Solving...");
			String [] res2 = res[1].split("SATISFIABLE");
			result = res2[0].replace("\n", "<br>");
			result = result.replace(" ", "<br>");
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return result;
	}

}

