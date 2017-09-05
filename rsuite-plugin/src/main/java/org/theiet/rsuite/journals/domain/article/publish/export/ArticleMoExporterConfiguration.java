package org.theiet.rsuite.journals.domain.article.publish.export;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.export.MoExportHandler;
import com.rsicms.projectshelper.export.impl.configuration.JatsMoExporterConfigurationImpl;

public class ArticleMoExporterConfiguration extends JatsMoExporterConfigurationImpl {

    public ArticleMoExporterConfiguration() throws RSuiteException {
        super();
    }

    @Override
    public MoExportHandler createMoExportHandler() {
        return new ArticleMoExportHandler();
    }
}
