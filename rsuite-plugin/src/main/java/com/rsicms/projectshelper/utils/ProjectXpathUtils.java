package com.rsicms.projectshelper.utils;

import org.w3c.dom.Element;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.xml.XPathEvaluator;

public class ProjectXpathUtils {

	private ProjectXpathUtils(){
	}

	public static String getElementValueFromMo(ExecutionContext context, ManagedObject mo,
			String xpath) throws RSuiteException {
		XPathEvaluator xpathEval = context.getXmlApiManager()
				.getXPathEvaluator();
		
		return xpathEval.executeXPathToString(xpath, mo.getElement());
	}
	
	public static Element getElementFromMo(ExecutionContext context, ManagedObject mo,
			String xpath) throws RSuiteException {
		XPathEvaluator xpathEval = context.getXmlApiManager()
				.getXPathEvaluator();

		return (Element) xpathEval.executeXPathToNode(xpath, mo.getElement());
	}

}
