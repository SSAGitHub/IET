package org.theiet.rsuite.journals.actionhandlers;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.delivery.author.AuthorDelivery;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadata;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadataUpdater;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.journals.transforms.InspecClassificationMerge;
import org.theiet.rsuite.utils.ExceptionUtils;
import org.theiet.rsuite.utils.PubtrackLogger;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.AbstractBaseActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.WorkflowVariables;

public class InspecMerge extends AbstractBaseActionHandler {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(WorkflowExecutionContext context) throws RSuiteException {
		Log log = context.getWorkflowLog();
		User user = getSystemUser();
		
		String articleCaId = context.getVariable(WorkflowVariables.RSUITE_CONTENTS.getVariableName());
				
		Article article = new Article(context, user, log, articleCaId);
		Journal journal = article.getJournal();
		
		if (journal.isAutomaticPdfGenerationWorkflow()){
			log.info("execute: Send complimentary email");
			AuthorDelivery.sendFinalComplimentaryPDF(context, user, article);
		}
				
		ArticleMetadata articleMetadata = article.getArticleMetadata();
		
		if (!articleMetadata.isInspecRequired()){
			log.info("execute: Inspec classification not required, skipping");
			return;
		}
		
		try{
		
			InspecClassificationMerge.mergeInspecClassification(context, article);
		}catch (Exception e) {
			ExceptionUtils.throwWorfklowException(context, e, e.getLocalizedMessage());
		}
		
		ArticleMetadataUpdater metadataUpdater = article.createArticleMetadataUpdater();
		metadataUpdater.makeArticleAvailable();
		metadataUpdater.updateMetadata();

		PubtrackLogger.completeProcess(user, context, "ARTICLE", article.getArticleId());

		
	}

}