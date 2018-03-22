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
	private static final String RSUITEWORKINGFOLDERPATH = "rsuiteWorkingFolderPath";
	private static final String TEMPFOLDER = "tempFolder";
	private static final String RSUITETEMPDIR = "rsuite.temp.dir";
	private static final String RSUITEWORKFLOWBASEWORKFOLDER = "rsuite.workflow.baseworkfolder";
	private Log logger = LogFactoryImpl.getLog(getClass());

	@Override
	public void handleEvent(ExecutionContext context, Event event, Object handback) throws RSuiteException {

		String eventType = event.getType();

		logger.info("**** WorkFlowCompletionEventHandler started  handleEvent eventType = " + eventType + "****");

		if (EVENT_WORKFLOW_COMPLETED.equals(eventType) || EVENT_WORKFLOW_KILLED.equals(eventType)) {
			WorkflowEventData workflowEventData = (WorkflowEventData) event.getUserData();


			String workFlowDataTempStr = context.getConfigurationProperties().getProperty(RSUITETEMPDIR, "");
			File workFlowDataTempDir = new File(workFlowDataTempStr);

			// Look for working folder path variable - this is the out of the box workflow work folder 
			String processInstanceId = workflowEventData.getProcessInstanceId();
			String workFlowFileLocationStr = (String) getWorkflowVariable(context, processInstanceId,RSUITEWORKINGFOLDERPATH);

			if (workFlowFileLocationStr != null) {

				File workFlowRootDir = getCurrentWorkFlowRootFolder(context, workFlowFileLocationStr); 
				moveWorkFlowFolderToTrash(workFlowDataTempDir, processInstanceId, workFlowRootDir);
			}
			
			// Look for the tempFolder variable - used in some workflows
			File tempFolderLocation = (File) getWorkflowVariable(context, processInstanceId,TEMPFOLDER);

			if (tempFolderLocation != null) {

				moveWorkFlowFolderToTrash(workFlowDataTempDir, processInstanceId, tempFolderLocation);
			}
			
		}

	}


	protected WorkflowTrashUtils getWorkflowTrashUtils() {
		return WORKFLOW_TRASH_UTILS;
	}

	@SuppressWarnings("unchecked")
	protected Object getWorkflowVariable(ExecutionContext context, String processInstanceId,
			String variableToFind) throws RSuiteException {

		com.reallysi.rsuite.api.User user = context.getAuthorizationService().getSystemUser();
		ProcessInstanceInfo processInstance = context.getProcessInstanceService().getProcessInstance(user,processInstanceId);

		List<VariableInfo> variables = processInstance.getVariables();

		for (VariableInfo variable : variables) {
			
			if (variableToFind.equals(variable.getName())) {
				return  variable.getValue();
			}
		}

		return null;
	}

	protected File getCurrentWorkFlowRootFolder(ExecutionContext context, String workFlowFileLocationStr) {
		
		String workFlowBaseStr = context.getConfigurationProperties().getProperty(RSUITEWORKFLOWBASEWORKFOLDER, "");

		int lengthOfBaseWorkFlowFolder = workFlowBaseStr.concat(File.separator).length();
		int endOfworkFlowRootFolder = workFlowFileLocationStr.indexOf(File.separator, lengthOfBaseWorkFlowFolder);
		String workFlowRootDirStr = workFlowFileLocationStr.substring(0, endOfworkFlowRootFolder);

		return new File(workFlowRootDirStr);
	}

	protected void moveWorkFlowFolderToTrash(File workFlowDataTempDir, String processId, File workFlowRootDir)
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
