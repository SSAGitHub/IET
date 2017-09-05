package org.theiet.maintenance.webservice;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.rsicms.rsuite.helpers.webservice.XmlRemoteApiResult;

/**
 * Custom RSuite web service to remove users
 * 
 */
public class Iet917 extends DefaultRemoteApiHandler {

	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */

	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		StringBuffer removedUsers = new StringBuffer();
		StringBuffer notFoundUsers = new StringBuffer();
		String usersForRemoval = args.getFirstString("usersForRemoval");
		if (usersForRemoval != null) {
			String userForRemoval[] = usersForRemoval.split("\\,");
			
			for (int i = 0; i < userForRemoval.length; i++) {
				List<String> users = Arrays.asList(context.getAuthorizationService().getAllUsers());
				if (users.contains(userForRemoval[i])) {
					context.getAuthorizationService().getLocalUserManager().removeUser(userForRemoval[i]);
					removedUsers.append("<user>" + userForRemoval[i] + "</user>");
					log.info("removed user: " + userForRemoval[i]);
				} else {
					notFoundUsers.append("<user>" + userForRemoval[i] + "</user>");
					log.info("user not found, therefore not removed: " + userForRemoval[i]);
				}
				
			}
		}
		
		return new XmlRemoteApiResult("<result><removed_users> " + removedUsers.toString() + " </removed_users><not_found_users>" + notFoundUsers.toString() + "</not_found_users></result>");
	}

}
