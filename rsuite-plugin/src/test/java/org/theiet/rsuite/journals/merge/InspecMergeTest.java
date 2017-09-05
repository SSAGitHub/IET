package org.theiet.rsuite.journals.merge;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.junit.Assert;
import org.junit.Test;
import org.theiet.rsuite.journals.transforms.InspecClassificationMerge;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class InspecMergeTest {

	@Test
	public void testInspecMerge() throws Exception{
		File dataFolder = new File("test/data/");
		
		final String dataFolderPath = dataFolder.getAbsolutePath();
		
		File articleFile = new File(dataFolder, "test_article.xml");
		File inspecFile = new File(dataFolder, "test_article_inspec.xml");

		DocumentBuilder dBuilder = setupDocBuilder(dataFolderPath);
		
		Document articleDoc = dBuilder.parse(articleFile);
		articleDoc.getDocumentElement().normalize();
		
		Document inspecDoc = dBuilder.parse(inspecFile);
		inspecDoc.getDocumentElement().normalize();

		//inspecFile
	
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expr2 = xpath.compile("/*/front/article-meta");
		
		Node metadataElement = (Node)expr2.evaluate(articleDoc, XPathConstants.NODE);
		

	    InspecClassificationMerge myClass = new InspecClassificationMerge();
	    Method method = InspecClassificationMerge.class.getDeclaredMethod("mergeInspeMetadata", Element.class, Element.class);
	    method.setAccessible(true);
	    method.invoke(myClass, inspecDoc.getDocumentElement(), metadataElement);

	
		
		XPathExpression expr = xpath.compile("/*/front/article-meta/kwd-group");

		

	    Object result = expr.evaluate(articleDoc, XPathConstants.NODESET);
	    NodeList nodes = (NodeList) result;
	   
	    Assert.assertEquals(3,  nodes.getLength());
		
	}

	private static DocumentBuilder setupDocBuilder(final String dataFolderPath)
			throws ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		
		final String jatsDtdFolderPath = new File("doctypes/jats/jats-publishing-dtd-1.0").getAbsolutePath();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		dBuilder.setEntityResolver(new EntityResolver() {
			
			@Override
			public InputSource resolveEntity(String publicId, String systemId)
					throws SAXException, IOException {
				
				systemId = systemId.replace(dataFolderPath, jatsDtdFolderPath);
				return new InputSource(systemId);
			}
		});
		return dBuilder;
	}

}
