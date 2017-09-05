package com.rsicms.projectshelper.export.impl.subexporter;

import com.reallysi.rsuite.api.*;
import com.rsicms.projectshelper.export.impl.cache.MoExportCache;


public interface MoSubExporter {

    SubExporterResult exportMo(MoExportCache moExportCache, ManagedObject moToExport,
            String exportPath) throws RSuiteException;

}
