package org.theiet.rsuite.utils;

import java.io.Writer;
import java.util.Map;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class StaxUtils
{

  public static XMLStreamWriter setUpWriter(Writer writer)
      throws FactoryConfigurationError, XMLStreamException
  {
    // Get an XML Output Factory and configure it:
    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    // Turn on automatic generation of namespace declarations:
    factory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, true);
    
    // Create an XML writer using our previously-created Writer object:
    XMLStreamWriter xmlWriter = factory.createXMLStreamWriter(writer);
    return xmlWriter;
  }

  public static void writeElementWithValue(XMLStreamWriter xmlWriter, String elementName, String elementValue) throws XMLStreamException{
    xmlWriter.writeStartElement(elementName);
    if (elementValue != null ){
      xmlWriter.writeCharacters(elementValue);
    }
    xmlWriter.writeEndElement();
  }
  
  public static void writeElementWithValue(XMLStreamWriter xmlWriter, String elementName, String elementValue, Map<String, String> attributes) throws XMLStreamException{
    xmlWriter.writeStartElement(elementName);
    if (attributes != null){
      for (String key : attributes.keySet()){
        String value = attributes.get(key);
        xmlWriter.writeAttribute(key, value == null ? "" : value);
      }
    }
    
    if (elementValue != null ){
      xmlWriter.writeCharacters(elementValue);
    }
    
    xmlWriter.writeEndElement();
  }

}
