package com.rsicms.projectshelper.contenttypes.dita;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.parsers.MoStaxParser;
import com.rsicms.projectshelper.parsers.handlers.DitaClassMoStaxParserHandler;

public class DitaUtils {

	public static final String DITA_MAP_TYPE = "map/map";
	public static final String DITA_TOPIC_TYPE = "topic/topic";

	public static boolean isDitaMap(ExecutionContext context, ManagedObject mo)
			throws RSuiteException {
		
		if (mo.isNonXml() || mo.isAssemblyNode()){
			return false;
		}
		
		String classAttribute = getClassAttribute(context, mo);
		return (classAttribute != null && classAttribute
				.contains(DITA_MAP_TYPE));
	}

	public static boolean isDitaTopic(ExecutionContext context, ManagedObject mo)
			throws RSuiteException {
		
		if (mo.isNonXml() || mo.isAssemblyNode()){
			return false;
		}
		
		String classAttribute = getClassAttribute(context, mo);
		return (classAttribute != null && classAttribute
				.contains(DITA_TOPIC_TYPE));

	}

	private static String getClassAttribute(ExecutionContext context,
			ManagedObject mo) throws RSuiteException {	   
		return mo.getElement().getAttribute("class");
	}

}
