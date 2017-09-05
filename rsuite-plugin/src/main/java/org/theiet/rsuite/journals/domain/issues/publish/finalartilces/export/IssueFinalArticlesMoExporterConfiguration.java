package org.theiet.rsuite.journals.domain.issues.publish.finalartilces.export;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.export.MoExportHandler;
import com.rsicms.projectshelper.export.impl.configuration.JatsMoExporterConfigurationImpl;

public class IssueFinalArticlesMoExporterConfiguration extends JatsMoExporterConfigurationImpl {

    public IssueFinalArticlesMoExporterConfiguration() throws RSuiteException {
        super();
    }

    @Override
    public MoExportHandler createMoExportHandler() {
        return new IssueFinalArticlesMoExportHandler();
    }
}
