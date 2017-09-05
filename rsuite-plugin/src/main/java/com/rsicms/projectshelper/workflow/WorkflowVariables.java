package com.rsicms.projectshelper.workflow;

import com.reallysi.rsuite.api.workflow.MoListWorkflowObject;

public enum WorkflowVariables implements RSuiteWorkflowVariable {

    RSUITE_PATH("rsuitePath"), RSUITE_USER_ID("rsuiteUserId"), RSUITE_SESSION_ID("rsuiteSessionId"), RSUITE_CONTENTS(
            "rsuite contents"), SESSION_KEY("skey"), MO_LIST_WORKFLOW_OBJECT(
            		MoListWorkflowObject.class.getName()), RSUITE_USER_FULL_NAME(
            "rsuiteUserFullName"), RSUITE_USER_EMAIL_ADDRESS("rsuiteUserEmailAddress"),
            RSUITE_URL("rsuiteURL");


    private String variableName;

    private WorkflowVariables(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }
}
