package org.theiet.rsuite.journals.domain.article.publish.generation;

import static org.theiet.rsuite.journals.domain.article.publish.datatype.ArticlePublishWorkflowVariables.*;

import org.theiet.rsuite.journals.domain.article.Article;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.generators.OutputGenerator;
import com.rsicms.projectshelper.publish.workflow.configuration.OutputGeneratorConfiguration;

public class ArticleOutputGeneratorConfiguration implements OutputGeneratorConfiguration {

    @Override
    public OutputGenerator getOutputGenerator(WorkflowExecutionContext context, User user)
            throws RSuiteException {
    	
        String articleCaId = context.getVariable(ARTICLE_CA_ID.getVariableName());
        Article article = new Article(context, user, context.getWorkflowLog(), articleCaId);
    	
        return new ArticlePdfOutputGenerator(article);
    }

}
