package org.theiet.rsuite.journals.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.delivery.author.AuthorDelivery;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.journals.utils.JournalBrowserUtils;
import org.theiet.rsuite.journals.utils.JournalMailUtils;
import org.theiet.rsuite.utils.ContentDisplayUtils;
import org.theiet.rsuite.utils.PubtrackLogger;
import org.theiet.rsuite.utils.WebServiceUtils;

import com.reallysi.rsuite.api.ContentAssemblyItem;
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
import com.rsicms.projectshelper.net.mail.MailMessage;
import com.rsicms.projectshelper.net.mail.MailUtils;

/**
 * Custom RSuite web service to sent email
 * 
 */
public class SendFinalRequestToTypesetterWebService extends
		DefaultRemoteApiHandler implements JournalConstants {

	private static final String WEB_SERVICE_LABEL = "Sent final request to typesetter";

	private static Log log = LogFactory
			.getLog(SendFinalRequestToTypesetterWebService.class);

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		try {
			User user = context.getSession().getUser();
			ManagedObjectService moSvc = context.getManagedObjectService();
			String contextRsuiteId = args.getFirstValue(PARAM_RSUITE_ID);

			ContentAssemblyItem articleCa = JournalBrowserUtils
					.getAncestorAritcle(context, contextRsuiteId);
			
			Article article = new Article(context, user, log, articleCa.getId());
			Journal journal = article.getJournal();
			
			if (journal.isAutomaticPdfGenerationWorkflow()){
				return WebServiceUtils.createResultDialog(WEB_SERVICE_LABEL, "This option is not available for " + journal.getJournalCode() + " journal");
			}
			
			AuthorDelivery.sendComplimentaryPDF(context, user, article);
			
			String articleId = article.getArticleId();
			String articleCaId = article.getArticleCaId();

			ContentAssemblyItem journalCa = JournalBrowserUtils
					.getAncestorJournal(context, contextRsuiteId);

			String emailFrom = JournalMailUtils.obtainEmailFrom(context,
					journalCa);

			Map<String, String> variables = JournalMailUtils.setUpVariablesMap(
					context, user, journalCa, articleCa);

			MailMessage typesetterMessage = createTypesetterMailMessage(
					context, variables, emailFrom, journalCa);

			
			MailUtils.sentEmail(context, typesetterMessage);

			ArrayList<MetaDataItem> items = new ArrayList<MetaDataItem>();

			items.add(new MetaDataItem(LMD_FIELD_AWAITING_TYPESETTER_UPDATES,
					LMD_VALUE_YES));
			items.add(new MetaDataItem(LMD_FIELD_TYPESETTER_UPDATE_TYPE,
					LMD_VALUE_FINAL));
			moSvc.setMetaDataEntries(context.getSession().getUser(),
					articleCaId, items);
			PubtrackLogger.logToProcess(user, context, log, PUBTRACK_PRODUCT_ARTICLE,
					articleId, PUBTRACK_REQUESTED_TYPESETTER_FINAL_FILES);

			return ContentDisplayUtils.getResultWithLabelRefreshing(
					MessageType.SUCCESS, WEB_SERVICE_LABEL, creatHtmlResult("Email has been sent."), "500", articleCaId);			
		} catch (Exception e) {
			log.error("Unable to send email for review", e);
			return new MessageDialogResult(MessageType.ERROR,
					WEB_SERVICE_LABEL, "Error: " + e.getMessage());
		}
	}
	
	private String creatHtmlResult(String message){		
		return "<html><body><div><p>" + message + "</p></div></body></html>";		
	}

	private MailMessage createTypesetterMailMessage(
			RemoteApiExecutionContext context, Map<String, String> variables,
			String emailFrom, ContentAssemblyItem journalCa)
			throws RSuiteException, IOException {

		String mailSubject = MailUtils.obtainEmailSubject(context,
				PROP_TYPESETTER_FINAL_REQUEST_MAIL_TITLE);

		String recipientEmailAddress = JournalMailUtils.getTypesetterEmail(context, journalCa);

		MailMessage message = new MailMessage(emailFrom, recipientEmailAddress,
				mailSubject);
		message.setMessageTemplateProperty(PROP_TYPESETTER_FINAL_REQUEST_MAIL_BODY);
		message.setVariables(variables);

		return message;
	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
