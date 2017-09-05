package org.theiet.rsuite.journals.webservice.issue;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.issues.IssuePublishWorkflowChecker;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.notification.typesetter.TypesetterNotification;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.utils.PubtrackLogger;
import org.theiet.rsuite.utils.WebServiceUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.reallysi.rsuite.service.ManagedObjectService;

/**
 * Custom RSuite web service to sent email
 * 
 */
public class SendIssueFinalRequestToTypesetterWebService extends
		DefaultRemoteApiHandler implements JournalConstants {

	private static final String WEB_SERVICE_LABEL = "Sent update request";

	private static Log log = LogFactory
			.getLog(SendIssueFinalRequestToTypesetterWebService.class);

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {
		
		try {
			User user = context.getSession().getUser();

			Issue issue = obtainIssue(context, args, user);
			Journal journal = issue.getJournal();
			
			if (IssuePublishWorkflowChecker.isAutomatedPublishWorkflowActiveForIssue(issue)){
				return WebServiceUtils.createResultDialog(WEB_SERVICE_LABEL, "This option is not available for " + journal.getJournalCode() + " journal. The automated workflow is active.");
			}

			sendFinalRequestToTypesetter(context, args, user, issue);

			return new MessageDialogResult(
					MessageType.SUCCESS,
					WEB_SERVICE_LABEL,
					"<html><body><div><p>The final request has been sent to the typesetter.</p></div></body></html>",
					"500");
		} catch (Exception e) {
			log.error("Unable to send email for review", e);
			return new MessageDialogResult(MessageType.ERROR,
					WEB_SERVICE_LABEL, "Error: " + e.getMessage());
		}

	}



	protected void sendFinalRequestToTypesetter(
			RemoteApiExecutionContext context, CallArgumentList args,
			User user, Issue issue) throws RSuiteException {
		TypesetterNotification.sendFinalRequestNotification(context, user, issue);
		
		
		String issueCaId = args.getFirstValue("rsuiteId");
		ArrayList<MetaDataItem> items = new ArrayList<MetaDataItem>();
		items.add(new MetaDataItem(LMD_FIELD_AWAITING_TYPESETTER_UPDATES, LMD_VALUE_YES));
		items.add(new MetaDataItem(LMD_FIELD_TYPESETTER_UPDATE_TYPE, LMD_VALUE_FINAL));
		
		ManagedObjectService moSvc = context.getManagedObjectService();
		moSvc.setMetaDataEntries(context.getSession().getUser(), issueCaId, items);
		String issueCode = issue.getIssueCa().getLayeredMetadataValue(LMD_FIELD_ISSUE_CODE);
		PubtrackLogger.logToProcess(user, context, log, "ISSUE", issueCode, PUBTRACK_REQUESTED_TYPESETTER_FINAL_FILES);
	}



	protected Issue obtainIssue(RemoteApiExecutionContext context,
			CallArgumentList args, User user) throws RSuiteException {
		return new Issue(context, args, user);
	}

	

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
