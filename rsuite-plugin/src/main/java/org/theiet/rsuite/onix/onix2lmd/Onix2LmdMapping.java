package org.theiet.rsuite.onix.onix2lmd;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.theiet.rsuite.utils.ExceptionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;


public class Onix2LmdMapping {

	private List<Onix2LmdMappingItem> mappingItems = new ArrayList<Onix2LmdMappingItem>();
	
	private Set<String> multivalueItems = new HashSet<String>();
	
	private static Onix2LmdMapping instance;
	
	public Onix2LmdMapping() throws RSuiteException {
		loadOnix2LmdMapping();
	}

	public static Onix2LmdMapping getInstance() throws RSuiteException{
		
		createInstance();
		
		return instance;
	}

	private static void createInstance()
			throws RSuiteException {
		try{
			
			if (instance == null){
				instance =   new Onix2LmdMapping();
			}
		}catch (Exception e){
			ExceptionUtils.createRsuiteException(e);
		}
	}
	
	public static void reload() throws RSuiteException{
		createInstance();
	}
	

	public void addMappingItem(Onix2LmdMappingItem item){
		mappingItems.add(item);
		if (item.isMultivalue()){
			multivalueItems.add(item.getLmdName());
		}
	}
	
	
	public List<Onix2LmdMappingItem> getMappingItems() {
		return mappingItems;
	}

	public Set<String> getMultivalueItems() {
		return multivalueItems;
	}
	
	private void loadOnix2LmdMapping()
			throws  RSuiteException {
		
		try{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document doc = builder.parse(getOnix2LmdConfiguration());
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath.compile("/*/entry");
	
		NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	
		
		
		for (int i =0; i < nl.getLength(); i++){
			Node node = nl.item(i);
			
			List<String> xpathsList = new ArrayList<String>();
			boolean multiValue = false;
			String lmdName = node.getAttributes().getNamedItem("lmdName").getNodeValue();
			
			
			processXpathElements(node, xpathsList);

			Node mutliValueAttr = node.getAttributes().getNamedItem("multivalue");
			
			if (mutliValueAttr != null){
				multiValue = Boolean.parseBoolean(mutliValueAttr.getNodeValue());
			}
			
			Onix2LmdMappingItem mappingItem = new Onix2LmdMappingItem(lmdName, multiValue, xpathsList);
			addMappingItem(mappingItem);			
		}
		
		}catch (Exception e){
			handleException(e);			
		}
		
	}

	public void processXpathElements(Node node, List<String> xpathsList) {
		NodeList xpaths = node.getChildNodes();
		for(int z =0; z < xpaths.getLength(); z++){
			Node xpathNode = xpaths.item(z);
			
			if (xpathNode instanceof Element && "xpath".equals(xpathNode.getNodeName())){
				Element xpathElement = (Element)xpathNode;
				xpathsList.add(xpathElement.getTextContent());
			}				
		}
	}

	public void handleException(Exception e)
			throws RSuiteException {
		throw new RSuiteException(0, "Unable to parse onix 2 lmd configuration", e);
	}

	protected InputStream getOnix2LmdConfiguration() {
		return Onix2LmdMapping.class.getResourceAsStream("/WebContent/conf/onix/onix2lmd_config.xml");
	}

}
