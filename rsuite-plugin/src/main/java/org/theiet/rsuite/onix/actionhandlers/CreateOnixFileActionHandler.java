package org.theiet.rsuite.onix.actionhandlers;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

import org.theiet.rsuite.onix.OnixConstants;
import org.theiet.rsuite.onix.domain.*;
import org.theiet.rsuite.onix.utils.OnixUtils;
import org.theiet.rsuite.utils.WorkflowUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class CreateOnixFileActionHandler extends
		AbstractNonLeavingActionHandler implements OnixConstants {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public void executeTask(WorkflowExecutionContext context) throws Exception {

		context.getWorkflowLog().info("Generating result ONIX file...");
		
		ManagedObjectService moSvc = context.getManagedObjectService();
		User user = context.getAuthorizationService().getSystemUser();

		OnixConfiguration onixConfiguration = new OnixConfiguration(context);
		String recipientName = context.getVariable(WF_VAR_RECIPIENT_NAME);
		OnixProcessor onixProcessor = OnixUtils.createProcessor(context,
				onixConfiguration, recipientName);

		Map<String, String> onixUnits = (Map<String, String>) context
				.getVariableAsObject(WF_VAR_ONIX_UNITS);
		for (Entry<String, String> onixUnitEntity : onixUnits.entrySet()) {
			String onixMoId = onixUnitEntity.getKey();
			String bookCaItemId = onixUnitEntity.getValue();
			ManagedObject onixMO = moSvc.getManagedObject(user, onixMoId);
			ContentAssemblyItem bookCaItem = ProjectContentAssemblyUtils.getCAItem(
					context, user, bookCaItemId);
			OnixUnit onixUnit = new OnixUnit(onixMO, bookCaItem);
			onixProcessor.addOnixToProcess(onixUnit);
		}

		String mergedOnixContent = onixProcessor.createOnixFile();
		File mergedOnixFile = WorkflowUtils.writeWorflowTempFile(context,
				ONIX_SUGGESTED_FILE_NAME, mergedOnixContent);
		context.setVariable(WF_VAR_MERGED_FILE, mergedOnixFile);

	}

}