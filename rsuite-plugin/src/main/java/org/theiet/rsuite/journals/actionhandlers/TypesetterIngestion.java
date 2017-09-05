package org.theiet.rsuite.journals.actionhandlers;

import static com.rsicms.projectshelper.workflow.WorkflowVariables.*;
import static org.theiet.rsuite.journals.domain.constants.JournalWorkflowVariables.*;

import java.io.File;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.journals.domain.article.*;
import org.theiet.rsuite.journals.domain.article.helpers.ArticleTypesetterIngestionHelper;
import org.theiet.rsuite.utils.IetUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractActionHandler;
import com.rsicms.rsuite.helpers.upload.RSuiteFileLoadResult;

public class TypesetterIngestion extends AbstractActionHandler {

    private static final long serialVersionUID = 1L;

    /*
     * Ingestion of typesetter files for article (filename ARTICLE-) only. Typesetter files for
     * issues and books (ISSUE-/BOOK-) are handled by the IssueIngestion action handler.
     */

    @Override
    public void executeTask(WorkflowExecutionContext context) throws Exception {
        Log log = context.getWorkflowLog();
        User user = getSystemUser();

        String pId = context.getProcessInstanceId();
        context.setVariable("pId", pId);
        String articleId = context.getVariable("id");
        File articleZip = context.getFileWorkflowObject().getFile();

        Article article =
                ArticleFactory.getArticleContainerBaseOnArticleId(context, user, log, articleId);

        String typesetterSubmission = setUpWorkflowVariables(context, articleId, article);

        log.info("execute: typesetterSubmission set to " + typesetterSubmission);

        RSuiteFileLoadResult loadResult =
                ArticleTypesetterIngestionHelper.loadTypesetterUpdate(context, user, article,
                        articleZip);

        if (loadResult.hasErrors()) {
            IetUtils.logLoadErrorResult(log, loadResult);
            throw new RSuiteException("Loading typesetter update has failed. Article " + articleId);
        }

    }

    private String setUpWorkflowVariables(WorkflowExecutionContext context, String articleId,
            Article article) throws RSuiteException {
        context.setVariable(WF_VAR_ARTICLE_ID.getVariableName(), articleId);
        context.setVariable(RSUITE_CONTENTS.getVariableName(), article.getArticleCaId());
        context.setVariable(WF_VAR_JOURNAL_ID.getVariableName(), article.getJounralCode());
        
        if (article.isInitialTypesetterUpdate()){
            context.setVariable(WF_VAR_FIRST_TYPESETTER_SUBMISSION.getVariableName(), "yes");   
        }
        
        
        String typesetterSubmission = article.getTypesetterUpdateType();
        context.setVariable(WF_VAR_SUBMISSION_TYPE.getVariableName(), typesetterSubmission);
        return typesetterSubmission;
    }



}
