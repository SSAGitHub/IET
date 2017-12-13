package org.theiet.rsuite.eventhandlers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.event.DefaultEventHandler;
import com.reallysi.rsuite.api.event.Event;
import com.reallysi.rsuite.api.event.events.WorkflowEventData;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.ProcessInstanceInfo;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.remote.api.User;
import com.rsicms.projectshelper.workflow.WorkflowTrashUtils;
import com.reallysi.rsuite.api.workflow.VariableInfo;

public class WorkFlowCompletionEventHandler extends DefaultEventHandler {

	public WorkflowTrashUtils WORKFLOW_TRASH_UTILS = new WorkflowTrashUtils();
	private static final String RequiredVariableName = "rsuiteWorkingFolderPath";
	private Log logger = LogFactoryImpl.getLog(getClass());

	@Override
	  public void handleEvent(ExecutionContext context, Event event, Object handback)
	      throws RSuiteException  {

	    String eventType = event.getType();
	    
	    logger.info("WorkFlowCompletionEventHandler started  handleEvent eventType = " + eventType);

	    if ("workflow.process.completed".equals(eventType)
	        || "workflow.process.aborted".equals(eventType)) {
	    	WorkflowEventData workflowEventData = (WorkflowEventData) event.getUserData();

	    	String processInstanceId = workflowEventData.getProcessInstanceId();
	    	String WorkFlowFileLocationStr = getWorkflowFileLocation(context,processInstanceId,RequiredVariableName);
         
	    	// make sure we found a value what we are looking for 
	    	if (WorkFlowFileLocationStr != null) {

	    		String WorkFlowDataTempStr = context.getConfigurationProperties().getProperty("rsuite.temp.dir", "");
	    		String WorkFlowBaseStr = context.getConfigurationProperties().getProperty("rsuite.workflow.baseworkfolder", "");
	      
	    		// get the length of the workflow base directory + the file separator from the config. 
		    	int start = WorkFlowBaseStr.concat(File.separator).length(); 
		     	// find the next file separator which will be the absolute path to the workflow root directory
		    	int end = WorkFlowFileLocationStr.indexOf(File.separator, start);
		    	  
		    	//chop the chunk out that represents the workflow root directory
		    	String WorkFlowRootDirStr = WorkFlowFileLocationStr.substring(0, end);
		    	  
		    	File WorkFlowDataTempDir = new File(WorkFlowDataTempStr);
		    	File WorkFlowRootDir = new File(WorkFlowRootDirStr);
		    	  		
			    logger.info("** WorkFlowDataTempStr = " + WorkFlowDataTempStr + " **");
			    logger.info("** WorkFlowRootDirStr = " + WorkFlowRootDirStr + " **");
		
		  	    processWorkFlowFile(WorkFlowDataTempDir, processInstanceId, WorkFlowRootDir);

	    	}
	    }
	    
	    super.handleEvent(context, event, handback);
	  }

	protected WorkflowTrashUtils getWorkflowTrashUtils() {
		return WORKFLOW_TRASH_UTILS;
	}

	@Override
	public boolean isEventEnabled(Event event) {
		return super.isEventEnabled(event);
	}

	protected String getWorkflowFileLocation(ExecutionContext context,String processInstanceId, String RequiredVariableName)
			throws RSuiteException {

		com.reallysi.rsuite.api.User user = context.getAuthorizationService().getSystemUser();
		ProcessInstanceInfo processInstance = context.getProcessInstanceService().getProcessInstance(user,processInstanceId);

		List<VariableInfo> variables = processInstance.getVariables();

		for (VariableInfo variable : variables) {
			if (RequiredVariableName.equals(variable.getName())) {
				logger.info("**variable " + variable.getName() + " found  value=" + variable.getValue());
				return (String) variable.getValue();
			}
		}

		return null;
	}


	protected void processWorkFlowFile(File WorkFlowDataTempDir, String ProcessId, File WorkFlowRootDir)
			throws RSuiteException {

		try {
			// make sure the workflow root directory actually exists
			if (WorkFlowRootDir.exists()) {
				getWorkflowTrashUtils().moveFolderToTrash(WorkFlowDataTempDir,ProcessId,WorkFlowRootDir);
			}
		} catch (Exception e) {
			throw new RSuiteException("Exception " + e.toString());
		}
	}

}
