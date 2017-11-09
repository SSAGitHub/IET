package org.theiet.rsuite.standards.domain.publish;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.theiet.rsuite.standards.StandardsBooksConstans.XSLT_URI_REG_2_ICML_WREG;
import static org.theiet.rsuite.standards.domain.publish.datatype.StandardsPublishWorkflowVariables.*;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;

import junit.framework.Assert;
import net.sf.saxon.FeatureKeys;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import org.apache.commons.io.*;
import org.apache.commons.logging.Log;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.util.XMLCatalogResolver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.theiet.rsuite.standards.domain.publish.generators.Xml2IcmlGenerator;
import org.theiet.rsuite.utils.JarUtils;
import org.theiet.rsuite.utils.ZipUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.xml.LoggingSaxonMessageListener;
import com.reallysi.rsuite.service.AuthorizationService;
import com.reallysi.rsuite.service.ReportManager;
import com.reallysi.rsuite.service.XmlApiManager;

import org.junit.rules.TemporaryFolder;

public class IcmlTransformationTest {

	private static final String RSUITE_PLUGIN_PROTOCOL = "rsuite:/res/plugin/";

	private File testInputFolder;

	private File testOutPutFolder;

	private ExecutionContext context;

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	
	
	public void testXml2IcmlGenerator() throws Exception {

	    Log log = Mockito.mock(Log.class);
	    
		Xml2IcmlGenerator generator = new Xml2IcmlGenerator();
		generator.initialize(context, log, null);

		File testFolder = testInputFolder.getParentFile();
		File tempFolder = new File(testFolder, "tmp");
		tempFolder.mkdirs();

		File moFolder = new File(tempFolder, "export/1245");
		moFolder.mkdirs();

		File inputZipFile = new File(
				"rsuite-plugin/src/test/resources/org/theiet/rsuite/standards/Guidance_Note_7.zip");
		ZipUtils.unzip(inputZipFile, moFolder);

		

		File testInputFile = new File(
				moFolder,
				"IET_Guidance_Note_7_Special_Locations/IET_Guidance_Note_7_Special_Locations.ditamap");
		generator.generateOutput(tempFolder, "222", testInputFile,
				testOutPutFolder);
		
		
		//EXPECTED
		
		int icmlCount = 0;
		for (File outFile : testOutPutFolder.listFiles()){
			
			if (outFile.isDirectory()){
				continue;
			}
			
			String extenstion = FilenameUtils.getExtension(outFile.getName());
			if ("icml".equalsIgnoreCase(extenstion)){
				icmlCount++;
			}
					
		}
		
		Assert.assertEquals(9, icmlCount);

	}
	
	@Test
	public void testXml2IcmlGenerator2() throws Exception {

		Xml2IcmlGenerator generator = new Xml2IcmlGenerator();
		Log log = Mockito.mock(Log.class);
		
		Map<String, String> variables = new HashMap<String, String>();
		variables.put(ICML_XSLT_URI.getVariableName(), XSLT_URI_REG_2_ICML_WREG);
		variables.put(MATHML_SIZE.getVariableName(), "1");
		
		generator.initialize(context, log, variables);

		File testFolder = testInputFolder.getParentFile();
		File tempFolder = new File(testFolder, "tmp");
		tempFolder.mkdirs();

		File moFolder = new File(tempFolder, "export/12456");
		moFolder.mkdirs();

		File inputZipFile = new File(
				"../rsuite-plugin/src/test/resources/org/theiet/rsuite/standards/index.zip");
		ZipUtils.unzip(inputZipFile, moFolder);

		

		File testInputFile = new File(
				moFolder,
				"index.dita");
		generator.generateOutput(tempFolder, "333", testInputFile,
				testOutPutFolder);
		
		
		//EXPECTED
		
		int icmlCount = 0;
		for (File outFile : testOutPutFolder.listFiles()){
			
			if (outFile.isDirectory()){
				continue;
			}
			
			String extenstion = FilenameUtils.getExtension(outFile.getName());
			if ("icml".equalsIgnoreCase(extenstion)){
				icmlCount++;
			}
					
		}
		
		Assert.assertEquals(1, icmlCount);

	}
	
	@Before
	public void before() throws Exception {

		Properties props = new Properties();
		props.load(new FileInputStream("test.properties"));

		final String ditaOtHome = props.getProperty("dita.ot.home");


		File ditaOtHomeFile = new File(ditaOtHome);

		if (!ditaOtHomeFile.exists()) {
			throw new Exception("This test requires DITA OT");
		}

		File tempTestFolder = testFolder.newFolder("test");
		tempTestFolder.mkdirs();

		File xsltTransformationTest = new File(tempTestFolder,
				"IcmlTransformationTest");
		FileUtils.deleteDirectory(xsltTransformationTest);

		testInputFolder = new File(xsltTransformationTest, "input");
		testInputFolder.mkdirs();

		testOutPutFolder = new File(xsltTransformationTest, "output");
		testInputFolder.mkdirs();

		File inputZipFile = new File(
				"../rsuite-plugin/src/test/resources/org/theiet/rsuite/standards/Guidance_Note_7.zip");
		ZipUtils.unzip(inputZipFile, testInputFolder);

		context = Mockito.mock(ExecutionContext.class);

		createXMLApiManagerMock(ditaOtHome);

		ReportManager reportManager = Mockito.mock(ReportManager.class);
		Mockito.when(context.getReportManager()).thenReturn(reportManager);

		AuthorizationService authSvc = Mockito.mock(AuthorizationService.class);
		Mockito.when(context.getAuthorizationService()).thenReturn(authSvc);

		User user = Mockito.mock(User.class);

		Mockito.when(authSvc.getSystemUser()).thenReturn(user);

	}

	public void createXMLApiManagerMock(final String ditaOtHome)
			throws RSuiteException {
		XmlApiManager xmlApiMgr = Mockito.mock(XmlApiManager.class);

		Mockito.when(context.getXmlApiManager()).thenReturn(xmlApiMgr);

		when(xmlApiMgr.getTransformer(any(URI.class))).then(
				new Answer<Transformer>() {

					@Override
					public Transformer answer(InvocationOnMock invocation)
							throws Throwable {

						Object[] args = invocation.getArguments();
						return createTransformer((URI) args[0]);
					}

				});

		when(
				xmlApiMgr.getSaxonXsltTransformer(any(URI.class),
						any(LoggingSaxonMessageListener.class))).then(
				new Answer<XsltTransformer>() {

					@Override
					public XsltTransformer answer(InvocationOnMock invocation)
							throws Throwable {

						Object[] args = invocation.getArguments();
						return getSaxonTransformer((URI) args[0]);
					}

				});

		when(xmlApiMgr.getRSuiteAwareURIResolver()).thenReturn(
				createURIResolver());
		LoggingSaxonMessageListener listener = Mockito
				.mock(LoggingSaxonMessageListener.class);

		when(xmlApiMgr.newLoggingSaxonMessageListener(any(Log.class)))
				.thenReturn(listener);

		when(
				xmlApiMgr.getLoggingXMLReader(any(Log.class),
						Mockito.anyBoolean())).then(new Answer<XMLReader>() {

			@Override
			public XMLReader answer(InvocationOnMock invocation)
					throws Throwable {

				// XMLReader reader =

				return createXMLReader();

			}

			public XMLReader createXMLReader()
					throws SAXNotRecognizedException, SAXNotSupportedException {
				String[] catalogs = { "file://" + ditaOtHome
						+ "/catalog-dita.xml" };

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

	private Transformer createTransformer(URI xsltUri) throws Exception {
		TransformerFactory tFactory = TransformerFactory.newInstance(
				"net.sf.saxon.TransformerFactoryImpl", null);

		URIResolver resolver = createURIResolver();

		Source baseSource = resolver.resolve(xsltUri.toString(), null);

		tFactory.setURIResolver(resolver);
		Transformer transformer = tFactory.newTransformer(baseSource);
		transformer.setURIResolver(resolver);

		return transformer;
	}

	public URIResolver createURIResolver() {
		URIResolver resolver = new URIResolver() {

			@Override
			public Source resolve(String href, String base)
					throws TransformerException {

				try {

					if (href.startsWith(("rsuite:/res/plugin/iet"))) {

						href = href.replace("rsuite:/res/plugin/iet",
								"../rsuite-plugin/src/main/resources/WebContent");
						SAXSource saxSource = new SAXSource(new InputSource(
								href));
						return saxSource;
					} else if (href.startsWith(RSUITE_PLUGIN_PROTOCOL)
							|| (base != null && base
									.startsWith(RSUITE_PLUGIN_PROTOCOL))) {

						if (base.startsWith(RSUITE_PLUGIN_PROTOCOL)) {

							href = (new URI(base)).resolve(href).toString();
						}

						String newHref = href.replace(RSUITE_PLUGIN_PROTOCOL,
								"");
						int index = newHref.indexOf("/", 1);

						String pluginId = newHref.substring(0, index);
						String resourcePath = newHref.substring(index);

						 InputStream resourceAsStream = getClass().getResourceAsStream("/WebContent"+resourcePath);

						if (resourceAsStream != null) { 
							InputSource is = new InputSource(
									resourceAsStream);
							is.setSystemId(href);
						SAXSource source = new SAXSource(is);
							source.setSystemId(href);

						return source;
						}

					}

					if (base.startsWith("file://") || base.startsWith("file:/")) {

						File baseFile = new File(new URI(base));
						File targetFile = new File(baseFile.getParentFile(),
								href);

						return new SAXSource(new InputSource(targetFile.toURI()
								.toString()));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}
		};
		return resolver;
	}

	public XsltTransformer getSaxonTransformer(URI xsltUri) throws Exception {

		URIResolver resolver = createURIResolver();

		Source style = resolver.resolve(xsltUri.toString(), null);

		Processor proc = new Processor(false); // Not schema aware.
		proc.setConfigurationProperty(FeatureKeys.DTD_VALIDATION, false);
		proc.setConfigurationProperty(FeatureKeys.LINE_NUMBERING, true);
		proc.getUnderlyingConfiguration().setURIResolver(resolver);

		XsltCompiler compiler = proc.newXsltCompiler();
		compiler.setURIResolver(resolver);

		XsltExecutable xsltExe = null;
		xsltExe = compiler.compile(style);

		XsltTransformer transformer = xsltExe.load();

		return transformer;

	}

}
