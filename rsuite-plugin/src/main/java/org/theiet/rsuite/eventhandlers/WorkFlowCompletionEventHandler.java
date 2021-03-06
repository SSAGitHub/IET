package org.theiet.rsuite.eventhandlers;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.event.DefaultEventHandler;
import com.reallysi.rsuite.api.event.Event;
import com.reallysi.rsuite.api.event.events.WorkflowEventData;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.ProcessInstanceInfo;
import com.rsicms.projectshelper.workflow.WorkflowTrashUtils;
import com.reallysi.rsuite.api.workflow.VariableInfo;

public class WorkFlowCompletionEventHandler extends DefaultEventHandler {

	private static final String EVENT_WORKFLOW_KILLED = "workflow.process.aborted";
	private static final String EVENT_WORKFLOW_COMPLETED = "workflow.process.completed";
	public WorkflowTrashUtils WORKFLOW_TRASH_UTILS = new WorkflowTrashUtils();
	private static final String REQUIRED_VARIABLE_NAME = "rsuiteWorkingFolderPath";
	private Log logger = LogFactoryImpl.getLog(getClass());

	@Override
	public void handleEvent(ExecutionContext context, Event event, Object handback) throws RSuiteException {

		String eventType = event.getType();

		logger.info("WorkFlowCompletionEventHandler started  handleEvent eventType = " + eventType);

		if (EVENT_WORKFLOW_COMPLETED.equals(eventType) || EVENT_WORKFLOW_KILLED.equals(eventType)) {
			WorkflowEventData workflowEventData = (WorkflowEventData) event.getUserData();

			String processInstanceId = workflowEventData.getProcessInstanceId();
			String workFlowFileLocationStr = getWorkflowFileLocation(context, processInstanceId,
					REQUIRED_VARIABLE_NAME);

			if (workFlowFileLocationStr != null) {

				String workFlowDataTempStr = context.getConfigurationProperties().getProperty("rsuite.temp.dir", "");
				String workFlowBaseStr = context.getConfigurationProperties()
						.getProperty("rsuite.workflow.baseworkfolder", "");

				// get the length of the workflow base directory + the file separator from the
				// config.
				int start = workFlowBaseStr.concat(File.separator).length();
				// find the next file separator which will be the absolute path to the workflow
				// root directory
				int end = workFlowFileLocationStr.indexOf(File.separator, start);

				// chop the chunk out that represents the workflow root directory
				String workFlowRootDirStr = workFlowFileLocationStr.substring(0, end);

				File workFlowDataTempDir = new File(workFlowDataTempStr);
				File workFlowRootDir = new File(workFlowRootDirStr);

				processWorkFlowFile(workFlowDataTempDir, processInstanceId, workFlowRootDir);

			}
		}

		super.handleEvent(context, event, handback);
	}

	protected WorkflowTrashUtils getWorkflowTrashUtils() {
		return WORKFLOW_TRASH_UTILS;
	}

	@SuppressWarnings("unchecked")
	protected String getWorkflowFileLocation(ExecutionContext context, String processInstanceId,
			String requiredVariableName) throws RSuiteException {

		com.reallysi.rsuite.api.User user = context.getAuthorizationService().getSystemUser();
		ProcessInstanceInfo processInstance = context.getProcessInstanceService().getProcessInstance(user,
				processInstanceId);

		List<VariableInfo> variables = processInstance.getVariables();

		for (VariableInfo variable : variables) {
			if (requiredVariableName.equals(variable.getName())) {
				return (String) variable.getValue();
			}
		}

		return null;
	}

	protected void processWorkFlowFile(File workFlowDataTempDir, String processId, File workFlowRootDir)
			throws RSuiteException {

		try {
			// make sure the workflow root directory actually exists
			if (workFlowRootDir.exists()) {
				getWorkflowTrashUtils().moveFolderToTrash(workFlowDataTempDir, processId, workFlowRootDir);
			}else {
				logger.error("Unable clean up workflow folder. Folder doesn't exists: " + workFlowRootDir.getAbsolutePath());
			}
		} catch (Exception e) {
			throw new RSuiteException("Exception " + e.toString());
		}
	}

}
