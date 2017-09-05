package org.theiet.rsuite.reporting;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.xml.stream.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.*;
import org.joda.time.*;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.utils.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.pubtrack.*;
import com.reallysi.rsuite.api.pubtrack.Process;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.report.*;
import com.reallysi.rsuite.service.*;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class PublishedIssueReport extends DefaultReportGenerator implements
		JournalConstants {

	private static final int TWELVE_MONTHS = 12;

	private static final double AVG_DAYS_IN_MONTH = 30.0;

	private class SummaryInfo {

		private List<Integer> daysSubmitToEFirstPublication = new ArrayList<Integer>();

		private List<Integer> daysAcceptToEFirstPublication = new ArrayList<Integer>();

		private List<Integer> daysAcceptToPrintPublication = new ArrayList<Integer>();
		
		private List<Integer> daysSubmitToPrintPublication = new ArrayList<Integer>();

		private List<Integer> daysSubmitToAccept = new ArrayList<Integer>();

		private int totalArticles = 0;

		private int articlesWithin12MonthsAccept = 0;
		
		private int articlesWithin12MonthsPrintPublication = 0;

	}

	private static Log log = LogFactory.getLog(PublishedIssueReport.class);

	private static final String XSLT_URI = "rsuite:/res/plugin/iet/xslt/journal-report/journal-report.xsl";

	private static final SimpleDateFormat REPORT_DATE_STRING_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final double DAYS_TO_MONTHS = 1.0 / AVG_DAYS_IN_MONTH;

	private static final DecimalFormat MONTH_FORMAT = new DecimalFormat("#0.0");

	private static final SimpleDateFormat DATE_PICKER_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	private static final SimpleDateFormat HQL_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	private static final String[] REPORT_ENTRIES = { "author", "email", "institution",
			"country", "licence", "submitted", "accepted",
			"article-pass-for-press", "issue-pass-for-press", "submit-to-acceptance",
			"submit-to-e-first-publication", "accept-to-e-first-publication",
			"submit-to-print-publication", "accept-to-print-publication",
			"volume", "issue", "typeset_pages" };

	private static final String[] REPORT_ENTRY_CLASS = { "ca", "ra", "ca", "ca", "ca",
			"ca", "ra", "ca", "ca", "ca", "ca", "ca", "ca", "ca", "ra", "ra", "ra" };

	public Report generateReport(ReportGenerationContext context,
			String reportId, CallArgumentList args) throws RSuiteException {

		try {

			PublishedIssueReportInputParameters inputParmaters = new PublishedIssueReportInputParameters(
					context, args);

			byte[] xmlSourceBytes = getXmlReport(context, inputParmaters);

			String reportHTML = ArticleReportHelper.transformToHTML(context, xmlSourceBytes, XSLT_URI);

			return new HTMLReport(reportHTML);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new HTMLReport(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private byte[] getXmlReport(ReportGenerationContext context,
			PublishedIssueReportInputParameters inputParameters)
			throws RSuiteException {
		byte[] bytes = null;
		try {

			User user = context.getSession().getUser();
			PubtrackManager ptMgr = context.getPubtrackManager();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			OutputStreamWriter streamWriter = new OutputStreamWriter(out);

			String hqlQuery = createHqlQuery(inputParameters);
			log.info("generateReport: HQL is\n" + hqlQuery);

			XMLStreamWriter xmlWriter = StaxUtils.setUpWriter(streamWriter);

			xmlWriter.writeStartDocument("UTF-8", "1.0");
			xmlWriter.writeStartElement("production-report");

			List<Process> issueProcessList = ptMgr.query(user, hqlQuery);

			log.info("getReportBytes: query returned "
					+ issueProcessList.size());

			SummaryInfo summaryInfo = new SummaryInfo();

			for (Process issueProcess : issueProcessList) {

				processIssue(context, xmlWriter, issueProcess, summaryInfo,
						inputParameters);
			}

			createSummaryInfoElement(xmlWriter, summaryInfo);

			xmlWriter.writeEndElement();
			xmlWriter.writeEndDocument();
			streamWriter.close();
			bytes = out.toByteArray();

		} catch (FactoryConfigurationError e) {
			ExceptionUtils.throwRsuiteException(e);
		} catch (XMLStreamException e) {
			ExceptionUtils.throwRsuiteException(e);
		} catch (IOException e) {
			ExceptionUtils.throwRsuiteException(e);
		} catch (ParseException e) {
			ExceptionUtils.throwRsuiteException(e);
		}

		return bytes;
	}

	private void processIssue(ReportGenerationContext context,
			XMLStreamWriter xmlWriter, Process issueProcess,
			SummaryInfo summaryInfo,
			PublishedIssueReportInputParameters inputParameters)
			throws RSuiteException, ParseException, XMLStreamException {

		Set<String> exclusions = inputParameters.getArticleTypesToExclude();

		ContentAssemblyService caSvc = context.getContentAssemblyService();
		User user = context.getSession().getUser();

		Map<String, String> issueProcessMetadata = getIssueProcessMetadata(issueProcess);

		String issueCaId = String
				.valueOf(issueProcessMetadata.get("OBJECT_ID"));

		ContentAssembly issueCa = caSvc.getContentAssembly(user, issueCaId);

		if (issueCa == null) {
			log.warn("There is no issue ca " + issueCaId
					+ " for pubtrack process " + issueProcess.getId());
		}

		String printPubDateFromPubtrack = issueCa
				.getLayeredMetadataValue(LMD_FIELD_PRINT_PUBLISHED_DATE);
		issueProcessMetadata.put(LMD_FIELD_PRINT_PUBLISHED_DATE,
				printPubDateFromPubtrack);

		List<ContentAssembly> articleCas = getIssueArticles(context, issueCa);
		for (ContentAssembly articleCa : articleCas) {

			String articleType = articleCa
					.getLayeredMetadataValue(LMD_FIELD_ARTICLE_TYPE);

			if (articleType != null && exclusions.contains(articleType)) {
				continue;
			}

			Map<String, String> articleDetails = getArticleDetails(articleCa,
					issueProcessMetadata);

			collectSummaryInfo(summaryInfo, articleDetails);

			serializeArticleDetails(xmlWriter, articleDetails);
		}

	}

	private List<ContentAssembly> getIssueArticles(
			ReportGenerationContext context, ContentAssembly issueCa)
			throws RSuiteException {
		ContentAssembly issueArticles = ProjectBrowserUtils.getChildCaByType(context,
				issueCa, JournalConstants.CA_TYPE_ISSUE_ARTICLES);
		if (issueArticles == null) {
			throw new RSuiteException(
					"No issueArticles container for issue caId "
							+ issueCa.getId());
		}

		List<ContentAssembly> articleCas = ProjectBrowserUtils.getChildrenCaByType(
				context, issueArticles, JournalConstants.CA_TYPE_ARTICLE);
		return articleCas;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getIssueProcessMetadata(Process issueProcess)
			throws RSuiteException {
		Set<ProcessMetaDataItem> metaSet = issueProcess.getMetaData();

		Map<String, String> pubtrackMetadata = new HashMap<String, String>();

		for (ProcessMetaDataItem processMetaItem : metaSet) {
			pubtrackMetadata.put(processMetaItem.getName(),
					processMetaItem.getValue());
		}

		if (!pubtrackMetadata.containsKey("OBJECT_ID")) {
			throw new RSuiteException("No OBJECT_ID for pubtrack process "
					+ issueProcess.getId());
		}
		return pubtrackMetadata;
	}

	private void createSummaryInfoElement(XMLStreamWriter xmlWriter,
			SummaryInfo summaryInfo) throws XMLStreamException {
		xmlWriter.writeStartElement("summary");

		createSummaryEntry(xmlWriter, "avg-submit-to-accept",
				summaryInfo.daysSubmitToAccept);
		createSummaryEntry(xmlWriter, "avg-submit-to-e-first-publication",
				summaryInfo.daysSubmitToEFirstPublication);

		createSummaryEntry(xmlWriter, "avg-accept-to-e-first-publication",
				summaryInfo.daysAcceptToEFirstPublication);
		createSummaryEntry(xmlWriter, "avg-submit-to-print-publication",
				summaryInfo.daysSubmitToPrintPublication);
		createSummaryEntry(xmlWriter, "avg-accept-to-print-publication",
				summaryInfo.daysAcceptToPrintPublication);

		xmlWriter.writeEndElement();

		String percentWithin12Accept = "N/A";
		String percentWithin12PrintPublication = "N/A";
		if (summaryInfo.totalArticles > 0) {
			double acceptPercent = new Double(summaryInfo.articlesWithin12MonthsAccept)
					/ new Double(summaryInfo.totalArticles) * 100;
			percentWithin12Accept = MONTH_FORMAT.format(acceptPercent);
			
			double printPublicationPercent = new Double(summaryInfo.articlesWithin12MonthsPrintPublication)
					/ new Double(summaryInfo.totalArticles) * 100;
			percentWithin12PrintPublication = MONTH_FORMAT.format(printPublicationPercent);
		}

		xmlWriter.writeStartElement("percent-within-12");
		xmlWriter.writeStartElement("percent-within-12-to-accept");
		xmlWriter.writeCharacters(percentWithin12Accept);
		xmlWriter.writeEndElement();
		xmlWriter.writeStartElement("percent-within-12-to-print-publication");
		xmlWriter.writeCharacters(percentWithin12PrintPublication);
		xmlWriter.writeEndElement();
	}

	private void createSummaryEntry(XMLStreamWriter xmlWriter,
			String elementName, List<Integer> daysList)
			throws XMLStreamException {
		String avarageValue = computeAverge(daysList);
		xmlWriter.writeStartElement(elementName);
		xmlWriter.writeCharacters(avarageValue);
		xmlWriter.writeEndElement();
	}

	private void collectSummaryInfo(SummaryInfo summaryInfo,
			Map<String, String> articleDetails) {

		summaryInfo.totalArticles++;

		collectDaysIfAvailable(summaryInfo.daysSubmitToAccept,
				articleDetails, "submit-to-acceptance_days");

		Integer value = collectDaysIfAvailable(summaryInfo.daysSubmitToEFirstPublication,
				articleDetails, "submit-to-e-first-publication_days");
		
		Double months = (value == null) ? null : value.intValue() * DAYS_TO_MONTHS;
		if (months != null && months < TWELVE_MONTHS) {
			summaryInfo.articlesWithin12MonthsAccept++;
		}
		
		Integer valuePrintPublication = collectDaysIfAvailable(summaryInfo.daysSubmitToPrintPublication,
				articleDetails, "submit-to-print-publication_days");
		
		Double monthsPrintPublication = (valuePrintPublication == null) ? null : valuePrintPublication.intValue() * DAYS_TO_MONTHS;
		if (monthsPrintPublication != null && monthsPrintPublication < TWELVE_MONTHS) {
			summaryInfo.articlesWithin12MonthsPrintPublication++;
		}

		collectDaysIfAvailable(summaryInfo.daysAcceptToEFirstPublication,
				articleDetails, "accept-to-e-first-publication_days");

		collectDaysIfAvailable(summaryInfo.daysAcceptToPrintPublication,
				articleDetails, "accept-to-print-publication_days");

	}
	
	private Integer collectDaysIfAvailable(List<Integer> summaryList,
			Map<String, String> articleDetails, String dayField) {
		String days = articleDetails.get(dayField);
		Integer value = null;
		String monthValue = "Unavailable";
		if (StringUtils.isNumeric(days)) {
			value = Integer.valueOf(days);
			summaryList.add(value);

			double months = value.intValue() * DAYS_TO_MONTHS;
			monthValue = MONTH_FORMAT.format(months);

		}
		
		articleDetails.put(dayField.replace("_days", ""), monthValue);

		return value;
	}

	private void serializeArticleDetails(XMLStreamWriter xmlWriter,
			Map<String, String> articleDetails) throws XMLStreamException {

		String articleId = articleDetails.get("articleId");

		xmlWriter.writeStartElement("article");
		xmlWriter.writeAttribute("id", articleId);
		xmlWriter.writeAttribute("class", "ca");
		for (int i = 0; i < REPORT_ENTRIES.length; i++) {
			xmlWriter.writeStartElement("meta-item");
			xmlWriter.writeAttribute("item", REPORT_ENTRIES[i]);
			xmlWriter.writeAttribute("class", REPORT_ENTRY_CLASS[i]);

			String reportEntry = articleDetails.get(REPORT_ENTRIES[i]);
			xmlWriter.writeCharacters(reportEntry == null ? "" : reportEntry);
			xmlWriter.writeEndElement();
		}
		xmlWriter.writeEndElement();
	}

	private Map<String, String> getArticleDetails(ContentAssembly articleCa,
			Map<String, String> pubtrackMetadata) throws RSuiteException,
			ParseException {

		Map<String, String> articleDetails = new HashMap<String, String>();

		Map<String, String> articleLmdMap = getLmdMap(articleCa);

		String printPubDateFromPubtrack = pubtrackMetadata
				.get(LMD_FIELD_PRINT_PUBLISHED_DATE);

		articleDetails.put("articleId", articleCa.getDisplayName());

		String author = getLMD(articleLmdMap, "author_surname") + ", "
				+ getLMD(articleLmdMap, "author_first_name");
		articleDetails.put("author", author);

		articleDetails.put("email", getLMD(articleLmdMap, "author_email"));

		articleDetails.put("institution", getLMD(articleLmdMap, LMD_FIELD_AUTHOR_INSTITUTION));
		
		articleDetails.put("country", getLMD(articleLmdMap, LMD_FIELD_AUTHOR_COUNTRY));

		articleDetails.put("licence", getLMD(articleLmdMap, LMD_FIELD_LICENCE_TYPE));

		String submittedDate = getLMD(articleLmdMap, "submitted_date");
		articleDetails.put("submitted", submittedDate);

		String acceptedDate = getLMD(articleLmdMap, "decision_date");
		articleDetails.put("accepted", acceptedDate);

		String articlePassDate = getLMD(articleLmdMap, "online_published_date");
		articleDetails.put("article-pass-for-press", articlePassDate);

		articleDetails.put("issue-pass-for-press", printPubDateFromPubtrack);

		Date dtSubmit = getDate(submittedDate);
		Date dtAccept = getDate(acceptedDate);
		Date dtArticlePass = getDate(articlePassDate);
		Date dtIssuePass = getDate(printPubDateFromPubtrack);

		collectDaysDelta(articleDetails, "submit-to-acceptance_days",
				dtSubmit, dtAccept);
		collectDaysDelta(articleDetails, "submit-to-e-first-publication_days",
				dtSubmit, dtArticlePass);

		collectDaysDelta(articleDetails, "accept-to-e-first-publication_days",
				dtAccept, dtArticlePass);
		collectDaysDelta(articleDetails, "submit-to-print-publication_days",
				dtSubmit, dtIssuePass);
		collectDaysDelta(articleDetails, "accept-to-print-publication_days",
				dtAccept, dtIssuePass);

		articleDetails
				.put("volume", pubtrackMetadata.get(REPORT_VOLUME_NUMBER));
		articleDetails.put("issue", pubtrackMetadata.get(REPORT_ISSUE_NUMBER));
		articleDetails.put("typeset_pages",
				getLMD(articleLmdMap, "typeset_pages"));

		return articleDetails;
	}

	private Map<String, String> getLmdMap(ContentAssembly articleCa)
			throws RSuiteException {
		List<MetaDataItem> lmdItems = articleCa.getMetaDataItems();

		Map<String, String> lmdHash = new HashMap<String, String>();

		for (MetaDataItem lmdItem : lmdItems) {
			lmdHash.put(lmdItem.getName(), lmdItem.getValue());
		}
		return lmdHash;
	}

	private void collectDaysDelta(Map<String, String> articleDetails,
			String detailFieldName, Date startDate, Date endDate) {

		String detailValue = "Unavailable";

		if (!(startDate == null || (endDate == null))) {
			int days = computeDeltaDays(startDate, endDate);
			detailValue = String.valueOf(days);
		}

		articleDetails.put(detailFieldName, detailValue);
	}

	private String computeAverge(List<Integer> daysArray) {
		int n = 0;
		int sum = 0;
		for (Integer days : daysArray) {
			n++;
			sum = sum + days;
		}
		return (n == 0) ? "N/A" : MONTH_FORMAT.format(DAYS_TO_MONTHS * sum / n);
	}

	private String createHqlQuery(
			PublishedIssueReportInputParameters inputParamaters)
			throws RSuiteException {
		StringBuilder sb = new StringBuilder("from Process p where p.name='"
				+ REPORT_IET_ISSUE + "' and p.dtCompleted is not null");

		sb.append(" and p.externalId LIKE '" + REPORT_IET_ISSUE + "_");

		for (String scalar : PublishedIssueReportInputParameters.SCALAR_ARGS) {
			String value = inputParamaters.getScalarValue(scalar);
			sb.append(StringUtils.isEmpty(value) ? "%" : value);
			sb.append("-");
		}

		sb.deleteCharAt(sb.length() - 1);

		sb.append("' ");

		String hqlStartDate = "";
		String hqlEndDate = "";
		try {
			String startDate = inputParamaters.getStartDate();
			if (!StringUtils.isBlank(startDate)) {
				Date date = DATE_PICKER_FORMAT.parse(startDate);
				hqlStartDate = HQL_DATE_FORMAT.format(date);
			}

			String endDate = inputParamaters.getEndDate();

			if (!StringUtils.isBlank(endDate)) {
				Date date = DATE_PICKER_FORMAT.parse(endDate);
				hqlEndDate = HQL_DATE_FORMAT.format(date);
			}

			if (!StringUtils.isBlank(hqlStartDate)) {
				sb.append(" and p.dtCompleted >= '" + hqlStartDate + "'");
			}
			if (!StringUtils.isBlank(hqlEndDate)) {
				sb.append(" and p.dtCompleted <= '" + hqlEndDate + "'");
			}
		} catch (ParseException e) {
			throw new RSuiteException(0, "Date parsing error", e);
		}
		return sb.toString();
	}

	private String getLMD(Map<String, String> lmdHash, String name) {
		return lmdHash.containsKey(name) ? lmdHash.get(name) : " ";
	}

	private int computeDeltaDays(Date d1, Date d2) {
		return Days.daysBetween(new DateTime(d1), new DateTime(d2)).getDays();
	}

	private Date getDate(String dateString) throws ParseException {
		if (StringUtils.isBlank(dateString)){
			return null;
		}
		return REPORT_DATE_STRING_FORMAT.parse(dateString);
	}

}