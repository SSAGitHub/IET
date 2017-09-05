package org.theiet.rsuite.journals.utils;

import java.util.List;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.books.utils.BookUtils;
import org.theiet.rsuite.datamodel.ExternalCompanyUser;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.utils.XpathUtils;
import org.w3c.dom.Element;

import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.api.xml.XPathEvaluator;
import com.reallysi.rsuite.service.SearchService;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.projectshelper.utils.ProjectUserUtils;

public class JournalUtils implements JournalConstants {

	private static final String DOI_XPATH = "/article/front/article-meta/article-id[@pub-id-type='doi']";
	private static final String AUTHOR_INSTITUTION_XPATH = "/article/front/article-meta/aff[@id='af1']/institution";
	private static final String AUTHOR_COUNTRY_XPATH = "/article/front/article-meta/aff[@id='af1']/country";

	public static String normalizeArticleId(String articleId) {
		if (articleId == null) {
			return null;
		}

		return articleId.toUpperCase();
	}

	//TODO refecator parameters
	/**
	 * Gets the journal caId from the journal code.
	 * 
	 * @param log
	 * @param user
	 * @param context
	 * @param srchSvc
	 * @param journalCode
	 * @return the journal caId or null is query does not return exactly one
	 *         result
	 * @throws RSuiteException
	 */
	public static String getJournalCaId(Log log, User user,
			ExecutionContext context, SearchService srchSvc, String journalCode)
			throws RSuiteException {
		List<ManagedObject> caSet = searchJournalWithCode(user, srchSvc,
				journalCode);
		int nJournals = caSet.size();
		log.info("getJournalCaId: query returned " + nJournals);
		if (nJournals != 1) {
			throw new RSuiteException("There should be only one journal with code " + journalCode + " but " + nJournals + "have been found");
		} else {
			return caSet.get(0).getId();
		}
	}

	public static List<ManagedObject> searchJournalWithCode(User user,
			SearchService srchSvc, String journalCode) throws RSuiteException {
		String journalQuery = "/rs_ca_map/rs_ca[rmd:get-type(.) = 'journal' and rmd:get-lmd-value(., 'journal_code') = '"
				+ journalCode + "']";
		
		journalQuery = XpathUtils.resolveRSuiteFunctionsInXPath(journalQuery);
		

		List<ManagedObject> caSet = srchSvc.executeXPathSearch(user,
				journalQuery, 1, 0);
		return caSet;
	}

	/**
	 * Gets the issue caId from the issue code.
	 * 
	 * @param log
	 * @param user
	 * @param context
	 * @param srchSvc
	 * @param issue_code
	 * @return the issue caId or null is query does not return exactly one
	 *         result
	 * @throws RSuiteException
	 */
	public static String getIssueCaId(Log log, User user,
			ExecutionContext context, String issue_code)
			throws RSuiteException {
		SearchService searchSvc = context.getSearchService();
		String issueQuery = "/rs_ca_map/rs_ca[rmd:get-type(.) = 'issue' and rmd:get-lmd-value(., 'issue_code') = '"
				+ issue_code + "']";
		
		issueQuery = XpathUtils.resolveRSuiteFunctionsInXPath(issueQuery);
		
		log.info("getIssueCaId: issue query is " + issueQuery);
		List<ManagedObject> caSet = searchSvc.executeXPathSearch(user,
				issueQuery, 1, 0);
		int nIssues = caSet.size();
		log.info("getIssueCaId: query returned " + nIssues);
		if (nIssues != 1) {
			return null;
		} else {
			return caSet.get(0).getId();
		}
	}
	
	/**
	 * Gets the book caId from the book code.
	 * 
	 * @param log
	 * @param user
	 * @param context
	 * @param srchSvc
	 * @param book_code
	 * @return the book caId or null is query does not return exactly one
	 *         result
	 * @throws RSuiteException
	 */
	public static String getBookCaId(Log log, User user,
			WorkflowExecutionContext context, SearchService srchSvc,
			String book_code) throws RSuiteException {
		String bookQuery = "/rs_ca_map/rs_ca[rmd:get-type(.) = 'book' and rmd:get-lmd-value(., 'book_code') = '"
			+ book_code + "']";
		
		bookQuery = XpathUtils.resolveRSuiteFunctionsInXPath(bookQuery);
		
		log.info("getIssueCaId: issue query is " + bookQuery);
		List<ManagedObject> caSet = srchSvc.executeXPathSearch(user,
				bookQuery, 1, 0);
		int nBooks = caSet.size();
		log.info("getIssueCaId: query returned " + nBooks);
		if (nBooks != 1) {
			return null;
		} else {
			return caSet.get(0).getId();
		}
	}
	
	public static ExternalCompanyUser getTypessterUser(RemoteApiExecutionContext context, String contextRsuiteId
			) throws RSuiteException{
			ContentAssemblyItem journalCa = JournalBrowserUtils.getAncestorJournal(context, contextRsuiteId);
			return BookUtils.getTypeSetterUser(context, journalCa);						
	}
	
	public static ExternalCompanyUser getInspecClassifierUser(RemoteApiExecutionContext context, String contextRsuiteId
			) throws RSuiteException{
			ContentAssemblyItem journalCa = JournalBrowserUtils.getAncestorJournal(context, contextRsuiteId);
			User user = ProjectUserUtils.getUser(context, BookUtils.getUserIdFormLmd(journalCa, LMD_FIELD_INSPEC_CLASSIFIER));
			return new ExternalCompanyUser(context, user);		
	}
	
	public static String getArticleDOI(XmlApiManager xmlApiManager,
			Element elem
			) throws RSuiteException {
		XPathEvaluator eval = xmlApiManager.getXPathEvaluator();
		return eval.executeXPathToString(DOI_XPATH, elem);
	}
	
	public static String getArticleAuthorInstitution(XmlApiManager xmlApiManager,
			Element elem
			) throws RSuiteException {
		XPathEvaluator eval = xmlApiManager.getXPathEvaluator();
		return eval.executeXPathToString(AUTHOR_INSTITUTION_XPATH, elem);
	}
	
	public static String getArticleAuthorCountry(XmlApiManager xmlApiManager,
			Element elem
			) throws RSuiteException {
		XPathEvaluator eval = xmlApiManager.getXPathEvaluator();
		return eval.executeXPathToString(AUTHOR_COUNTRY_XPATH, elem);
	}
}
