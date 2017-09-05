package org.theiet.rsuite.standards.domain.book.index.embed;

import static org.theiet.rsuite.standards.domain.book.index.embed.ElementsNames.*;

import java.io.StringWriter;
import java.util.*;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

public class PrologElementParser {

    private boolean metadataElement;
    
    private boolean keywordsElement;
    
    private Set<String> existingKeywords = new HashSet<String>();
    
    private XMLOutputFactory xmlOutputFactory;
    
    private String prologContent;
    
    
    public PrologElementParser(XMLOutputFactory xmlOutputFactory) {
        super();
        this.xmlOutputFactory = xmlOutputFactory;
    }

    public void parsePrologElement(XMLEventReader ditaFileXMLReader) throws XMLStreamException{
        
        StringWriter stringWriter = new StringWriter();
        
        XMLEventWriter tempPrologEventWriter = xmlOutputFactory.createXMLEventWriter(stringWriter);
  
        parse(ditaFileXMLReader, tempPrologEventWriter);
        
        prologContent = stringWriter.toString();
    }

    private void parse(XMLEventReader ditaFileXMLReader, XMLEventWriter tempPrologEventWriter)
            throws XMLStreamException {
        boolean keywordElement = false;
        StringBuilder keyword = null;
        
        while (ditaFileXMLReader.hasNext()){
            XMLEvent xmlEvent = ditaFileXMLReader.nextEvent();
            
            if (xmlEvent.isStartElement()){
                StartElement startElement = xmlEvent.asStartElement();
                QName name = startElement.getName();
                
                if (ELEMENT_METADATA.equals(name)){
                    metadataElement = true;
                }else if (ELEMENT_KEYWORD.equals(name)){
                    keywordElement = true;
                    keyword = new StringBuilder();
                }else if (ELEMENT_KEYWORD.equals(name)){
                    keywordsElement = true;
                }
            }
            
            if (xmlEvent.isCharacters() && keywordElement){
                keyword.append(xmlEvent.asCharacters().getData());
            }
            
            tempPrologEventWriter.add(xmlEvent);
            
            if (xmlEvent.isEndElement()){
                EndElement endElement = xmlEvent.asEndElement();
                QName endElementName = endElement.getName();
                
                if (ELEMENT_KEYWORD.equals(endElementName)){
                    existingKeywords.add(keyword.toString());
                }else if (ELEMENT_PROLOG.equals(endElementName)){
                    break;
                }
            }
        }
    }

    public boolean isMetadataElement() {
        return metadataElement;
    }

    public Set<String> getExistingKeywords() {
        return existingKeywords;
    }

    public String getPrologContent() {
        return prologContent;
    }

    public boolean isKeywordsElement() {
        return keywordsElement;
    }
    
}
