package com.rsicms.projectshelper.export.impl.context;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.repository.ComposedXQuery;
import com.reallysi.rsuite.api.repository.QueryBuilder;
import com.reallysi.rsuite.service.RepositoryService;

public class MoExportContextTestHelper {

	public static ExecutionContext createExecutionContextMock() throws IOException,
			RSuiteException {
		ExecutionContext context = mock(ExecutionContext.class);
		RepositoryService repositorySvc = mock(RepositoryService.class);
		
		
		File mockResultFile = new File(MoExportContextFactory.TEST_EXPORT_MO_CONTEXT_QUERY_RESULT);
		String resultEntry = FileUtils.readFileToString(mockResultFile);
		
		String[] queryResult = {resultEntry}; 
		when(context.getRepositoryService()).thenReturn(repositorySvc);
		when(repositorySvc.queryAsStringArray(any(ComposedXQuery.class))).thenReturn(queryResult);
		
		QueryBuilder queryBuilterStub = mock(QueryBuilder.class);
		when(repositorySvc.getQueryBuilder()).thenReturn(queryBuilterStub);
		
		return context;
	}

}
