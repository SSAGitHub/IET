package com.rsicms.projectshelper.parsers;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.EntityResolver;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.validation.resolvers.RSuiteEntityResolver;
import com.rsicms.projectshelper.validation.resolvers.RSuiteXmlResolver;

public class MoStaxParser {

	public static void parseMo(ExecutionContext context, MoStaxParserHandler parserHandler, ManagedObject mo) throws RSuiteException{
		
		EntityResolver rsuiteEntityResolver = new RSuiteEntityResolver(
				context);

		XMLInputFactory factory = XMLInputFactory.newFactory();
		RSuiteXmlResolver resolver = new RSuiteXmlResolver(rsuiteEntityResolver);
		factory.setXMLResolver(resolver);

		try {
			XMLEventReader reader = factory.createXMLEventReader(mo
					.getInputStream(), "utf-8");

			parserHandler.parseXML(reader);
			
			
		} catch (XMLStreamException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
					"Unable to parse for mo " + mo.getId(), e);
		}
		
	}
}
