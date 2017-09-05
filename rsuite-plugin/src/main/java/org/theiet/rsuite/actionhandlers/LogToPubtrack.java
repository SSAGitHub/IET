package org.theiet.rsuite.actionhandlers;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.utils.PubtrackLogger;

import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.AbstractBaseActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;

public class LogToPubtrack extends AbstractBaseActionHandler {

	private static final long serialVersionUID = 1L;
	private static String EVENT = "event";

	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {
		Log log = context.getWorkflowLog();
		User user = getSystemUser();
		String event = getParameter(EVENT);
		String productName = context.getVariable(JournalConstants.WF_VAR_PRODUCT);
		String id = context.getVariable(BooksConstans.WF_BOOK_PRODUCT_CODE);
		PubtrackLogger.logToProcess(user, context, log, productName, id, event);
	}
	
	public void setEvent(String event) {
		this.setParameter(EVENT, event);
	}

}
