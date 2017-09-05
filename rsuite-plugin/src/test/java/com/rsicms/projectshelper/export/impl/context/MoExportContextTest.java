package com.rsicms.projectshelper.export.impl.context;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.export.MoExportContainerContext;

public class MoExportContextTest {

	private static MoExportContainerContext moExportContext;

	@BeforeClass
	public static void before() throws RSuiteException, IOException {
		ExecutionContext context = MoExportContextTestHelper
				.createExecutionContextMock();
		moExportContext = MoExportContainerContextFactory.createMoExportContainerContext(context,
				"101");
	}

	@Test
	public void test_getContextMoVersion_pinnedVersion() throws IOException,
			RSuiteException {
		String id = "107798";

		ManagedObject moStub = createMoStub(id);

		String contextMoVersion = moExportContext.getContextMoVersion(moStub);
		assertEquals("2.0", contextMoVersion);
	}
	
	@Test
	public void test_getContextMoVersion_moReference_pinnedVersion() throws IOException,
			RSuiteException {
		String id = "107887";

		ManagedObject moStub = createMoStub(id);
		when(moStub.getTargetId()).thenReturn("107798");

		String contextMoVersion = moExportContext.getContextMoVersion(moStub);
		assertEquals("2.0", contextMoVersion);
	}
	
	@Test
	public void test_getContextMoVersion_notPinnedVersion() throws IOException,
			RSuiteException {
		String id = "107796";

		ManagedObject moStub = createMoStub(id);

		String contextMoVersion = moExportContext.getContextMoVersion(moStub);
		assertEquals(null, contextMoVersion);
	}
	
	@Test
	public void test_getContextPath_pinnedVersion() throws IOException,
			RSuiteException {
		String id = "107798";

		ManagedObject moStub = createMoStub(id);

		String path = moExportContext.getContextPath(moStub);
		assertEquals("/Typescript/Part 2/", path);
	}

	public ManagedObject createMoStub(String id) {
		ManagedObject managedObjectStub = mock(ManagedObject.class);
		when(managedObjectStub.getId()).thenReturn(id);
		return managedObjectStub;
	}

}
