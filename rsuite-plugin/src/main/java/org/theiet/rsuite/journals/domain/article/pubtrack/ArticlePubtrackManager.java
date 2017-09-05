package org.theiet.rsuite.journals.domain.article.pubtrack;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.constants.JournalsEvents;
import org.theiet.rsuite.utils.PubtrackLogger;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class ArticlePubtrackManager {

	private static final String PUBTRACK_PRODUCT_NAME = "ARTICLE";

	private ArticlePubtrackManager() {
	}

	public static void createArticleProcess(ExecutionContext context,
			User user, Log log, Article article) throws RSuiteException {		
		PubtrackLogger.createProcess(user, context, log, PUBTRACK_PRODUCT_NAME, article.getArticleId(), article.getArticleCaId());
		PubtrackLogger.addMetaItemToProcess(user, context, log, PUBTRACK_PRODUCT_NAME, article.getArticleId(), JournalConstants.PUBTRACK_JOURNAL_CODE, article.getJounralCode());
	}
	
	
	public static void completeArticleProcess(ExecutionContext context,
			User user, Article article) throws RSuiteException {
		PubtrackLogger.completeProcess(user, context, PUBTRACK_PRODUCT_NAME,
				article.getArticleId());
	}

	public static void logSendToDigitalLibrary(ExecutionContext context,
			User user, Article article) throws RSuiteException {
		PubtrackLogger.logToProcess(context, user, PUBTRACK_PRODUCT_NAME,
				article.getArticleId(),
				JournalsEvents.DELIVERED_TO_DIGITAL_LIBRARY);

	}
	
	public static void logInitialSendToDigitalLibrary(ExecutionContext context,
			User user, Article article) throws RSuiteException {
		PubtrackLogger.logToProcess(context, user, PUBTRACK_PRODUCT_NAME,
				article.getArticleId(),
				JournalsEvents.INITIAL_DELIVERY_TO_DIGITAL_LIBRARY);

	}
}