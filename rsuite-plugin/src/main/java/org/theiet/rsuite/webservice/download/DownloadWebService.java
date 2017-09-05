package org.theiet.rsuite.webservice.download;

import org.theiet.rsuite.domain.download.DownloadWebserviceHelper;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.content.ContentObjectPath;
import com.reallysi.rsuite.api.remoteapi.*;

public class DownloadWebService extends DefaultRemoteApiHandler {

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context, CallArgumentList args) throws RSuiteException {

		String rsuiteId = args.getFirstValue("rsuiteId");
		User user = context.getSession().getUser();

		ManagedObject mo = context.getManagedObjectService().getManagedObject(user, rsuiteId);
		ContentObjectPath objectPath = args.getContentObjectPath(user, 0);
		String variant = args.getFirstString("variantName");
		DownloadWebserviceHelper downloadHelper = new DownloadWebserviceHelper(context, user);
		return downloadHelper.createWebServiceReuslt(objectPath, mo, variant);

	}
}
