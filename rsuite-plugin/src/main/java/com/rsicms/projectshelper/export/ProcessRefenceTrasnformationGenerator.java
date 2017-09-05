package com.rsicms.projectshelper.export;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.reallysi.rsuite.api.RSuiteException;

public class ProcessRefenceTrasnformationGenerator {

	public String generateProcessRefenceTransformationDTD(Map<String, String> namespaceDeclarations,
			List<String> referenceXpaths) throws RSuiteException {

		Document transformationDocument = createTransformationDocument(namespaceDeclarations, referenceXpaths);

		return serializeTransformationDocumentToString(transformationDocument);
	}

	public String generateProcessRefenceTransformationXSD(Map<String, String> namespaceDeclarations,
			List<String> referenceXpaths) throws RSuiteException {

		Document transformationDocument = createTransformationDocument(namespaceDeclarations, referenceXpaths);
		removeDoctypeAttributesFromOutputElement(transformationDocument);

		return serializeTransformationDocumentToString(transformationDocument);

	}

	private void removeDoctypeAttributesFromOutputElement(
			Document transformationDocument) {
		NodeList templateElements = transformationDocument
				.getElementsByTagName("xsl:result-document");

		for (int i = 0; i < templateElements.getLength(); i++) {
			Element outputElement = (Element) templateElements.item(i);
			outputElement.removeAttribute("doctype-public");
			outputElement.removeAttribute("doctype-system");
		}

	}

	private String serializeTransformationDocumentToString(
			Document transformationDocument) throws RSuiteException {
		StringWriter stringWriter = new StringWriter();

		serializeDocument(stringWriter, transformationDocument);

		return stringWriter.toString();
	}

	private Document createTransformationDocument(Map<String, String> namespaceDeclarations, List<String> referenceXpaths)
			throws RSuiteException {

		Document transformationDocument = null;

		try {
			transformationDocument = getRefenceTransformationDocument();

			addNamespaceDeclarations(transformationDocument, namespaceDeclarations);
			
			Element refenceTemplateElment = getProcessRefenceTemplateElement(transformationDocument);

			addRefenceXslTemplates(referenceXpaths, transformationDocument,
					refenceTemplateElment);

			removeTemplate(refenceTemplateElment);
		} catch (Exception e) {
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		}
		return transformationDocument;
	}

    private void addNamespaceDeclarations(Document transformationDocument, Map<String, String> namespaceDeclarations) throws RSuiteException {
        if (namespaceDeclarations != null){
            Element documentElement = transformationDocument.getDocumentElement();
            
            for (Entry<String, String> entry : namespaceDeclarations.entrySet()){
                String namespacePrefix = entry.getKey();
                String value = documentElement.getAttribute("xmlns:" + namespacePrefix);
                
                if (StringUtils.isNotBlank(value)){
                    if (!value.equals(entry.getValue())){
                        throw new RSuiteException("Namespace prefix " + namespacePrefix  + " is already used");
                    }else{
                        continue;
                    }
                }
                
                documentElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + namespacePrefix, entry.getValue());
            }
            
        }
    }

	private Document getRefenceTransformationDocument()
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();

		Document transformationDocument = documentBuilder
				.parse(getProcessRefenceTransformationTemplate());

		return transformationDocument;
	}

	private void addRefenceXslTemplates(List<String> referenceXpaths,
			Document transformationDocument, Element refenceTemplateElment) {
		for (String refenceXpath : referenceXpaths) {
			Node newRefenceTemplateNode = createNewRefenceTemplateNode(
					refenceTemplateElment, refenceXpath);
			Node importedRefenceTemplateNode = transformationDocument
					.importNode(newRefenceTemplateNode, true);
			refenceTemplateElment.getParentNode().insertBefore(
					importedRefenceTemplateNode, refenceTemplateElment);

		}
	}

	private void removeTemplate(Element refenceTemplateElment) {
		refenceTemplateElment.getParentNode()
				.removeChild(refenceTemplateElment);
	}

	private void serializeDocument(Writer writer, Document documentToSerialize)
			throws RSuiteException {

		Source xmlSource = new DOMSource(documentToSerialize);
		Result outputTarget = new StreamResult(writer);
		try {
			Transformer transformer = getTransformer();
			transformer.transform(xmlSource, outputTarget);
		} catch (Exception e) {
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		}
	}

	protected Transformer getTransformer()
			throws TransformerConfigurationException,
			TransformerFactoryConfigurationError {
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		return transformer;
	}

	private Node createNewRefenceTemplateNode(Element refenceTemplateElment,
			String refenceXpath) {
		Node newRefenceTemplateNode = refenceTemplateElment.cloneNode(true);
		getMatchAttribute(newRefenceTemplateNode).setNodeValue(refenceXpath);
		return newRefenceTemplateNode;
	}

	private Node getMatchAttribute(Node newRefenceTemplateNode) {
		return newRefenceTemplateNode.getAttributes().getNamedItem("match");
	}

	private Element getProcessRefenceTemplateElement(
			Document transformationDocument) throws RSuiteException {
		NodeList templateElements = transformationDocument
				.getElementsByTagName("xsl:template");

		for (int i = 0; i < templateElements.getLength(); i++) {
			Node templateNode = templateElements.item(i);
			if (templateNode instanceof Element) {
				Element templateElement = (Element) templateNode;
				String matchAttribute = templateElement.getAttribute("match");

				if ("$REFENCE_XPATH".equals(matchAttribute)) {
					return templateElement;
				}
			}
		}

		throw new RSuiteException("Unable to find refence template");
	}

	protected XMLEventReader createStaxReader()
			throws FactoryConfigurationError, XMLStreamException {
		XMLInputFactory inputFactory = XMLInputFactory.newFactory();
		XMLEventReader xmlEventReader = inputFactory
				.createXMLEventReader(getProcessRefenceTransformationTemplate());
		return xmlEventReader;
	}

	protected XMLEventWriter createStaxWriter(Writer stringWriter)
			throws FactoryConfigurationError, XMLStreamException {
		XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
		XMLEventWriter xmlWriter = xmlOutputFactory
				.createXMLEventWriter(stringWriter);
		return xmlWriter;
	}

	protected InputStream getProcessRefenceTransformationTemplate() {
		return getClass()
				.getResourceAsStream(
						"/com/rsicms/projectshelper/resources/export/processRefenceTransformTemplate.xsl");
	}
}
