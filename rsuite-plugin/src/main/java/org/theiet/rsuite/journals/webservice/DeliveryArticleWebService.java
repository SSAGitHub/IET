package org.theiet.rsuite.journals.webservice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.delivery.digitallibrary.ArticleDigitalLibrary;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.reallysi.rsuite.service.ContentAssemblyService;

public class DeliveryArticleWebService extends DefaultRemoteApiHandler {
	
	private static final String DIALOG_TITLE = "Deliver to Digital Library";
	private static Log log = LogFactory.getLog(DeliveryArticleWebService.class);
	
	public MessageDialogResult execute (
			RemoteApiExecutionContext context,
			CallArgumentList args) {
		User user = context.getSession().getUser();
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		String articleCaId = args.getFirstValue(IetConstants.PARAM_RSUITE_ID);
		try {
			ContentAssembly articleCa = caSvc.getContentAssembly(user, articleCaId);
			String articleTitle = articleCa.getLayeredMetadataValue(JournalConstants.LMD_FIELD_ARTICLE_TITLE);
			Article article = new Article(context, user, articleCa);
	
			log.info("execute: attempting delivery of article " + articleTitle + " (" + articleCaId + ")");
			ArticleDigitalLibrary digitalLibrary = new ArticleDigitalLibrary(context, user, log);
			digitalLibrary.deliverArticle(article, false);		
		
			return new MessageDialogResult(MessageType.SUCCESS, DIALOG_TITLE, "Article " + articleTitle + " delivered");
		} catch (RSuiteException e) {
			log.error(e,e);
			return new MessageDialogResult(MessageType.ERROR, DIALOG_TITLE, "Server returned exception " + e.getMessage());
		}
	}

}
