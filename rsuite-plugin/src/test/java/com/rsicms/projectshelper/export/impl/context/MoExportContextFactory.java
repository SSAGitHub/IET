package com.rsicms.projectshelper.export.impl.context;


import java.io.IOException;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

//TODO complete test
public class MoExportContextFactory {

	private static final String TEST_ONIX_BASE_PATH = "src/test/resources/com/rsicms/projectshelper/export/impl/context/";
		
	static final String TEST_EXPORT_MO_CONTEXT_QUERY_RESULT = TEST_ONIX_BASE_PATH + "exportContextQueryResult.xml";
	
	
	public void test_createMoExportContext() throws RSuiteException, IOException {
		
		ExecutionContext context = MoExportContextTestHelper.createExecutionContextMock();
		
		MoExportContainerContextFactory.createMoExportContainerContext(context, "101");
	}

}
