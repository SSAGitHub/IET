package org.theiet.rsuite.journals.domain.issues.publish.common.areatree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.IOUtils;

import com.reallysi.rsuite.api.RSuiteException;

public class AreaTreeParser {

	private static XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

	private static final String NAMESPACE_AREA_TREE = "http://www.antennahouse.com/names/XSL/AreaTree";

	private static final QName ATTRIBUTE_ID = new QName("id");

	private static final QName ATTRIBUTE_TEXT = new QName("text");

	private static final QName ELEMENT_BLOCK_AREA = new QName(NAMESPACE_AREA_TREE,
			"BlockArea");

	private static final QName ELEMENT_TEXT_AREA = new QName(NAMESPACE_AREA_TREE, "TextArea");

	private static final QName ELEMENT_PAGE_VIEWPORT_AREA = new QName(NAMESPACE_AREA_TREE,
			"PageViewportArea");

	public IssueProofInformation parseAreaTreeFile(File areaTreeFile) throws RSuiteException {
		FileInputStream areaTreeInput = null;

		AreaTreeParserResult areaTreeResult = new AreaTreeParserResult();

		try {
			areaTreeInput = new FileInputStream(areaTreeFile);

			XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(
					areaTreeInput, "utf-8");

			while (eventReader.hasNext()) {
				XMLEvent xmlEvent = eventReader.nextEvent();

				if (xmlEvent.isStartElement()) {
					StartElement startElement = xmlEvent.asStartElement();
					QName name = startElement.getName();

					if (ELEMENT_PAGE_VIEWPORT_AREA.equals(name)) {
						areaTreeResult.incrementPageNumbers();
					} else if (ELEMENT_BLOCK_AREA.equals(name)) {
						parseBlockAreaElement(areaTreeResult, eventReader,
								startElement);
					}
				}

				if (isEndPageViewPortArea(xmlEvent)) {
					areaTreeResult.endPDFPage();
				}
			}

		} catch (FileNotFoundException e) {
			throw new RSuiteException(0, "Unable to parse area tree file");			
		} catch (XMLStreamException e) {
			throw new RSuiteException(0, "Unable to parse area tree file");
		} finally {
			IOUtils.closeQuietly(areaTreeInput);
		}

		return areaTreeResult;
	}

	private boolean isEndPageViewPortArea(XMLEvent xmlEvent) {
		return xmlEvent.isEndElement()
				&& ELEMENT_PAGE_VIEWPORT_AREA.equals(xmlEvent.asEndElement()
						.getName());
	}

	private void parseBlockAreaElement(AreaTreeParserResult areaTreeResult,
			XMLEventReader eventReader, StartElement startElement)
			throws XMLStreamException {
		Attribute idAttribute = startElement.getAttributeByName(ATTRIBUTE_ID);

		if (idAttribute == null) {
			return;
		}

		String id = idAttribute.getValue();

		if (id.equals("start_instruct_page")){
			areaTreeResult.startInstructPage();
		}else if(id.equals("end_instruct_page")){
			areaTreeResult.endInstructPage();			
		}else if (id.equals("start_toc")){
			areaTreeResult.startTOC();
		}else if(id.equals("end_toc")){
			areaTreeResult.endTOC();			
		} else if (id.startsWith("startarticle")) {
			areaTreeResult.startArticle(getDOIfromIdAttributeValue(id));
		} else if (id.startsWith("endarticle")) {
			areaTreeResult.endArticle(getDOIfromIdAttributeValue(id));
		}

		if ("pageNumber".equals(id)) {
			String actualPageNumber = parsePageNumber(eventReader);
			areaTreeResult.currentPage(actualPageNumber);
		}
	}

	private String getDOIfromIdAttributeValue(String value) {
		return value.substring(value.indexOf("_") + 1);
	}

	private String parsePageNumber(XMLEventReader eventReader)
			throws XMLStreamException {

		String pageNumber = "";

		while (eventReader.hasNext()) {
			XMLEvent xmlEvent = eventReader.nextEvent();

			if (isEndBlockElement(xmlEvent)) {
				break;
			}

			if (xmlEvent.isStartElement()
					&& xmlEvent.asStartElement().getName()
							.equals(ELEMENT_TEXT_AREA)) {
				Attribute textAttribute = xmlEvent.asStartElement()
						.getAttributeByName(ATTRIBUTE_TEXT);
				if (textAttribute != null) {
					pageNumber = textAttribute.getValue();
				}
			}
		}

		return pageNumber;
	}

	private boolean isEndBlockElement(XMLEvent xmlEvent) {
		if (xmlEvent.isEndElement()
				&& xmlEvent.asEndElement().getName().equals(ELEMENT_BLOCK_AREA)) {
			return true;
		}
		return false;
	}
}
