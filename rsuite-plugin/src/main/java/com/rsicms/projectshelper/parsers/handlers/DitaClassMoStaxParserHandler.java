package com.rsicms.projectshelper.parsers.handlers;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.parsers.MoStaxParserHandler;

public class DitaClassMoStaxParserHandler implements MoStaxParserHandler {

	private String classAttributeValue;
	
	@Override
	public void parseXML(XMLEventReader reader) throws RSuiteException, XMLStreamException {

		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				Attribute classAttrbute = startElement.getAttributeByName(new QName("class"));
				
				if (classAttrbute != null){
					this.classAttributeValue = classAttrbute.getValue();
				}
				
				break;
			}
		}
	}

	public String getClassAttributeValue() {
		return classAttributeValue;
	}

		
}
