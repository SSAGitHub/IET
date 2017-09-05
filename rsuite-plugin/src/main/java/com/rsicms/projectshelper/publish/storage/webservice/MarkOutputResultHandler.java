package com.rsicms.projectshelper.publish.storage.webservice;

import static com.rsicms.projectshelper.publish.storage.datatype.OutputLmd.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.rsicms.projectshelper.lmd.MetadataUtils;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;

public abstract class MarkOutputResultHandler extends ProjectRemoteApiHandler 
	 {

	@Override
	protected String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		User user = context.getSession().getUser();
		
		String outputResultId = getMoId(args);
		
		MetadataUtils.setMetadata(context, user, outputResultId, IS_REMOVABLE_PUBLICATION_RESULT.getLmdName(), isRemovablePublicationResultValue());

		return "Output result has been marked as " + getMarkAs();
	}

	@Override
	protected String getDialogTitle() {
		return "Mark output result";
	}
	
	public abstract String isRemovablePublicationResultValue();

	public abstract String getMarkAs();

}
