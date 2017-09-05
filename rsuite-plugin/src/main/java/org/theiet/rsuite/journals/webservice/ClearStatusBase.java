package org.theiet.rsuite.journals.webservice;

import com.reallysi.rsuite.api.remoteapi.result.MessageType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.utils.ContentDisplayUtils;
import org.theiet.rsuite.utils.IetUtils;
import org.theiet.rsuite.utils.PubtrackLogger;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;

public abstract class ClearStatusBase extends DefaultRemoteApiHandler implements JournalConstants {
	
	@Override
	public MessageDialogResult execute (
			RemoteApiExecutionContext context,
			CallArgumentList args) {
		Log log = LogFactory.getLog(ClearStatusBase.class);
		String articleNodeId = args.getFirstValue("rsuiteId");		
		User user = context.getSession().getUser();
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		ManagedObjectService moSvc = context.getManagedObjectService();
		try {
			ContentAssemblyItem articleCa = ProjectContentAssemblyUtils.getAncestorCAbyType(
					context, articleNodeId, CA_TYPE_ARTICLE);
			ContentAssembly ca = caSvc.getContentAssembly(user, articleNodeId);
			IetUtils.removeMetaDataFieldFromCa(log, user, moSvc, ca, getLmdField());
			if (getEventName() != null) {
				PubtrackLogger.logToProcess(user, context, log, "ARTICLE", ca.getDisplayName(), getEventName());
			}

			return ContentDisplayUtils.getResultWithLabelRefreshing(
					MessageType.SUCCESS, getDialogTitle(), "<html><body><div><p>" + getMessage() + "</p></div></body></html>", "500", articleCa.getId());
		}
		catch (RSuiteException e) {
			return new MessageDialogResult(MessageType.ERROR, getDialogTitle(), "Server returned " + e.getMessage());
		}
	}
	
	abstract protected String getDialogTitle();
	
	abstract protected String getMessage();
	
	abstract protected String getLmdField();
	
	abstract protected String getEventName();
}
