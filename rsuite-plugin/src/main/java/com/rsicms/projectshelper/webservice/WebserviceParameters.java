package com.rsicms.projectshelper.webservice;

public enum WebserviceParameters implements RSuiteWebserviceParameters {
    RSUITE_ID("rsuiteId");
        
    private String parameterName;
    
    private WebserviceParameters(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }
    
}
