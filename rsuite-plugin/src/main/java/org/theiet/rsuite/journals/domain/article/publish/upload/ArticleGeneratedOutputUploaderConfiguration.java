package org.theiet.rsuite.journals.domain.article.publish.upload;

import static org.theiet.rsuite.journals.domain.article.publish.datatype.ArticlePublishWorkflowVariables.ARTICLE_CA_ID;

import org.theiet.rsuite.journals.domain.article.Article;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.storage.upload.GeneratedOutputUploader;
import com.rsicms.projectshelper.publish.workflow.configuration.GeneratedOutputUploaderConfiguration;

public class ArticleGeneratedOutputUploaderConfiguration implements
        GeneratedOutputUploaderConfiguration {

    @Override
    public GeneratedOutputUploader getGeneratedOutputUploader(WorkflowExecutionContext context,
            User user) throws RSuiteException {
 
        String workflowProcessId = context.getProcessInstanceId();
        
        String articleCaId = context.getVariable(ARTICLE_CA_ID.getVariableName());
        Article article = new Article(context, user, context.getWorkflowLog(), articleCaId);
        
        return new ArticleGeneratedOutputUploader(context, user, context.getWorkflowLog(), workflowProcessId, article);
    }

}
