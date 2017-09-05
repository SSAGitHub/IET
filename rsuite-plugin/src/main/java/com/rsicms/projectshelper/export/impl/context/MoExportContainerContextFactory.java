package com.rsicms.projectshelper.export.impl.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.repository.ComposedXQuery;
import com.reallysi.rsuite.service.RepositoryService;
import com.rsicms.projectshelper.export.MoExportContainerContext;

public class MoExportContainerContextFactory {

	public static MoExportContainerContext createMoExportContainerContext(
			ExecutionContext context, String caId) throws RSuiteException {

		try {
			String queryResult = getContextFromDatabase(context, caId);
			MoExportContextXqueryResultParser parser = new MoExportContextXqueryResultParser();
			return parser.parseXqueryResult(caId, queryResult);
		} catch (IOException e) {
			throw new RSuiteException(0, "Unable to obtain context for ca "
					+ caId, e);
		} catch (RSuiteException e) {
			throw new RSuiteException(0, "Unable to obtain context for ca "
					+ caId, e);
		} catch (XMLStreamException e) {
			throw new RSuiteException(0, "Unable to obtain context for ca "
					+ caId, e);
		}
	}

	private static String getContextFromDatabase(ExecutionContext context,
			String caId) throws IOException, RSuiteException {
		RepositoryService repositoryService = context.getRepositoryService();
		String xquery = MoExportContextXqueryBuilder
				.createContextXquery(caId);
		ComposedXQuery composeQuery = repositoryService.getQueryBuilder().composeQuery(xquery, new HashMap<String, String>());
		return repositoryService.queryAsStringArray(composeQuery)[0];
	}
	
	public static MoExportContainerContext createMoExportContainerContext(
			ExecutionContext context, List<String> caIds) throws RSuiteException {

		List<MoExportContainerContext> contextsList = new ArrayList<MoExportContainerContext>();
		String caIdContext = "";
		try {
			
			for (String caId : caIds){
				caIdContext = caId;
				MoExportContainerContext moExportContext = createMoExportContainerContext(context, caId);
				contextsList.add(moExportContext);
			}
			
			return new MoExportContextBulk(contextsList);			
		} catch (RSuiteException e) {
			throw new RSuiteException(0, "Unable to obtain context for ca "
					+ caIdContext, e);
		} 
	}
}
