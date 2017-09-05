package com.rsicms.projectshelper.publish.workflow.configuration;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.export.*;

public interface ContentExporterConfiguration {

    MoExportConfiguration getMoExporterConfiguration(WorkflowExecutionContext context, User user) throws RSuiteException; 
    
    MoExportContainerContext getMoExportContainerContext(WorkflowExecutionContext context, User user) throws RSuiteException;
    
}
