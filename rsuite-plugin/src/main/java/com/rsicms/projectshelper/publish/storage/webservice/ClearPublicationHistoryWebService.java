package com.rsicms.projectshelper.publish.storage.webservice;

import static com.rsicms.projectshelper.webservice.WebserviceParameters.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.rsicms.projectshelper.publish.storage.cleanup.ClearPublicationHistory;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;

public class ClearPublicationHistoryWebService extends ProjectRemoteApiHandler {

	Log log = LogFactoryImpl.getLog(ClearPublicationHistoryWebService.class);

	@Override
	protected String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		User user = context.getSession().getUser();

		String historyCaId = args.getFirstString(RSUITE_ID.getParameterName());

		ClearPublicationHistory clearPublicationHistory = new ClearPublicationHistory(context, user);

		clearPublicationHistory.startHistoryCleanup(historyCaId);

		return "Publication history has been cleaned up";
	}

	@Override
	protected String getDialogTitle() {
		return "Clear Publication history";
	}

	@Override
	protected boolean refreshParent() {
		return true;
	}

}
