package org.theiet.rsuite.journals.domain.issues.publish.common.datatype;

import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;

public enum IssuePublishWorkflowVariables implements RSuiteWorkflowVariable {
    ISSUE_CA_ID("issueCaId");

    private String variableName;
    
    private IssuePublishWorkflowVariables(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public String getVariableName() {
        return variableName;
    }
    
    
}
