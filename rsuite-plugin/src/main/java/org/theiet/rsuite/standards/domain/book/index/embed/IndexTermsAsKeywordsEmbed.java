package org.theiet.rsuite.standards.domain.book.index.embed;

import static org.theiet.rsuite.standards.domain.book.index.embed.ElementsNames.*;

import java.io.*;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

import org.apache.commons.io.IOUtils;
import org.theiet.rsuite.standards.domain.book.index.datatypes.*;

import com.reallysi.rsuite.api.RSuiteException;

public class IndexTermsAsKeywordsEmbed {

    private static XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
    
    static {
    	configureXMLInputFactory();
    }

    private static XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();

    private static void configureXMLInputFactory() {
		xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
	}
    
    public void embedIndexTermsAsKeyWords(File inputFile, DitaFileIndexTerms indexTerms,
            File outputFile) throws RSuiteException {

        InputStream fileInputStream = null;
        FileOutputStream fileOutputstream = null;

        try {

            fileInputStream = new FileInputStream(inputFile);
            fileOutputstream = new FileOutputStream(outputFile);

            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(fileInputStream);

            XMLEventWriter xmlEventWriter = xmlOutputFactory.createXMLEventWriter(fileOutputstream);

            while (xmlEventReader.hasNext()) {
                XMLEvent event = (XMLEvent) xmlEventReader.next();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();

                    List<IndexTerm> indexTermsForElement =
                            getIndexTermsForElement(startElement, indexTerms);

                    if (indexTermsForElement != null && indexTermsForElement.size() > 0) {
                        xmlEventWriter.add(event);
                        addIndexTermsAsKeywords(xmlEventReader, xmlEventWriter,
                                indexTermsForElement);
                        continue;
                    }
                }

                xmlEventWriter.add(event);

            }

        } catch (IOException e) {
            throw new RSuiteException(-1, createErrorMessage(inputFile), e);
        } catch (XMLStreamException e) {
            throw new RSuiteException(-1, createErrorMessage(inputFile), e);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
            IOUtils.closeQuietly(fileOutputstream);
        }

    }

    private String createErrorMessage(File inputFile) {
        return "Unable to embed index terms as keywords for " + inputFile.getName();
    }

    private void addIndexTermsAsKeywords(XMLEventReader xmlEventReader,
            XMLEventWriter xmlEventWriter, List<IndexTerm> indexTermsForElement)
            throws XMLStreamException {

        XMLEvent xmlEvent = peekNextStartElementWithRewriting(xmlEventReader, xmlEventWriter);

        if (isStartTitle(xmlEvent)) {
            copyElement(xmlEventReader, xmlEventWriter, xmlEvent.asStartElement().getName());
            xmlEvent = peekNextStartElementWithRewriting(xmlEventReader, xmlEventWriter);
        }


        PrologElementParser prologParser = new PrologElementParser(xmlOutputFactory);

        if (isStartElement(xmlEvent, ELEMENT_PROLOG)) {
            prologParser.parsePrologElement(xmlEventReader);
        }

        PrologElementWriter prologWriter =
                new PrologElementWriter(xmlInputFactory, prologParser, xmlEventWriter,
                        indexTermsForElement);
        prologWriter.writeProlog();
    }

    private void copyElement(XMLEventReader xmlEventReader, XMLEventWriter xmlEventWriter,
            QName elementName) throws XMLStreamException {
        while (xmlEventReader.hasNext()) {
            XMLEvent xmlEvent = xmlEventReader.nextEvent();

            xmlEventWriter.add(xmlEvent);

            if (xmlEvent.isEndElement()) {
                EndElement endElement = xmlEvent.asEndElement();
                if (elementName.equals(endElement.getName())) {
                    return;
                }
            }
        }

    }

    private boolean isStartTitle(XMLEvent xmlEvent) {

        if (xmlEvent == null || !xmlEvent.isStartElement()) {
            return false;
        }

        StartElement startElement = xmlEvent.asStartElement();
        Attribute classAttribute = startElement.getAttributeByName(new QName("class"));

        if (classAttribute != null && classAttribute.getValue().contains("topic/title")) {
            return true;
        }

        return false;
    }

    private XMLEvent peekNextStartElementWithRewriting(XMLEventReader xmlEventReader,
            XMLEventWriter xmlEventWriter) throws XMLStreamException {
        XMLEvent xmlEvent = xmlEventReader.peek();

        if (xmlEvent.isEndElement()){
            return null;
        }
        
        if (xmlEvent.isStartElement()) {
            return xmlEvent;
        }

        do {
            xmlEvent = xmlEventReader.nextEvent();
            xmlEventWriter.add(xmlEvent);
            xmlEvent = xmlEventReader.peek();
            
            if (xmlEvent.isEndElement()){
                return null;
            }

        } while (!xmlEvent.isStartElement());


        return xmlEvent;

    }

    private List<IndexTerm> getIndexTermsForElement(StartElement startElement,
            DitaFileIndexTerms fileIndexTerms) {

        Attribute idAttribute = startElement.getAttributeByName(new QName("id"));

        if (idAttribute == null) {
            return null;
        }

        String id = idAttribute.getValue();

        return fileIndexTerms.getIndexTermsForElement(id);
    }
}
