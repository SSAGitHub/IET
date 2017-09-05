package org.theiet.rsuite.journals.domain.constants;

import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;

public enum JournalWorkflowVariables implements RSuiteWorkflowVariable {
    WF_VAR_ARTICLE_ID("articleId"), WF_VAR_JOURNAL_CA_ID("journalCaId"), WF_VAR_PRODUCT_ID("id"), 
    WF_VAR_ARTICLE("ARTICLE"), WF_VAR_ISSUE("ISSUE"), WF_VAR_YEAR("year"), WF_VAR_JOURNAL_ID("journalId"),
    WF_VAR_SUBMISSION_TYPE("typesetter_update_type"), WF_VAR_FIRST_TYPESETTER_SUBMISSION("firstTypesetterSubmission");

    private String variableName;

    private JournalWorkflowVariables(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public String getVariableName() {
        return variableName;
    }

}
