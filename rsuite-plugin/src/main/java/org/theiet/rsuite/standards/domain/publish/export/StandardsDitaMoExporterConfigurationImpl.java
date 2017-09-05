package org.theiet.rsuite.standards.domain.publish.export;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.export.MoExportHandler;
import com.rsicms.projectshelper.export.impl.configuration.DitaMoExporterConfigurationImpl;

public class StandardsDitaMoExporterConfigurationImpl extends DitaMoExporterConfigurationImpl {

	private StandardsDitaMoExportHandler moExportHandler;
	
	public StandardsDitaMoExporterConfigurationImpl(StandardsDitaMoExportHandler moExportHandler) throws RSuiteException {
		this.moExportHandler = moExportHandler;
	}

	@Override
	public MoExportHandler createMoExportHandler() {
		return moExportHandler;
	}
}
