package org.theiet.maintenance.webservice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.ValidationException;
import com.reallysi.rsuite.api.VersionType;
import com.reallysi.rsuite.api.control.ObjectUpdateOptions;
import com.reallysi.rsuite.api.control.XmlObjectSource;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.reallysi.rsuite.service.SearchService;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.rsuite.helpers.webservice.XmlRemoteApiResult;

/**
 * Custom RSuite web service to save search results to user's clipboard
 * 
 */
public class Iet356 extends DefaultRemoteApiHandler {

	private static final int MAX_RESULT_ITEMS = 20000;
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		Session sess = context.getSession();
		User user = sess.getUser();

		StringBuilder result = new StringBuilder();
		boolean failedMo = false;

		SearchService searchSvc = context.getSearchService();
		ManagedObjectService moSvc = context.getManagedObjectService();

		StringBuilder logContent = new StringBuilder();
		File logDir = context.getRSuiteServerConfiguration().getLogsDir();
		File logFile = new File(logDir, "maintenence_iet356.log");
		
		List<String> notFixedMo = new ArrayList<String>();
		
		XmlApiManager xmlManager = context.getXmlApiManager();
		try {
			XPathFactory xpathFactory = xmlManager.getXPathFactory();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath
					.compile("/*/front/article-meta/kwd-group[@kwd-group-type = following-sibling::kwd-group/@kwd-group-type]");

			List<ManagedObject> results = findMoToFix(user, searchSvc,
					logContent);
			
			
			for (ManagedObject moMeta : results) {
				try{
					logContent.append("Trying to fix article-meta: " + moMeta.getId());
					logContent.append("\n");
					
					ManagedObject mo = moSvc.getParentManagedObject(user, moMeta);
							
					result.append(mo.getId());
					logContent.append("\n");
	
					
					
					if (mo.getCheckedOutUser() == null) {
						fixMo(user, moSvc, logContent, expr, moMeta, mo);
					} else {
						notFixedMo.add(mo.getId());
					}
				
				}catch (RSuiteException e){
					logContent.append(" ERROR: " + e.getLocalizedMessage());
					result.append("\n");
					failedMo = true;
				}
			}
		} catch (XPathExpressionException e) {
			logContent.append(e.getLocalizedMessage());
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
					e.getLocalizedMessage(), e);
		}
		
		if (notFixedMo.size() > 0){

			logContent.append("Following mo has been NOT fixed because are checked out");
			for (String moId : notFixedMo){
				logContent.append("MO:" + moId);
				logContent.append("\n");
			}
		}
		
		
		try {
			FileUtils.writeStringToFile(logFile, logContent.toString());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		
		String resultMessage = "Please check processing log: " + logFile.getAbsolutePath();
		
		if (failedMo){
			resultMessage += "WARNING: Unable to fix some MOs"; 
		}

		return new XmlRemoteApiResult("<result>" + resultMessage + "</result>");

	}

	private List<ManagedObject> findMoToFix(User user, SearchService searchSvc,
			StringBuilder logContent) throws RSuiteException {
		String query = "//article/front/article-meta[./kwd-group[@kwd-group-type = following-sibling::kwd-group/@kwd-group-type]]";
		List<ManagedObject> results = searchSvc.executeXPathSearch(user,
				query, 0, MAX_RESULT_ITEMS);


		logContent.append("Found " + results.size() + " MO(s) to fix");
		logContent.append("\n");
		return results;
	}

	private void fixMo(User user, ManagedObjectService moSvc,
			StringBuilder logContent, XPathExpression expr,
			ManagedObject moMeta, ManagedObject mo) throws RSuiteException,
			XPathExpressionException {
		moSvc.checkOut(user, mo.getId());
		Element root = mo.getElement();

		 NodeList listToRemove = (NodeList)expr.evaluate(root, XPathConstants.NODESET);
		 
		 for (int i = 0; i < listToRemove.getLength(); i++){
			Node duplicate = listToRemove.item(i);
			duplicate.getParentNode().removeChild(duplicate);
		 }
		 						  
		 moSvc.update(user, mo.getId(), new XmlObjectSource(root), new ObjectUpdateOptions());
		 
		 moSvc.checkIn(user, mo.getId(), VersionType.MINOR, "Remove inspec duplicates.", false);
		 logContent.append("Updated mo: " + mo.getId()).append(" for article-meta ").append(moMeta.getId());						  
		 logContent.append("\n");
	}

}
