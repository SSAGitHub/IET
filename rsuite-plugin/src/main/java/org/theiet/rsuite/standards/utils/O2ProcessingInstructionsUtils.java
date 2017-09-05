package org.theiet.rsuite.standards.utils;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.XMLEvent;

import com.reallysi.rsuite.api.RSuiteException;

final public class O2ProcessingInstructionsUtils {

	private O2ProcessingInstructionsUtils(){
	};
	
	/** Event factory */
	private static XMLEventFactory eventFactory = XMLEventFactory.newInstance();

	/**
	 * Removes all attributes from the defined namespaces
	 * 
	 * @param inStream
	 *            The XML input stream
	 * @param outStream
	 *            The result XML stream
	 * @param namespaceToRemove
	 *            set of namespace to remove
	 * @throws RSuiteException
	 *             if error occured
	 */
	public static boolean removeO2ChangeTrackingPI(InputStream inStream,
			OutputStream outStream)
			throws RSuiteException {
		
		boolean changedDocument = false;
		
		try {
			XMLInputFactory factory = XMLInputFactory.newInstance();
			factory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
			factory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
			XMLEventReader reader = factory.createXMLEventReader(inStream);

			XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
			XMLEventWriter writer = xmlof.createXMLEventWriter(outStream,			
					"utf-8");

			XMLEvent event = reader.peek();

			while (!event.isEndDocument()) {
				event = reader.nextEvent();

				if (writer != null) {

					boolean writeEvent = true;

					if (event.getEventType() == XMLEvent.DTD) {
						DTD dtd = (DTD)event;
						String declaration = dtd.getDocumentTypeDeclaration();
						if (declaration.contains("[]")){
							event = eventFactory.createDTD(declaration.replace("[]", ""));
						}
					}
					
					if (event.isProcessingInstruction()){
						ProcessingInstruction pi = (ProcessingInstruction)event;
						if (pi.getTarget().startsWith("oxy_")){
							writeEvent = false;
							changedDocument = true;
						}
					}

					if (writeEvent) {
						writer.add(event);
					}
				}
			}
			
			writer.close();
		} catch (XMLStreamException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
					"Unable to remove rsuite attribute", e);
		}
		
		return changedDocument;
	}


}
