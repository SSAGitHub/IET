package org.theiet.rsuite.books.domain;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.domain.TypesetterIngestionHelper;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.pubtrack.Process;
import com.reallysi.rsuite.api.workflow.FileWorkflowObject;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.AuthorizationService;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.reallysi.rsuite.service.PubtrackManager;

public class BookTypesetterIngestionHelperTest {

	@Test
	public void initialTypesetterIngestion() throws Exception{
		
		final Map<String, String> variablesMap = new HashMap<String, String>();
		
		WorkflowExecutionContext contextMock = mock(WorkflowExecutionContext.class);

		FileWorkflowObject fileWorfklowObjectStub = mock(FileWorkflowObject.class);
		Log logStub = mock(Log.class);

		
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {				
				String name = invocation.getArguments()[0].toString();
				
				String value =  invocation.getArguments()[1] == null ? "" : invocation.getArguments()[1].toString();
				variablesMap.put(name, value);
				return null;
			}
		}).when(contextMock).setVariable(anyString(), anyObject());
		
		
		when(contextMock.getFileWorkflowObject())
				.thenReturn(fileWorfklowObjectStub);
		when(contextMock.getWorkflowLog()).thenReturn(logStub);
		
		ContentAssemblyService caServiceStub = mock(ContentAssemblyService.class);
		
		String dummyProductCaId = "111";
		
		ContentAssembly productCaStub = mock(ContentAssembly.class);
		ManagedObjectService moServiceStub = mock(ManagedObjectService.class);
		PubtrackManager pubtrackManagerStub = mock(PubtrackManager.class);
		AuthorizationService authorizationStub = mock(AuthorizationService.class);
		
		List<Process> processList = new ArrayList<Process>();
		processList.add(mock(Process.class));
		when(pubtrackManagerStub.getProcessByExternalId(any(User.class), anyString())).thenReturn(processList);
		

		
		when(caServiceStub.getContentAssembly(any(User.class), eq(dummyProductCaId))).thenReturn(productCaStub);
		
		when(contextMock.getContentAssemblyService()).thenReturn(caServiceStub);
		when(contextMock.getManagedObjectService()).thenReturn(moServiceStub);
		
		when(contextMock.getPubtrackManager()).thenReturn(pubtrackManagerStub);
		
		when(contextMock.getAuthorizationService()).thenReturn(authorizationStub);
		
		
		TypesetterIngestionHelper ingestionHelper = new BookTypesetterIngestionHelperStub(contextMock);
		ingestionHelper.loadTypesetterFiles();
		
		String submisstionType = variablesMap.get(IetConstants.WF_VAR_SUBMISSION_TYPE);
		assertEquals("initial", submisstionType);
		
		
	}
	
	@Test
	public void testSubmissionType() throws RSuiteException{
	
		WorkflowExecutionContext contextMock = mock(WorkflowExecutionContext.class);
		ContentAssembly productCaStub = mock(ContentAssembly.class);
		
		AuthorizationService authorizationStub = mock(AuthorizationService.class);
		ManagedObjectService moServiceStub = mock(ManagedObjectService.class);
		when(contextMock.getManagedObjectService()).thenReturn(moServiceStub);
		
		when(contextMock.getAuthorizationService()).thenReturn(authorizationStub);
		when(productCaStub.getLayeredMetadataValue(BooksConstans.LMD_FIELD_PRINT_PUBLISHED_DATE)).thenReturn("2014-02-01");
		
		
		BookTypesetterIngestionHelper ingestionHelper = new BookTypesetterIngestionHelperStub(contextMock);
		
		
		
		
		String type = ingestionHelper.obtainTypesetterSubmission(productCaStub);
		
		assertEquals("update", type);
	}
}
