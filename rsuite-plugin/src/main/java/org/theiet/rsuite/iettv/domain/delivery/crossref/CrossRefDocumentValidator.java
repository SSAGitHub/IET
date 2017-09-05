package org.theiet.rsuite.iettv.domain.delivery.crossref;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FilenameUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import com.reallysi.rsuite.api.RSuiteException;

public class CrossRefDocumentValidator {

	private CrossRefDocumentValidator() {
	}

	private static SAXParserFactory factory = SAXParserFactory.newInstance();

	public static CrossRefDocumentValidationResult validateCrossRefDocument(String document,
			EntityResolver entityResolver) throws RSuiteException {

		try (StringReader stringReader = new StringReader(document)) {

			CrossRefDocumentValidatorErrorHandler errorHandler = new CrossRefDocumentValidatorErrorHandler();

			XMLReader reader = createXmlRearder(entityResolver, errorHandler);

			InputSource input = new InputSource(stringReader);
			reader.parse(input);

			if (errorHandler.hasErrors()) {
				return new CrossRefDocumentValidationResult(false, errorHandler.getErrorMessages().toString());				
			}
			
			return new CrossRefDocumentValidationResult(true);

		} catch (IOException | SAXException e) {
			throw new RSuiteException(0, "Unable to validate the document", e);
		} catch (ParserConfigurationException e) {
			throw new RSuiteException(0, "Unable to validate the document", e);
		}

	}

	private static XMLReader createXmlRearder(EntityResolver entityResolver,
			CrossRefDocumentValidatorErrorHandler errorHandler)
			throws ParserConfigurationException, SAXException,
			SAXNotRecognizedException, SAXNotSupportedException {

		SAXParser parser = createSaxParser();
		XMLReader reader = parser.getXMLReader();
		reader.setEntityResolver(entityResolver);

		reader.setErrorHandler(errorHandler);
		return reader;
	}

	private static SAXParser createSaxParser()
			throws ParserConfigurationException, SAXException,
			SAXNotRecognizedException, SAXNotSupportedException {
		factory.setValidating(true);

		factory.setNamespaceAware(true);

		SAXParser parser = factory.newSAXParser();
		parser.setProperty(
				"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				"http://www.w3.org/2001/XMLSchema");
		return parser;
	}

	public static CrossRefDocumentValidationResult validateCrossRefDocument(String document)
			throws RSuiteException {
		return validateCrossRefDocument(document, createPluginClassPathResolver());
	}

	private static EntityResolver createPluginClassPathResolver() {
		return new EntityResolver() {

			@Override
			public InputSource resolveEntity(String publicId, String systemId)
					throws SAXException, IOException {
				String name = FilenameUtils.getName(systemId);

				String schemaResourcePath = "/WebContent/doctypes/crossref/"
						+ name;
				InputStream schemaResource = this.getClass()
						.getResourceAsStream(schemaResourcePath);

				if (schemaResource != null) {
					return new InputSource(schemaResource);
				}
				return null;

			}
		};
	}
}
