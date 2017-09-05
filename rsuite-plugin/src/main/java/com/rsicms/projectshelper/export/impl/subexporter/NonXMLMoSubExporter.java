package com.rsicms.projectshelper.export.impl.subexporter;

import java.io.*;

import org.apache.commons.io.*;
import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.*;
import com.rsicms.projectshelper.export.MoExportHandler;
import com.rsicms.projectshelper.export.impl.cache.MoExportCache;
import com.rsicms.projectshelper.export.impl.exportercontext.MoExportContext;

public class NonXMLMoSubExporter implements MoSubExporter  {

    private MoExportContext exportContext;
    
    private File outputFolder;
    
    public NonXMLMoSubExporter(MoExportContext exportContext, File outputFolder) {
        this.exportContext = exportContext;
        this.outputFolder = outputFolder;
    }
    
    @Override
    public SubExporterResult exportMo(MoExportCache moExportCache, ManagedObject moToExport,
            String exportPath) throws RSuiteException {
        
        File outputFile = new File(outputFolder, exportPath);
        
        InputStream managedObjectInputStream = getMoStream(moToExport);
        writeStreamMoToFile(moToExport, outputFile, managedObjectInputStream);
        
        return SubExporterResult.getEmptyResult();
    }

    private void writeStreamMoToFile(ManagedObject moToExport, File outputFile,
            InputStream managedObjectInputStream) throws RSuiteException {
        try{
        FileUtils.writeByteArrayToFile(outputFile,
                IOUtils.toByteArray(managedObjectInputStream));
        }catch (IOException e){
            throw new RSuiteException(0, "Unable to save file " + outputFile.getAbsolutePath() + " for " + moToExport.getId());
        }
    }

    private InputStream getMoStream(ManagedObject moToExport)
            throws RSuiteException {
        
        InputStream managedObjectInputStream = null;
        
        String variantToExport = exportContext.getExportConfiguration()
                .getExportNonXmlVariant();


        if (StringUtils.isNotBlank(variantToExport)) {
            managedObjectInputStream =
                    getVariantInputStream(moToExport, variantToExport, managedObjectInputStream);
        }

        if (managedObjectInputStream == null) {
            managedObjectInputStream = moToExport.getInputStream();
        }

        MoExportHandler exportHandler = exportContext.getExportHandler();
        managedObjectInputStream = exportHandler.getMangedObjectContent(
                moToExport, managedObjectInputStream);
        return managedObjectInputStream;
    }

    private InputStream getVariantInputStream(ManagedObject moToExport, String variantToExport,
            InputStream managedObjectInputStream) throws RSuiteException {
        VariantDescriptor variant = moToExport.getVariant(variantToExport);
        if (variant != null) {
            managedObjectInputStream = new ByteArrayInputStream(
                    variant.getContent());
        } else {

            exportContext.getMessageHandler().warning(
                    "Unable to find variant '" + variantToExport
                            + "' for mo " + moToExport.getId());
        }
        return managedObjectInputStream;
    }

    
   
}
