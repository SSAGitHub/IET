package org.theiet.rsuite.journals.webservice;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.utils.JournalBrowserUtils;
import org.theiet.rsuite.journals.utils.JournalMailUtils;
import org.theiet.rsuite.utils.ContentDisplayUtils;
import org.theiet.rsuite.utils.PubtrackLogger;

import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.net.mail.*;

/**
 * Custom RSuite web service to sent email
 * 
 */
public class SendEmailForReviewWebService extends DefaultRemoteApiHandler
		implements JournalConstants {

	private static final String WS_PARAM_EMAIL_ADDRESS = "emailAddress";

	private static final String WEB_SERVICE_LABEL = "Sent email for review";

	private static Log log = LogFactory
			.getLog(SendEmailForReviewWebService.class);

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		try {
			String contextRsuiteId = args.getFirstValue(PARAM_RSUITE_ID);
			User user = context.getSession().getUser();
			ManagedObjectService moSvc = context.getManagedObjectService();
			String moId = args.getFirstManagedObject(user).getId();

			ContentAssemblyItem articleCa = JournalBrowserUtils
					.getAncestorAritcle(context, contextRsuiteId);
			String articleId = articleCa.getDisplayName();

			ContentAssemblyItem journalCa = JournalBrowserUtils
					.getAncestorJournal(context, contextRsuiteId);

			String emailFrom = JournalMailUtils.obtainEmailFrom(context,
					journalCa);

			String recipientEmailAddress = args
					.getFirstValue(WS_PARAM_EMAIL_ADDRESS);

			String mailSubject = MailUtils.obtainEmailSubject(context,
					PROP_PDF_PROOF_MAIL_TITLE);			

			Map<String, String> variables = JournalMailUtils.setUpVariablesMap(
					context, user, journalCa, articleCa);

			ManagedObject mo = obtainMOtoSend(context, user, moId);

			MailMessage message = new MailMessage(emailFrom,
					recipientEmailAddress, mailSubject);
			message.setMessageTemplateProperty(PROP_PDF_ROOF_MAIL_BODY);
			message.setVariables(variables);

			MailUtils.sentMoViaEmail(context, message, mo);
			
			String articleCaId = articleCa.getId();
			
			MetaDataItem lmdEntry = new MetaDataItem(LMD_FIELD_AWAITING_AUTHOR_COMMENTS, LMD_VALUE_YES);
			moSvc.setMetaDataEntry(context.getSession().getUser(), articleCaId, lmdEntry);
			PubtrackLogger.logToProcess(user, context, log, "ARTICLE", articleId, PUBTRACK_REQUESTED_AUTHOR_COMMENTS);
					
			return ContentDisplayUtils.getResultWithLabelRefreshing(
					MessageType.SUCCESS, WEB_SERVICE_LABEL, "<html><body><div><p>Email has been sent.</p></div></body></html>", "500", articleCaId);
		} catch (Exception e) {
			log.error("Unable to send email for review", e);
			return new MessageDialogResult(MessageType.ERROR, WEB_SERVICE_LABEL, "Error: " + e.getMessage());
		}

	}

	private ManagedObject obtainMOtoSend(RemoteApiExecutionContext context,
			User user, String nodeId) throws RSuiteException {
		ManagedObjectService moSvc = context.getManagedObjectService();
		ManagedObject mo = moSvc.getManagedObject(user, nodeId);
		return mo;
	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
