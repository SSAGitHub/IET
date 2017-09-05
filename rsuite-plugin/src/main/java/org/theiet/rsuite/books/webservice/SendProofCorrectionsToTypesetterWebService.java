package org.theiet.rsuite.books.webservice;

import org.theiet.rsuite.books.BooksConstans;

import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;

/**
 * Sends update request to typesetter
 * 
 */
public class SendProofCorrectionsToTypesetterWebService extends ABookTypesetterRequestWebService implements BooksConstans {

	public static final String WEB_SERVICE_LABEL = "Send update request to typesetter";

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

	@Override
	protected String getWebserviceLabel() {
		return WEB_SERVICE_LABEL;
	}

	@Override
	protected String getSuccessMessage() {
		return "Update request has been sent to the typesetter.";
		
	}

	@Override
	protected String getTargetFolderProperty() {
		return BooksConstans.CFG_BOOK_UPDATE_REQUEST_TARGET_FOLDER;
	}

	@Override
	protected String getMailTitleProperty() {
		return PROP_BOOKS_TYPESETTER_CORRECTIONS_MAIL_TITLE;
	}

	@Override
	protected String getMailBodyProperty() {
		return PROP_BOOKS_TYPESETTER_CORRECTIONS_MAIL_BODY;
	}

	@Override
	protected String getRequestType() {
		return LMD_VALUE_UPDATE;
	}

}
