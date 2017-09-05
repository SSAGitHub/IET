package org.theiet.rsuite.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.theiet.rsuite.mocks.api.service.ContentAssemblyServiceMock;
import org.theiet.rsuite.mocks.api.service.ManagedObjectServiceMock;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ContentAssemblyCreateOptions;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.AuthorizationService;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;

public class IetUtilsTest {

	@Test
	public void testCreateContentAssembly() throws RSuiteException {
		
		ExecutionContext context = Mockito.mock(ExecutionContext.class);
		
		AuthorizationService authSvcMock = Mockito.mock(AuthorizationService.class);
		
		ContentAssemblyService caSvcMock = new ContentAssemblyServiceMock();
		ManagedObjectService moSvcMock = new ManagedObjectServiceMock();
		
		Mockito.when(context.getContentAssemblyService()).thenReturn(caSvcMock);
		Mockito.when(context.getManagedObjectService()).thenReturn(moSvcMock);
		Mockito.when(context.getAuthorizationService()).thenReturn(authSvcMock);
		
		String type = "bookType";
		String displayName = "Test Display";
		
		ContentAssembly ca = ProjectContentAssemblyUtils.createContentAssembly(context, "1111", displayName, type);
		
		assertEquals(type, ca.getType());
		assertEquals(displayName, ca.getDisplayName());
		
	}
	

	/**
	 * Creates a new content assembly with a specific type
	 * @param context the execution context
	 * @param parentId The rsuite id of parent container
	 * @param displayName new CA display name 
	 * @param type new CA type
	 * @return newly created CA
	 * @throws RSuiteException
	 */
	public static ContentAssembly createContentAssembly(ExecutionContext context, String parentId, String displayName, String type) throws RSuiteException{
		User user = context.getAuthorizationService().getSystemUser();
		ContentAssemblyService caService = context.getContentAssemblyService();
		ContentAssemblyCreateOptions options = new ContentAssemblyCreateOptions();
		if (type != null){
			options.setType(type);
		}
		
		return caService.createContentAssembly(user, parentId, displayName,options);
	}
}
