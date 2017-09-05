package org.theiet.rsuite.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.xml.RSuiteAwareDocumentBuilder;
import com.reallysi.rsuite.service.XmlApiManager;

/**
 * This helper class abstracts the invocation of XSL transforms so that various
 * action handlers in the plugin can invoke with differing configurations.
 */
public final class XsltUtils {

	private XsltUtils() {
	}

	private static Log log = LogFactory.getLog(XsltUtils.class);

	private XmlApiManager xmlApiManager;

	/**
	 * Create a new transform helper instance.
	 */
	public XsltUtils(XmlApiManager xmlApiManager) {
		this.xmlApiManager = xmlApiManager;
	}

	/**
	 * Execute an XSL transform.
	 * 
	 * @param inputFile
	 *            XML document to transform.
	 * @param xslUri
	 *            String URI to XSLT source.
	 * @param params
	 *            Transform paramaters.
	 * @param outputFile
	 *            Location to place transform output.
	 * @throws Exception
	 */
	public void transform(File inputFile, String xslUri,
			Map<String, String> params, File outputFile) throws RSuiteException {
		URI u = createURIFromString(xslUri);
		transform(inputFile, u, params, outputFile, true);
	}

	public void transform(File inputFile, String xslUri,
			Map<String, String> params, OutputStream out)
			throws RSuiteException {
		transform(inputFile, xslUri, params, out, true);
	}

	public void transform(File inputFile, String xslUri,
			Map<String, String> params, OutputStream out,
			boolean useRSuiteAwareDocumentBuilder) throws RSuiteException {
		Source xmlSource = useRSuiteAwareDocumentBuilder ? constructRSuiteAwareDocument(inputFile)
				: new StreamSource(inputFile);

		Transformer transformer = xmlApiManager
				.getTransformer(createURIFromString(xslUri));

		if (params != null) {
			for (String key : params.keySet()) {
				logParameters(params, key);
				transformer.setParameter(key, params.get(key));
			}
		}
		transformToOutputStream(xmlSource, transformer, out);
	}

	private void logParameters(Map<String, String> params, String key) {
		log.info("setting parameter: " + key + "=" + params.get(key));
	}

	private URI createURIFromString(String xsltUri) throws RSuiteException {
		try {
			return new URI(xsltUri);
		} catch (URISyntaxException e) {
			throw ExceptionUtils.createRsuiteException(e);
		}
	}

	/**
	 * Execute an XSL transform.
	 * 
	 * @param inputFile
	 *            XML document to transform.
	 * @param xslUri
	 *            URI to XSLT source
	 * @param params
	 *            Transform paramaters.
	 * @param outputFile
	 *            Location to place transform output.
	 * @throws Exception
	 */
	public void transform(File inputFile, URI xslUri,
			Map<String, String> params, File outputFile) throws RSuiteException {
		transform(inputFile, xslUri, params, outputFile, true);
	}

	/**
	 * Execute an XSL transform.
	 * 
	 * @param inputFile
	 *            XML document to transform.
	 * @param xslFile
	 *            File to XSLT source
	 * @param params
	 *            Transform parameters.
	 * @param outputFile
	 *            Location to place transform output.
	 * @throws Exception
	 */
	public void transform(File inputFile, File xslFile,
			Map<String, String> params, File outputFile) throws RSuiteException {
		transform(inputFile, xslFile.toURI(), params, outputFile, true);
	}

	public void transform(File inputFile, URI xslFile,
			Map<String, String> params, File outputFile,
			boolean useRSuiteAwareDocumentBuilder) throws RSuiteException {
		// Parse the document into a DOM, using an entity resolver
		// that knows the schemas in RSuite

		Source xmlSource = useRSuiteAwareDocumentBuilder ? constructRSuiteAwareDocument(inputFile)
				: new StreamSource(inputFile);

		Transformer transformer = xmlApiManager.getTransformer(xslFile);

		if (params != null) {
			for (String key : params.keySet()) {
				logParameters(params, key);
				transformer.setParameter(key, params.get(key));
			}
		}

		transformToFile(xmlSource, transformer, outputFile);
	}

	public void transform(File inputFile, URI xslFile,
			Map<String, String> params, OutputStream outputStream,
			boolean useRSuiteAwareDocumentBuilder) throws RSuiteException {
		// Parse the document into a DOM, using an entity resolver
		// that knows the schemas in RSuite

		Source xmlSource = useRSuiteAwareDocumentBuilder ? constructRSuiteAwareDocument(inputFile)
				: new StreamSource(inputFile);

		Transformer transformer = xmlApiManager.getTransformer(xslFile);

		if (params != null) {
			for (String key : params.keySet()) {
				logParameters(params, key);
				transformer.setParameter(key, params.get(key));
			}
		}

		transformToOutputStream(xmlSource, transformer, outputStream);
	}

	/**
	 * Execute an XSL transform.
	 * 
	 * @param inputFile
	 *            XML document to transform.
	 * @param xslFile
	 *            File to XSLT source
	 * @param params
	 *            Transform paramaters.
	 * @param outputStream
	 *            Stream to send output of transform to.
	 * @throws Exception
	 */
	public void transform(String inputXML, File xslFile,
			Map<String, String> params, OutputStream outputStream)
			throws RSuiteException {
		Transformer transformer = xmlApiManager.getTransformer(xslFile);
		if (params != null) {
			for (String key : params.keySet()) {
				logParameters(params, key);
				transformer.setParameter(key, params.get(key));
			}
		}
		transformToOutputStream(new StreamSource(new StringReader(inputXML)),
				transformer, outputStream);
	}

	/**
	 * Execute an XSL transform.
	 * 
	 * @param inputFile
	 *            XML document to transform.
	 * @param xslUri
	 *            URI string to XSLT source.
	 * @param params
	 *            Transform paramaters.
	 * @param outputStream
	 *            Stream to send output of transform to.
	 * @throws Exception
	 */
	public void transform(String inputXML, String xslUri,
			Map<String, String> params, OutputStream outputStream)
			throws RSuiteException {
		transform(inputXML, createURIFromString(xslUri), params, outputStream);
	}

	/**
	 * Execute an XSL transform.
	 * 
	 * @param inputFile
	 *            XML document to transform.
	 * @param xslUri
	 *            URI to XSLT source.
	 * @param params
	 *            Transform paramaters.
	 * @param outputStream
	 *            Stream to send output of transform to.
	 * @throws Exception
	 */
	public void transform(String inputXML, URI xslUri,
			Map<String, String> params, OutputStream outputStream)
			throws RSuiteException {
		Transformer transformer = xmlApiManager.getTransformer(xslUri);
		if (params != null) {
			for (String key : params.keySet()) {
				logParameters(params, key);
				transformer.setParameter(key, params.get(key));
			}
		}
		transformToOutputStream(new StreamSource(new StringReader(inputXML)),
				transformer, outputStream);
	}

	/**
     * 
     */
	private File transformToFile(Source source, Transformer transformer,
			File outputFile) throws RSuiteException {
		File file = outputFile;
		FileOutputStream os = null;

		try {
			os = new FileOutputStream(file);
			transformToOutputStream(source, transformer, os);

		} catch (Exception e) {
			log.warn("unable to transform to output: "
					+ outputFile.getAbsolutePath() + ": " + e.getMessage());
			ExceptionUtils.throwRsuiteException(e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					log.info("Proble with closing stream", e);
				}
			}
		}
		return file;
	}

	private void transformToOutputStream(Source source,
			Transformer transformer, OutputStream outputStream)
			throws RSuiteException {
		try {
			StreamResult result = new StreamResult(outputStream);

			transformer.transform(source, result);
		} catch (Exception e) {
			log.warn("unable to transform to output stream: " + e.getMessage());
			ExceptionUtils.throwRsuiteException(e);
		}
	}

	/**
     * 
     */
	protected DOMSource constructRSuiteAwareDocument(File inputFile)
			throws RSuiteException {
		RSuiteAwareDocumentBuilder builder = xmlApiManager
				.getRSuiteAwareDocumentBuilder();

		try {
			Document doc = builder.getDocument(inputFile, false);
			return new DOMSource(doc);
		} catch (Exception e) {
			ExceptionUtils.throwRsuiteException(e);
		}

		return null;
	}

	/**
	 * Transforms inputStream with specific stylesheet
	 * 
	 * @param context
	 *            The execution context
	 * @param inputStream
	 *            The input stream to transform
	 * @param stylesheetURI
	 *            The stylesheet URI
	 * @param parameters
	 *            The transform parameters
	 * @param customResolver
	 *            Custom entity resolver, if null RSuite aware resolver will be
	 *            used
	 * @param outputStream
	 *            the output stream fro transformation result
	 * @throws RSuiteException
	 *             if transfromation will fail
	 */
	public static void transform(ExecutionContext context,
			InputStream inputStream, String stylesheetURI,
			Map<String, String> parameters, EntityResolver customResolver,
			OutputStream outputStream) throws RSuiteException {

		try {
			XmlApiManager xmlMgr = context.getXmlApiManager();

			XMLReader xmlReader = XMLReaderFactory.createXMLReader();

			if (customResolver != null) {
				xmlReader.setEntityResolver(customResolver);
			} else {
				xmlReader.setEntityResolver(xmlMgr
						.getRSuiteAwareEntityResolver());
			}

			Source source = new SAXSource(xmlReader, new InputSource(
					inputStream));

			StreamResult streamResult = new StreamResult(outputStream);

			Transformer transformer = xmlMgr.getTransformer(new URI(
					stylesheetURI));

			if (parameters != null) {
				for (String key : parameters.keySet()) {
					transformer.setParameter(key, parameters.get(key));
				}
			}

			transformer.transform(source, streamResult);
		} catch (TransformerException e) {
			handleTransformationException(e);
		} catch (SAXException e) {
			handleTransformationException(e);
		} catch (URISyntaxException e) {
			handleTransformationException(e);
		}
	}

	/**
	 * Transforms inputStream with specific stylesheet
	 * 
	 * @param context
	 *            The execution context
	 * @param inputStream
	 *            The input stream to transform
	 * @param stylesheetURI
	 *            The stylesheet URI
	 * @param parameters
	 *            The transform parameters
	 * @param customResolver
	 *            Custom entity resolver, if null RSuite aware resolver will be
	 *            used
	 * @param wirter
	 *            the writer for transformation result
	 * @throws RSuiteException
	 *             if transformation will fail
	 */
	public static void transform(ExecutionContext context, ManagedObject mo,
			String stylesheetURI, Map<String, String> parameters, Writer writer)
			throws RSuiteException {

		try {

			XmlApiManager xmlMgr = context.getXmlApiManager();

			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setEntityResolver(xmlMgr.getRSuiteAwareEntityResolver());

			Source source = new SAXSource(xmlReader, new InputSource(
					mo.getInputStream()));

			StreamResult streamResult = new StreamResult(writer);

			Transformer transformer = xmlMgr.getTransformer(new URI(
					stylesheetURI));

			if (parameters != null) {
				for (String key : parameters.keySet()) {
					transformer.setParameter(key, parameters.get(key));
				}
			}

			transformer.transform(source, streamResult);
		} catch (TransformerException e) {
			handleTransformationException(e);
		} catch (SAXException e) {
			handleTransformationException(e);
		} catch (URISyntaxException e) {
			handleTransformationException(e);
		}
	}

	/**
	 * Handles exception occured during transformation process.
	 * 
	 * @param e
	 *            Exception object
	 * @throws RSuiteException
	 */
	private static void handleTransformationException(Exception e)
			throws RSuiteException {
		ExceptionUtils.throwRsuiteException("Unable to perform transformation",
				e);
	}
}