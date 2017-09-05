package org.theiet.rsuite.journals.domain.issues.publish.proof.export;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.export.MoExportHandler;
import com.rsicms.projectshelper.export.impl.configuration.JatsMoExporterConfigurationImpl;

public class IssueMoExporterConfiguration extends JatsMoExporterConfigurationImpl {

	
    public IssueMoExporterConfiguration() throws RSuiteException {
        super();
    }

    @Override
    public MoExportHandler createMoExportHandler() {
        return new IssueMoExportHandler();
    }
}
