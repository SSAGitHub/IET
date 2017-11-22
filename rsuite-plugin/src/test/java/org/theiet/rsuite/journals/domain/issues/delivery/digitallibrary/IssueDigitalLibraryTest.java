package org.theiet.rsuite.journals.domain.issues.delivery.digitallibrary;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadata;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.AuthorizationService;

public class IssueDigitalLibraryTest {
	
	
	@Test
	public void should_create_issue_file_name_with_custom_digitial_library_prefix() throws RSuiteException {
		
		ExecutionContext context = mock(ExecutionContext.class);
		AuthorizationService authorizationService = mock(AuthorizationService.class);
		when(context.getAuthorizationService()).thenReturn(authorizationService);
		IssueDigitalLibrary issueDigitalLibrary = new  IssueDigitalLibrary(context, null, null);
		Issue issueStub = mock(Issue.class);
		
		ContentAssembly journalCa = mock(ContentAssembly.class);
		when(journalCa.getLayeredMetadataValue(Mockito.eq("journal_code"))).thenReturn("CRI");
		when(journalCa.getLayeredMetadataValue(Mockito.eq("prefix_digital_library_delivery"))).thenReturn("OAP-CRIED");
		
		when(issueStub.getJournalCa()).thenReturn(journalCa);
		
		IssueMetadata issueMetadata = mock(IssueMetadata.class);
		when(issueMetadata.getIssueCode()).thenReturn("CRI-2017-1-1");
		
		when(issueStub.getIssueMetadata()).thenReturn(issueMetadata);
		
		String archiveFileName = issueDigitalLibrary.createIssueArchiveFileName(issueStub);
		
		assertThat(archiveFileName, is("OAP-CRIED-2017-1-1.zip"));
	}

}
