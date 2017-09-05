package org.theiet.rsuite.onix.actionhandlers;

import java.io.File;

import org.theiet.rsuite.onix.OnixConstants;
import org.theiet.rsuite.onix.domain.OnixUploader;

import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class PersistOnixDeliveryActionHandler extends AbstractNonLeavingActionHandler implements OnixConstants {

	private static final long serialVersionUID = 1L;

	@Override
	public void executeTask(WorkflowExecutionContext context)
			throws Exception {
		

		context.getWorkflowLog().info("Uploading result ONIX file to RSuite...");
		
		String recipientCaId = context.getVariable(WF_VAR_RSUITE_CONTENTS);
		
		File validatedOnixFile = (File)context.getVariableAsObject(WF_VAR_RESULT_FILE);

		OnixUploader.uploadOnixResultFile(context, recipientCaId, validatedOnixFile);
		
		
	}


}