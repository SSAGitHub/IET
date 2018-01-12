package org.theiet.rsuite.journals.domain.article.manuscript;

import java.util.Arrays;
import java.util.List;

import javax.xml.xpath.*;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.*;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.xml.XPathEvaluator;

public class ManifestXPath {

	private Document manifestDoc;

	private XPathEvaluator xpathEvaluator;
	

	public ManifestXPath(XPathEvaluator xpathEvaluator, Document manifestDoc) {
		this.manifestDoc = manifestDoc;
		this.xpathEvaluator = xpathEvaluator;
	}

	public String getValueFromManifestDocument(String xpath) throws RSuiteException {
		return getValueFromManifestDocument(manifestDoc, xpath);
	}

	public boolean getBooleanValueFromManifestDocument(String xpath) throws RSuiteException {
		String value = getValueFromManifestDocument(manifestDoc, xpath);
		return convertYesNoToBoolean(value);
	}

	public String getValueFromManifestDocument(Node contextNode, String xPath) throws RSuiteException {
		return getValueFromManifestDocument(contextNode, xPath, false);
	}

	private String getValueFromManifestDocument(Node contextNode, String xPath, boolean returnArray)
			throws RSuiteException {

		String value = null;
		if (returnArray) {
			value = "";

			String values[] = xpathEvaluator.executeXPathToStringArray(xPath, contextNode);
			for (int i = 0; i < values.length; i++) {
				value += (i == 0 ? "" : " ") + values[i];
			}
			value = value.replaceAll(" ", ", ");
		} else {
			value = xpathEvaluator.executeXPathToString(xPath, contextNode);
		}

		if (StringUtils.isBlank(value)) {
			value = null;
		}

		return value;
	}

	public Node getNodeFromManifestDocument(String xPath) throws RSuiteException {

		Node value = xpathEvaluator.executeXPathToNode(xPath, manifestDoc);

		if (value == null) {
			throw new RSuiteException("Unable to locate " + xPath + " in the manifest file");
		}

		return value;
	}

	public List<String> getMultiValueFromManifestDocument(String xPath) throws RSuiteException {

		String values[] = xpathEvaluator.executeXPathToStringArray(xPath, manifestDoc);
		if (values != null && values.length > 0) {
			return Arrays.asList(values);
		}

		return null;
	}

	public boolean convertYesNoToBoolean(String value) {
		String normalizedValue = StringUtils.isBlank(value) ? "no" : value.toLowerCase();
		if ("yes".equalsIgnoreCase(normalizedValue)) {
			return true;
		}

		return false;
	}

//	private String executeXPathToString(String expression, Node contextNode) throws RSuiteException {
//		try {
//			XPath xpath = xpathFactory.newXPath();
//			return (String) xpath.evaluate(expression, contextNode, XPathConstants.STRING);
//		} catch (XPathExpressionException e) {
//			throw new RSuiteException(0, e.getMessage(), e);
//		}
//
//	}


//	private String[] executeXPathToStringArray(String expression, Object context) throws RSuiteException {
//		String[] strings = null;
//
//		Node[] nodes = executeXPathToNodeArray(expression, context);
//		if (nodes != null) {
//			strings = new String[nodes.length];
//			for (int i = 0; i < nodes.length; i++) {
//				strings[i] = nodes[i] == null ? null : nodes[i].getTextContent();
//			}
//		}
//		return strings;
//
//	}
//
//	private Node[] executeXPathToNodeArray(String expression, Object context) throws RSuiteException {
//		XPath xPath = xpathFactory.newXPath();
//		try {
//			NodeList nodeList = (NodeList) xPath.evaluate(expression, context, XPathConstants.NODESET);
//			Node[] nodeArray = new Node[nodeList.getLength()];
//			for (int i = 0; i < nodeList.getLength(); i++) {
//				Node node = nodeList.item(i);
//				nodeArray[i] = node;
//			}
//
//			return nodeArray;
//		} catch (XPathExpressionException e) {
//			throw new RSuiteException(0, e.getMessage(), e);
//		}
//
//	}
}
