package org.theiet.tools.schema;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.FileUtils;


public class AddDefaultNamespaceToSchema {

    private static final String[] attributesBame = {"base", "ref", "itemType", "type"};

    private static final String UTF_8 = "utf-8";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

    /** Event factory */
    private XMLEventFactory eventFactory = XMLEventFactory.newInstance();

    private OutputStream outStream;

    private InputStream inStream;

    public AddDefaultNamespaceToSchema(File schemaFileToFix) throws IOException {
        File tempInputFile =
                new File(schemaFileToFix.getParent(), schemaFileToFix.getName() + "_"
                        + dateFormat.format(new Date()));
        FileUtils.moveFile(schemaFileToFix, tempInputFile);
        this.outStream = new FileOutputStream(schemaFileToFix);
        this.inStream = new FileInputStream(tempInputFile);
    }


    public AddDefaultNamespaceToSchema(OutputStream outStream, InputStream inStream) {
        this.outStream = outStream;
        this.inStream = inStream;
    }


    public void addDefaultNamespace(List<NamespaceToAdd> namespacesToAdd) throws Exception {

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader reader = factory.createXMLEventReader(inStream, UTF_8);
            XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
            XMLEventWriter xmlw = xmlof.createXMLEventWriter(outStream, UTF_8);

            copyDocument(reader, xmlw, namespacesToAdd);
            xmlw.close();
        } catch (XMLStreamException e) {
            throw new Exception(e);
        }

    }

    /**
     * @param reader - the document reader
     * @param writer - the document writer
     **/
    private void copyDocument(XMLEventReader reader, XMLEventWriter writer,
            List<NamespaceToAdd> namespacesToAdd) throws Exception {

        boolean isRootElement = true;

        String defaultNamespacePrefix = getDefaultNamespacePrefix(namespacesToAdd);

        XMLEvent event = reader.peek();

        while (!event.isEndDocument()) {
            event = reader.nextEvent();
            boolean copyEvent = true;
            if (writer != null) {



                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();

                    if (isRootElement) {
                        addNamespaces(writer, namespacesToAdd, startElement);
                        copyEvent = false;
                        isRootElement = false;
                    }


                    for (String attName : attributesBame) {
                        Attribute baseAtt = startElement.getAttributeByName(new QName(attName));
                        if (baseAtt != null) {
                            copyEvent =
                                    updateReference(writer, startElement, defaultNamespacePrefix,
                                            baseAtt);
                        }
                    }
                }


                if (copyEvent) {
                    writer.add(event);
                }
            }
        }
    }



    private String getDefaultNamespacePrefix(List<NamespaceToAdd> namespacesToAdd) throws Exception {
        for (NamespaceToAdd namespace : namespacesToAdd) {

            if (namespace.isDefaultNamespace()) {
                return namespace.getPrefix();
            }

        }
        throw new Exception("Default namespaces is not provided");
    }


    @SuppressWarnings("unchecked")
    private void addNamespaces(XMLEventWriter writer, List<NamespaceToAdd> namespacesToAdd,
            StartElement startElement) throws XMLStreamException {
        QName qname = startElement.getName();
        XMLEvent startElementNew =
                eventFactory.createStartElement(qname.getPrefix(), qname.getNamespaceURI(),
                        qname.getLocalPart());

        writer.add(startElementNew);
        Iterator<Attribute> attributes = startElement.getAttributes();

        while (attributes.hasNext()) {
            Attribute attribute = (Attribute) attributes.next();
            writer.add(attribute);
        }

        Iterator<Namespace> namespaces = startElement.getNamespaces();
        while (namespaces.hasNext()) {
            Namespace namespace = (Namespace) namespaces.next();
            writer.add(namespace);
        }

        for (NamespaceToAdd namespaceToAdd : namespacesToAdd) {

            Namespace namespace =
                    eventFactory.createNamespace(namespaceToAdd.getPrefix(),
                            namespaceToAdd.getUri());
            writer.add(namespace);

            if (namespaceToAdd.isDefaultNamespace()) {
                writer.add(eventFactory.createAttribute("targetNamespace", namespaceToAdd.getUri()));
                writer.add(eventFactory.createAttribute("elementFormDefault", "qualified"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    /**
     * Update navtitle attribute with the latest referenced topic title. 
     */
    private boolean updateReference(XMLEventWriter writer, StartElement startElement,
            String defaultNamespacePrefix, Attribute attributeToUpdate) throws XMLStreamException {
        boolean copyEvent = true;



        String value = attributeToUpdate.getValue();

        if (!value.contains(":")) {
            QName qname = startElement.getName();
            XMLEvent startElementNew =
                    eventFactory.createStartElement(qname.getPrefix(), qname.getNamespaceURI(),
                            qname.getLocalPart());

            writer.add(startElementNew);
            Iterator<Attribute> attributes = startElement.getAttributes();

            while (attributes.hasNext()) {
                Attribute attribute = (Attribute) attributes.next();

                QName attributeQname = attribute.getName();

                if (attributeToUpdate.getName().getLocalPart()
                        .equals(attributeQname.getLocalPart())) {
                    attribute =
                            eventFactory.createAttribute(attributeQname.getPrefix(),
                                    attributeQname.getNamespaceURI(),
                                    attributeQname.getLocalPart(), defaultNamespacePrefix + ":"
                                            + attribute.getValue());
                } else {
                    attribute =
                            eventFactory.createAttribute(attributeQname.getPrefix(),
                                    attributeQname.getNamespaceURI(),
                                    attributeQname.getLocalPart(), attribute.getValue());
                }

                copyEvent = false;
                writer.add(attribute);
            }
        }


        return copyEvent;
    }

}
