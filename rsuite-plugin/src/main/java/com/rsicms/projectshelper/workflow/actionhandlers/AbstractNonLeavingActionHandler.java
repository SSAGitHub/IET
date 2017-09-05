package com.rsicms.projectshelper.workflow.actionhandlers;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.utils.ActionHandlerUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.workflow.*;

public abstract class AbstractNonLeavingActionHandler extends AbstractBaseNonLeavingActionHandler {

	/** serial uid. */
	private static final long serialVersionUID = 7014237859038679219L;
	private WorkflowExecutionContext executionContext;

	@Override
	final public void execute(WorkflowExecutionContext executionContext)
			throws Exception {
		try {
			this.executionContext = executionContext;
			executeTask(executionContext);

		} catch (Exception e) {
			ActionHandlerUtils.handleException(executionContext, e);
		}
	}

	public String getIngestionWorkspaceFolder () {
		return new File(executionContext.getWorkflowJobContext().getTempFolderPath()).getParent();
	}

	public abstract void executeTask(WorkflowExecutionContext executionContext)
			throws Exception;


	protected String getRSuiteId() throws RSuiteException{
	    return executionContext.getVariable("rsuite contents");
	}
	
	protected File getTempFolder(){
	    return new File(executionContext.getWorkflowJobContext().getTempFolderPath());
	}

	 @SuppressWarnings("unchecked")
	protected Map<String, String> getWorkflowVariables(WorkflowExecutionContext context) {
	        Map<String, String> variables = getParameters();

	        Map<String, Object> variablesMap = context.getContextInstance()
	                .getVariables();

	        for (Entry<String, Object> entry : variablesMap.entrySet()) {
	            if (entry.getValue() instanceof String) {
	                String value = (String) entry.getValue();
	                if (StringUtils.isNotBlank(value)) {
	                    value = resolveVariablesAndExpressions(context, value);
	                }
	                variables.put(entry.getKey(), (String) entry.getValue());
	            }
	        }

	        return variables;
	    }
}
