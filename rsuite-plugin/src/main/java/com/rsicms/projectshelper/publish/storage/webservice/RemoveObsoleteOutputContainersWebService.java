package com.rsicms.projectshelper.publish.storage.webservice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.rsicms.projectshelper.publish.storage.cleanup.ObsoleteOutputContainersRemover;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;

public class RemoveObsoleteOutputContainersWebService extends ProjectRemoteApiHandler {

	Log log = LogFactoryImpl.getLog(RemoveObsoleteOutputContainersWebService.class);

	@Override
	protected String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		User user = context.getSession().getUser();

		String outputsContainerId = getMoId(args);

		ObsoleteOutputContainersRemover remover = new ObsoleteOutputContainersRemover(context, user);

		remover.removeDuplicates(outputsContainerId);

		return "Obsolete containers have been removed";
	}

	@Override
	protected String getDialogTitle() {
		return "Remove obsolete containers from outputs container";
	}

	@Override
	protected boolean refreshParent() {
		return true;
	}

}
