package org.theiet.rsuite.journals.domain.article.publish.datatype;

import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;

public enum ArticlePublishWorkflowVariables implements RSuiteWorkflowVariable {

    ARTICLE_CA_ID("articleCaId"),
    PUBLISH_TYPE("article_publish_type");

    private String variableName;
    
    private ArticlePublishWorkflowVariables(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public String getVariableName() {
        return variableName;
    }
    
    
}
