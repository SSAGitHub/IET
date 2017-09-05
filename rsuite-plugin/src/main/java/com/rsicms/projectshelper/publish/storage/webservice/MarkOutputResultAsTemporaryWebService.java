package com.rsicms.projectshelper.publish.storage.webservice;

import com.rsicms.projectshelper.lmd.value.YesNoLmdValue;

public class MarkOutputResultAsTemporaryWebService extends MarkOutputResultHandler {

	@Override
	public String getMarkAs() {
		return "temporary";
	}

	@Override
	public String isRemovablePublicationResultValue() {
		return YesNoLmdValue.YES.getValue();
	}

}
