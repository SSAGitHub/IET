package org.theiet.rsuite.standards.domain.book.export.esp;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.export.MoExportHandler;
import com.rsicms.projectshelper.export.impl.configuration.DitaMoExporterConfigurationImpl;

public class ESPDitaMoExporterConfiguration extends DitaMoExporterConfigurationImpl {

    public ESPDitaMoExporterConfiguration() throws RSuiteException {
        super();
    }

    @Override
    public MoExportHandler createMoExportHandler() {
        return new ESPDitaMoExportHandler();
    }
}
