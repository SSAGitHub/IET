package com.rsicms.projectshelper.publish.workflow.configuration;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.export.*;
import com.rsicms.projectshelper.export.impl.configuration.DitaMoExporterConfigurationImpl;

public class DefaultContentExporterConfiguration implements ContentExporterConfiguration {

    @Override
    public MoExportConfiguration getMoExporterConfiguration(WorkflowExecutionContext context,
            User user) throws RSuiteException {
        return new DitaMoExporterConfigurationImpl();
    }

    @Override
    public MoExportContainerContext getMoExportContainerContext(WorkflowExecutionContext context,
            User user) {
        return null;
    }

}
