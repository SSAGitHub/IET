package org.theiet.rsuite.onix.domain;

import java.io.*;
import java.util.Map;

import javax.xml.stream.*;
import javax.xml.stream.events.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.theiet.rsuite.onix.OnixConstants;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.helpers.utils.RsuiteXMLUtils;

public class InitialOnixFileCreator implements OnixConstants {

	private static final String ELEMENT_NAME_SUBTITLE = "Subtitle";

	private static final String ELEMENT_NAME_NO_EDITION = "NoEdition";

	private static final String ELEMENT_NAME_EDITION_NUMBER = "EditionNumber";

	private XMLEventFactory eventFactory = XMLEventFactory.newFactory();

	private ExecutionContext context;

	private User user;

	public InitialOnixFileCreator(ExecutionContext context, User user) {
		this.context = context;
		this.user = user;
	}

	public void createInitialOnixMo(ContentAssembly onixTargetCa,
			Map<String, String> variables) throws RSuiteException {

		OnixDefaultConfiguration defaultConfiguration = OnixDefaultConfiguration
				.getInstance(context);

		ManagedObject defaultTemplateMo = defaultConfiguration
				.getDefaultTemplateMo();

		InputStream templateInputStream = RsuiteXMLUtils
				.removeAttributesFromRSuiteNamespace(defaultTemplateMo
						.getInputStream());

		ByteArrayOutputStream initialOnixOutStream = new ByteArrayOutputStream();

		createInitialOnixFromTemplate(templateInputStream,
				initialOnixOutStream, variables);

		loadAndAttachInitialOnixFile(onixTargetCa, variables,
				initialOnixOutStream);
	}

	private void loadAndAttachInitialOnixFile(ContentAssembly onixTargetCa,
			Map<String, String> variables, ByteArrayOutputStream outStream)
			throws RSuiteException {

		String productCode = variables.get("product_code");

		String onixFileName = productCode + ONIX_FILE_SUFFIX;

		ObjectSource src = new XmlObjectSource(outStream.toByteArray());

		ObjectInsertOptions insertOptions = new ObjectInsertOptions(
				onixFileName, null, null, false);

		ManagedObject initalOnixMo = context.getManagedObjectService().load(
				user, src, insertOptions);

		context.getContentAssemblyService().attach(user, onixTargetCa.getId(),
				initalOnixMo, null, new ObjectAttachOptions());
	}

	public void replaceVarialbes() {

	}

	/**
	 * @throws FileNotFoundException
	 * @throws XMLStreamException
	 */
	public void createInitialOnixFromTemplate(InputStream inStream,
			OutputStream outStream, Map<String, String> variables)
			throws RSuiteException {

		try {

			XMLInputFactory factory = XMLInputFactory.newInstance();

			XMLEventReader reader = factory.createXMLEventReader(inStream,
					UTF_8);

			XMLOutputFactory xmlof = XMLOutputFactory.newInstance();

			XMLEventWriter xmlw = xmlof.createXMLEventWriter(outStream, UTF_8);
			copyDocumentAndReplaceVariables(reader, xmlw, variables);
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
	private void copyDocumentAndReplaceVariables(XMLEventReader reader,
			XMLEventWriter writer, Map<String, String> variables)
			throws XMLStreamException {

		XMLEvent event = reader.peek();

		boolean addEvent = true;

		boolean removingElement = false;

		while (!event.isEndDocument()) {
			addEvent = true;
			event = reader.nextEvent();

			if (writer != null) {

				if (event.isCharacters()) {
					Characters characters = event.asCharacters();
					String data = characters.getData();

					StrSubstitutor sub = new StrSubstitutor(variables);
					String resolvedString = sub.replace(data);

					event = eventFactory.createCharacters(resolvedString);
				}

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					String localName = startElement.getName().getLocalPart();

					addEvent = removeChoiceElements(variables, localName);

					addEditionNumber(writer, variables, localName);

					if (!addEvent) {
						removingElement = true;
					}
				}

				if (event.isEndElement()) {
					EndElement startElement = event.asEndElement();
					String localName = startElement.getName().getLocalPart();

					addEvent = removeChoiceElements(variables, localName);

					if (!addEvent) {
						removingElement = false;
					}

				}

				if (addEvent && !removingElement) {
					writer.add(event);
				}

			}
		}
	}

	private void addEditionNumber(XMLEventWriter writer,
			Map<String, String> variables, String localName)
			throws XMLStreamException {
		if (ELEMENT_NAME_NO_EDITION.equals(localName)
				&& StringUtils.isNotBlank(variables
						.get(ONIX_VAR_EDITION_NUMBER))) {
			writer.add(eventFactory.createStartElement("", "",
					ELEMENT_NAME_EDITION_NUMBER));
			writer.add(eventFactory.createCharacters(variables
					.get(ONIX_VAR_EDITION_NUMBER)));
			writer.add(eventFactory.createEndElement("", "",
					ELEMENT_NAME_EDITION_NUMBER));
		}
	}

	private boolean removeChoiceElements(Map<String, String> variables,
			String localName) {

		boolean addEvent = true;

		if (ELEMENT_NAME_SUBTITLE.equals(localName)
				&& StringUtils.isBlank(variables.get(ONIX_VAR_BOOK_SUBTITLE))) {
			addEvent = false;
		}

		if (ELEMENT_NAME_NO_EDITION.equals(localName)
				&& StringUtils.isNotBlank(variables
						.get(ONIX_VAR_EDITION_NUMBER))) {
			addEvent = false;
		}
		return addEvent;
	}
}
