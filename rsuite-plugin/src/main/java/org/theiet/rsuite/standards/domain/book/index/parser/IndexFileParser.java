package org.theiet.rsuite.standards.domain.book.index.parser;

import java.io.*;
import java.util.*;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

import org.apache.commons.io.IOUtils;
import org.theiet.rsuite.standards.domain.book.index.PublicationIndexTerms;

import com.reallysi.rsuite.api.RSuiteException;

public class IndexFileParser {

    private static final String ATTRIBUTE_VALUE_STATUS_DELETED = "deleted";

    private static final String ELEMENT_NAME_INDEX_TERM = "indexTerm";

    private static final String ELEMENT_NAME_XREF = "xref";

    private static final String ELEMENT_INDEX_ENTRY = "indexEntry";

    private static XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
    
    static {
    	configureXMLInputFactory();
    }

	private static void configureXMLInputFactory() {
		xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
	}

    private Stack<QName> parsedElementStack = new Stack<QName>();

    private File indexFile;

    private IndexFileParserProcessor parserProcessor = new IndexFileParserProcessor();

    public IndexFileParser(File indexFile) {
        this.indexFile = indexFile;
    }

    public PublicationIndexTerms parse() {

        FileInputStream indexInputStream = null;

        try {

            indexInputStream = new FileInputStream(indexFile);
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(indexInputStream);
            System.out.println(xmlEventReader.getClass());
            while (xmlEventReader.hasNext()) {
                XMLEvent event = (XMLEvent) xmlEventReader.next();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    QName elementName = startElement.getName();
                    parsedElementStack.push(event.asStartElement().getName());

                    if (isIndexEntryElement(elementName)) {
                        parserProcessor.processStartIndexEntry();
                    } else if (isIndexTermElement(elementName)) {
                        praseIndexTermElement(xmlEventReader);
                    } else if (isXrefElement(elementName)) {
                        parseXrefElement(startElement);
                        continue;
                    }
                }

                if (event.isEndElement()) {
                    parsedElementStack.pop();
                    QName elementName = event.asEndElement().getName();

                    if (isIndexEntryElement(elementName)) {
                        parserProcessor.processEndIndexEntry();
                    }
                }
            }

        } catch (IOException e) {
            new RSuiteException(-1, "Unable to parse index file" + indexFile.getAbsolutePath(), e);
        } catch (XMLStreamException e) {
            new RSuiteException(-1, "Unable to parse index file" + indexFile.getAbsolutePath(), e);
        } finally {
            IOUtils.closeQuietly(indexInputStream);
        }


        return parserProcessor.getIndexTerms();
    }

    private void parseXrefElement(StartElement startElement) {
        Attribute hrefAttribute = startElement.getAttributeByName(new QName("href"));
        String href = hrefAttribute.getValue();

        String[] hrefSplit = href.split("#");

        String fileName = hrefSplit[0];
        String elementId = hrefSplit[1];
        
        if (elementId.contains("/")){
            elementId = elementId.substring(0, elementId.indexOf("/"));
        }

        parserProcessor.processIndexItem(fileName, elementId);
    }


    private void praseIndexTermElement(XMLEventReader xmlEventReader) throws XMLStreamException {

        StringBuilder indexTermValue = new StringBuilder();

        boolean deletedChangeTrackElement = false;
        do {
            XMLEvent event = xmlEventReader.nextEvent();

            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (isDeletedChangeTrackElement(startElement)){
                    deletedChangeTrackElement = true;
                }
                parsedElementStack.push(startElement.getName());
                
            }

            if (event.isCharacters() && !deletedChangeTrackElement) {
                indexTermValue.append(event.asCharacters().getData());
            }

            if (event.isEndElement()) {
                parsedElementStack.pop();
                deletedChangeTrackElement = false;
            }


        } while (isNotEndTermElement(xmlEventReader.peek()));

        String termValue = indexTermValue.toString();

        parserProcessor.addTermValue(termValue);
    }

    private boolean isDeletedChangeTrackElement(StartElement startElement) {
        Attribute statusAttribute = startElement.getAttributeByName(new QName("status"));
        
        if (statusAttribute != null && ATTRIBUTE_VALUE_STATUS_DELETED.equalsIgnoreCase(statusAttribute.getValue())){
            return true;
        }
        
        return false;
    }

    private boolean isIndexEntryElement(QName elementName) {
        return isElement(elementName, ELEMENT_INDEX_ENTRY);
    }

    private boolean isXrefElement(QName elementName) {
        return isElement(elementName, ELEMENT_NAME_XREF);
    }

    private boolean isIndexTermElement(QName elementName) {
        return isElement(elementName, ELEMENT_NAME_INDEX_TERM);
    }

    private boolean isElement(QName elementName, String expectedName) {
        if (expectedName.equals(elementName.getLocalPart())) {
            return true;
        }
        return false;
    }

    private boolean isNotEndTermElement(XMLEvent peek) {
        return !endIndexTermElement(peek);
    }

    private boolean endIndexTermElement(XMLEvent event) {
        if (event.isEndElement() && isIndexTermElement(event.asEndElement().getName())) {
            return true;
        }
        return false;
    }

}
