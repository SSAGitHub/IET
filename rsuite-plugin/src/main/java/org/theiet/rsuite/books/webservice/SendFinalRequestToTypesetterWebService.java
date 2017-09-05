package org.theiet.rsuite.books.webservice;

import org.theiet.rsuite.books.BooksConstans;

import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;

/**
 * Sends request to typesetter
 * 
 */
public class SendFinalRequestToTypesetterWebService extends
		ABookTypesetterRequestWebService implements BooksConstans {

	public static final String WEB_SERVICE_LABEL = "Send final request to typesetter";

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

	@Override
	protected String getWebserviceLabel() {
		return WEB_SERVICE_LABEL;
	}

	@Override
	protected String getSuccessMessage() {
		return "Final request has been sent to the typesetter.";

	}

	@Override
	protected String getTargetFolderProperty() {
		return null;
	}

	@Override
	protected String getMailTitleProperty() {
		return PROP_BOOKS_TYPESETTER_FINAL_REQUEST_MAIL_TITLE;
	}

	@Override
	protected String getMailBodyProperty() {
		return PROP_BOOKS_TYPESETTER_FINAL_REQUEST_MAIL_BODY;
	}

	@Override
	protected boolean sendPackageToFtp() {
		return false;
	}

	@Override
	protected String getRequestType() {
		return LMD_VALUE_FINAL;
	}
}
