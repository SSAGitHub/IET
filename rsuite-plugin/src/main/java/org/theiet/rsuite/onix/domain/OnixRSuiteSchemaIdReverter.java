package org.theiet.rsuite.onix.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.theiet.rsuite.onix.OnixConstants;

import com.reallysi.rsuite.api.RSuiteException;

public class OnixRSuiteSchemaIdReverter implements OnixConstants {

	private String UTF_8 = "utf-8";

	private XMLEventFactory eventFactory = XMLEventFactory.newFactory();

	public void revertRSuiteOnixSchema(File inputFile, File outputFile)
			throws RSuiteException {

		try {
			FileInputStream inputStream = new FileInputStream(inputFile);
			
			
			outputFile.getParentFile().mkdirs();
			outputFile.createNewFile();
			FileOutputStream outStream = new FileOutputStream(outputFile);
			
			revertRSuiteOnixSchema(inputStream, outStream);
		} catch (FileNotFoundException e) {
			throw new RSuiteException(0,
					"Unable to create initial onix file from the template ", e);
		} catch (IOException e) {
			throw new RSuiteException(0,
					"Unable to create initial onix file from the template ", e);
		}

	}

	public void revertRSuiteOnixSchema(InputStream inStream,
			OutputStream outStream) throws RSuiteException {

		try {

			XMLInputFactory factory = XMLInputFactory.newInstance();

			XMLEventReader reader = factory.createXMLEventReader(inStream,
					UTF_8);

			XMLOutputFactory xmlof = XMLOutputFactory.newInstance();

			XMLEventWriter xmlw = xmlof.createXMLEventWriter(outStream, UTF_8);
			revertOnixSchemaDeclaration(reader, xmlw);
			xmlw.close();
		} catch (XMLStreamException e) {
			throw new RSuiteException(0,
					"Unable to create initial onix file from the template ", e);
		}
	}

	/**
	 * @param reader
	 *            - the document reader
	 * @param writer
	 *            - the document writer
	 * @throws XMLStreamException
	 *             if something goes wrong.
	 * @throws LibraryReferenceResolverException
	 *             if something goes wrong.
	 */
	private void revertOnixSchemaDeclaration(XMLEventReader reader,
			XMLEventWriter writer) throws XMLStreamException {

		XMLEvent event = reader.peek();

		boolean addEvent = true;

		while (!event.isEndDocument()) {
			addEvent = true;
			event = reader.nextEvent();

			if (writer != null) {

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					String localName = startElement.getName().getLocalPart();

					if (ELEMENT_NAME_ONIX_MESSAGE.equals(localName)) {

						Iterator<Attribute> updateSchemaAttIterartor = createUpdatedAttributeIterator(startElement);

						event = eventFactory.createStartElement(
								startElement.getName(),
								updateSchemaAttIterartor,
								startElement.getNamespaces());

					}
				}

				if (addEvent) {
					writer.add(event);
				}

			}
		}
	}

	@SuppressWarnings("unchecked")
	public Iterator<Attribute> createUpdatedAttributeIterator(
			StartElement startElement) {

		final Iterator<Attribute> attributeIt = startElement.getAttributes();

		Iterator<Attribute> updateSchemaAttIterartor = new Iterator<Attribute>() {

			@Override
			public boolean hasNext() {

				return attributeIt.hasNext();
			}

			@Override
			public Attribute next() {
				Attribute next = attributeIt.next();

				if (ATTRIBUTE_NAME_NO_NAMESPACE_SCHEMA_LOCATION.equals(next
						.getName().getLocalPart())) {
					next = eventFactory.createAttribute(next.getName(),
							ONIX_RSUITE_SCHEMA_FILE_NAME);
				}

				return next;
			}

			@Override
			public void remove() {
				attributeIt.remove();
			}
		};
		return updateSchemaAttIterartor;
	}

}
