package com.rsicms.projectshelper.export;

import java.io.File;
import java.util.List;

import com.reallysi.rsuite.api.*;

public interface MoExporter {

	MoExportResult exportMo(MoExportContainerContext exportContainerContext, ManagedObject mo, File outputFolder)
			throws RSuiteException;

	MoExportResult exportMo(ManagedObject mo, File outputFolder)
			throws RSuiteException;

    MoExportResult exportMo(MoExportContainerContext exportContainerContext,
            List<ManagedObject> mo, File outputFolder) throws RSuiteException;

}