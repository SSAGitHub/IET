package com.rsicms.projectshelper.publish.workflow.actionhandlers;

import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.*;
import static com.rsicms.projectshelper.workflow.WorkflowTrashUtils.*;
import static com.rsicms.projectshelper.workflow.WorkflowUtils.*;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.workflow.*;

public class PublishWorkflowCleanUpActionHandler extends
		AbstractBaseNonLeavingActionHandler {

	/** UID */
	private static final long serialVersionUID = -9176061904853674255L;

	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {
	    
	    Log workflowLog = context.getWorkflowLog();
	    workflowLog.info("Cleaning up the workflow folder");
	    
		String publishTempFolderPath = context.getVariable(TEMP_FOLDER.getVariableName());
		File publishTempFolder = new File(publishTempFolderPath);
		
		File trashFolder = moveFolderToTrash(context, publishTempFolder);
		
		if (trashFolder != null){
	        workflowLog.info("Workflow folder " + publishTempFolder.getAbsolutePath() + " has been moved to " + trashFolder.getAbsolutePath());   
		}
		
	    File workflowFolder = getMainWorkflowFolder(context);
		FileUtils.deleteQuietly(workflowFolder);
		
		updatePathInWorkflowVariables(context, publishTempFolderPath, trashFolder.getAbsolutePath());
		
	}

	private void updatePathInWorkflowVariables(WorkflowExecutionContext context, String oldPath, String newPath) throws RSuiteException{
	    Map<String, Object> workflowVariables = context.getWorkflowVariables();
	    for (Entry<String, Object> entry : workflowVariables.entrySet()){
	        Object value = entry.getValue();
	        String key = entry.getKey();
	        
	        if (value instanceof String){
	            updateWorkflowVariable(context, oldPath, newPath, value, key);
	        }
	    }
	}

    private void updateWorkflowVariable(WorkflowExecutionContext context, String oldPath,
            String newPath, Object value, String key) throws RSuiteException {
        String variableValue = (String)value;
        if (variableValue.contains(oldPath)){
            variableValue = variableValue.replace(oldPath, newPath);
            context.setVariable(key, variableValue);
        }
    }
}