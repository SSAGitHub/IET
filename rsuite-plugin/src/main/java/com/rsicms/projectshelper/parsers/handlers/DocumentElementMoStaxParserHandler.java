package com.rsicms.projectshelper.parsers.handlers;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.parsers.MoStaxParserHandler;

public class DocumentElementMoStaxParserHandler implements MoStaxParserHandler {

	private QName documentElementName;
	
	@Override
	public void parseXML(XMLEventReader reader) throws RSuiteException, XMLStreamException {

		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				documentElementName = startElement.getName();
				
				break;
			}
		}
	}

    public QName getDocumentElementName() {
        return documentElementName;
    }

		
}
