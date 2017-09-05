package org.theiet.rsuite.standards.domain.book.index.embed;

import static org.theiet.rsuite.standards.domain.book.index.embed.ElementsNames.*;

import java.io.StringReader;
import java.util.*;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.standards.domain.book.index.IndexTermToKeywordsConventer;
import org.theiet.rsuite.standards.domain.book.index.datatypes.IndexTerm;

public class PrologElementWriter {

    private PrologElementParser prologParser;

    private static XMLEventFactory xmlEventFactory = XMLEventFactory.newFactory();

    private XMLInputFactory xmlInputFactory;

    private XMLEventWriter xmlEventWriter;

    private List<IndexTerm> indexTermsForElement;

    public PrologElementWriter(XMLInputFactory xmlInputFactory, PrologElementParser prologParser,
            XMLEventWriter xmlEventWriter, List<IndexTerm> indexTermsForElement) {
        super();
        this.xmlInputFactory = xmlInputFactory;
        this.prologParser = prologParser;
        this.xmlEventWriter = xmlEventWriter;
        this.indexTermsForElement = indexTermsForElement;
    }

    public void writeProlog() throws XMLStreamException {

        String originalPrologContent = prologParser.getPrologContent();

        if (StringUtils.isNotBlank(originalPrologContent)) {
            rewritePrologElement(originalPrologContent);
        }else{
            writeKeywordElements(ELEMENT_PROLOG, ELEMENT_METADATA, ELEMENT_KEYWORDS);
        }
    }

    private void rewritePrologElement(String originalPrologContent) throws XMLStreamException {
        StringReader prologStringReader = new StringReader(originalPrologContent);
        XMLEventReader originalPrologEventReader =
                xmlInputFactory.createXMLEventReader(prologStringReader);

        boolean keywordsAddded = false;
        
        while (originalPrologEventReader.hasNext()) {
            XMLEvent xmlEvent = (XMLEvent) originalPrologEventReader.next();

            if (xmlEvent.isStartDocument() || xmlEvent.isEndDocument()){
                continue;
            }
            
            if (isStartElement(xmlEvent, ELEMENT_PROLOG) && !prologParser.isMetadataElement()) {
                xmlEventWriter.add(xmlEvent);
                writeKeywordElements(ELEMENT_METADATA, ELEMENT_KEYWORDS);
                continue;
            } else if (isStartElement(xmlEvent, ELEMENT_METADATA)
                    && !prologParser.isKeywordsElement()) {
                xmlEventWriter.add(xmlEvent);
                writeKeywordElements(ELEMENT_KEYWORDS);
                continue;
            } else if (isStartElement(xmlEvent, ELEMENT_KEYWORDS)
                    && keywordsAddded) {
                xmlEventWriter.add(xmlEvent);
                writeKeywordElements();
                keywordsAddded = true;
                continue;
            }
            
            xmlEventWriter.add(xmlEvent);
        }
    }

    private void writeKeywordElements(QName... wrapperElements) throws XMLStreamException {
        List<String> elementsToGenerate = new ArrayList<String>();
        
        for (QName name : wrapperElements){
            elementsToGenerate.add(name.getLocalPart());
        }
        
        addKeywordElements(xmlEventWriter, indexTermsForElement, elementsToGenerate);
    }
    
    private void addKeywordElements(XMLEventWriter xmlEventWriter,
            List<IndexTerm> indexTermsForElement, List<String> elementsToGenerate)
            throws XMLStreamException {

        writeStartElements(xmlEventWriter, elementsToGenerate);

        for (IndexTerm indexTerm : indexTermsForElement) {
            List<String> keywords =
                    IndexTermToKeywordsConventer.convertIndexTermToKeywords(indexTerm);
            writeKeywordElements(xmlEventWriter, keywords);

        }

        writeEndElements(xmlEventWriter, elementsToGenerate);
    }


    private void writeEndElements(XMLEventWriter xmlEventWriter, List<String> elementsToGenerate)
            throws XMLStreamException {
        for (int i = elementsToGenerate.size() - 1; i >= 0; i--) {
            xmlEventWriter.add(createEndElementEvent(elementsToGenerate.get(i)));
        }
    }


    private void writeStartElements(XMLEventWriter xmlEventWriter, List<String> elementsToGenerate)
            throws XMLStreamException {
        for (String elementName : elementsToGenerate) {
            xmlEventWriter.add(createStartElement(elementName));
        }
    }


    private void writeKeywordElements(XMLEventWriter xmlEventWriter, List<String> keywords)
            throws XMLStreamException {
        for (String keyword : keywords) {
            if (!prologParser.getExistingKeywords().contains(keyword)){
                addTextElement(xmlEventWriter, ELEMENT_KEYWORD.getLocalPart(), keyword);   
            }
        }
    }

    private void addTextElement(XMLEventWriter xmlEventWriter, String elementName, String value)
            throws XMLStreamException {
        xmlEventWriter.add(createStartElement(elementName));
        xmlEventWriter.add(xmlEventFactory.createCharacters(value));
        xmlEventWriter.add(createEndElementEvent(elementName));
    }

    private XMLEvent createEndElementEvent(String elementName) {
        return xmlEventFactory.createEndElement("", "", elementName);
    }

    private StartElement createStartElement(String elementName) {
        return xmlEventFactory.createStartElement("", "", elementName);
    }
}
