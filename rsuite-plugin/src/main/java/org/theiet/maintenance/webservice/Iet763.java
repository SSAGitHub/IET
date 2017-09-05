package org.theiet.maintenance.webservice;

import java.io.File;

import org.theiet.maintenance.domain.Iet763Helper;
import org.theiet.maintenance.domain.MaintenanceUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.rsicms.rsuite.helpers.webservice.XmlRemoteApiResult;

/**
 * Custom RSuite web service to save search results to user's clipboard
 * 
 */
public class Iet763 extends DefaultRemoteApiHandler {

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		Session sess = context.getSession();
		User user = sess.getUser();

		String maintenanceIssueId = "iet763";

		File logFile = MaintenanceUtils.createMaintenanceLogFile(context,
				maintenanceIssueId);

		Iet763Helper helper = new Iet763Helper();

		helper.removeAliasesFromSubMo(context, user, logFile);

		return new XmlRemoteApiResult("<result />");

	}

}
