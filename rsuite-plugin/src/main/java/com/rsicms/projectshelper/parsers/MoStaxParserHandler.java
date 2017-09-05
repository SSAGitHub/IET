package com.rsicms.projectshelper.parsers;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import com.reallysi.rsuite.api.RSuiteException;

public interface MoStaxParserHandler {

		void parseXML(XMLEventReader reader) throws RSuiteException, XMLStreamException;
}
