package org.theiet.rsuite.books.webservice;

import java.util.*;

import org.theiet.rsuite.books.BooksConstans;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

/**
 * Sends request to typesetter
 * 
 */
public class SendPrinterRequestWebService extends AExternalRequestWebService {

	public static final String WEB_SERVICE_LABEL = "Send request to printer";

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

	@Override
	protected String getWebserviceLabel() {
		return WEB_SERVICE_LABEL;
	}

	@Override
	protected String getSuccessMessage() {
		return "Request has been sent to the printer";

	}

	@Override
	protected String getTargetFolderProperty() {
		return BooksConstans.CFG_BOOK_REQUEST_TARGET_FOLDER;
	}

	@Override
	protected String getMailTitleProperty() {
		return PROP_BOOKS_PRINTER_REQUEST_MAIL_TITLE;
	}

	@Override
	protected String getMailBodyProperty() {
		return PROP_BOOKS_PRINTER_REQUEST_MAIL_BODY;
	}

	protected String getUserLmdName() {
		return LMD_FIELD_PRINTER_USER;
	}

	@Override
	protected String getTargetFileName(ContentAssemblyItem bookCa)
			throws RSuiteException {
		String shortTitle = bookCa
				.getLayeredMetadataValue(LMD_FIELD_BOOK_TITLE_SHORT);
		shortTitle = shortTitle.replaceAll("\\s+", "_");
		shortTitle += ".zip";
		return shortTitle;
	}

	@Override
	protected List<ContentAssembly> getAssemblyToAttach(
			ExecutionContext context, ContentAssemblyItem bookCa)
			throws RSuiteException {
		ContentAssembly contextCa = ProjectBrowserUtils.getContentAssembly(context,
				context.getAuthorizationService().getSystemUser(),
				bookCa);
		
		ContentAssembly productionFilesCA = ProjectBrowserUtils.getChildCaByDisplayName(context, contextCa,
				CA_NAME_PRODUCTION_FILES);
		
		ContentAssembly instructionsCA = ProjectBrowserUtils.getChildCaByType(context, productionFilesCA,
				CA_TYPE_PRINTER_INSTRUCTIONS);
		
		List<ContentAssembly> attachments = new ArrayList<ContentAssembly>();
		if (instructionsCA != null){
			attachments.add(instructionsCA);
		}
		
		
		return attachments;
	}

	@Override
	protected String getRequestType() {
		return TYPESETTER_REQUEST_PRINTER;
	}
}
