package org.theiet.rsuite.journals.domain.issues.publish.proof.export;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.export.*;
import com.rsicms.projectshelper.publish.workflow.configuration.ContentExporterConfiguration;

public class IssueContentExporterConfiguration implements ContentExporterConfiguration {

    @Override
    public MoExportConfiguration getMoExporterConfiguration(WorkflowExecutionContext context,
            User user) throws RSuiteException {
        return new IssueMoExporterConfiguration();
    }

    @Override
    public MoExportContainerContext getMoExportContainerContext(WorkflowExecutionContext context,
            User user) throws RSuiteException {
        return null;
    }

}
