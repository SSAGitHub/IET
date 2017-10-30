package org.theiet.rsuite.reporting;

import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.mocks.api.MockUtils;
import org.theiet.rsuite.mocks.api.pubtrack.ProcessMock;
import org.theiet.rsuite.mocks.api.remoteapi.CallArgumentListMock;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ContentAssemblyCreateOptions;
import com.reallysi.rsuite.api.pubtrack.Process;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.report.Report;
import com.reallysi.rsuite.api.report.ReportGenerationContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;

public class PublishedIssueReportTest implements JournalConstants {

	@Test
	public void testPublishedIssueReport() throws Exception {

		String reportId = "testId";
		ReportGenerationContext context = MockUtils.createReportBaseContext();

		ContentAssembly journalCA = createTestJournalCa(context);
		
		Map<String, String> arguments = new HashMap<String, String>();
		arguments.put(REPORT_JOURNAL_CODE, journalCA.getId());
		
		 

		CallArgumentList args = new CallArgumentListMock(context, arguments);

		ContentAssembly issueCa = createTestIssuelCa(context);
		
		ContentAssembly articlesCa = createTestArticlesContainerCa(context, issueCa);
		
		createTestArticleCAs(context, articlesCa);
		
		
		ContentAssemblyItem articlesChild = Mockito.mock(ContentAssemblyItem.class);
		Mockito.when(articlesChild.getId()).thenReturn(articlesCa.getId());
		Mockito.when(articlesChild.getType()).thenReturn(articlesCa.getId());
		
		
		
		Map<String, String> processMetadata = new HashMap<String, String>();
		processMetadata.put("OBJECT_ID", issueCa.getId());
		processMetadata.put(REPORT_VOLUME_NUMBER , "7");
		processMetadata.put(REPORT_ISSUE_NUMBER , "4");
		
		
		
		Process processMock = new ProcessMock(processMetadata);
		
		List<Process> pubtrackResult = new ArrayList<Process>();
		pubtrackResult.add(processMock);
		
		Mockito.when(context.getPubtrackManager().query(any(User.class), any(String.class))).thenReturn(pubtrackResult);
		
		PublishedIssueReport report = new PublishedIssueReport();
		Report result = report.generateReport(context, reportId, args);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(result.getInputStream());
		
		XPath xPath = XPathFactory.newInstance().newXPath();

		String months = (String)xPath.evaluate("/html/body/table/tr[2]/td[14]",
		        doc.getDocumentElement(), XPathConstants.STRING);
		
		String percentOfPublishedWithin12Months = (String)xPath.evaluate("/html/body/table/tr[4]/td[2]",
			        doc.getDocumentElement(), XPathConstants.STRING);

		Assert.assertEquals("5.8", percentOfPublishedWithin12Months);
		Assert.assertEquals("11.5", months);
			
	}

	private ContentAssembly createTestJournalCa(
			ReportGenerationContext context) throws RSuiteException {

		List<MetaDataItem> entries = new ArrayList<MetaDataItem>();
		entries.add(new MetaDataItem(LMD_FIELD_JOURNAL_CODE, "COM"));
		
		return createTestCa(context, "Comunnication", entries, null, null);
	}
	
	private ContentAssembly createTestIssuelCa(
			ReportGenerationContext context) throws RSuiteException {

		List<MetaDataItem> entries = new ArrayList<MetaDataItem>();
		entries.add(new MetaDataItem(LMD_FIELD_JOURNAL_CODE, "COM"));
		entries.add(new MetaDataItem(LMD_FIELD_PRINT_PUBLISHED_DATE, "2013-04-18"));
		
		
		return createTestCa(context, "4", entries, null, null);
	}
	
	private ContentAssembly createTestArticlesContainerCa(
			ReportGenerationContext context, ContentAssembly issueCA ) throws RSuiteException {

		List<MetaDataItem> entries = new ArrayList<MetaDataItem>();
		ContentAssemblyCreateOptions options = new ContentAssemblyCreateOptions();
		options.setType(CA_TYPE_ISSUE_ARTICLES);
		
		return createTestCa(context, "Articles", entries, options, issueCA.getId());
	}
	
	private List<ContentAssembly> createTestArticleCAs(
			ReportGenerationContext context, ContentAssembly articlesCA ) throws RSuiteException {

		List<MetaDataItem> entries = new ArrayList<MetaDataItem>();
		ContentAssemblyCreateOptions options = new ContentAssemblyCreateOptions();
		options.setType(CA_TYPE_ARTICLE);
		
		List<ContentAssembly> articleCAs = new ArrayList<ContentAssembly>();
		entries.add(new MetaDataItem(LMD_FIELD_SUBMITTED_DATE, "2012-05-09"));
		
		entries.add(new MetaDataItem(LMD_FIELD_AUTHOR_SURNAME, "Lacki"));
		entries.add(new MetaDataItem(LMD_FIELD_AUTHOR_FIRST_NAME, "Lukas"));
		entries.add(new MetaDataItem(LMD_FIELD_AUTHOR_EMAIL, "llacki@gmail.com"));
		
		entries.add(new MetaDataItem(LMD_FIELD_DECISION_DATE, "2012-11-20"));
		
		entries.add(new MetaDataItem(LMD_FIELD_TYPESET_PAGES, "9"));
		
	
		
		
		ContentAssembly aritcle1=  createTestCa(context, "COM-2012-0247.R2", entries, options, articlesCA.getId());
		
		entries.clear();
		entries.add(new MetaDataItem(LMD_FIELD_SUBMITTED_DATE, "2012-06-29"));
		
		entries.add(new MetaDataItem(LMD_FIELD_AUTHOR_SURNAME, "Lacki"));
		entries.add(new MetaDataItem(LMD_FIELD_AUTHOR_FIRST_NAME, "Lukas"));
		entries.add(new MetaDataItem(LMD_FIELD_AUTHOR_EMAIL, "llacki@gmail.com"));
		
		entries.add(new MetaDataItem(LMD_FIELD_DECISION_DATE, "2012-11-29"));

		entries.add(new MetaDataItem(LMD_FIELD_TYPESET_PAGES, "10"));		
		
		ContentAssembly aritcle2=  createTestCa(context, "COM-2012-0315.R2", entries, options, articlesCA.getId());
		
		articleCAs.add(aritcle2);
		articleCAs.add(aritcle1);
		
		
		return articleCAs; 
	}

	private ContentAssembly createTestCa(ReportGenerationContext context, String name, List<MetaDataItem> entries, ContentAssemblyCreateOptions options, String parentId) throws RSuiteException{
		ManagedObjectService moSvcMock = context.getManagedObjectService();

		User user = context.getSession().getUser();
		ContentAssemblyService caSvcMock = context.getContentAssemblyService();
		ContentAssembly testCA = caSvcMock.createContentAssembly(user, parentId,
				name, options);

		moSvcMock.setMetaDataEntries(user, testCA.getId(), entries);
		
		
		return testCA;
	}
}
