package org.theiet.rsuite.journals.actionhandlers;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.delivery.digitallibrary.IssueDigitalLibrary;

import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.AbstractBaseActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;

public class DeliverIssueToDigitalLibrary extends AbstractBaseActionHandler {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {
		Log log = context.getWorkflowLog();
		User user = getSystemUser();
		String issueCaId = context.getVariable(IetConstants.WF_VAR_RSUITE_CONTENTS);
		log.info("execute: deliver issueCaId " + issueCaId);
		
		Issue issue = new Issue(context, user, issueCaId);
		IssueDigitalLibrary issueDL = new IssueDigitalLibrary(context, user, log);
		issueDL.deliverIssue(issue);
	}

}
