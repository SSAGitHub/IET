package org.theiet.rsuite.journals.webservice.issue;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.domain.permissions.PermissionsUtils;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.issues.IssueCreator;
import org.theiet.rsuite.journals.utils.JournalBrowserUtils;
import org.theiet.rsuite.utils.PubtrackLogger;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.reallysi.rsuite.service.ProcessInstanceService;
import com.reallysi.rsuite.service.SecurityService;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

/**
 * Custom RSuite web service to sent email
 * 
 */
public class CreateNewIssueWebService extends DefaultRemoteApiHandler implements
		JournalConstants {

	private static final String WEB_SERVICE_LABEL = "Create a new issue";

	private static Log log = LogFactory.getLog(CreateNewIssueWebService.class);

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		try {
			SecurityService secSvc = context.getSecurityService();
			User user = context.getSession().getUser();

			String year = args.getFirstValue("year");
			String volume = args.getFirstValue("volume");
			String issue = args.getFirstValue("issue");

			String journalCaId = args.getFirstManagedObject(user).getId();
			String contextRsuiteId = args.getFirstValue(PARAM_RSUITE_ID);

			

			ContentAssembly issueCa = IssueCreator.createIssue(context, user, journalCaId, year,
					volume, issue);
			String issueCaId = issueCa.getId();
			log.info("execute: created new issue " + issueCaId);
			ContentAssembly newIssueCa = context.getContentAssemblyService().getContentAssembly(user, issueCaId);
			log.info("execute: recovered new issue " + newIssueCa.getId());
			String issueCode = newIssueCa.getLayeredMetadataValue(LMD_FIELD_ISSUE_CODE);
			log.info("execute: issueCode is " + issueCode);
			User systemUser = context.getAuthorizationService().getSystemUser();
			PermissionsUtils.setAdminOnlyDeleteACL(secSvc, systemUser, issueCaId, log);
			ContentAssembly articlesCa = ProjectBrowserUtils.getChildCaByType(context, newIssueCa, JournalConstants.CA_TYPE_ISSUE_ARTICLES);
			String articlesCaId = articlesCa.getId();
			log.info("execute: articles CA is " + articlesCaId);
			PermissionsUtils.setAdminOnlyDeleteACL(secSvc, systemUser, articlesCaId, log);
			ContentAssemblyItem journalCa = JournalBrowserUtils.getAncestorJournal(context, contextRsuiteId);
			String journalCode = journalCa.getLayeredMetadataValue(LMD_FIELD_JOURNAL_CODE);
			ProcessInstanceService piSvc = context.getProcessInstanceService();
			HashMap<String, Object> varMap = new HashMap<String, Object>();
			varMap.put(WF_VAR_PRODUCT_ID, issueCode);
			varMap.put(WF_VAR_PRODUCT, WF_VAR_ISSUE);
			varMap.put(WF_VAR_JOURNAL_ID, journalCode);
			varMap.put(WF_VAR_RSUITE_CONTENTS, issueCaId);
			HashMap<String, String> additionalParams = new HashMap<String, String>();
			additionalParams.put("YEAR", year);
			additionalParams.put("JOURNAL_CODE", journalCode);
			additionalParams.put("ISSUE_NUMBER", issue);
			additionalParams.put("VOLUME_NUMBER", volume);
			piSvc.createAndStart(user, JournalConstants.WF_PREPARE_ISSUE, varMap);
			PubtrackLogger.createProcess(user, context, log, WF_VAR_ISSUE, issueCode, issueCaId, additionalParams);
			return new MessageDialogResult(
					MessageType.SUCCESS,
					WEB_SERVICE_LABEL,
					"<html><body><div><p>A new issue has been created.</p></div></body></html>",
					"500");
		} catch (Exception e) {
			log.error("Unable to create issue", e);
			return new MessageDialogResult(MessageType.ERROR,
					WEB_SERVICE_LABEL, "Error: " + e.getMessage());
		}

	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
