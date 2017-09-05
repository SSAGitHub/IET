package com.rsicms.projectshelper.export.impl.context;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.StringUtils;

public class MoExportContextXqueryResultParser {

	private Map<String, String> moIdToMoVersionMapping = new HashMap<String, String>();

	private Map<String, String> moIdToPathMapping = new HashMap<String, String>();

	private Map<String, String> moReferenceIdToMoVersionMapping = new HashMap<String, String>();

	private Map<String, String> caIdToCaDisplayNameMapping = new HashMap<String, String>();

	private Stack<String> caStackPath = new Stack<String>();

	private String currentPath = "";

	public MoExportContextImpl parseXqueryResult(String caId, String result) throws XMLStreamException {

		XMLInputFactory factory = XMLInputFactory.newFactory();

		XMLEventReader reader = factory.createXMLEventReader(new StringReader(
				result));

		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();

			if (event.isStartElement()) {
				handleStartElement(reader, event);
			}

			if (isEndElement(event, "ca")) {
				caStackPath.pop();
				currentPath = generatePathFromCaStack();
			}
		}

		return new MoExportContextImpl(caId, moIdToMoVersionMapping, moIdToPathMapping);
	}

	public void handleStartElement(XMLEventReader reader, XMLEvent event)
			throws XMLStreamException {
		StartElement startElement = event.asStartElement();
		String localName = startElement.getName().getLocalPart();

		if ("moRevisionInfo".equals(localName)) {
			processMoRevisionInfoElement(reader);
		} else if ("ci".equals(localName)) {
			processCiElement(reader, startElement);
		} else if ("ca".equals(localName)) {
			processCaElement(startElement);
		} else if ("mf".equals(localName)) {
			processMfElement(startElement);
		}
	}

	private void processMoRevisionInfoElement(XMLEventReader reader)
			throws XMLStreamException {

		String moRevisionData = parseElementText(reader, "moRevisionInfo");

		if (StringUtils.isBlank(moRevisionData)){
			return;
		}
		
		String[] moRevisions = moRevisionData.split("\\s+");

		for (String moRevision : moRevisions) {
			String[] moRevisionInfo = moRevision.split("_");
			moReferenceIdToMoVersionMapping.put(moRevisionInfo[0],
					moRevisionInfo[1]);
		}

	}

	private String parseElementText(XMLEventReader reader, String elementName)
			throws XMLStreamException {

		String elementText = "";

		while (reader.hasNext()) {

			XMLEvent event = reader.nextEvent();

			if (event.isCharacters()) {
				elementText += event.asCharacters().getData();
			}

			if (isEndElement(event, elementName)) {
				break;
			}
		}
		return elementText;
	}

	private boolean isEndElement(XMLEvent event, String elementName) {
		return (event.isEndElement() && elementName.equals(event.asEndElement()
				.getName().getLocalPart()));
	}

	private void processCaElement(StartElement startElement) {
		String caId = getAttribute(startElement, "id");
		caStackPath.push(caId);
		currentPath = generatePathFromCaStack();
	}

	private void processCiElement(XMLEventReader reader,
			StartElement startElement) throws XMLStreamException {

		String attributeName = "id";

		String caId = getAttribute(startElement, attributeName);
		String displayName = parseElementText(reader, "ci");
		caIdToCaDisplayNameMapping.put(caId, displayName);

	}

	private String getAttribute(StartElement startElement, String attributeName) {
		Attribute attribute = startElement.getAttributeByName(new QName(
				attributeName));
		return attribute.getValue();
	}

	private void processMfElement(StartElement startElement) {
		String moId = getAttribute(startElement, "h");
		String moReferenceId = getAttribute(startElement, "i");

		String version = moReferenceIdToMoVersionMapping.get(moReferenceId);

		if (StringUtils.isNotBlank(version)) {
			moIdToMoVersionMapping.put(moId, version);
		}

		moIdToPathMapping.put(moId, currentPath);
	}

	private String generatePathFromCaStack() {

		StringBuilder path = new StringBuilder("/");
		for (String item : caStackPath) {
			String displayName = caIdToCaDisplayNameMapping.get(item);
			path.append(displayName).append("/");
		}

		return path.toString();
	}
	
	
	
}
