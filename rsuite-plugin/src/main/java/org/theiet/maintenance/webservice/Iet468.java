package org.theiet.maintenance.webservice;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.utils.XqueryUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.VersionType;
import com.reallysi.rsuite.api.control.ObjectAttachOptions;
import com.reallysi.rsuite.api.control.ObjectSource;
import com.reallysi.rsuite.api.control.ObjectUpdateOptions;
import com.reallysi.rsuite.api.control.XmlObjectSource;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.repository.ComposedXQuery;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.rsuite.helpers.webservice.XmlRemoteApiResult;

/**
 * Custom RSuite web service to save search results to user's clipboard
 * 
 */
public class Iet468 extends DefaultRemoteApiHandler {

	private static final int MILISECONDS_TO_WAIT = 100;
	private static final String RSUITE_NS = "http://www.rsuitecms.com/rsuite/ns/metadata";
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		Session sess = context.getSession();
		User user = sess.getUser();

		boolean writeOpertaion = Boolean.parseBoolean(args.getFirstValue(
				"write", "false"));
		
		String count =  args.getFirstValue("count");
		int countTofix = -1;
		if (count != null){
			countTofix = Integer.parseInt(count);
		}
		
		StringBuilder logContent = new StringBuilder();
		File logDir = context.getRSuiteServerConfiguration().getLogsDir();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmss");

		File logFile = new File(logDir, "maintenence_iet468_"
				+ sdf.format(new Date()) + ".log");

		if (writeOpertaion) {
			logContent.append("WRITE OPERATION... ");
		} else {
			logContent.append("DRY RUN... ");
		}
		logContent.append("\n");

		XmlApiManager xmlManager = context.getXmlApiManager();
		XPathFactory xpathFactory = xmlManager.getXPathFactory();
		XPath xpath = xpathFactory.newXPath();

		String resultMessage = "";
		try {

			List<String> articleIdsToFix = getArticleIdsToFix(context, xpath);
			int index = 0;
			for (String moIdToFix : articleIdsToFix) {
				index++;
				try {
					fixMo(context, user, xpath, moIdToFix, logContent,
							writeOpertaion);

				} catch (Exception e) {
					logContent.append("ERROR occured for " + moIdToFix + " "
							+ e.getLocalizedMessage());
					logContent.append("\n");

					logContent.append(ExceptionUtils.getFullStackTrace(e));
					logContent.append("\n");
					logContent.append("============================");
					logContent.append("\n");

					resultMessage += " err " + e.getLocalizedMessage();
					log.error(e, e);

				}
				
				if (countTofix != -1 && index > countTofix){
						break;
				}
			}

			FileUtils.writeStringToFile(logFile, logContent.toString());

		} catch (Exception e) {
			resultMessage += " err " + e.getLocalizedMessage();
			log.error(e, e);
			resultMessage += e.getLocalizedMessage();
		}

		return new XmlRemoteApiResult("<result>" + resultMessage + "</result>");

	}

	private void fixMo(ExecutionContext context, User user, XPath xpath,
			String moId, StringBuilder resultMessage, boolean write)
			throws Exception {

		ManagedObjectService moSvc = context.getManagedObjectService();
		ManagedObject mo = moSvc.getManagedObject(user, moId);
		XmlApiManager xmlManager = context.getXmlApiManager();

		Document doc = xmlManager.constructNewDocument();
		XPathExpression expr = xpath.compile("/*/front/article");

		Element root = mo.getElement();

		String articleMetaId = "";

		Node node = (Node) expr.evaluate(root, XPathConstants.NODE);

		if (!write && mo.isCheckedout()) {
			moSvc.undoCheckout(user, moId);
		}

		if (node == null) {
			updateMV(context, user, moId, resultMessage, write);

			return;
		}

		Element articleEle = (Element) node;
		articleMetaId = articleEle.getAttributeNS(
				RSUITE_NS, "rsuiteId");

		Node newNode = doc.importNode(node, true);

		doc.appendChild(newNode);

		NamedNodeMap attributes = root.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {

			Node attr = attributes.item(i);

			if ("http://www.w3.org/2000/xmlns/".equals(attr.getNamespaceURI())) {
				doc.getDocumentElement().setAttributeNS(attr.getNamespaceURI(),
						"xmlns:" + attr.getLocalName(), attr.getNodeValue());
			}
		}

		doc.getDocumentElement().setAttributeNS(
				RSUITE_NS, "r:rsuiteId",
				moId);
		XPathExpression expr2 = xpath.compile("/*/front/article-meta");

		Element articleMeta = (Element) expr2.evaluate(
				doc.getDocumentElement(), XPathConstants.NODE);

		String oldArticleMetaId = articleMeta.getAttributeNS(
				RSUITE_NS, "rsuiteId");

		articleMeta.setAttributeNS(
				RSUITE_NS, "r:rsuiteId",
				articleMetaId);

		resultMessage.append("fixing document for " + moId + " ");
		resultMessage.append("\n");
		resultMessage.append("changing article id " + articleMetaId + " to "
				+ moId);
		resultMessage.append("\n");
		resultMessage.append("changing article-meta id " + oldArticleMetaId
				+ " to " + articleMetaId);
		resultMessage.append("\n");

		if (write) {
			ObjectSource src = new XmlObjectSource(doc);
			moSvc.checkOut(user, moId);
			moSvc.update(user, moId, src, new ObjectUpdateOptions());
			moSvc.checkIn(user, moId, VersionType.MAJOR,
					"Fixed corrupted article MO", true);
		}

		resultMessage.append("fixing ca for " + moId);
		resultMessage.append("\n");

		updateMV(context, user, articleMetaId, resultMessage, write);
		updateMV(context, user, moId, resultMessage, write);
		Thread.sleep(MILISECONDS_TO_WAIT);
		fixCa(context, xpath, articleMetaId, moId, resultMessage, write);

		resultMessage.append("================================\n");
	}

	private void updateMV(ExecutionContext context, User user, String moId,
			StringBuilder resultMessage, boolean write) throws RSuiteException {
		resultMessage.append("Updating MV for  " + moId);
		resultMessage.append("\n");
		if (write) {
			context.getManagedObjectService().refreshSearchView(user, moId);
		}
	}

	private void fixCa(ExecutionContext context, XPath xpath,
			String articleMetaId, String articleId,
			StringBuilder resultMessage, boolean write) throws RSuiteException, XPathExpressionException {

		User user = context.getAuthorizationService().getSystemUser();
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("moId", articleMetaId);

		ContentAssemblyService caSvc = context.getContentAssemblyService();

		ComposedXQuery xqueryObject = XqueryUtils.getXquery("searchCa.xqy",
				variables);
		Node res = context.getRepositoryService().queryAsNode(xqueryObject);

		ManagedObjectService mosvc = context.getManagedObjectService();

		XPathExpression exprRes = xpath.compile("/result/ca");

		NodeList resultList = (NodeList) exprRes.evaluate(res,
				XPathConstants.NODESET);
		for (int i = 0; i < resultList.getLength(); i++) {
			Node node = resultList.item(i);
			Node attr = node.getAttributes().getNamedItem("rsuiteId");

			Node morefIdattr = node.getAttributes().getNamedItem("morefId");

			if (attr != null) {
				String caId = attr.getNodeValue();
				String moRef = morefIdattr.getNodeValue();
				resultMessage.append("Detaching " + articleMetaId + " from "
						+ caId);
				resultMessage.append("\n");

				resultMessage
						.append("Attaching " + articleId + " from " + caId);
				resultMessage.append("\n");

				if (write) {
					mosvc.remove(user, moRef);
					caSvc.attach(user, caId, articleId,
							new ObjectAttachOptions());
				}
			}
		}

	}

	private List<String> getArticleIdsToFix(RemoteApiExecutionContext context,
			XPath xpath) throws RSuiteException, XPathExpressionException {
		List<String> articleIdsToFix = new ArrayList<String>();
		ComposedXQuery xqueryObject = XqueryUtils.getXquery(
				"searchBrokenArticles.xqy", new HashMap<String, String>());
		Node res = context.getRepositoryService().queryAsNode(xqueryObject);

		XPathExpression exprRes = xpath.compile("/docs/current-version/result");

		NodeList resultList = (NodeList) exprRes.evaluate(res,
				XPathConstants.NODESET);
		for (int i = 0; i < resultList.getLength(); i++) {
			Node node = resultList.item(i);
			Node attr = node.getAttributes().getNamedItem("rsuiteId");
			Node nestedArticles = node.getAttributes().getNamedItem(
					"nestedArticles");

			if (attr != null && nestedArticles != null && (Integer.parseInt(nestedArticles.getNodeValue()) == 1)) {
			
					articleIdsToFix.add(attr.getNodeValue());
				
			}
		}

		return articleIdsToFix;
	}

}
