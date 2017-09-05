package org.theiet.rsuite.journals.webservice;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.utils.JournalBrowserUtils;
import org.theiet.rsuite.journals.utils.JournalMailUtils;

import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgument;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.rsicms.projectshelper.net.mail.*;

public class SendAuthorEmailFromEditorialAssistant extends DefaultRemoteApiHandler
				implements JournalConstants {

	private static final String PARAM_ADDITIONAL_TEXT = EMAIL_VAR_ADDITIONAL_TEXT;
	private static final String PARAM_ATTACHED_FILE = EMAIL_VAR_ATTACHED_FILE;
	private String DIALOG_TITLE = "Send E-mail to the author";
	private static Log log = LogFactory.getLog(SendAuthorEmailFromEditorialAssistant.class);
	
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {
		try {
			log.info("Starting author email sending from EA");
			User user = context.getSession().getUser();
			String contextRsuiteId = args.getFirstValue(PARAM_RSUITE_ID);
			ContentAssemblyItem journalCa = JournalBrowserUtils.getAncestorJournal(context, contextRsuiteId);
			ContentAssemblyItem articleCa = JournalBrowserUtils.getAncestorAritcle(context, contextRsuiteId);
			
			String additionalText = args.getFirstValue(PARAM_ADDITIONAL_TEXT);
			if (StringUtils.isBlank(additionalText)) {
				additionalText = "";
			}
			FileItem attachedFile = getFileItem(args);
			log.info("Getting from part to email author ");
			String emailFrom = JournalMailUtils.obtainEmailFrom(context, journalCa);
			log.info("Loading author message variables");
			Map<String, String> variables = JournalMailUtils.setUpVariablesMap(context, user, journalCa, articleCa);
			variables.put(EMAIL_VAR_ADDITIONAL_TEXT, additionalText);
			log.info("Creating author message");
			MailMessage message = createAuthorMailMessage(context, variables, emailFrom, contextRsuiteId, attachedFile);
			log.info("Sending email to the author");
			MailUtils.sentEmail(context, message);
			log.info("Email has been sent to the author");
		} catch (RSuiteException ex) {
			return new MessageDialogResult(MessageType.ERROR, DIALOG_TITLE, "Server exception was " + ex.getMessage());
		} catch (IOException ex) {
			return new MessageDialogResult(MessageType.ERROR, DIALOG_TITLE, "Server exception was " + ex.getMessage());
		}
		
		String msg = "Email has been sent to the author";
		return new MessageDialogResult(MessageType.SUCCESS, DIALOG_TITLE, msg, "500");
	}
	
	
	
	
	private MailMessage createAuthorMailMessage(RemoteApiExecutionContext context,
			Map<String, String> variables, String emailFrom,
			String contextRsuiteId, FileItem attachedFile) throws RSuiteException, IOException {
		ContentAssemblyItem articleCa = JournalBrowserUtils.getAncestorAritcle(context, contextRsuiteId);
		
		String mailSubject = MailUtils.obtainEmailSubject(context,
				PROP_JOURNALS_SEND_EMAIL_AUTHOR_FROM_EA_MAIL_TITLE);
		
		String recipientEmailAddress = articleCa.getLayeredMetadataValue(LMD_FIELD_AUTHOR_EMAIL);

		MailMessage message = new MailMessage(emailFrom, recipientEmailAddress,
				mailSubject);
		message.setMessageTemplateProperty(PROP_JOURNALS_SEND_EMAIL_AUTHOR_FROM_EA_MAIL_BODY);
		message.setVariables(variables);	
		
		if (attachedFile != null) {
			message.addAttachment(attachedFile.getName(), IOUtils.toByteArray(attachedFile.getInputStream()));
		}
		
		return message;		
	}




	private FileItem getFileItem (CallArgumentList args) {
		List<CallArgument> callList = args.getAll();
		for(CallArgument arguments : callList){
			if (arguments.isFileItem() && arguments.getName().equals(PARAM_ATTACHED_FILE)) {
				return arguments.getValueAsFileItem();
			}
		}
		return null;
	}
	
}
