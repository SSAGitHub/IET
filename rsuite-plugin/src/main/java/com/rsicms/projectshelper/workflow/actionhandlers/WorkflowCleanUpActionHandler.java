package com.rsicms.projectshelper.workflow.actionhandlers;

import static com.rsicms.projectshelper.workflow.WorkflowTrashUtils.moveFolderToTrash;
import static com.rsicms.projectshelper.workflow.WorkflowUtils.getMainWorkflowFolder;

import java.io.File;

import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.workflow.AbstractBaseNonLeavingActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;

public class WorkflowCleanUpActionHandler extends
		AbstractBaseNonLeavingActionHandler {

	/** UID */
	private static final long serialVersionUID = -9176061904853674255L;

	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {
	    
	    Log workflowLog = context.getWorkflowLog();
	    workflowLog.info("Cleaning up the workflow folder");
	    
		File workflowFolder = getMainWorkflowFolder(context);
		
		File trashFolder = moveFolderToTrash(context, workflowFolder);
		
		if (trashFolder != null){
		    	        
	        workflowLog.info("Workflow folder " + workflowFolder.getAbsolutePath() + " has been moved to " + trashFolder.getAbsolutePath());   
		}
		
	}


}