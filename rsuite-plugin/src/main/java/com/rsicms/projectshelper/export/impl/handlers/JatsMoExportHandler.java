package com.rsicms.projectshelper.export.impl.handlers;

import org.apache.commons.io.FilenameUtils;

import com.reallysi.rsuite.api.*;

public class JatsMoExportHandler extends DefaultMoExportHandler {

    @Override
    public String getMoExportPath(ManagedObject mo, String exportUri)
            throws RSuiteException {
        
        String newExportUir = exportUri;
        if (mo.isNonXml()) {
            String fileName = FilenameUtils.getName(exportUri);
            newExportUir = "images/" + fileName;
        }

        return newExportUir;
    }
    
}
