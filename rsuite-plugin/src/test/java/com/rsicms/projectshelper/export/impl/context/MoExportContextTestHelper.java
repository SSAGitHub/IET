package com.rsicms.projectshelper.export.impl.context;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.RepositoryService;

public class MoExportContextTestHelper {

	public static ExecutionContext createExecutionContextMock() throws IOException,
			RSuiteException {
		ExecutionContext context = mock(ExecutionContext.class);
		RepositoryService repositorySvc = mock(RepositoryService.class);
		
		File mockResultFile = new File(MoExportContextFactory.TEST_EXPORT_MO_CONTEXT_QUERY_RESULT);
		String mockQueryResult = FileUtils.readFileToString(mockResultFile);
		
		when(context.getRepositoryService()).thenReturn(repositorySvc);
		when(repositorySvc.queryAsString(anyString())).thenReturn(mockQueryResult);
		return context;
	}

}
