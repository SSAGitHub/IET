package org.theiet.rsuite.standards.webservices.export;

import org.theiet.rsuite.standards.StandardsBooksConstans;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;
import com.rsicms.projectshelper.workflow.WorkflowLauncher;

public class ESPPlatformDeliveryWebService extends ProjectRemoteApiHandler implements
		StandardsBooksConstans {

	private static final String DIALOG_TITLE = "Delivery the book edition to the ESP platform";

	@Override
	protected String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		StringBuilder successResponse = new StringBuilder(
				"The delivery workflow has been started.");
		String publicationCaId = getMoSourceId(args);

		ManagedObjectService moService = context.getManagedObjectService();
		
		User user = context.getSession().getUser();

		ManagedObject publicationMo = moService.getManagedObject(user, publicationCaId);
		WorkflowLauncher.startWorkflowWithContext(context, user, publicationMo, "IET Standards ESP Delivery");

		return successResponse.toString() + ".";
	}

	@Override
	protected String getDialogTitle() {
		return DIALOG_TITLE;
	}
}