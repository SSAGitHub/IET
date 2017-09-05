package org.theiet.rsuite.journals.domain.issues;

import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadata;
import org.theiet.rsuite.journals.domain.journal.Journal;

import com.reallysi.rsuite.api.RSuiteException;

public class IssuePublishWorkflowChecker {

	private IssuePublishWorkflowChecker(){
		
	}
	
	public static boolean isAutomatedPublishWorkflowActiveForIssue(Issue issue) throws RSuiteException{
		Journal journal = issue.getJournal();
		IssueMetadata issueMetadata = issue.getIssueMetadata();
		
		if (journal.isAutomaticPdfGenerationWorkflow() && issueMetadata.isAutomaticPdfGenerationWorkflow()){
			return true;
		}
		
		return false;
	}
	
	public static boolean isTypestterWorkflowActiveForIssue(Issue issue) throws RSuiteException{
		return !isAutomatedPublishWorkflowActiveForIssue(issue);
	}
}
