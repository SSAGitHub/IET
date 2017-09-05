package com.rsicms.projectshelper.publish.workflow.actionhandlers;

import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public abstract class PublishWorkflowActionHandler extends AbstractNonLeavingActionHandler {

	private static final long serialVersionUID = 6393062329920262751L;

	@Override
	public void executeTask(WorkflowExecutionContext executionContext)
			throws Exception {
		
		Log logger = executionContext.getWorkflowLog();
		logStartTaskEvent(logger);
		executeHandler(executionContext);
		logEndTaskEvent(logger);
		
	}
	
	public void logStartTaskEvent(Log logger){
		logNewLines(logger);
		logger.info("==== START " + getWorkflowTaskName()  + " task\n\r\n\r");
		logNewLines(logger);
	}

	private void logNewLines(Log logger) {
		logger.info("");
		logger.info("");
	}

	public void logEndTaskEvent(Log logger){
		logNewLines(logger);
		logger.info("==== END " + getWorkflowTaskName()  + " task\n\n");
		logNewLines(logger);
	}
	
	abstract String getWorkflowTaskName();
	
	abstract void executeHandler(WorkflowExecutionContext executionContext) throws Exception;
}
