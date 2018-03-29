package org.theiet.rsuite.eventhandlers;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.theiet.rsuite.utils.WorkflowUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.event.DefaultEventHandler;
import com.reallysi.rsuite.api.event.Event;
import com.reallysi.rsuite.api.event.events.WorkflowEventData;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.ProcessInstanceInfo;
import com.rsicms.projectshelper.workflow.WorkflowTrashUtils;

public class WorkFlowCompletionEventHandler extends DefaultEventHandler {

	private static final String EVENT_WORKFLOW_KILLED = "workflow.process.aborted";
	private static final String EVENT_WORKFLOW_COMPLETED = "workflow.process.completed";
	public WorkflowTrashUtils WORKFLOW_TRASH_UTILS = new WorkflowTrashUtils();
	private static final String RSUITE_WORKING_FOLDER_PATH = "rsuiteWorkingFolderPath";
	private static final String TEMP_FOLDER = "tempFolder";
	private static final String RSUITE_TEMPDIR = "rsuite.temp.dir";
	private static final String RSUITE_WORKFLOW_BASE_WORK_FOLDER = "rsuite.workflow.baseworkfolder";
	private Log logger = LogFactoryImpl.getLog(getClass());

	@Override
	public void handleEvent(ExecutionContext context, Event event, Object handback) throws RSuiteException {

		String eventType = event.getType();

		logger.info("**** WorkFlowCompletionEventHandler started  handleEvent eventType = " + eventType + "****");

		if (EVENT_WORKFLOW_COMPLETED.equals(eventType) || EVENT_WORKFLOW_KILLED.equals(eventType)) {
			WorkflowEventData workflowEventData = (WorkflowEventData) event.getUserData();

			String workFlowDataTempStr = context.getConfigurationProperties().getProperty(RSUITE_TEMPDIR, "");
			File workFlowDataTempDir = new File(workFlowDataTempStr);

			String processInstanceId = workflowEventData.getProcessInstanceId();
			String workFlowFileLocationStr = (String) getWorkflowVariable(context, processInstanceId,
					RSUITE_WORKING_FOLDER_PATH);

			if (workFlowFileLocationStr != null) {

				File workFlowRootDir = getCurrentWorkFlowRootFolder(context, workFlowFileLocationStr);
				moveWorkFlowFolderToTrash(workFlowDataTempDir, processInstanceId, workFlowRootDir);
			}

			File tempFolderLocation = (File) getWorkflowVariable(context, processInstanceId, TEMP_FOLDER);

			if (tempFolderLocation != null) {

				moveWorkFlowFolderToTrash(workFlowDataTempDir, processInstanceId, tempFolderLocation);
			}

		}

	}

	protected WorkflowTrashUtils getWorkflowTrashUtils() {
		return WORKFLOW_TRASH_UTILS;
	}

	protected Object getWorkflowVariable(ExecutionContext context, String processInstanceId, String variableToFind)
			throws RSuiteException {

		User user = context.getAuthorizationService().getSystemUser();
		ProcessInstanceInfo processInstance = context.getProcessInstanceService().getProcessInstance(user,
				processInstanceId);

		return WorkflowUtils.getWorkflowVariable(processInstance, variableToFind);
	}

	protected File getCurrentWorkFlowRootFolder(ExecutionContext context, String workFlowFileLocationStr) {

		String workFlowBaseStr = context.getConfigurationProperties().getProperty(RSUITE_WORKFLOW_BASE_WORK_FOLDER, "");

		int lengthOfBaseWorkFlowFolder = workFlowBaseStr.concat(File.separator).length();
		int endOfworkFlowRootFolder = workFlowFileLocationStr.indexOf(File.separator, lengthOfBaseWorkFlowFolder);
		String workFlowRootDirStr = workFlowFileLocationStr.substring(0, endOfworkFlowRootFolder);

		return new File(workFlowRootDirStr);
	}

	protected void moveWorkFlowFolderToTrash(File workFlowDataTempDir, String processId, File workFlowRootDir)
			throws RSuiteException {

		try {
			if (workFlowRootDir.exists()) {
				getWorkflowTrashUtils().moveFolderToTrash(workFlowDataTempDir, processId, workFlowRootDir);
			} else {
				logger.error(
						"Unable clean up workflow folder. Folder doesn't exists: " + workFlowRootDir.getAbsolutePath());
			}
		} catch (Exception e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR, "Exception " + e.toString(), e);
		}
	}

}
