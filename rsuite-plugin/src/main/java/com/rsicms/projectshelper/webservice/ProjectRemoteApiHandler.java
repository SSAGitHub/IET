package com.rsicms.projectshelper.webservice;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.*;
import org.theiet.rsuite.utils.ContentDisplayUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.api.remoteapi.result.*;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public abstract class ProjectRemoteApiHandler extends DefaultRemoteApiHandler {

	private Log log = LogFactory.getLog(ProjectRemoteApiHandler.class);

	public MessageDialogResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) {

		String rsuitePath = getRSuitePath(args);

		String moIdToRefresh = null;

		String message = "";

		try {
			message = exectuteAction(context, args);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new MessageDialogResult(MessageType.ERROR, getDialogTitle(),
					"Server returned error " + e.getMessage());
		}

		if (refreshParent()) {
			moIdToRefresh = ProjectBrowserUtils.getParentIdFromPath(rsuitePath);
		} else if (getMoIdToRefresh() != null) {
			moIdToRefresh = getMoIdToRefresh();
		}

		return ContentDisplayUtils.getResultWithLabelRefreshing(
				MessageType.SUCCESS, getDialogTitle(), "<html><body><div><p>"
						+ message + "</p></div></body></html>",
				getDialogWidth(), moIdToRefresh);
	}

	public String getMoIdToRefresh() {
		return null;
	}

	public static String getMoId(CallArgumentList args) {
		return args.getFirstValue("rsuiteId");
	}

	public User getUser(RemoteApiExecutionContext context) {
		return context.getSession().getUser();
	}

	public String getMoSourceId(CallArgumentList args) {
		return args.getFirstValue("sourceId");
	}

	public String getRSuitePath(CallArgumentList args) {
		return args.getFirstString("rsuiteBrowseUri");
	}
	
	public String getRsuiteId (CallArgumentList args) {
		String rsuiteId = args.getFirstValue("sourceId");

       if (StringUtils.isBlank(rsuiteId)){
    	   rsuiteId = args.getFirstValue("rsuiteId");
       }

       return rsuiteId;
	}

	protected String getDialogWidth() {
		return "500";
	}

	protected boolean refreshParent() {
		return false;
	}

	protected abstract String getDialogTitle();

	protected abstract String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException;
}