package org.theiet.rsuite.reporting;

import static org.theiet.rsuite.reporting.ArticleReportHelper.*;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.xml.stream.*;

import org.apache.commons.lang.StringUtils;
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

public class EFirstReport extends DefaultReportGenerator implements
		JournalConstants {

	private static Log log = LogFactory.getLog(EFirstReport.class);

	private static final String XSLT_URI = "rsuite:/res/plugin/iet/xslt/journal-report/efirst-report.xsl";

	public static final SimpleDateFormat DATE_PICKER_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	private static final String[] REPORT_ENTRIES = { 
			"author", "email", "licenceType",
			"submitted", "accepted", "article-pass-for-press",
			"submit-to-acceptance",
			"submit-to-e-first-publication", "accept-to-e-first-publication",
			};

	private static final String[] REPORT_ENTRY_CLASS = { 
			"ca", "ca", "ca",
			"ca", "ca", "ca", 
			"ca",
			"ca", "ca"
			};

	public Report generateReport(ReportGenerationContext context,
			String reportId, CallArgumentList args) throws RSuiteException {

		try {

			EFirstReportInputParameters inputParmaters = new EFirstReportInputParameters(
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
			EFirstReportInputParameters inputParameters)
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

			List<Process> EFirstProcessList = ptMgr.query(user, hqlQuery);

			log.info("getReportBytes: query returned "
					+ EFirstProcessList.size());

			SummaryInfo summaryInfo = new SummaryInfo();
			
			writeEFirstRows(context, xmlWriter, EFirstProcessList, summaryInfo, inputParameters);
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
	
	private void writeEFirstRows (ReportGenerationContext context,
			XMLStreamWriter xmlWriter,
			List<Process> efirstProcessList,
			SummaryInfo summaryInfo,
			EFirstReportInputParameters inputParameters) throws FactoryConfigurationError, XMLStreamException, IOException, ParseException, RSuiteException {

		for (Process efirstProcess : efirstProcessList) {
			writeEFirstRowIfCaExist(context, xmlWriter, efirstProcess, summaryInfo, inputParameters);;
		}
	}
	
	private void writeEFirstRowIfCaExist(ReportGenerationContext context, XMLStreamWriter xmlWriter, 
			Process efirstProcess, 
			SummaryInfo summaryInfo, 
			EFirstReportInputParameters inputParameters) throws FactoryConfigurationError, XMLStreamException, IOException, ParseException, RSuiteException {
		if (caExist(context, efirstProcess)) {
			writeEFirstRow(context, xmlWriter, efirstProcess, summaryInfo, inputParameters);
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean caExist(ReportGenerationContext context, Process efirstProcess) {
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		User user = context.getSession().getUser();
		Set<ProcessMetaDataItem> metaSet = efirstProcess.getMetaData();
		for (ProcessMetaDataItem processMetaItem : metaSet) {
			if (processMetaItem.getName().equals("OBJECT_ID")) {
				try {
					caSvc.getContentAssembly(user, processMetaItem.getValue());
				} catch (Exception ex) {
					log.info("E-First Report - Object " + processMetaItem.getValue() + " could not be found, but report will be generated though.");
					return false;
				}
			}
		}

		return true;
	}

	private void writeEFirstRow(ReportGenerationContext context, XMLStreamWriter xmlWriter, 
			Process efirstProcess, 
			SummaryInfo summaryInfo, 
			EFirstReportInputParameters inputParameters) throws FactoryConfigurationError, XMLStreamException, IOException, ParseException, RSuiteException {
		
		xmlWriter.writeStartElement("efirst-row");			
		processEFirst(context, xmlWriter, efirstProcess, summaryInfo, inputParameters);	
		xmlWriter.writeEndElement();
	}

	private void writeSummary (XMLStreamWriter xmlWriter, SummaryInfo summaryInfo) throws XMLStreamException {
		xmlWriter.writeStartElement("efirst-summary");
		createSummaryInfoElement(xmlWriter, summaryInfo);
		xmlWriter.writeEndElement();
	}

	private void processEFirst(ReportGenerationContext context,
			XMLStreamWriter xmlWriter, Process efirstProcess,
			SummaryInfo summaryInfo,
			EFirstReportInputParameters inputParameters)
			throws RSuiteException, ParseException, XMLStreamException {

		Set<String> exclusions = inputParameters.getArticleTypesToExclude();

		ContentAssemblyService caSvc = context.getContentAssemblyService();
		User user = context.getSession().getUser();
								
		Map<String, String> efirstProcessMetadata = getEFirstProcessMetadata(efirstProcess);

		String efirstCaId = String.valueOf(efirstProcessMetadata.get("OBJECT_ID"));

		ContentAssembly efirstCa = caSvc.getContentAssembly(user, efirstCaId);

		String printPubDateFromPubtrack = efirstCa
				.getLayeredMetadataValue(LMD_FIELD_PRINT_PUBLISHED_DATE);
		efirstProcessMetadata.put(LMD_FIELD_PRINT_PUBLISHED_DATE,
				printPubDateFromPubtrack);
		
		String articleType = efirstCa.getLayeredMetadataValue(LMD_FIELD_ARTICLE_TYPE);
		
		String onlinePublishedDateCa = efirstCa.getLayeredMetadataValue(LMD_FIELD_ONLINE_PUBLISHED_DATE);

		boolean isAvailable = StringUtils.isNotEmpty(efirstCa.getLayeredMetadataValue(LMD_FIELD_ARTICLE_AVAILABLE))
				? efirstCa.getLayeredMetadataValue(LMD_FIELD_ARTICLE_AVAILABLE).equals(LMD_VALUE_YES) : false;
				
		boolean isAssigned = StringUtils.isNotEmpty(efirstCa.getLayeredMetadataValue(LMD_FIELD_ARTICLE_ASSIGNED))
				? efirstCa.getLayeredMetadataValue(LMD_FIELD_ARTICLE_ASSIGNED).equals(LMD_VALUE_YES) : false;
		
		
		String articleManuscirpId = efirstCa.getDisplayName();
		
		if (articleType != null && exclusions.contains(articleType) ||
		   !inputParameters.isBetweenDateRange(articleManuscirpId, onlinePublishedDateCa) ||
		   !ArticleReportUtils.passFilter(inputParameters.getFilters(), efirstCa) ||
		   !(isAvailable || isAssigned)) {
			return;
		}

		Map<String, String> articleDetails = getArticleDetails(efirstCa, efirstProcessMetadata);
		
		collectSummaryInfo(summaryInfo, articleDetails);

		serializeArticleDetails(xmlWriter, articleDetails, REPORT_ENTRIES, REPORT_ENTRY_CLASS);

	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getEFirstProcessMetadata(Process efirstProcess)
			throws RSuiteException {
		Set<ProcessMetaDataItem> metaSet = efirstProcess.getMetaData();

		Map<String, String> pubtrackMetadata = new HashMap<String, String>();

		for (ProcessMetaDataItem processMetaItem : metaSet) {
			pubtrackMetadata.put(processMetaItem.getName(),
					processMetaItem.getValue());
		}

		if (!pubtrackMetadata.containsKey("OBJECT_ID")) {
			throw new RSuiteException("No OBJECT_ID for pubtrack process "
					+ efirstProcess.getId());
		}
		return pubtrackMetadata;
	}

	private void createSummaryInfoElement(XMLStreamWriter xmlWriter,
			SummaryInfo summaryInfo) throws XMLStreamException {
		xmlWriter.writeStartElement("summary");
		
		
		createSummaryEntry(xmlWriter, "paper-count-summary",
				String.valueOf(summaryInfo.getTotalArticles()));		
		
		xmlWriter.writeStartElement("average-summary");
		createSummaryEntry(xmlWriter, "avg-submit-to-accept",
				summaryInfo.daysSubmitToAccept);
		createSummaryEntry(xmlWriter, "avg-submit-to-e-first-publication",
				summaryInfo.daysSubmitToEFirstPublication);

		createSummaryEntry(xmlWriter, "avg-accept-to-e-first-publication",
				summaryInfo.daysAcceptToEFirstPublication);		
		xmlWriter.writeEndElement();
		
		xmlWriter.writeEndElement();
		
	}
	
	private void createSummaryEntry(XMLStreamWriter xmlWriter,
			String elementName, List<Integer> daysList)
			throws XMLStreamException {
		String avarageValue = computeAverge(daysList);
		createSummaryEntry(xmlWriter, elementName, avarageValue);
	}
	
	private void createSummaryEntry(XMLStreamWriter xmlWriter,
			String elementName, String elementValue)
			throws XMLStreamException {
		xmlWriter.writeStartElement(elementName);
		xmlWriter.writeCharacters(elementValue);
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
			EFirstReportInputParameters inputParamaters)
			throws RSuiteException {
	      
		StringBuilder sb = new StringBuilder("from Process p where p.name='"
				+ REPORT_IET_ARTICLE + "' and p.dtCompleted is not null");

		sb.append(" and p.externalId LIKE '" + REPORT_IET_ARTICLE + "_");

		for (String scalar : EFirstReportInputParameters.SCALAR_ARGS) {
			String value = inputParamaters.getScalarValue(scalar);
			sb.append(StringUtils.isEmpty(value) ? "%" : value + "%");
			sb.append("-");
		}

		sb.deleteCharAt(sb.length() - 1);

		sb.append("' ");
		
		return sb.toString();
	}

}