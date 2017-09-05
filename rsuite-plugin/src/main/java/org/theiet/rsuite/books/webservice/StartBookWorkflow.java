package org.theiet.rsuite.books.webservice;

import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.utils.WorkflowUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;

public class StartBookWorkflow extends ProjectRemoteApiHandler implements
		BooksConstans {
	
	private static final String DIALOG_TITLE = "Start Book Workflow";

	@Override
	protected String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {
		String caId = args.getFirstValue("rsuiteId");
		
			User user = context.getAuthorizationService().getSystemUser();

			if (!WorkflowUtils.isAlreadyInWorkflow(caId, "")) {
				WorkflowUtils.startWorkflow(context, user, caId, WF_PD_PREPARE_BOOK);
				setUpInitialLmd(context, caId, user);
			} else {
				ContentAssembly bookCA = context.getContentAssemblyService()
						.getContentAssembly(user, caId);
				return bookCA.getDisplayName() + " (" + caId
						+ ") is already in workflow.";
			}
		
		return "Workflow for Book (ID: " + caId
						+ ") has successfully started.";
	}
	

	private void setUpInitialLmd(RemoteApiExecutionContext context,
			String caId, User user) throws RSuiteException {
		ManagedObjectService moSvc = context.getManagedObjectService();
		MetaDataItem typesetterUpdateType = new MetaDataItem(JournalConstants.LMD_FIELD_TYPESETTER_UPDATE_TYPE, LMD_VALUE_WORKFLOW_STARTED);
		moSvc.setMetaDataEntry(user, caId, typesetterUpdateType);
	}

	@Override
	protected boolean refreshParent() {
		return true;
	}
	
	@Override
	protected String getDialogTitle() {
		return DIALOG_TITLE;
	}
	

}
