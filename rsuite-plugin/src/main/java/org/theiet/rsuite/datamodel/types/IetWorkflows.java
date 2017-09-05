package org.theiet.rsuite.datamodel.types;

public enum IetWorkflows {

    IET_PUBLISH_WORKFLOW("IET Publish Workflow");
    
    private String workflowName;

    private IetWorkflows(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getWorkflowName() {
        return workflowName;
    }
    
    
    
}
