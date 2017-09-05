package org.theiet.rsuite.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Node;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.repository.ComposedXQuery;

public class XqueryUtils {

	public static Node searchByTopic(ExecutionContext context, String topic)
			throws RSuiteException {

		Map<String, String> variables = new HashMap<String, String>();
		variables.put(
				"phrase",
				topic);

		ComposedXQuery xqueryObject = getXquery(
				"searchByTopic.xqy",
				variables);

		return context.getRepositoryService().queryAsNode(
				xqueryObject);
	}

	public static Node contentStyle(ExecutionContext context)
			throws RSuiteException {

		Map<String, String> variables = new HashMap<String, String>();

		ComposedXQuery xqueryObject = getXquery(
				"contentStyleReport.xqy",
				variables);
		return context.getRepositoryService().queryAsNode(
				xqueryObject);
	}

	public static ComposedXQuery getXquery(
			String name,
			Map<String, String> variables) throws RSuiteException {
		ComposedXQuery xqueryObject = null;
		try {
			InputStream is = XqueryUtils.class
					.getResourceAsStream("/xquery/" + name);
			
			if(is == null){
				is = XqueryUtils.class
						.getResourceAsStream("xquery/" + name);
			}
			
			
			String queryContent = IOUtils.toString(is);

			xqueryObject = new ComposedXQuery(
					queryContent,
					variables);
		} catch (IOException e) {
			throw new RSuiteException(
					0,
					"Unable to load xquery",
					e);
		}
		return xqueryObject;
	}

}