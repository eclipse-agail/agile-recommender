package at.tugraz.ist.configurator.CSHL;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class XMLtestDataParser {
   public static void main(String[] args){

      try {	
         File inputFile = new File("testData/normalized-dsjc-125-1-4-ext.xml");
         DocumentBuilderFactory dbFactory 
            = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         
         //////////////////////////////////////////
         doc.getDocumentElement().normalize();
         System.out.println("Root element :" 
            + doc.getDocumentElement().getNodeName());
         //////////////////////////////////////////
         
         
         //////////////////////////////////////////
         NodeList domain = doc.getElementsByTagName("domains");
         Node n = domain.item(0);
         Element e = (Element) n;
         int numberofdomains = Integer.valueOf(e.getAttribute("nbDomains"));
         System.out.println("number of domains : " + numberofdomains);
         //////////////////////////////////////////
			         
         //////////////////////////////////////////
         NodeList nList = doc.getElementsByTagName("domains");
         System.out.println("----------------------------");
         for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" 
               + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               System.out.println("nbValues : " 
                  + eElement.getAttribute("nbValues"));
               System.out.println("name : " 
                       + eElement.getAttribute("name"));
               System.out.println("domain value range:" 
                       + eElement
                       .getElementsByTagName("domain")
                       .item(0)
                       .getTextContent());
              
            }
         }
         //////////////////////////////////////////
         
			//////////////////////////////////////////
			NodeList vars = doc.getElementsByTagName("variables");
			n = vars.item(0);
			e = (Element) n;
			int numberofvariables = Integer.valueOf(e.getAttribute("nbVariables"));
			System.out.println("number of variables : " + numberofvariables);
			//////////////////////////////////////////	
         
         
         //////////////////////////////////////////
         nList = doc.getElementsByTagName("variable");
         System.out.println("----------------------------");
         for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" 
               + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               System.out.println("name : " 
                  + eElement.getAttribute("name"));
               System.out.println("domain : " 
                       + eElement.getAttribute("domain"));
              
            }
         }
         //////////////////////////////////////////
         
         
         
         
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}