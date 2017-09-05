package org.theiet.rsuite.actionhandlers;

import com.reallysi.rsuite.api.workflow.AbstractBaseNonLeavingActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;

import static com.rsicms.projectshelper.workflow.WorkflowConstants.*;

public class ClearExcpetionAbstractActionHandler extends AbstractBaseNonLeavingActionHandler {

	
	/** serial uid. */
	private static final long serialVersionUID = -7209300840997626646L;

	@Override
	final public void execute(WorkflowExecutionContext executionContext)
			throws Exception {

		executionContext.getContextInstance().deleteVariable(EXCEPTION_OCCUR);
		executionContext.getContextInstance().deleteVariable(EXCEPTION_TYPE);

	}



}
