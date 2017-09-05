package org.theiet.rsuite.journals.webservice.issue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.delivery.digitallibrary.IssueDigitalLibrary;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;

public class DeliveryIssueWebService extends DefaultRemoteApiHandler {
	
	private static final String DIALOG_TITLE = "Deliver to Digital Library";
	private static Log logger = LogFactory.getLog(DeliveryIssueWebService.class);
	
	public MessageDialogResult execute (
			RemoteApiExecutionContext context,
			CallArgumentList args) {
		User user = context.getSession().getUser();

		String issueCaId = args.getFirstValue(IetConstants.PARAM_RSUITE_ID);
		
		try {
			Issue issue = new Issue(context, user, issueCaId);	
			
			IssueDigitalLibrary issueDL = new IssueDigitalLibrary(context, user, logger);
			issueDL.deliverIssue(issue);
			
			return new MessageDialogResult(MessageType.SUCCESS, DIALOG_TITLE, "Issue " + issue.getIssueCode() + " delivered");
		} catch (RSuiteException e) {
			logger.error(e, e);
			return new MessageDialogResult(MessageType.ERROR, DIALOG_TITLE, "Server returned exception " + e.getMessage());
		}
	}

}
