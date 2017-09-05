package org.theiet.rsuite.reporting;

import static org.theiet.rsuite.reporting.ArticleReportHelper.*;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.xml.stream.*;

import org.apache.commons.logging.*;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.reporting.ArticleReportHelper.SummaryInfo;
import org.theiet.rsuite.utils.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.pubtrack.*;
import com.reallysi.rsuite.api.pubtrack.Process;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.report.*;
import com.reallysi.rsuite.service.*;

public class JOEReport extends DefaultReportGenerator implements
		JournalConstants {

	private static Log log = LogFactory.getLog(JOEReport.class);

	private static final String XSLT_URI = "rsuite:/res/plugin/iet/xslt/journal-report/joe-report.xsl";

	public static final String JOURNAL_TYPE_ALL = "All";

	public static final SimpleDateFormat DATE_PICKER_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	private static final String[] REPORT_ENTRIES = { "submissionType", "categoryCode", "licenceType",
			"author", "email",
			"authorInstitution", "authorCountry",
			"typesetPages",
			"submitted", "accepted", "article-pass-for-press",
			"submit-to-acceptance",
			"submit-to-e-first-publication", "accept-to-e-first-publication",
			};

	private static final String[] REPORT_ENTRY_CLASS = { "ca", "ca", "ca",
			"ca", "ca",
			"ca", "ca",
			"ca",
			"ca", "ca", "ca",
			"ca",
			"ca", "ca"
			};

	public Report generateReport(ReportGenerationContext context,
			String reportId, CallArgumentList args) throws RSuiteException {

		try {

			JOEReportInputParameters inputParmaters = new JOEReportInputParameters(
					context, args);

			byte[] xmlSourceBytes = getXmlReport(context, inputParmaters);

			String reportHTML = transformToHTML(context, xmlSourceBytes, XSLT_URI);

			return new HTMLReport(reportHTML);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new HTMLReport(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private byte[] getXmlReport(ReportGenerationContext context,
			JOEReportInputParameters inputParameters)
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

			List<Process> joeArticleProcessList = ptMgr.query(user, hqlQuery);

			log.info("getReportBytes: query returned "
					+ joeArticleProcessList.size());

			SummaryInfo summaryInfo = new SummaryInfo();
			
			writeJOEArticleRows(context, xmlWriter, joeArticleProcessList, summaryInfo, inputParameters);
			writeSummary(xmlWriter, summaryInfo);
			
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
	
	private void writeJOEArticleRows (ReportGenerationContext context,
			XMLStreamWriter xmlWriter,
			List<Process> joeArticleProcessList,
			SummaryInfo summaryInfo,
			JOEReportInputParameters inputParameters) throws FactoryConfigurationError, XMLStreamException, IOException, ParseException, RSuiteException {
		xmlWriter.writeStartElement("joe-article-row");			
		for (Process joeArticleProcess : joeArticleProcessList) {

			processJOEArticle(context, xmlWriter, joeArticleProcess, summaryInfo,
					inputParameters);
		}			
		xmlWriter.writeEndElement();
	}
	
	private void writeSummary (XMLStreamWriter xmlWriter, SummaryInfo summaryInfo) throws XMLStreamException {
		xmlWriter.writeStartElement("joe-article-summary");
		createSummaryInfoElement(xmlWriter, summaryInfo);
		xmlWriter.writeEndElement();
	}


	private void processJOEArticle(ReportGenerationContext context,
			XMLStreamWriter xmlWriter, Process joeArticleProcess,
			SummaryInfo summaryInfo,
			JOEReportInputParameters inputParameters)
			throws RSuiteException, ParseException, XMLStreamException {

		Set<String> exclusions = inputParameters.getArticleTypesToExclude();

		ContentAssemblyService caSvc = context.getContentAssemblyService();
		User user = context.getSession().getUser();
								
		Map<String, String> joeArticleProcessMetadata = getJOEArticleProcessMetadata(joeArticleProcess);

		String joeArticleCaId = String.valueOf(joeArticleProcessMetadata.get("OBJECT_ID"));

		ContentAssembly joeArticleCa = caSvc.getContentAssembly(user, joeArticleCaId);

		String printPubDateFromPubtrack = joeArticleCa
				.getLayeredMetadataValue(LMD_FIELD_PRINT_PUBLISHED_DATE);
		joeArticleProcessMetadata.put(LMD_FIELD_PRINT_PUBLISHED_DATE,
				printPubDateFromPubtrack);
		
		String articleType = joeArticleCa.getLayeredMetadataValue(LMD_FIELD_ARTICLE_TYPE);
		
		String onlinePublishedDateCa = joeArticleCa.getLayeredMetadataValue(LMD_FIELD_ONLINE_PUBLISHED_DATE);

		String articleManuscirpId = joeArticleCa.getDisplayName();

		if (articleType != null && exclusions.contains(articleType)) {
			return;
		}
		
		if (!inputParameters.isBetweenDateRange(articleManuscirpId, onlinePublishedDateCa)) {
			return;
		}

		Map<String, String> articleDetails = getArticleDetails(joeArticleCa, joeArticleProcessMetadata);

		collectSummaryInfo(summaryInfo, articleDetails);

		serializeArticleDetails(xmlWriter, articleDetails, REPORT_ENTRIES, REPORT_ENTRY_CLASS);

	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getJOEArticleProcessMetadata(Process joeArticleProcess)
			throws RSuiteException {
		Set<ProcessMetaDataItem> metaSet = joeArticleProcess.getMetaData();

		Map<String, String> pubtrackMetadata = new HashMap<String, String>();

		for (ProcessMetaDataItem processMetaItem : metaSet) {
			pubtrackMetadata.put(processMetaItem.getName(),
					processMetaItem.getValue());
		}

		if (!pubtrackMetadata.containsKey("OBJECT_ID")) {
			throw new RSuiteException("No OBJECT_ID for pubtrack process "
					+ joeArticleProcess.getId());
		}
		return pubtrackMetadata;
	}

	private void createSummaryInfoElement(XMLStreamWriter xmlWriter,
			SummaryInfo summaryInfo) throws XMLStreamException {

		xmlWriter.writeStartElement("summary");
		
		xmlWriter.writeStartElement("typesetter-pages-total");
		xmlWriter.writeCharacters(String.valueOf(summaryInfo.pagesTypesetter));
		xmlWriter.writeEndElement();

		xmlWriter.writeStartElement("average");

		createSummaryEntry(xmlWriter, "avg-submit-to-accept",
				summaryInfo.daysSubmitToAccept);
		createSummaryEntry(xmlWriter, "avg-submit-to-e-first-publication",
				summaryInfo.daysSubmitToEFirstPublication);

		createSummaryEntry(xmlWriter, "avg-accept-to-e-first-publication",
				summaryInfo.daysAcceptToEFirstPublication);

		xmlWriter.writeEndElement();

		String percentWithin12 = "N/A";
		if (summaryInfo.totalArticles > 0) {
			double percent = new Double(summaryInfo.articlesWithin12Months)
					/ new Double(summaryInfo.totalArticles) * 100;
			percentWithin12 = MONTH_FORMAT.format(percent);
		}

		xmlWriter.writeStartElement("percent-within-12");
		xmlWriter.writeCharacters(percentWithin12);
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
			JOEReportInputParameters inputParamaters)
			throws RSuiteException {
	      
		StringBuilder sb = new StringBuilder("from Process p where p.name='"
				+ REPORT_IET_ARTICLE + "'");

		sb.append(" and ");
		
		switch (inputParamaters.getJournalCode()) {
			case JOURNAL_TYPE_ALL:
				sb.append("(");
				
				StringBuffer availableJournalsQuery = new StringBuffer();
				List<String> allArchiveJournalCodes = inputParamaters.getAllArchiveJournalCodes();
				for (String journalCode : allArchiveJournalCodes) {
					if (availableJournalsQuery.length() > 0) {
						availableJournalsQuery.append(" or ");
					}
					availableJournalsQuery.append("p.externalId LIKE '" + REPORT_IET_ARTICLE + "_" + journalCode + "%'");
				}
				sb.append(availableJournalsQuery);
				sb.append(") ");
				break;
			default:
				sb.append("p.externalId LIKE '" + REPORT_IET_ARTICLE + "_" + inputParamaters.getJournalCode() + "%' ");
				break;
		}

		return sb.toString();
		
		
	}

}