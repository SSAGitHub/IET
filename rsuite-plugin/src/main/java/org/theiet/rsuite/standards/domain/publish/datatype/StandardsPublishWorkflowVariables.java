package org.theiet.rsuite.standards.domain.publish.datatype;

import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;

public enum StandardsPublishWorkflowVariables implements RSuiteWorkflowVariable {

    GENERATOR_ID("standardsGeneratorId"),
    
    TRANSTYPE("transtype"),
    
    ICML_XSLT_URI("icmlXslt"),
    
    MATHML_SIZE("mathml_size"),
    
    MATHML_FONT("mathml_font"),
    
    DOMAIN_URL("domainURL"),
	
	GENERATE_CHANGE_TRACKING("generateChangeTracking");

    private String variableName;
    
    private StandardsPublishWorkflowVariables(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public String getVariableName() {
        return variableName;
    }
    
    
}
