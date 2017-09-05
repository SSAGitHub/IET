package org.theiet.rsuite.standards.domain.book.export.esp;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.export.MoExportHandler;
import com.rsicms.projectshelper.export.impl.configuration.JatsMoExporterConfigurationImpl;

public class ESPJatsMoExporterConfiguration extends JatsMoExporterConfigurationImpl {

    public ESPJatsMoExporterConfiguration() throws RSuiteException {
        super();
    }

    @Override
    public MoExportHandler createMoExportHandler() {
        return new ESPJatsMoExportHandler();
    }
}
