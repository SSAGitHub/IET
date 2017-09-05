package org.theiet.rsuite.iettv.webservices;

import org.theiet.rsuite.iettv.domain.datatype.VideoRecord;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;

public class DeliverVideoToIetTvProductionWebService extends ProjectRemoteApiHandler {

	private static final String DIALOG_TITLE = "Deliver Video Recort to Iet.tv Production";

	@Override
	protected String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		StringBuilder successResponse = new StringBuilder(
				"The delivery workflow has been started.");
		String videoContainerId = getMoId(args);
		User user = context.getSession().getUser();
		
		VideoRecord videoRecord = new VideoRecord(context, user, videoContainerId);
		videoRecord.startProductionDeliveryWorkflow();		

		return successResponse.toString() + ".";
	}

	@Override
	protected String getDialogTitle() {
		return DIALOG_TITLE;
	}
}