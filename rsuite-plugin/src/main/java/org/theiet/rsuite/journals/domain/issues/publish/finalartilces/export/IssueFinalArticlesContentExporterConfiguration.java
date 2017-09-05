package org.theiet.rsuite.journals.domain.issues.publish.finalartilces.export;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.export.*;
import com.rsicms.projectshelper.publish.workflow.configuration.ContentExporterConfiguration;

public class IssueFinalArticlesContentExporterConfiguration implements ContentExporterConfiguration {

    @Override
    public MoExportConfiguration getMoExporterConfiguration(WorkflowExecutionContext context,
            User user) throws RSuiteException {
        return new IssueFinalArticlesMoExporterConfiguration();
    }

    @Override
    public MoExportContainerContext getMoExportContainerContext(WorkflowExecutionContext context,
            User user) throws RSuiteException {
        return null;
    }

}
