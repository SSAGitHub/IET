package org.theiet.rsuite.books.webservice;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.books.datatype.BookRequestDTO;
import org.theiet.rsuite.books.utils.BookUtils;
import org.theiet.rsuite.datamodel.ExternalCompanyUser;
import org.theiet.rsuite.utils.ContentDisplayUtils;
import org.theiet.rsuite.utils.ExternalRequestUtils;
import org.theiet.rsuite.utils.IetUtils;
import org.theiet.rsuite.utils.WebServiceUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.net.mail.*;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.rsuite.helpers.webservice.RemoteApiHandlerBase;

/**
 * An abstract class for book request (typesetter and printer) : sends context assembly via FTP
 * and sends email to external party
 * 
 */
public abstract class AExternalRequestWebService extends RemoteApiHandlerBase
		implements BooksConstans {

	
	private static final String PARAM_ADDITIONAL_TEXT = EMAIL_VAR_ADDITIONAL_TEXT;
	private static Log log = LogFactory
			.getLog(AExternalRequestWebService.class);

	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		try {
			String contextRsuiteId = args.getFirstValue(PARAM_RSUITE_ID);
			String additionalText = args.getFirstValue(PARAM_ADDITIONAL_TEXT);

			if (StringUtils.isBlank(additionalText)) {
				additionalText = "";
			}

			ContentAssemblyItem bookCa = ProjectContentAssemblyUtils.getAncestorCAbyType(
					context, contextRsuiteId,
					BooksConstans.CA_TYPE_BOOK);

			BookRequestDTO bookRequest = new BookRequestDTO(context, bookCa,
					getUser(context), getTargetFolderProperty(),
					getTargetFileName(bookCa));

			if (sendPackageToFtp()) {
				String externalUserId = BookUtils.getUserIdFormLmd(bookCa,
						getUserLmdName());

				ExternalRequestUtils.sendBookRequestPackage(context, bookRequest,
						contextRsuiteId, externalUserId);
			}

			sendNotificationEmail(context, getUser(context), bookCa,
					additionalText, bookRequest);
			
			setAwaitingFlag(context, bookCa, getRequestType());
			
			return ContentDisplayUtils.getResultWithLabelRefreshing(
					MessageType.SUCCESS, getWebserviceLabel(), getSuccessMessage(), "500", bookCa.getId());
		} catch (Exception e) {
			return WebServiceUtils
					.handleException(e, log, getWebserviceLabel());
		}

	}

	private void setAwaitingFlag(RemoteApiExecutionContext context,
			ContentAssemblyItem bookCa, String requestType) throws RSuiteException {

					ManagedObjectService moSvc = context.getManagedObjectService();
					String bookCaId = bookCa.getId();
					User user = getUser(context);
					
					IetUtils.removeMetaDataFieldFromCa(log, user, moSvc, bookCa, LMD_FIELD_TYPESETTER_UPDATE_TYPE);
					
					if (!TYPESETTER_REQUEST_PRINTER.equals(requestType)) {						
						moSvc.setMetaDataEntry(user, bookCaId, new MetaDataItem(LMD_FIELD_AWAITING_TYPESETTER_UPDATES, LMD_VALUE_YES));
						
						if (requestType != null) {
							moSvc.setMetaDataEntry(user, bookCaId, new MetaDataItem(LMD_FIELD_TYPESETTER_UPDATE_TYPE, requestType));
						}
					}
					
					
	}

	private void sendNotificationEmail(ExecutionContext context,
			User currentUser, ContentAssemblyItem bookCa,
			String additionalText, BookRequestDTO bookRequest)
			throws RSuiteException, IOException {
		String recipientUserId = BookUtils.getUserIdFormLmd(bookCa,
				getUserLmdName());
		
    	 ExternalCompanyUser recipientUser = new ExternalCompanyUser(context, recipientUserId);
		 
		
		Map<String, String> variables = bookRequest.getEmailVariables();
		variables.put(EMAIL_VAR_ADDITIONAL_TEXT, additionalText);
		variables.put(EMAIL_VAR_CONTACT_NAME, recipientUser.getContactFirstName());

		String mailSubject = MailUtils.obtainEmailSubject(context,
				getMailTitleProperty());

		MailMessage message = new MailMessage(currentUser.getEmail(),
				recipientUser.getEmail(), mailSubject);
		message.setMessageTemplateProperty(getMailBodyProperty());
		message.setVariables(variables);

		MailUtils.attachCAsToMessage(context, message, getAssemblyToAttach(context, bookCa));

		MailUtils.sentEmail(context, message);
	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

	protected boolean sendPackageToFtp() {
		return true;
	}

	protected abstract String getWebserviceLabel();

	protected abstract String getSuccessMessage();

	protected abstract String getTargetFolderProperty();

	protected abstract String getMailTitleProperty();

	protected abstract String getMailBodyProperty();
	
	protected abstract String getRequestType();

	protected abstract String getUserLmdName();

	protected abstract String getTargetFileName(ContentAssemblyItem bookCa)
			throws RSuiteException;

	protected abstract List<ContentAssembly> getAssemblyToAttach(
			ExecutionContext context, ContentAssemblyItem bookCa)
			throws RSuiteException;
}
