package org.theiet.rsuite.journals.webservice.issue;

import org.theiet.rsuite.journals.domain.issues.IssuePublishWorkflowChecker;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.publish.proof.IssueProofPdfWorkflowLauncher;
import org.theiet.rsuite.journals.domain.journal.Journal;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;

/**
 * Custom RSuite web service to sent email
 * 
 */
public class GenerateIssueProofPdfWebService extends ProjectRemoteApiHandler {

    private static final String WEB_SERVICE_LABEL = "Generate Issue Proof PDF";

	@Override
	protected String getDialogTitle() {
		return WEB_SERVICE_LABEL;
	}

	@Override
	protected String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {
		
		User user = context.getSession().getUser();
		Issue issue = new Issue(context, args, user);
		Journal journal = issue.getJournal();
		
		if (IssuePublishWorkflowChecker.isTypestterWorkflowActiveForIssue(issue)){
			return  "This option is not available for " + journal.getJournalCode() + " journal. The typesetter workflow is active.";
		}
		
		IssueProofPdfWorkflowLauncher.launchProofPdfWorkflowForIssue(context, args, issue);
 
		return "Publish process has been started.";
	}

}
