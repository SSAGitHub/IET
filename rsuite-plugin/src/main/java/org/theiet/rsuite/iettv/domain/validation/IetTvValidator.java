package org.theiet.rsuite.iettv.domain.validation;

import java.io.*;

import javax.xml.parsers.*;

import org.xml.sax.*;

import com.reallysi.rsuite.api.RSuiteException;

public class IetTvValidator {

    private static SAXParserFactory factory = SAXParserFactory.newInstance();

    static {
        factory.setValidating(true);
        factory.setNamespaceAware(true);
    }

    public static void validateFile(File videoXMLFile, final EntityResolver entityResolver)
            throws RSuiteException {

        IetTvValidationErrorHandler errorHandler = new IetTvValidationErrorHandler(videoXMLFile);
        XMLReader reader = createXMLReader(entityResolver, errorHandler);

        InputSource inputSource = new InputSource(videoXMLFile.getAbsolutePath());
        try {
            reader.parse(inputSource);
            errorHandler.checkForErrors();
        } catch (IOException e) {
            throw new RSuiteException(0, "Unable to validate file "
                    + videoXMLFile.getAbsolutePath(), e);
        } catch (SAXException e) {
            throw new RSuiteException(0, "Unable to validate file "
                    + videoXMLFile.getAbsolutePath(), e);
        }
    }

    private static XMLReader createXMLReader(final EntityResolver entityResolver,
            IetTvValidationErrorHandler errorHandler) throws RSuiteException {

        SAXParser parser = createSaxParser();
        try {
            XMLReader reader = parser.getXMLReader();
            reader.setEntityResolver(createEntityResolver(entityResolver));
            reader.setErrorHandler(errorHandler);

            return reader;
        } catch (SAXException e) {
            throw new RSuiteException(0, "Unable to create XML Reader", e);
        }
    }

    private static EntityResolver createEntityResolver(final EntityResolver entityResolver) {
        return new EntityResolver() {

            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException,
                    IOException {

                InputSource entity = entityResolver.resolveEntity(publicId, systemId);

                if (entity == null && systemId.startsWith("http")) {
                    // URL url = new URL(systemId);
                    // new InputSource((url.openConnection().getInputStream()), systemId)
                    return new InputSource(systemId);
                }

                return entity;
            }
        };
    }

    private static SAXParser createSaxParser() throws RSuiteException {
        try {
            SAXParser parser = factory.newSAXParser();
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");
            return parser;
        } catch (ParserConfigurationException e) {
            throw new RSuiteException(0, "Unable to create sax parser", e);
        } catch (SAXException e) {
            throw new RSuiteException(0, "Unable to create sax parser", e);
        }

    }
}
