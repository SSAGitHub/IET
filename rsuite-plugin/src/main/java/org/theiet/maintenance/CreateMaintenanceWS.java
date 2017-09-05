package org.theiet.maintenance;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.theiet.maintenance.webservice.MaintenanceTemplateWS;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.reallysi.rsuite.api.RSuiteException;

public class CreateMaintenanceWS {

	private CreateMaintenanceWS(){
		
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParserConfigurationException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws RSuiteException, IOException, ParserConfigurationException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		String javaSrc = "src/main/java/";
		String packageName = "org.theiet.maintenance.webservice";
		String packageDir = javaSrc + packageName.replace('.', '/');
		

		String templateClassName = MaintenanceTemplateWS.class.getSimpleName();
		String templateClass = templateClassName + ".java";

		File templateClassFile = new File(packageDir, templateClass);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
		
		System.out.println("Please provide a WS name: ");
		String wsName = "";

		Scanner in = new Scanner(System.in);

		// Reads a single line from the console
		// and stores into name variable
		wsName = in.nextLine();
		in.close();

		if (StringUtils.isEmpty(wsName)) {
			throw new RuntimeException("WS name cannot be empty");
		}

		// Prints name and age to the console
		System.out.println("Creating new script :" + wsName);

		File newClassFile = createClassFile(wsName, packageDir);

		if (newClassFile.exists()) {
			throw new RSuiteException("WS already exist");
		}

		FileUtils.copyFile(templateClassFile, newClassFile);
		
		Element newWSDefinition = dBuilder.newDocument().createElement("remoteApiDefinition");
		newWSDefinition.setAttribute("id", "iet.maintenance." + wsName.toLowerCase());
		newWSDefinition.setAttribute("handler", packageName +  "."   + FilenameUtils.getBaseName(newClassFile.getName()));
		newWSDefinition.setAttribute("description", "Maintenance web service for " + wsName);
		  
		
		String newClass = FileUtils.readFileToString(newClassFile);
		newClass =  newClass.replaceAll("MaintenanceTemplateWS", FilenameUtils.getBaseName(newClassFile.getName()));
		FileUtils.writeStringToFile(newClassFile, newClass, "utf-8");
		


		System.out.println("New script has been generated:" + wsName);

		System.out.println("Code for rsuite-plugins");
		
		  printElement(newWSDefinition);
	        System.out.println();
	}

	private static void printElement(Element newWSDefinition)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();    
		  DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("XML 3.0 LS 3.0");
	        if (impl == null) {
	            System.out.println("No DOMImplementation found !");
	            System.exit(0);
	        }
		  
	        
		  LSSerializer serializer = impl.createLSSerializer();
		  
		  serializer.getDomConfig().setParameter("xml-declaration", false);
		  
	        LSOutput output = impl.createLSOutput();
	        
	        output.setEncoding("UTF-8");
	        output.setByteStream(System.out);

	        serializer.write(newWSDefinition, output);
	}

	private static File createClassFile(String taskName, String packageDir) {
		return new File(packageDir + "/"
				+ StringUtils.capitalize(taskName) + ".java");
	}

}
