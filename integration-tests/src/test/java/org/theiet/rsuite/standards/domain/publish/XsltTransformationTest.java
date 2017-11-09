package org.theiet.rsuite.standards.domain.publish;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.util.XMLCatalogResolver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.theiet.rsuite.standards.domain.publish.generators.XsltTransformation;
import org.theiet.rsuite.utils.ZipUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.XmlApiManager;

public class XsltTransformationTest {

	private File testInputFolder;
	
	private File testOutPutFolder;
	
	private ExecutionContext context;
	
	private String xsltUri = "rsuite:/res/plugin/iet/xslt/books/O2_PIs_to_DITA.xsl";
	
	private XsltTransformation transformation;
	
	
	@Before
	public void before() throws Exception{
		
		Properties props = new Properties();
		props.load(new FileInputStream("test.properties"));
		
		final String ditaOtHome = props.getProperty("dita.ot.home");
		
		File ditaOtHomeFile = new File(ditaOtHome);
		
		if (!ditaOtHomeFile.exists()){
			throw new Exception("This test requires DITA OT");
		}
		
		
		File tempTestFolder = new File("target/test/temp");
		tempTestFolder.mkdirs();
		
		File xsltTransformationTest = new File(tempTestFolder, "XsltTransformationTest");
		FileUtils.deleteDirectory(xsltTransformationTest);
		
		testInputFolder = new File(xsltTransformationTest, "input");
		testInputFolder.mkdirs();
		
		testOutPutFolder = new File(xsltTransformationTest, "output");
		testInputFolder.mkdirs();
		
		File inputZipFile = new File("../rsuite-plugin/src/test/resources/org/theiet/rsuite/standards/Guidance_Note_7.zip");
		
		ZipUtils.unzip(inputZipFile, testInputFolder);
		
		context = Mockito.mock(ExecutionContext.class);
		
		createXMLApiManagerMock(ditaOtHome);  
		
		transformation = new XsltTransformation(context, xsltUri);
		
	}

	public void createXMLApiManagerMock(final String ditaOtHome)
			throws RSuiteException {
		XmlApiManager xmlApiMgr = Mockito.mock(XmlApiManager.class);
		
		
		Mockito.when(context.getXmlApiManager()).thenReturn(xmlApiMgr);
		
		when(xmlApiMgr.getTransformer(any(URI.class))).then(new Answer<Transformer>() {

			@Override
			public Transformer answer(InvocationOnMock invocation)
					throws Throwable {

				 Object[] args = invocation.getArguments();
				return createTransformer((URI)args[0]);
			}
			
		});
				
		when(xmlApiMgr.getLoggingXMLReader(any(Log.class), Mockito.anyBoolean())).then(new Answer<XMLReader>() {

			@Override
			public XMLReader answer(InvocationOnMock invocation)
					throws Throwable {

				//XMLReader reader = 
				
				return createXMLReader();

			}

			public XMLReader createXMLReader()
					throws SAXNotRecognizedException, SAXNotSupportedException, IOException {
				File file = new File(ditaOtHome, "catalog-dita.xml");
				String[] catalogs =   {file.toURI().toURL().toString()};
				
				// Create catalog resolver and set a catalog list.
				XMLCatalogResolver resolver = new XMLCatalogResolver();
				resolver.setPreferPublic(true);
				resolver.setCatalogList(catalogs);

				XMLReader reader = new SAXParser();
				// Set the resolver on the parser.
				reader.setProperty(
				  "http://apache.org/xml/properties/internal/entity-resolver", 
				  resolver);
				return reader;
			}
		});
	}
	
	@Test
	public void testTransformation() throws Exception {

		transformation.transformFolder(testInputFolder);
		
		//expected
		checkOutPutFile(testInputFolder);
	}
	
	@Test
	public void testFolderTransformationWithOutputFolder() throws Exception {
		
		
		transformation.transformFolder(testInputFolder, testOutPutFolder);
		
		//expected
		checkImages();
		checkOutPutFile(testOutPutFolder);
	}

	@SuppressWarnings("unchecked")
	private void checkImages() {
		File imagesOutput = new File(testOutPutFolder, "IET_Guidance_Note_7_Special_Locations/images");
		String[] pngExt =  {"png"};
		Collection<File>  files = FileUtils.listFiles(imagesOutput,   pngExt, false);
		assertEquals(4, files.size());
	}

	private void checkOutPutFile(File baseFolder)
			throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException {
		Document xmlDocument = getDocumentToCheck(baseFolder);

		XPath xPath =  XPathFactory.newInstance().newXPath();
		String expression = "count(/chapter/division[@id='division-intro-general']/title/ph[@status='changed'])";
		 

		String changeMarkupCount = xPath.compile(expression).evaluate(xmlDocument);
		assertEquals(1, Integer.parseInt(changeMarkupCount));
		
		String classCountXpath = "count(//*[contains(@class,'chapter/chapter')])";
		 

		String classCount = xPath.compile(classCountXpath).evaluate(xmlDocument);
		assertEquals(1, Integer.parseInt(classCount));
	}

	public Document getDocumentToCheck(File baseFolder)
			throws ParserConfigurationException, SAXException, IOException {
		File introductionFile = new File(baseFolder, "IET_Guidance_Note_7_Special_Locations/topics/IET_Guidance_Note_7_Introduction.dita");

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document xmlDocument = dBuilder.parse(introductionFile);
		return xmlDocument;
	}

	
	private Transformer createTransformer(URI xsltUri) throws Exception{
		TransformerFactory tFactory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl",null);

		URIResolver resolver = new URIResolver() {
			
			@Override
			public Source resolve(String href, String base) throws TransformerException {
				if (href.startsWith("rsuite:/")){
				href = href.replace("rsuite:/res/plugin/iet", "../rsuite-plugin/src/main/resources/WebContent");
					return new SAXSource(new InputSource(href));
				}
				return null;
			}
		};
		
		 Source baseSource = resolver.resolve(xsltUri.toString(), null);
		

	    Transformer transformer =  tFactory.newTransformer(baseSource);
	   return transformer;
	}
}
