package org.theiet.rsuite.journals.utils;

import static org.theiet.rsuite.journals.JournalConstants.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.theiet.rsuite.journals.domain.article.delivery.digitallibrary.ArticleDigitalLibraryPackageBuilder;
import org.theiet.rsuite.journals.domain.journal.Journal;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.AuthorizationService;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.SearchService;

public class DigitalLibraryUtilsTest {

	@Test
	public void test_getDlFinalFileName_code_bmt_prefix_required()
			throws RSuiteException {
		String articleId = "BMT-2013-0044.R1";
		String ext = "pdf";
		String fileName = getDigitalLibraryFilename(articleId, ext, null);
		assertEquals("IET-BMT.2013.0044.pdf", fileName);
	}

	@Test
	public void test_getDlFinalFileName_code_ell_no_prefix_required()
			throws RSuiteException {
		String articleId = "ELL-2013-0044.R1";
		String ext = "pdf";
		String fileName = getDigitalLibraryFilename(articleId, ext, "no");
		assertEquals("EL.2013.0044.pdf", fileName);
	}

	@Test
	public void test_getDlFinalFileName_code_htl_no_prefix_required()
			throws RSuiteException {
		String articleId = "HTL-2013-0044.R1";
		String ext = "pdf";
		String fileName = getDigitalLibraryFilename(articleId, ext, "no");

		assertEquals("HTL.2013.0044.pdf", fileName);
	}

	@Test
	public void test_getDlFinalFileName_code_mnl_no_prefix_required()
			throws RSuiteException {
		String articleId = "MNL-2013-0044";
		String ext = "pdf";
		String fileName = getDigitalLibraryFilename(articleId, ext, "no");
		assertEquals("MNL.2013.0044.pdf", fileName);
	}

	@Test
	public void test_getFixedJournalName_code_mnl_no_prefix_required()
			throws RSuiteException {

		Journal journalMock = createJournalMock("MNL");

		String fileName = ArticleDigitalLibraryPackageBuilder.getFixedJournalName(journalMock);
		assertEquals("MNL", fileName);
	}

	public Journal createJournalMock(String journalCode) {
		Journal journalMock = mock(Journal.class);
		when(journalMock.getJournalCode()).thenReturn(journalCode);
		return journalMock;
	}

	@Test
	public void test_getFixedJournalName_code_bmt_no_prefix_required()
			throws RSuiteException {
		ExecutionContext context = stubExecutionContext(null);
		Journal journalMock = new Journal(context, "BMT");
		String fileName = ArticleDigitalLibraryPackageBuilder.getFixedJournalName(journalMock);
		assertEquals("IET-BMT", fileName);
	}

	@Test
	public void test_getFixedJournalName_code_htl_no_prefix_required()
			throws RSuiteException {

		ExecutionContext context = stubExecutionContext("no");
		Journal journalMock = new Journal(context, "HTL");

		String fileName = ArticleDigitalLibraryPackageBuilder.getFixedJournalName(journalMock);
		assertEquals("HTL", fileName);
	}

	@Test
	public void test_getFixedJournalName_code_ell_no_prefix_required()
			throws RSuiteException {
		ExecutionContext context = stubExecutionContext(null);
		Journal journalMock = new Journal(context, "ELL");
		String fileName = ArticleDigitalLibraryPackageBuilder.getFixedJournalName(journalMock);
		assertEquals("EL", fileName);
	}
	
	@Test
	public void test_getDlFinalFileName_code_conference_prefix_required()
			throws RSuiteException {
		String articleId = "CIR-2017-0044";
		String ext = "pdf";
		String fileName = getDigitalLibraryFilename(articleId, ext, null, "OAP-CIRED");
		assertEquals("OAP-CIRED.2017.0044.pdf", fileName);
	}
	
	@Test
	public void test_getFixedJournalName_code_conference_prefix_required()
			throws RSuiteException {
		ExecutionContext context = stubExecutionContext(null, "OAP-CIRED");
		Journal journalMock = new Journal(context, "CIR");
		String fileName = ArticleDigitalLibraryPackageBuilder.getFixedJournalName(journalMock);
		assertEquals("OAP-CIRED", fileName);
	}
	
	public String getDigitalLibraryFilename(String articleId, String ext,
			String addPrefixLmdValue, String prefixLmdValue) throws RSuiteException {

		ExecutionContext context = stubExecutionContext(addPrefixLmdValue, prefixLmdValue);

		return ArticleDigitalLibraryPackageBuilder.createDigitalLibraryFinalFileName(context, articleId, ext);
	}

	public String getDigitalLibraryFilename(String articleId, String ext,
			String addPrefixLmdValue) throws RSuiteException {

		ExecutionContext context = stubExecutionContext(addPrefixLmdValue);

		return ArticleDigitalLibraryPackageBuilder.createDigitalLibraryFinalFileName(context, articleId, ext);
	}

	public ExecutionContext stubExecutionContext(String addPrefixLmdValue) throws RSuiteException {
		return stubExecutionContext(addPrefixLmdValue, null);
	}
	
	public ExecutionContext stubExecutionContext(String addPrefixLmdValue, String prefixLmdValue)
			throws RSuiteException {
		ExecutionContext context = mock(ExecutionContext.class);
		SearchService searchSvc = mock(SearchService.class);

		AuthorizationService authSvc = mock(AuthorizationService.class);
		ContentAssemblyService caSvc = mock(ContentAssemblyService.class);
		ManagedObject journalMo = mock(ManagedObject.class);

		List<ManagedObject> searchJournalResults = new ArrayList<ManagedObject>();
		searchJournalResults.add(journalMo);

		when(
				searchSvc.executeXPathSearch(any(User.class), anyString(),
						anyInt(), anyInt())).thenReturn(searchJournalResults);
		when(context.getAuthorizationService()).thenReturn(authSvc);
		when(context.getSearchService()).thenReturn(searchSvc);
		when(context.getContentAssemblyService()).thenReturn(caSvc);

		ContentAssembly journalCa = mock(ContentAssembly.class);

		when(
				journalCa
						.getLayeredMetadataValue(LMD_FIELD_ADD_PREFIX_DIGITAL_LIBRARY_DELIVERY))
				.thenReturn(addPrefixLmdValue);
		
		if (StringUtils.isNotBlank(prefixLmdValue)) {
			when(
					journalCa
							.getLayeredMetadataValue(LMD_FIELD_PREFIX_DIGITAL_LIBRARY_DELIVERY))
					.thenReturn(prefixLmdValue);
		}

		when(caSvc.getContentAssembly(any(User.class), anyString()))
				.thenReturn(journalCa);
		return context;
	}

}
