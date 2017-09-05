package org.theiet.rsuite.iettv.domain.ingestion;

import java.io.*;

import javax.xml.stream.*;
import javax.xml.stream.events.*;

import org.apache.commons.io.FileUtils;

import com.reallysi.rsuite.api.RSuiteException;


public class VideoRecordXmlPreprocessor {

    private static final String ELEMENT_NAME_VIDEO_INSPEC = "VideoInspec";

    private static XMLInputFactory inputFactory = XMLInputFactory.newFactory();

    private static XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
    
    private static XMLEventFactory eventFactory = XMLEventFactory.newFactory();

    public void preprocessFile(File file) throws RSuiteException {

        try {
            File inputFile = new File(file.getParentFile(), file.getName() + "_original");
            FileUtils.copyFile(file, inputFile);
            File outputFile = file;
            removeInspecElement(inputFile, outputFile);
        } catch (IOException e) {
            throw new RSuiteException(-1, "Unable to preprocess file " + file.getAbsolutePath(), e);
        } catch (XMLStreamException e) {
            throw new RSuiteException(-1, "Unable to preprocess file " + file.getAbsolutePath(), e);
        }


    }

    private void removeInspecElement(File inputFile, File outputFile) throws XMLStreamException,
            FileNotFoundException, IOException {


        XMLEventReader xmlReader = inputFactory.createXMLEventReader(new FileReader(inputFile));
        XMLEventWriter xmlWriter = outputFactory.createXMLEventWriter(new FileWriter(outputFile));

        boolean copyEvent = true;

        while (xmlReader.hasNext()) {
            XMLEvent xmlEvent = xmlReader.nextEvent();

            if(xmlEvent.isStartDocument()){
            	xmlWriter.add(eventFactory.createStartDocument("utf-8"));
            	continue;
            }
            
            if (isStartVideoInspecElement(xmlEvent)) {
            	copyEvent = false;
            }

            if (copyEvent) {
                xmlWriter.add(xmlEvent);
            }

            if (isEndVideoInspecElement(xmlEvent)) {
                copyEvent = true;
            }
        }

        xmlWriter.flush();
        xmlWriter.close();
        xmlReader.close();
    }

    private boolean isStartVideoInspecElement(XMLEvent xmlEvent) {
        return xmlEvent.isStartElement()
                && ELEMENT_NAME_VIDEO_INSPEC.equals(getLocalNameStartElement(xmlEvent));
    }

    private String getLocalNameStartElement(XMLEvent xmlEvent) {
        StartElement startElement = xmlEvent.asStartElement();
        String localName = startElement.getName().getLocalPart();
        return localName;
    }

    private boolean isEndVideoInspecElement(XMLEvent xmlEvent) {
        return xmlEvent.isEndElement()
                && ELEMENT_NAME_VIDEO_INSPEC.equals(getLocalNameEndElement(xmlEvent));
    }

    private String getLocalNameEndElement(XMLEvent xmlEvent) {
        EndElement endElement = xmlEvent.asEndElement();
        String localName = endElement.getName().getLocalPart();
        return localName;
    }

}
