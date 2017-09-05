package com.rsicms.test;

import java.util.List;

import javax.xml.xpath.*;

import org.w3c.dom.Node;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.xml.Namespace;
import com.reallysi.rsuite.api.xml.XPathEvaluator;

public class XPathEvalutorForTest implements XPathEvaluator {

	private XPath xpath = XPathFactory.newInstance().newXPath();
	
	@Override
	public void addNamespaceDeclaration(Namespace arg0) throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addNamespaceDeclaration(String arg0, String arg1) throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addNamespaceDeclarations(List<Namespace> arg0) throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addNamespaceDeclarations(Namespace[] arg0) throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean executeXPathToBoolean(String arg0, Object arg1) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node executeXPathToNode(String arg0, Object arg1) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node[] executeXPathToNodeArray(String arg0, Object arg1) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Number executeXPathToNumber(String arg0, Object arg1) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String executeXPathToString(String expression, Object arg1) throws RSuiteException {
		try {
			return (String) xpath.evaluate(expression, arg1, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		}
	}

	@Override
	public String[] executeXPathToStringArray(String arg0, Object arg1) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XPathExpression getXPathExpression(String arg0) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

}
