package org.theiet.rsuite.books.webservice;

import org.theiet.rsuite.books.BooksConstans;

import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;

/**
 * Sends request to typesetter
 * 
 */
public class SendRequestToTypesetterWebService extends ABookTypesetterRequestWebService {

	public static final String WEB_SERVICE_LABEL = "Send request to typesetter";

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

	@Override
	protected String getWebserviceLabel() {
		return WEB_SERVICE_LABEL;
	}

	@Override
	protected String getSuccessMessage() {
		return "Request has been sent to the typesetter.";
		
	}

	@Override
	protected String getTargetFolderProperty() {
		return BooksConstans.CFG_BOOK_REQUEST_TARGET_FOLDER;
	}

	@Override
	protected String getMailTitleProperty() {
		return PROP_BOOKS_TYPESETTER_REQUEST_MAIL_TITLE;
	}

	@Override
	protected String getMailBodyProperty() {
		return PROP_BOOKS_TYPESETTER_REQUEST_MAIL_BODY;
	}

	@Override
	protected String getRequestType() {
		return null;
	}

}
