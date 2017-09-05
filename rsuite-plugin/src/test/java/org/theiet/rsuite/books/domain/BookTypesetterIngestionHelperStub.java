package org.theiet.rsuite.books.domain;

import org.mockito.Mockito;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;

public class BookTypesetterIngestionHelperStub extends BookTypesetterIngestionHelper {

	public BookTypesetterIngestionHelperStub(WorkflowExecutionContext context) {
		super(context);
	}

	@Override
	protected String obtainProductCAId(String productCode)
			throws RSuiteException {
		return "111";		
	}
	
	@Override
	protected ContentAssembly obtainTypesetterCA(ContentAssembly productCA)
			throws RSuiteException {
		
		return Mockito.mock(ContentAssembly.class);
	}
	
	@Override
	protected void loadFilesToRSuite(ContentAssembly typesetterAssembly)
			throws Exception {
	}
}
