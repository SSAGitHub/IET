package org.theiet.rsuite.journals.webservice;

import org.theiet.rsuite.journals.JournalConstants;

public class ClearAwaitingAuthorStatus extends ClearStatusBase implements JournalConstants {

	@Override
	protected String getDialogTitle() {
		return "Clear awaiting author flag";
	}

	@Override
	protected String getLmdField() {
		return LMD_FIELD_AWAITING_AUTHOR_COMMENTS;
	}

	@Override
	protected String getMessage() {
		return "Flag cleared";
	}

	@Override
	protected String getEventName() {
		return PUBTRACK_AUTHOR_COMMENTS_RECEIVED;
	}
	
	

}
