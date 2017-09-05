package org.theiet.rsuite.iettv.webservices.crossref;

import org.theiet.rsuite.iettv.domain.datatype.VideoRecord;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;

public class DeliverCrossRefWebService extends ProjectRemoteApiHandler {

	private static final String DIALOG_TITLE = "Deliver Video Record to CrossRef";

	@Override
	protected String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		StringBuilder successResponse = new StringBuilder(
				"The delivery workflow has been started.");
		String videoContainerId = getMoId(args);
		User user = context.getSession().getUser();
		
		VideoRecord videoRecord = new VideoRecord(context, user, videoContainerId);
		videoRecord.startCrossRefDeliveryWorkflow();		

		return successResponse.toString();
	}

	@Override
	protected String getDialogTitle() {
		return DIALOG_TITLE;
	}
}