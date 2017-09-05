package org.theiet.rsuite.journals.utils;

import java.util.List;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.utils.ExceptionUtils;
import org.theiet.rsuite.utils.XpathUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyReference;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.ReferenceInfo;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.DependencyTracker;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.reallysi.rsuite.service.SearchService;

public class JournalWorkflowUtils implements JournalConstants{

    @Deprecated
	public static String getArticleCaId(WorkflowExecutionContext worfklowContext, String pId, String articleId) throws RSuiteException{
		
		User user = worfklowContext.getAuthorizationService().getSystemUser();
		SearchService srchSvc = worfklowContext.getSearchService();
		articleId = JournalUtils.normalizeArticleId(articleId);
		
		String articleQuery = createArticleQuery(articleId);

		List<ManagedObject> caSet = srchSvc.executeXPathSearch(user, articleQuery, 1, 1);
		int nArticles = caSet.size();
		
		if (nArticles != 1) {
			String msg = "Article query " + articleQuery + " returned " + nArticles + ". It should be a exactly one article with id: " + articleId;
			worfklowContext.setVariable(WF_VAR_ERROR_MSG, ExceptionUtils.constructWfReportLink(msg, pId));
			throw new RSuiteException(msg);
		}
		
		return caSet.get(0).getId();
	}

	private static String createArticleQuery(String articleId) {
		return XpathUtils.resolveRSuiteFunctionsInXPath("/rs_ca_map/rs_ca[rmd:get-type(.) = 'article' and rmd:get-display-name(.) = '" + articleId + "']");
	}
	
	public static int getArticleCaCount(ExecutionContext worfklowContext, String articleId) throws RSuiteException{
		
		User user = worfklowContext.getAuthorizationService().getSystemUser();
		SearchService srchSvc = worfklowContext.getSearchService();
		articleId = JournalUtils.normalizeArticleId(articleId);
		
		String articleQuery = createArticleQuery(articleId);
		
		List<ManagedObject> caSet = srchSvc.executeXPathSearch(user, articleQuery, 1, 1);
		
		return caSet.size();
}
	
	public static String getJournalCodeFromWorkflow(WorkflowExecutionContext context, 
			Log log, 
			String articleCaId) {
		String journal_code = new String();
		ManagedObjectService moSvc = context.getManagedObjectService();
		DependencyTracker tracker = moSvc.getDependencyTracker();
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		User user = context.getAuthorizationService().getSystemUser();
		try {
			log.info("getJournalCodeFromWorkflow: look up journal code for article node " + articleCaId);
			List<ReferenceInfo> refList = tracker.listDirectReferences(user, articleCaId);
			if (refList.size() == 1) {
				ReferenceInfo ref = refList.get(0);
				String browseUri = ref.getParentBrowseUri();
				String[] parents = browseUri.split("\\/");
				String refCaId = parents[parents.length - 1].split(":")[1];
				ContentAssemblyReference refCa = (ContentAssemblyReference)caSvc.getContentAssemblyItem(user, refCaId);
				String journalCaId = refCa.getTargetId();
				ContentAssembly journalCa = caSvc.getContentAssembly(user, journalCaId);
				journal_code = journalCa.getLayeredMetadataValue(JournalConstants.LMD_FIELD_JOURNAL_CODE);
			}
			else {
				log.info("getJournalCodeFromWorkflow: parent list is size " + refList.size());
			}
		}
		catch (RSuiteException e) {
			log.debug(e.getMessage(), e);
		}
		return journal_code;
	}
}