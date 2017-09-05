package org.theiet.rsuite.journals.actionhandlers;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.datatype.JournalWorkflowType;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.delivery.digitallibrary.ArticleDigitalLibrary;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.utils.ExceptionUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.AbstractBaseActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;

public class DeliverArticleToDigitalLibrary extends AbstractBaseActionHandler
		implements JournalConstants {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {
		Log log = context.getWorkflowLog();
		User user = getSystemUser();
		String articleCaId = context
				.getVariable(IetConstants.WF_VAR_RSUITE_CONTENTS);
		log.info("execute: start delivery of articleCaId " + articleCaId);
		
		ArticleDigitalLibrary digitalLibrary = new ArticleDigitalLibrary(context, user, log);
		Article article = new Article(context, user, articleCaId);
		try {
			digitalLibrary.deliverArticle(article);
			ContentAssembly articleCa = article.getArticleCA();

			archiveJoeArticles(context, articleCa);
		} catch (RSuiteException e) {
			ExceptionUtils.throwWorfklowException(context, e,
					"Delivery failure was " + e.getMessage());
		}
	}

	private void archiveJoeArticles(ExecutionContext context,
			ContentAssembly articleCa) throws RSuiteException {

		String journalCode = articleCa
				.getLayeredMetadataValue(LMD_FIELD_JOURNAL_CODE);
		Journal journal = new Journal(context, journalCode);

		JournalWorkflowType workflowType = journal.getWorflowType();

		if (workflowType == JournalWorkflowType.ARCHIVE) {
			journal.archiveArticle(articleCa);
		}

	}

}
