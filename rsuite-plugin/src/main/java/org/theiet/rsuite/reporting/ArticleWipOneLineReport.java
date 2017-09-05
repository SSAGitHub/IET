package org.theiet.rsuite.reporting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.utils.StaxUtils;
import org.theiet.rsuite.utils.StringUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.pubtrack.Process;
import com.reallysi.rsuite.api.pubtrack.ProcessMetaDataItem;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.report.DefaultReportGenerator;
import com.reallysi.rsuite.api.report.HTMLReport;
import com.reallysi.rsuite.api.report.Report;
import com.reallysi.rsuite.api.report.ReportGenerationContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.PubtrackManager;
import com.reallysi.rsuite.service.XmlApiManager;

public class ArticleWipOneLineReport extends DefaultReportGenerator {
	
	private static Log log = LogFactory.getLog(ArticleWipOneLineReport.class);
	private static final String XSLT_URI = "rsuite:/res/plugin/iet/xslt/journal-report/ArticleSummary.xsl";
	
	
	/*
	 * Array EVENTS is a list of cells to be written out for each article.
	 * The XML supplied to the transform contains entries such as:
	 * <event name="initalProof">03/04/13</event>
	 * However, the @name attribute is only used for bookkeeping and data management,
	 * and is not relevant for the XSLT
	 */
	private static final String[] EVENTS = {
		"initalProof",
		"sentToAuthor",
		"authCorrections",
		"1stUpdateRequested",
		"1stUpdateReceived",
		"2ndUpdateRequested",
		"2ndUpdateReceived",
		"finalRequested",
		"finalReceived",
		"deliver",
		"issueCode"
	};
	
	private static ArrayList<String> NOT_META_LIST = new ArrayList<String>();
	private static String[] NOT_META_ARRAY = {
		"PRODUCT_ID",
		"OBJECT_ID",
		"PRODUCT",
		"JOURNAL_CODE"
	};
	
	private static HashMap<String, String>nullMap = new HashMap<String, String>();
	
	static {
		for (String name : NOT_META_ARRAY) {
			NOT_META_LIST.add(name);
		}
		nullMap.put("complete", "is not null");
		nullMap.put("wip", "is null");
	}
	
	public Report generateReport(ReportGenerationContext context,
		    String reportId,
		    CallArgumentList args) throws RSuiteException {
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		XmlApiManager xmlApiMgr = context.getXmlApiManager();
		PubtrackManager ptMgr = context.getPubtrackManager();
		User user = context.getSession().getUser();
		String skey = context.getSession().getKey();
		if (StringUtils.isBlank(args.getFirstValue("status"))) {
			return new HTMLReport("Please select in process, all, or complete");
		}
		String HQL = ArticleReportUtils.getHQL(log, args, caSvc, user);
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			OutputStreamWriter streamWriter = new OutputStreamWriter(out);
			XMLStreamWriter xmlWriter = StaxUtils.setUpWriter(streamWriter);
			xmlWriter.writeStartDocument("UTF-8", "1.0");
			xmlWriter.writeStartElement("articles");
			xmlWriter.writeAttribute("status", args.getFirstValue("status"));
			writeReportXML(log, args, user, context, ptMgr, caSvc, xmlWriter, HQL);
			xmlWriter.writeEndDocument();
			xmlWriter.close();
			streamWriter.close();
	        byte[] xmlSourceBytes = out.toByteArray();
	        log.info("\n" + new String(xmlSourceBytes, "UTF-8"));
	        
			Transformer trans = xmlApiMgr.getTransformer(new URI(XSLT_URI));
			trans.setParameter("skey", skey);
			
	        ByteArrayInputStream bais = new ByteArrayInputStream(xmlSourceBytes);
			StreamSource xmlSource = new StreamSource(bais);
			StringWriter stringWriter = new StringWriter();
			StreamResult result = new StreamResult(stringWriter);
			trans.transform(xmlSource, result);
			return new HTMLReport(stringWriter.toString());
	        
		} catch (FactoryConfigurationError e) {
			log.error("generateReport: exception is " + e.getMessage(), e);
		} catch (XMLStreamException e) {
			log.error("generateReport: exception is " + e.getMessage(), e);
		} catch (IOException e) {
			log.error("generateReport: exception is " + e.getMessage(), e);
		} catch (TransformerException e) {
			log.error("generateReport: exception is " + e.getMessage(), e);
		} catch (URISyntaxException e) {
			log.error("generateReport: exception is " + e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void writeReportXML(Log log, 
			CallArgumentList args,
			User user, ReportGenerationContext context, 
			PubtrackManager ptMgr,
			ContentAssemblyService caSvc,
			XMLStreamWriter xmlWriter,
			String HQL) throws RSuiteException, XMLStreamException {
		log.info("writeReportXML: HQL is\n" + HQL);
		List<Process> processList = ptMgr.query(user, HQL);
		log.info("writeReportXML: processList is size " + processList.size());
		TreeMap<String, Process> processMap = new TreeMap<String, Process>();
		Map<String, String> issueCodes = new HashMap<String, String>();
		for (Process p: processList) {
			String articleId = p.getExternalId().replaceFirst("IET_ARTICLE_", "");
			Set<ProcessMetaDataItem> metaSet = p.getMetaData();
			for (ProcessMetaDataItem item : metaSet) {
				if (item.getName().equals("OBJECT_ID")) {
					try {
						ContentAssembly articleCa = caSvc.getContentAssembly(user, item.getValue());
						String issueCode = null;
						if (ArticleReportUtils.passFilter(args, articleCa)) {
							processMap.put(articleId, p);
							issueCode = articleCa.getLayeredMetadataValue(JournalConstants.LMD_FIELD_ISSUE_CODE);
							issueCodes.put(articleId, (issueCode == null) ? "" : issueCode);
						}
					}
					catch (RSuiteException e) {
						log.error("writeReportXML: object id " + item.getValue() + " not found");
					}
					
				}
			}
			
		}
		Set<String> keySet = processMap.keySet();
		for (String articleCode : keySet) {
			String issueCode = issueCodes.get(articleCode);

			String issueCodeFilter = args.getFirstString("issueCode", "");
			String issueCodeRegex = issueCodeFilter.replaceAll("%", "(.)+");
			if (org.apache.commons.lang.StringUtils.isNotBlank(issueCodeFilter)
					&& !issueCode.matches(issueCodeRegex)) {
				log.info("Skipping to process article (ID: " + articleCode + "), because it does not match with the specified issue code");
				continue;
			}
			
			Process p = processMap.get(articleCode);
			Set<ProcessMetaDataItem> metaSet = p.getMetaData();
			String articleId = getArticleIdFromMetadata(metaSet, articleCode);			
			ContentAssembly articleCa = caSvc.getContentAssembly(user, articleId);
			String category = getLmdFromArticleCa(articleCa, JournalConstants.LMD_FIELD_CATEGORY);
			String articleType = getLmdFromArticleCa(articleCa, JournalConstants.LMD_FIELD_ARTICLE_TYPE);
			String articleTitle = getLmdFromArticleCa(articleCa, JournalConstants.LMD_FIELD_ARTICLE_TITLE);
			TreeMap<Date, String>dateSortedMetaMap = new TreeMap<Date, String>();
			String dtStarted = IetConstants.UK_DATE_FORMAT_SHORT.format(p.getDtStarted());
			HashMap<String, String> htmlMap = new HashMap<String, String>();
			htmlMap.put("issueCode", issueCode);
			for (ProcessMetaDataItem item : metaSet) {
				String name = item.getName();
				if (!NOT_META_LIST.contains(name)) {
					try {
						long id = item.getId();
						Date eventDate = new Date(IetConstants.UK_DATE_FORMAT_SHORT.parse(item.getValue()).getTime() + id);					
						dateSortedMetaMap.put(eventDate, name);
					} catch (ParseException e) {
						throw new RSuiteException("Date parsing error " + e.getMessage());
					}
				}
			}
			Set<Date> dateKeySet = dateSortedMetaMap.keySet();
			short typesetterUpdateRequestCount = 0;
			short typesetterUpdateReceivedCount = 0;		
			for (Date date : dateKeySet) {
				String dtString = IetConstants.UK_DATE_FORMAT_SHORT.format(date);
				String eventName = dateSortedMetaMap.get(date);
				if (eventName.equals(JournalConstants.PUBTRACK_AUTHOR_COMMENTS_RECEIVED)) {
					htmlMap.put("authCorrections", dtString);
				}
				else if (eventName.equals(JournalConstants.PUBTRACK_REQUESTED_AUTHOR_COMMENTS)) {
					htmlMap.put("sentToAuthor", dtString);
				}
				else if (eventName.equals(JournalConstants.PUBTRACK_REQUESTED_TYPESETTER_FINAL_FILES)) {
					htmlMap.put("finalRequested", dtString);
				}
				else if (eventName.equals(JournalConstants.PUBTRACK_TYPESETTER_FINAL_RECEIVED)) {
					htmlMap.put("finalReceived", dtString);
				}
				else if (eventName.equals(JournalConstants.PUBTRACK_TYPESETTER_INITIAL_RECEIVED)) {
					htmlMap.put("initalProof", dtString);
				}
				else if (eventName.equals(JournalConstants.PUBTRACK_SENT_TO_DIGITAL_LIBRARY)) {
					htmlMap.put("deliver", dtString);
				}
				else if (eventName.equals(JournalConstants.PUBTRACK_REQUESTED_TYPESETTER_UPDATE)) {
					if (typesetterUpdateRequestCount == 0) {
						htmlMap.put("1stUpdateRequested", dtString);
					} else if (typesetterUpdateRequestCount == 1) {
						htmlMap.put("2ndUpdateRequested", dtString);
					}
					typesetterUpdateRequestCount++;
				}
				else if (eventName.equals(JournalConstants.PUBTRACK_TYPESETTER_UPDATE_RECEIVED)) {
					if (typesetterUpdateReceivedCount == 0) {
						htmlMap.put("1stUpdateReceived", dtString);
					} else if (typesetterUpdateReceivedCount == 1) {
						htmlMap.put("2ndUpdateReceived", dtString);
					}
					typesetterUpdateReceivedCount++;
				}
				
			}
			xmlWriter.writeStartElement("article");
			xmlWriter.writeAttribute("id", articleCode);
			xmlWriter.writeAttribute("category", category);
			xmlWriter.writeAttribute("articleType", articleType);
			xmlWriter.writeAttribute("articleTitle", articleTitle);
			xmlWriter.writeAttribute("dtStarted", dtStarted);
			for (int i=0; i<EVENTS.length; i++) {
				xmlWriter.writeStartElement("event");
				xmlWriter.writeAttribute("name", EVENTS[i]);
				String dtString = htmlMap.containsKey(EVENTS[i]) ? htmlMap.get(EVENTS[i]) : " ";
				xmlWriter.writeCharacters(dtString);
				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement();
		}
	}

	private String getLmdFromArticleCa(ContentAssembly articleCa, String lmdName) throws RSuiteException {
		String lmdValue = articleCa.getLayeredMetadataValue(lmdName);
		return org.apache.commons.lang.StringUtils.isNotBlank(lmdValue) ? lmdValue : "";
	}

	private String getArticleIdFromMetadata(Set<ProcessMetaDataItem> metaSet, String articleCode) throws RSuiteException {
		for (ProcessMetaDataItem item : metaSet) {
			if (item.getName().equals("OBJECT_ID")){
				return item.getValue();
			}			
		}

		throw new RSuiteException(
				RSuiteException.ERROR_INTERNAL_ERROR, "No OBJECT_ID found for article " + articleCode);
	}	

}
