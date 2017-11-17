package org.theiet.rsuite.eventhandlers;

import java.io.File;
import java.io.IOException;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.event.DefaultEventHandler;
import com.reallysi.rsuite.api.event.Event;
import com.reallysi.rsuite.api.event.events.WorkflowEventData;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.WorkflowTrashUtils;
import com.reallysi.rsuite.api.system.RSuiteServerConfiguration;


public class WorkFlowCompletionEventHandler extends DefaultEventHandler {

	  public WorkflowTrashUtils WORKFLOW_TRASH_UTILS = new WorkflowTrashUtils();
	  private File WorkFlowTempDir;
	  
	  
	  @Override
	  public void handleEvent(ExecutionContext context, Event event, Object handback)
	      throws RSuiteException  {

	    String eventType = event.getType();

	    if ("workflow.process.completed".equals(eventType)
	        || "workflow.process.aborted".equals(eventType)) {
	      WorkflowEventData workflowEventData = (WorkflowEventData) event.getUserData();

	      String ProcessId = workflowEventData.getProcessInstanceId();
	      WorkFlowTempDir = context.getRSuiteServerConfiguration().getTmpDir();

	      processWorkFlowFile(WorkFlowTempDir, ProcessId);
	      
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

	  protected void  processWorkFlowFile(File WorkFlowTempDir, String ProcessId) throws RSuiteException {
		  
		  File WorkFlowFolder = new File(WorkFlowTempDir, ProcessId );

	      try {
	    	  getWorkflowTrashUtils().moveFolderToTrash(WorkFlowTempDir,ProcessId , WorkFlowFolder);
	      }
	      catch (Exception e) {
	    	  throw new RSuiteException("Exception " + e.toString());
	      }
	  }

}
	
	
	
	

