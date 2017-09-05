package org.theiet.rsuite.journals.webservice;

import java.util.Map;

import org.apache.commons.logging.*;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.utils.JournalUtils;
import org.theiet.rsuite.utils.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.api.remoteapi.result.*;
import com.reallysi.rsuite.service.*;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class UnassignArticle extends DefaultRemoteApiHandler {
	
	private static final String DIALOG_TITLE = "Unassign Article";

	public MessageDialogResult execute (
			RemoteApiExecutionContext context,
			CallArgumentList args) {
		Log log = LogFactory.getLog(UnassignArticle.class);
		User user = context.getSession().getUser();
		SearchService srchSvc = context.getSearchService();
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		ManagedObjectService moSvc = context.getManagedObjectService();
		ObjectDetachOptions detachOpts = new ObjectDetachOptions();
		ObjectAttachOptions attachOpts = new ObjectAttachOptions();
		
		String articleCaId = args.getFirstValue("rsuiteId");
		try {
			ContentAssembly articleCa = caSvc.getContentAssembly(user, articleCaId);
			String journalCode = articleCa.getLayeredMetadataValue(JournalConstants.LMD_FIELD_JOURNAL_CODE);
			if (StringUtils.isBlank(journalCode)) {
				throw new RSuiteException("Article does not have journal code");
			}
			ContentAssembly issueCa = (ContentAssembly)ProjectContentAssemblyUtils.getAncestorCAbyType(context, articleCaId, JournalConstants.CA_TYPE_ISSUE);
			String issuePublishedDate = issueCa.getLayeredMetadataValue(JournalConstants.LMD_FIELD_PRINT_PUBLISHED_DATE);
			if (!StringUtils.isBlank(issuePublishedDate)) {
				return new MessageDialogResult(MessageType.ERROR, DIALOG_TITLE, 
						"Issue has already been published");
			}			ContentAssembly issueArticlesCa = (ContentAssembly)ProjectContentAssemblyUtils.getAncestorCAbyType(context, articleCaId, JournalConstants.CA_TYPE_ISSUE_ARTICLES);
			log.info("execute: check issuePublishedDate is " + issuePublishedDate);

			String issueCaId = issueArticlesCa.getId();
			Map<String, String> refIdMap = ProjectBrowserUtils.getCaRefIdMap(user, caSvc, issueArticlesCa);
			String journalCaId = JournalUtils.getJournalCaId(log, user, context, srchSvc, journalCode);
			ContentAssembly journalCa = caSvc.getContentAssembly(user, journalCaId);
			ContentAssembly articlePoolCa = ProjectBrowserUtils.getChildCaByType(context, journalCa, JournalConstants.CA_TYPE_ARTICLES);
			String articlePoolCaId = articlePoolCa.getId();
			log.info("execute: attach " + articleCaId + " to " + articlePoolCaId);
			caSvc.attach(user, articlePoolCaId, articleCa, attachOpts);
			String refId = refIdMap.get(articleCaId);
			log.info("execute: detach refid " + refId + " from " + issueCaId);
			caSvc.detach(user, issueCaId, refIdMap.get(articleCaId), detachOpts);
			MetaDataItem item = new MetaDataItem(JournalConstants.LMD_FIELD_ARTICLE_AVAILABLE, JournalConstants.LMD_VALUE_YES);
			moSvc.setMetaDataEntry(user, articleCaId, item);
			IetUtils.removeMetaDataFieldFromCa(log, user, moSvc, articleCa, JournalConstants.LMD_FIELD_ARTICLE_ASSIGNED);
			IetUtils.removeMetaDataFieldFromCa(log, user, moSvc, articleCa, JournalConstants.LMD_FIELD_ISSUE_CODE);
			return new MessageDialogResult(DIALOG_TITLE, "Article unassigned");
		}
		catch (RSuiteException e) {
			return new MessageDialogResult(MessageType.ERROR, DIALOG_TITLE, 
					"Server returned " + e.getMessage());
		}
	}

}
