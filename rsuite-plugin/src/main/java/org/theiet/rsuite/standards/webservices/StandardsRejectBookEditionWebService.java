package org.theiet.rsuite.standards.webservices;

import org.theiet.rsuite.standards.StandardsBooksConstans;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;

public class StandardsRejectBookEditionWebService extends ProjectRemoteApiHandler
		implements StandardsBooksConstans {

	@Override
	protected String getDialogTitle() {
		return "Reject Edition";
	}

	@Override
	protected String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		User user = context.getSession().getUser();

		String editionCaId = getMoId(args);
		ManagedObjectService moSvc = context.getManagedObjectService();
		moSvc.setMetaDataEntry(user, editionCaId, new MetaDataItem(
				StandardsBooksConstans.LMD_FIELD_BOOK_STATUS, StandardsBooksConstans.LMD_VALUE_REJECTED));

		return "The editon book has been rejected";
	}

	@Override
	protected boolean refreshParent() {
		return true;
	}

}
