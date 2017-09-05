package org.theiet.rsuite.actionhandlers;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.utils.PubtrackLogger;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.AbstractBaseActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;

public class ClosePubtrackProcess extends AbstractBaseActionHandler {

	public static final String PROCESS_ID = "processId";	
	public static final String PRODUCT_NAME = "productName";
	public static final long serialVersionUID = 1L;

	@Override
	public void execute(WorkflowExecutionContext context)
			throws Exception {
		Log log = context.getWorkflowLog();
		User user = getSystemUser();
		String processId = getParameter(PROCESS_ID);
		String productName = getParameter(PRODUCT_NAME);
		processId = resolveVariables(context, processId);
		productName = resolveVariables(context, productName);
		log.info("execute: attempt to complete process for " +
				"product " + productName + " id " + processId);
		try {
			PubtrackLogger.completeProcess(user, context, productName, processId);
			log.info("execute: closed process");
		}
		catch (RSuiteException e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void setProcessId(String processId) {
		this.setParameter(PROCESS_ID, processId);
	}
	
	public void setProductName(String productName) {
		this.setParameter(PRODUCT_NAME, productName);
	}

}
