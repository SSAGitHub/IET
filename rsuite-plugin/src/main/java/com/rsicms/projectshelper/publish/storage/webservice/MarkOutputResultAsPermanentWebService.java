package com.rsicms.projectshelper.publish.storage.webservice;

import com.rsicms.projectshelper.lmd.value.YesNoLmdValue;

public class MarkOutputResultAsPermanentWebService extends MarkOutputResultHandler {

	@Override
	public String getMarkAs() {
		return "permanent";
	}

	@Override
	public String isRemovablePublicationResultValue() {
		return YesNoLmdValue.NO.getValue();
	}

}
