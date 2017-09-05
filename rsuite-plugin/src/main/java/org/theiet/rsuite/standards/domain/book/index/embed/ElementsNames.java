package org.theiet.rsuite.standards.domain.book.index.embed;

import javax.xml.namespace.QName;
import javax.xml.stream.events.*;

public class ElementsNames {

    public static final QName ELEMENT_KEYWORDS = new QName("keywords");
    
    public static final QName ELEMENT_KEYWORD = new QName("keyword");
    
    public static final QName ELEMENT_PROLOG = new QName("prolog");
    
    public static final QName ELEMENT_METADATA = new QName("metadata");
    
    
    public static boolean isStartElement(XMLEvent xmlEvent, QName elementName) {

        if (xmlEvent == null || !xmlEvent.isStartElement()){
            return false;
        }
        
        StartElement startElement = xmlEvent.asStartElement();        
        return elementName.equals(startElement.getName());
    }
}
