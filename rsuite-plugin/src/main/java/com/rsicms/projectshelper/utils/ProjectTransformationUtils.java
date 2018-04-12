package com.rsicms.projectshelper.utils;

import static com.rsicms.projectshelper.utils.ProjectPluginUtils.getPluginResource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.XmlApiManager;

public class ProjectTransformationUtils {

	private ProjectTransformationUtils() {
	}

	public static Templates createTransformTemplate(ExecutionContext context,
			TransformerFactory transformerFactory, String xsltURI)
			throws RSuiteException {

		Templates template = null;
		try {
			InputStream xsltStream = getPluginResource(context,
					new URI(xsltURI));
			StreamSource xsltSource = new StreamSource(xsltStream);
			xsltSource.setSystemId(xsltURI);

			template = transformerFactory.newTemplates(xsltSource);
		} catch (URISyntaxException e) {
			throw new RSuiteException(0,
					"unable to create transform template for " + xsltURI, e);

		} catch (TransformerConfigurationException e) {
			throw new RSuiteException(0,
					"unable to create transform template for " + xsltURI, e);
		}
		return template;
	}
	
	public static void transformDocument(ExecutionContext context,
			File inputFile, String xslURI, File outputFile,
			Map<String, String> paramters) throws RSuiteException {
		transformDocument(context.getXmlApiManager(), inputFile, xslURI, outputFile, paramters);
	}

	public static void transformDocument(XmlApiManager xmlManager,
			File inputFile, String xslURI, File outputFile,
			Map<String, String> paramters) throws RSuiteException {

		Writer fileWriter = null;

		try {
			fileWriter = new FileWriter(outputFile);
			transformDocument(xmlManager, inputFile, xslURI, fileWriter, paramters);
		} catch (IOException e) {
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		} finally {
			IOUtils.closeQuietly(fileWriter);
		}

	}

	public static void transformMo(XmlApiManager xmlManager, ManagedObject mo,
			String xslURI, Writer writer, Map<String, String> parameters)
			throws RSuiteException {
		
		DOMSource domSource = new DOMSource(mo.getElement());
		transformDocument(xmlManager, domSource, xslURI, writer, parameters);
	}
	
	public static void transformDocument(ExecutionContext context,
			File inputFile, String xslURI, Writer writer,
			Map<String, String> paramters) throws RSuiteException {
		transformDocument(context.getXmlApiManager(), inputFile, xslURI, writer, paramters);	
	}

	public static void transformDocument(XmlApiManager xmlManager,
			File inputFile, String xslURI, Writer writer,
			Map<String, String> paramters) throws RSuiteException {

		InputSource inputSource = new InputSource(inputFile.toURI().toString());
		Source transformSource = createSourceForTransform(xmlManager, inputSource);
		transformDocument(xmlManager, transformSource, xslURI, writer, paramters);
	}

	public static void transformDocument(XmlApiManager xmlManager,
			InputStream contentToTransform, String xslURI, Writer writer,
			Map<String, String> paramters) throws RSuiteException {
		try {
			InputSource inputSource = new InputSource(contentToTransform);
			Source transformSource = createSourceForTransform(xmlManager, inputSource);
			transformDocument(xmlManager, transformSource, xslURI, writer, paramters);
		} finally {
			IOUtils.closeQuietly(contentToTransform);
		}
	}

	private static Source createSourceForTransform(XmlApiManager xmlManager,
			InputSource inputSource) throws RSuiteException {

		try{
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setEntityResolver(xmlManager.getRSuiteAwareEntityResolver());

			SAXSource inputDocument = new SAXSource(xmlReader, inputSource);
			return inputDocument;			
		} catch (SAXException e) {
			throw new RSuiteException(0, "Unable to create sax source", e);
		}

	}

	public static void transformDocument(XmlApiManager xmlManager,
			Source inputSource, String xslURI, Writer writer,
			Map<String, String> paramters) throws RSuiteException {
		try {

			Transformer transformer = xmlManager.getTransformer(new URI(
					xslURI));
			
			transformer.setOutputProperty(OutputKeys.INDENT, "no");
			
			StreamResult streamResult = new StreamResult(writer);

			setTransformParameters(paramters, transformer);

			transformer.transform(inputSource, streamResult);

		} catch (TransformerConfigurationException e) {
			throw new RSuiteException(0, generateErrorMessage(xslURI), e);
		}  catch (TransformerException e) {
			throw new RSuiteException(0, generateErrorMessage(xslURI), e);
		} catch (URISyntaxException e) {
			throw new RSuiteException(0, generateErrorMessage(xslURI), e);
		} finally {
			IOUtils.closeQuietly(writer);
		}

	}

	private static void setTransformParameters(Map<String, String> paramters,
			Transformer transformer) {
		if (paramters != null) {
			for (Entry<String, String> entry : paramters.entrySet()) {
				transformer.setParameter(entry.getKey(), entry.getValue());
			}
		}
	}

	private static String generateErrorMessage(String xslURI) {
		return "Unable to tranform content. xsl uri " + xslURI;
	}

	//TODO check if needed 
	//final URIResolver rsuiteURIResolver = transformer.getURIResolver();
	//logger.info("=== = " + transformer.getURIResolver());
	// transformer.setURIResolver(new URIResolver() {
	//
	// private Log logger = LogFactory.getLog(getClass());
	//
	// @Override
	// public Source resolve(String href, String base) throws
	// TransformerException {
	// logger.info("--- href " + href);
	// logger.info("--- base " + base);
	// URIResolver baseURIResolver = rsuiteURIResolver;
	//
	// if (baseURIResolver != null){
	// Source source = baseURIResolver.resolve(href, base);
	//
	// if (source != null){
	// InputSource inputSource2 = new InputSource(source.getSystemId());
	//
	// SAXSource inputDocument = new SAXSource(xmlReader, inputSource2);
	// return inputDocument;
	// }
	// }
	//
	//
	//
	// //InputSource x = new InputSource();
	//
	//
	// URI baseURI = URI.create(base);
	// URI hrefURI = URI.create(href);
	//
	// if ("file".equals(baseURI.getScheme()) && hrefURI.getScheme() ==
	// null){
	//
	// URI relativeURI = baseURI.resolve(hrefURI);
	// return new StreamSource(relativeURI.toString());
	//
	//
	// }
	//
	// // TODO Auto-generated method stub
	// return new StreamSource(href);
	// }
	// });
}
