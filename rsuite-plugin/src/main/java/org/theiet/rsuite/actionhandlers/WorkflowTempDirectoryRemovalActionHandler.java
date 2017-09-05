package org.theiet.rsuite.actionhandlers;

import static org.theiet.rsuite.standards.StandardsBooksConstans.WF_VAR_REMOVABLE_DIRECTORY;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractActionHandler;

public class WorkflowTempDirectoryRemovalActionHandler extends AbstractActionHandler {

	private static final long serialVersionUID = 1L;

	@Override
	public void executeTask(WorkflowExecutionContext executionContext)
			throws Exception {
		Log wLog = executionContext.getWorkflowLog();
		String removableDirectory = executionContext.getVariable(WF_VAR_REMOVABLE_DIRECTORY);
		FileUtils.deleteDirectory(new File(removableDirectory));
		wLog.info("Following directory has been removed: " + removableDirectory);
		
	}



}
