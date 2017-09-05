package org.theiet.rsuite.journals.domain.article.helpers;

import static org.theiet.rsuite.journals.domain.constants.JournalsEvents.*;

import java.io.File;

import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.publish.ArticlePublishPdfWorkflowLauncher;
import org.theiet.rsuite.journals.domain.journal.Journal;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.helpers.upload.RSuiteFileLoadResult;

public final class ArticleTypesetterIngestionHelper {

    private ArticleTypesetterIngestionHelper() {}

    public static RSuiteFileLoadResult loadTypesetterUpdate(ExecutionContext context, User user,
            Article article, File typesetterUpdatePackage) throws RSuiteException {

        RSuiteFileLoadResult loadResult = article.loadTypesetterFiles(typesetterUpdatePackage);
        
        article.cleanTypesetterAwaitingStatus();

        if (article.isInitialTypesetterUpdate()) {
            article.setReceivedTypesetterUpdateFlag();
            ArticleWorkflowLauncher.startPrepareArticle(context, user, article);   
            
            article.logArticleEvent(TYPESETTER_INITIAL_RECEIVED);
        } else if (article.isFinalTypesetterUpdate()) {
            article.logArticleEvent(TYPESETTER_FINAL_RECEIVED);
        } else {
            article.logArticleEvent(TYPESETTER_UPDATE_RECEIVED);
        }

        Journal journal = article.getJournal();
        if (journal.isAutomaticPdfGenerationWorkflow()){
            generateArticlePDF(context, user, article);
        }

        return loadResult;
    }

	private static void generateArticlePDF(ExecutionContext context, User user,
			Article article) throws RSuiteException {
		if (article.isInitialTypesetterUpdate()) {
		    ArticlePublishPdfWorkflowLauncher.launchProofPublishPdfWorkflowForArticle(context,
		            user, article);
		} else {
		    ArticlePublishPdfWorkflowLauncher.launchFinalPublishPdfWorkflowForArticle(context,
		            user, article);
		}
	}

}
