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
import java.util.List;
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
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadata;
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
import com.rsicms.rsuite.helpers.utils.RSuiteUtils;

public class ArticleWipReport extends DefaultReportGenerator {
	
	/*********************************************************
	 * NOTE: This class is misnamed, since it now applies to both
	 * work in progress as well as completed article - HG
	 *********************************************************/
	
	private static final String XSLT_URI = "rsuite:/res/plugin/iet/xslt/journal-report/ArticleWIP.xsl";
	private static Log log = LogFactory.getLog(ArticleWipReport.class);
	public static final String DEF_REST_URL = "/rsuite/rest/v1";
	
	private static ArrayList<String> NOT_META_LIST = new ArrayList<String>();
	private static String[] NOT_META_ARRAY = {
		"PRODUCT_ID",
		"OBJECT_ID",
		"PRODUCT",
		"JOURNAL_CODE"
	};
	
	static {
		for (String name : NOT_META_ARRAY) {
			NOT_META_LIST.add(name);
		}
	}
	
	public Report generateReport(ReportGenerationContext context,
		    String reportId,
		    CallArgumentList args) throws RSuiteException {
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		XmlApiManager xmlApiMgr = context.getXmlApiManager();
		PubtrackManager ptMgr = context.getPubtrackManager();
		User user = context.getSession().getUser();
		String skey = context.getSession().getKey();
		String detailsURL = constructUrlForDetailsReport(null, null, skey, reportId);
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			OutputStreamWriter streamWriter = new OutputStreamWriter(out);
			XMLStreamWriter xmlWriter = StaxUtils.setUpWriter(streamWriter);
			xmlWriter.writeStartDocument("UTF-8", "1.0");
			xmlWriter.writeStartElement("wip");
			if (!StringUtils.isBlank(args.getFirstValue("details"))) {
				String pid = args.getFirstValue("pid");
				String HQL = "from Process p where p.id='" + pid + "'";				
				writeDetailsXML(log, user, context, ptMgr, xmlWriter, HQL, reportId);
			}
			else {
				if (StringUtils.isBlank(args.getFirstValue("status"))) {
					return new HTMLReport("Please select in process, all, or complete");
				}
				String HQL = ArticleReportUtils.getHQL(log, args, caSvc, user);
				writeReportXML(log, user, context, args, ptMgr, caSvc, xmlWriter, HQL);
			}
			xmlWriter.writeEndElement();
			xmlWriter.writeEndDocument();
			xmlWriter.close();
			
			streamWriter.close();
	        byte[] xmlSourceBytes = out.toByteArray();
	        log.info("\n" + new String(xmlSourceBytes, "UTF-8"));
	        
	        
			Transformer trans = xmlApiMgr.getTransformer(new URI(XSLT_URI));
			trans.setParameter("skey", skey);
			trans.setParameter("basedetailsurl", detailsURL);
			log.info(detailsURL);
			
	        ByteArrayInputStream bais = new ByteArrayInputStream(xmlSourceBytes);
			StreamSource xmlSource = new StreamSource(bais);
			StringWriter stringWriter = new StringWriter();
			StreamResult result = new StreamResult(stringWriter);
			trans.transform(xmlSource, result);
			return new HTMLReport(stringWriter.toString());
			
		} catch (RSuiteException e) {
			log.error("generateReport: exception is " + e.getMessage(), e);
		} catch (FactoryConfigurationError e) {
			log.error("generateReport: exception is " + e.getMessage(), e);
		} catch (XMLStreamException e) {
			log.error("generateReport: exception is " + e.getMessage(), e);
		} catch (IOException e) {
			log.error("generateReport: exception is " + e.getMessage(), e);
		} catch (URISyntaxException e) {
			log.error("generateReport: exception is " + e.getMessage(), e);
		} catch (TransformerException e) {
			log.error("generateReport: exception is " + e.getMessage(), e);
		}
		return null;		
	}

	@SuppressWarnings("unchecked")
	private void writeDetailsXML(Log log, User user,
			ReportGenerationContext context, PubtrackManager ptMgr,
			XMLStreamWriter xmlWriter, String HQL, String reportId) throws RSuiteException, XMLStreamException {
		List<Process> processList = ptMgr.query(user, HQL);
		Process p = processList.get(0);
		Set<ProcessMetaDataItem> metaSet = p.getMetaData();
		xmlWriter.writeStartElement("details");
		TreeMap<Date, String>metaMap = new TreeMap<Date, String>();
		for (ProcessMetaDataItem item : metaSet) {
			String name = item.getName();
			if (NOT_META_LIST.contains(name)) {
				if ("PRODUCT_ID".equals(name)) {
					xmlWriter.writeAttribute("id", item.getValue());
				}
			}
			else {
				try {
					long id = item.getId();
					Date eventDate = new Date(IetConstants.UK_DATE_FORMAT_SHORT.parse(item.getValue()).getTime() + id);					
					metaMap.put(eventDate, name);
				} catch (ParseException e) {
					throw new RSuiteException("Date parsing error " + e.getMessage());
				}
				
			}
		}
		Set<Date> keySet = metaMap.keySet();
		
		for (Date key : keySet) {
			xmlWriter.writeStartElement("event");
			xmlWriter.writeAttribute("name", metaMap.get(key));
			xmlWriter.writeCharacters(IetConstants.UK_DATE_FORMAT_SHORT.format(key));
			xmlWriter.writeEndElement();
		}
		xmlWriter.writeEndElement();
	}

	@SuppressWarnings("unchecked")
	private void writeReportXML(Log log, 
			User user, ReportGenerationContext context, 
			CallArgumentList args,
			PubtrackManager ptMgr,
			ContentAssemblyService caSvc, 
			XMLStreamWriter xmlWriter,
			String HQL) throws RSuiteException, XMLStreamException {
				
//		Write out <articles> and add <article> for each good process
		log.info("writeReportXML: HQL is\n" + HQL);
		List<Process> processList = ptMgr.query(user, HQL);
		log.info("writeReportXML: processList is size " + processList.size());
		xmlWriter.writeStartElement("articles");
		xmlWriter.writeAttribute("status", args.getFirstValue("status"));
		TreeMap<String, Process> processMap = new TreeMap<String, Process>();
		for (Process p: processList) {
			String articleId = p.getExternalId().replaceFirst("IET_ARTICLE_", "");
			processMap.put(articleId, p);
		}
		Set<String> keySet = processMap.keySet();
		for (String key : keySet) {
			Process p = processMap.get(key);
			Set<ProcessMetaDataItem> metaSet = p.getMetaData();
			String articleCaId = new String();
			String productId = new String();
			for (ProcessMetaDataItem item : metaSet) {
				if (item.getName().equals("OBJECT_ID")) {
					articleCaId = item.getValue();
				}
				if (item.getName().equals("PRODUCT_ID")) {
					productId = articleCaId = item.getValue();
				}
			}
			if (StringUtils.isBlank(articleCaId) || StringUtils.isBlank(productId)) {
				log.info("getReportBytes: skip process " + p.getId());
			}
			else {
				String dtStarted = IetConstants.UK_DATE_FORMAT_SHORT.format(p.getDtStarted());
				
				ContentAssembly articleCa = null;
				try {
					articleCa = caSvc.getContentAssembly(user, articleCaId);
				}
				catch (RSuiteException e) {
					log.info("getReportBytes: no article found for articleCaId " + articleCaId);
				}
				
				if (articleCa != null) {
					Article article = new Article(context, user, articleCa);
					ArticleMetadata articleMetadata = article.getArticleMetadata();
					
					String isepecialissue = getLmdForArticle(articleCa, articleMetadata.getSpecialIssue());
					String inspecrequired = getLmdForArticle(articleCa, articleMetadata.getInspecRequired());
					String isopenaccess = getLmdForArticle(articleCa, articleMetadata.getOpenAcess());
					boolean passFilter = ArticleReportUtils.passFilter(args, articleCa);
					if (passFilter) {
						xmlWriter.writeStartElement("article");
						xmlWriter.writeAttribute("id", productId);
						xmlWriter.writeAttribute("process_id", String.valueOf(p.getId()));
						xmlWriter.writeAttribute("caid", articleCaId);
						xmlWriter.writeAttribute("dtstarted", dtStarted);
						xmlWriter.writeAttribute("isspecialissue", isepecialissue);
						xmlWriter.writeAttribute("inspecrequired", inspecrequired);
						xmlWriter.writeAttribute("isopenaccess", isopenaccess);
						xmlWriter.writeEndElement();
					}
				}
			}
		}
		xmlWriter.writeEndElement();
	}
	
	private String constructUrlForDetailsReport(
			String host,
			String port,
			String skey, String reportId) {
		StringBuilder urlBuf = new StringBuilder();
		urlBuf.append(RSuiteUtils.DEF_REST_URL)
		.append("/report/fromdefinition/")
		.append(reportId)
		.append("?")
		.append("skey=")
		.append(skey);
		return urlBuf.toString();
	}
	
	private String getLmdForArticle(ContentAssembly articleCa,
			String name) throws RSuiteException {
		String value = articleCa.getLayeredMetadataValue(name);
		return (StringUtils.isBlank(value)) ? "missing" : value;
	}
	

}