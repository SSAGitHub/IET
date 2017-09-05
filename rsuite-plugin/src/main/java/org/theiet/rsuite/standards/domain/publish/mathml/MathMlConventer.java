package org.theiet.rsuite.standards.domain.publish.mathml;

import java.io.*;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.XMLEvent;

import org.theiet.rsuite.utils.ExceptionUtils;

import com.reallysi.rsuite.api.RSuiteException;

class MathMlConventer {

	private static final String IMAGE_ELEMENT_NAME = "image";

	private static final String MATH_ML_WRAPPER_D4P_DISPLAY_EQUATION = "d4p_display-equation";
	
	private static final String MATH_ML_WRAPPER_D4P_MATHML = "d4p_MathML";

	private static final String UTF_8 = "UTF-8";

	private XMLInputFactory inputFactory = XMLInputFactory.newInstance();

	private XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

	private XMLEventFactory eventFactory = XMLEventFactory.newInstance();

	private File contextFile = null;

	private MathMlEntryProcessor mathMlEntryProcessor;

	protected MathMlConventer(
			MathMlEntryProcessor mathMlImageGenerator) {
		super();
		this.mathMlEntryProcessor = mathMlImageGenerator;
		inputFactory.setXMLResolver(createXmlResolver());
	}

	private XMLResolver createXmlResolver() {
		return new XMLResolver() {
			
			@Override
			public Object resolveEntity(String publicID, String systemID, String baseURI, String namespace)
					throws XMLStreamException {
				if (systemID != null && systemID.endsWith("dtd")){
					return new StringReader("");
				}
				
				return null;
			}
		};
	}

	void convertMathMlInXMLContent(File inputXML, File outputXML)
			throws RSuiteException {

		try {
			contextFile = inputXML;
			InputStream inputXMLStream = new FileInputStream(inputXML);
			OutputStream ouputXMLStream = new FileOutputStream(outputXML);

			convertMathMlInXMLContent(inputXMLStream, ouputXMLStream);
		} catch (IOException e) {
			throw ExceptionUtils.createRsuiteException(
					createErrorMessage(inputXML), e);
		} catch (XMLStreamException e) {
			throw ExceptionUtils.createRsuiteException(
					createErrorMessage(inputXML), e);
		}
	}

	protected String createErrorMessage(File inputXML) {
		String errorMessage = "Math ml convertion failed for "
				+ inputXML.getAbsolutePath();
		return errorMessage;
	}

	void convertMathMlInXMLContent(InputStream inputXMLStream,
			OutputStream outputXMLStream) throws XMLStreamException, RSuiteException {
		
		XMLEventReader xmlReader = inputFactory.createXMLEventReader(
				inputXMLStream, UTF_8);

		XMLEventWriter xmlWriter = outputFactory.createXMLEventWriter(
				outputXMLStream, UTF_8);

		processXMLContent(xmlReader, xmlWriter);

		xmlWriter.close();
	}

	private void processXMLContent(XMLEventReader xmlReader,
			XMLEventWriter xmlWriter) throws XMLStreamException, RSuiteException {

		XMLEvent event = xmlReader.peek();

		while (!event.isEndDocument()) {
			event = xmlReader.nextEvent();

			if (isMathMlWrapperEvent(event)){
			    continue;
			}
			
			if (isStartMathMLElement(event)){
			    convertMathMlToImageElement(xmlReader, xmlWriter, event);
			    continue;
			}			
			
			xmlWriter.add(event);
			
		}
	}
	
	 private boolean isStartMathMLElement(XMLEvent event) {
        
	     if (event.isStartElement()) {
             QName elementName = event.asStartElement().getName();
             return isMathML(elementName);
	     }
	     
        return false;
    }

    private boolean isMathMlWrapperEvent(XMLEvent event) {
	        if (startMathMlElementWrapper(event) || endMathMlElementWrapper(event)){
	            return true;
	        }
	        return false;
	    }

	    private boolean startMathMlElementWrapper(XMLEvent event) {

	        if (event.isStartElement()
	                && isMathMlWrapperElement(event.asStartElement().getName())) {
	            return true;
	        }
	        return false;
	    }
	    
	    private boolean endMathMlElementWrapper(XMLEvent event) {

	        if (event.isEndElement()
	                && isMathMlWrapperElement(event.asEndElement().getName())) {
	            return true;
	        }
	        return false;
	    }

	private void convertMathMlToImageElement(XMLEventReader xmlReader, XMLEventWriter xmlWriter, XMLEvent event) throws XMLStreamException, RSuiteException {
	    String mathMlXML = extractML(xmlReader, event);
        String mathMlImageRelativePath = mathMlEntryProcessor
                .convertMathMlToImage(contextFile, mathMlXML);
        createImageRefernceElement(xmlWriter, mathMlImageRelativePath);
        
    }


	private boolean isMathMlWrapperElement(QName elementName) {
		String localName = elementName.getLocalPart();

		if (MATH_ML_WRAPPER_D4P_DISPLAY_EQUATION.equals(localName) || MATH_ML_WRAPPER_D4P_MATHML.equals(localName)) {
			return true;
		}

		return false;
	}

	private void createImageRefernceElement(XMLEventWriter xmlWriter,
			String linkValue) throws XMLStreamException {
		xmlWriter.add(eventFactory.createStartElement("", "", IMAGE_ELEMENT_NAME));
		xmlWriter.add(eventFactory.createAttribute("href", linkValue));
		xmlWriter.add(eventFactory.createAttribute("class", "- topic/image "));
		xmlWriter.add(eventFactory.createEndElement("", "", IMAGE_ELEMENT_NAME));
	}

	private String extractML(XMLEventReader xmlReader, XMLEvent startMathMLEvent)
			throws XMLStreamException {

		StringWriter stringWriter = new StringWriter();

		XMLEventWriter xmlWriter = outputFactory
				.createXMLEventWriter(stringWriter);

		xmlWriter.add(startMathMLEvent);
		XMLEvent xmlEvent = null;
		do {
			xmlEvent = xmlReader.nextEvent();
			xmlWriter.add(xmlEvent);
		}while (isNotMathMlElement(xmlEvent) && !xmlEvent.isEndDocument());
		
		xmlWriter.flush();
		
		return stringWriter.toString();
	}

	private boolean isNotMathMlElement(XMLEvent xmlEvent)
			throws XMLStreamException {
		
		if (xmlEvent.isEndElement()) {
			EndElement endElement = xmlEvent.asEndElement();
			return !isMathML(endElement.getName());
		}

		return true;
	}

	private boolean isMathML(QName elementName) {
		String localName = elementName.getLocalPart();
		String namespaceUri = elementName.getNamespaceURI();

		if ("math".equals(localName)
				&& "http://www.w3.org/1998/Math/MathML".equals(namespaceUri)) {
			return true;
		}

		return false;
	}
}
