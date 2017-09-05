package org.theiet.rsuite.journals.actionhandlers;

import static com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils.getCAFromMO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Calendar;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.delivery.digitallibrary.ArticleDigitalLibrary;
import org.theiet.rsuite.utils.StaxUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.report.StoredReport;
import com.reallysi.rsuite.api.workflow.AbstractBaseActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.ReportManager;
import com.reallysi.rsuite.service.SearchService;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.projectshelper.utils.ProjectExecutionContextUtils;
import com.rsicms.rsuite.helpers.utils.RSuiteUtils;

public class BatchDeliverArticle extends AbstractBaseActionHandler {

	private static final long serialVersionUID = 1L;
	private static final String ARTICLE_QUERY = "/rs_ca_map/rs_ca[rmd:get-type(.) = 'article' and not(rmd:get-lmd-value(., 'online_published_date')) and rmd:get-lmd-value(., 'available') = 'yes']";
	private static final String XSLT_URI = "rsuite:/res/plugin/iet/xslt/journal-report/delivery-report.xsl";
	private static final String REPORT_TYPE = "Article";
	
	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {
		Log log = context.getWorkflowLog();
		User user = getSystemUser();
		SearchService srchSvc = context.getSearchService();
		XmlApiManager xmlApiMgr = context.getXmlApiManager();
		ReportManager rptMgr = context.getReportManager();
		
		List<ManagedObject> articleSet = srchSvc.executeXPathSearch(user, ARTICLE_QUERY, 1, 0);
		int nArticles = articleSet.size();
		String dtTime = IetConstants.UK_DATE_FORMAT_LONG.format(Calendar.getInstance().getTime());
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStreamWriter streamWriter = new OutputStreamWriter(out);
		XMLStreamWriter xmlWriter = StaxUtils.setUpWriter(streamWriter);
		xmlWriter.writeStartDocument("UTF-8", "1.0");
		xmlWriter.writeStartElement("delivery-report");
		xmlWriter.writeAttribute("type", REPORT_TYPE);
		xmlWriter.writeAttribute("timestamp", dtTime);
		log.info("Article query " + ARTICLE_QUERY + " returned " + nArticles);
		
		ArticleDigitalLibrary digitalLibrary = new ArticleDigitalLibrary(context, user, log);
		
		for (ManagedObject caAsMo : articleSet) {
			
			
			ContentAssembly articleCa = getCAFromMO(context, user, caAsMo.getId());
			Article article = new Article(context, user, articleCa);
			log.info("Process " + article);	
			xmlWriter.writeStartElement("article");
			xmlWriter.writeAttribute("id", article.getArticleId());
			try {
				digitalLibrary.deliverArticle(article);				
				xmlWriter.writeAttribute("success", "yes");
				xmlWriter.writeCharacters("OK");
			}
			catch (RSuiteException e) {
				xmlWriter.writeAttribute("success", "no");
				xmlWriter.writeCharacters(e.getMessage());
			}
			xmlWriter.writeEndElement();
		}
		xmlWriter.writeEndElement();
		xmlWriter.writeEndDocument();
		xmlWriter.close();
		log.info(out.toString());
		byte[] bytes = out.toByteArray();
		
		String skey = ProjectExecutionContextUtils.getSkey(context);
		log.info("execute: got new skey " + skey);
		Transformer trans = xmlApiMgr.getTransformer(new URI(XSLT_URI));
		trans.setParameter("skey", skey);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		StreamSource xmlSource = new StreamSource(bais);
		StringWriter stringWriter = new StringWriter();
		StreamResult result = new StreamResult(stringWriter);
		trans.transform(xmlSource, result);
		StoredReport report = rptMgr.saveReport("DigitalLibrary", stringWriter.toString(), "text/html");
		String reportId = report.getId();
		String htmlReportURL = RSuiteUtils.constructUrlForStoredReport("RSUITE-SESSION-KEY", reportId);
		context.setVariable("result", "See details <a target=\"_blank\" href=\"" +  htmlReportURL + "\">here</a>");
		context.getSessionService().removeSession(skey);
	}

}
