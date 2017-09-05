package org.theiet.rsuite.onix.domain;

import static org.theiet.rsuite.onix.OnixConstants.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

import com.reallysi.rsuite.api.RSuiteException;

public class OnixFormatter {

	private static final String UTF_8 = "utf-8";

	private static final XMLEventFactory XML_EVENT_FACTORY = XMLEventFactory.newFactory();

	private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newInstance();

	private static final XMLOutputFactory XML_OUTPUT_FACTORY = XMLOutputFactory.newInstance();

	private static final Set<QName> ATTRIBUTES_TO_SKIP = new HashSet<>();

	private static final List<Namespace> NAMESPACE_LIST = new ArrayList<>();

	static {
		ATTRIBUTES_TO_SKIP.add(new QName("http://www.rsuitecms.com/rsuite/ns/metadata", "rsuiteId"));
		ATTRIBUTES_TO_SKIP.add(new QName("http://www.w3.org/2001/XMLSchema-instance", "noNamespaceSchemaLocation"));
		ATTRIBUTES_TO_SKIP.add(new QName("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation"));

		NAMESPACE_LIST.add(XML_EVENT_FACTORY.createNamespace(NAMESPACE_ONIX));
	}

	public void removeRSuiteDataFromOnixMessage(InputStream xmlInput, OutputStream xmlOutput) throws RSuiteException {

		try {
			XMLEventReader xmlReader = XML_INPUT_FACTORY.createXMLEventReader(xmlInput, UTF_8);

			XMLEventWriter xmlWriter = XML_OUTPUT_FACTORY.createXMLEventWriter(xmlOutput, UTF_8);

			XMLEvent event = xmlReader.peek();

			event = rewriteDocumentAndRemoveRSuiteData(xmlReader, xmlWriter, event);

		} catch (XMLStreamException e) {
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		}

	}

	private XMLEvent rewriteDocumentAndRemoveRSuiteData(XMLEventReader xmlReader, XMLEventWriter xmlWriter,
			XMLEvent event) throws XMLStreamException {
		while (!event.isEndDocument()) {
			event = xmlReader.nextEvent();

			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String localName = startElement.getName().getLocalPart();

				if (ELEMENT_NAME_ONIX_MESSAGE.equals(localName)) {

					Iterator<Attribute> updateSchemaAttIterartor = createUpdatedAttributeIterator(startElement);

					event = XML_EVENT_FACTORY.createStartElement(startElement.getName(), updateSchemaAttIterartor,
							NAMESPACE_LIST.iterator());
				}
			}

			xmlWriter.add(event);
		}
		return event;
	}

	@SuppressWarnings("unchecked")
	private Iterator<Attribute> createUpdatedAttributeIterator(StartElement startElement) {
		Iterator<Attribute> attributes = startElement.getAttributes();

		List<Attribute> newAttributeList = new ArrayList<>();

		while (attributes.hasNext()) {
			Attribute attribute = (Attribute) attributes.next();

			if (ATTRIBUTES_TO_SKIP.contains(attribute.getName())) {
				continue;
			}

			newAttributeList.add(attribute);
		}

		return newAttributeList.iterator();
	}
}
