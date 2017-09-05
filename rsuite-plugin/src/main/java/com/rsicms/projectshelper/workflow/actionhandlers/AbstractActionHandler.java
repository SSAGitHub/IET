package com.rsicms.projectshelper.workflow.actionhandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.theiet.rsuite.utils.ActionHandlerUtils;

import com.reallysi.rsuite.api.workflow.AbstractBaseActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;

public abstract class AbstractActionHandler extends AbstractBaseActionHandler {

	private Log log = LogFactoryImpl.getLog(AbstractActionHandler.class);

	/** serial uid. */
	private static final long serialVersionUID = -7209300840997626646L;

	@Override
	final public void execute(WorkflowExecutionContext executionContext)
			throws Exception {
		try {

			executeTask(executionContext);

		} catch (Exception e) {
			log.error(e,e);
			ActionHandlerUtils.handleException(executionContext, e);
		}
	}

	public abstract void executeTask(WorkflowExecutionContext executionContext)
			throws Exception;	

}
