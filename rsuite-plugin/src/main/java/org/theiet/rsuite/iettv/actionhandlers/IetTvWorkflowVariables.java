package org.theiet.rsuite.iettv.actionhandlers;

public enum IetTvWorkflowVariables {

    VIDEO_ID("iettv_video_id");
    
    private String variableName;
    
    private IetTvWorkflowVariables(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }
    
}
