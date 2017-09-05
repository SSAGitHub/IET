package org.theiet.rsuite.journals.webservice.issue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadata;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadataFields;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.mocks.api.ContentAssemblyMock;

import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;

public class SendIssueFinalRequestToTypesetterWebServiceTestable  extends SendIssueFinalRequestToTypesetterWebService{

	private String issueAutomatedWorkflowLmd = "";
	
	private boolean journalAutomatedWorkflow = true;
	
	SendIssueFinalRequestToTypesetterWebServiceTestable(String issueAutomatedWorkflowLmd){
		this.issueAutomatedWorkflowLmd = issueAutomatedWorkflowLmd; 
	}
	
	SendIssueFinalRequestToTypesetterWebServiceTestable(boolean journalAutomatedWorkflow, String issueAutomatedWorkflowLmd){
		this.issueAutomatedWorkflowLmd = issueAutomatedWorkflowLmd;
		this.journalAutomatedWorkflow = journalAutomatedWorkflow; 
	}
	
	@Override
	protected void sendFinalRequestToTypesetter(
			RemoteApiExecutionContext context, CallArgumentList args,
			User user, Issue issue) throws RSuiteException {		
	}
	
	@Override
	protected Issue obtainIssue(RemoteApiExecutionContext context,
			CallArgumentList args, User user) throws RSuiteException {
		Issue issue = mock(Issue.class);
		
		Journal journal = mock(Journal.class);
		when(journal.isAutomaticPdfGenerationWorkflow()).thenReturn(journalAutomatedWorkflow);
		when(issue.getJournal()).thenReturn(journal);
		
		IssueMetadata issueMetadata = createIssueMetadata();
		
		when(issue.getIssueMetadata()).thenReturn(issueMetadata);
		
		
		return issue;
	}

	protected IssueMetadata createIssueMetadata() {
		List<MetaDataItem> metadata = new ArrayList<MetaDataItem>();
		metadata.add(new MetaDataItem(IssueMetadataFields.LMD_FIELD_ISSUE_AUTOMATED_PDF_GENERATION_WORKFLOW, issueAutomatedWorkflowLmd));		
		return new IssueMetadata(new ContentAssemblyMock(metadata));
	}
}
