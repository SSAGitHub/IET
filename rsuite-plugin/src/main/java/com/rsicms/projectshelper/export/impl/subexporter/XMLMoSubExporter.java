package com.rsicms.projectshelper.export.impl.subexporter;

import java.io.File;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.*;

import net.sf.saxon.FeatureKeys;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.export.impl.cache.MoExportCache;
import com.rsicms.projectshelper.export.impl.exportercontext.MoExportContext;
import com.rsicms.projectshelper.export.impl.refence.ReferenceProcessor;

public class XMLMoSubExporter implements MoSubExporter {

    private File outputFolder;
    
    private User user;
    
    private MoExportContext exportContext;
    
    private ExecutionContext context;
    
    private static SAXTransformerFactory TRANSFORMER_FACTORY = (SAXTransformerFactory) TransformerFactory
            .newInstance();
    
    public XMLMoSubExporter(ExecutionContext context, User user, MoExportContext exportContext,
            File outputFolder) {
        this.context = context;
        this.user = user;
        this.exportContext = exportContext;
        this.outputFolder = outputFolder;
    }

    @Override
    public SubExporterResult exportMo(MoExportCache moExportCache, ManagedObject moToExport, String exportPath) throws RSuiteException {
       
        
        File outputFile =  new File(outputFolder, exportPath);
        createParentFolders(outputFile);

        return processReferencesInManagedObject(moToExport, moExportCache,
                outputFile);                
    }
    
    private void createParentFolders(File outputFile) {
        outputFile.getParentFile().mkdirs();
    }
    
    private SubExporterResult processReferencesInManagedObject(
            ManagedObject moToExport, MoExportCache moExportCache,
            File outputFile) throws RSuiteException {

        ReferenceProcessor referenceProcessor = new ReferenceProcessor(context, user,
                exportContext, moExportCache);

        ClassLoader tcl = Thread.currentThread().getContextClassLoader();

        try {

            Thread.currentThread().setContextClassLoader(
                    this.getClass().getClassLoader());
            
            MoExportTransformationHelper moExportTransformation = new MoExportTransformationHelper(
                    TRANSFORMER_FACTORY, exportContext.getExportHandler(),
                    exportContext.getExportConfiguration());
            
            TransformerHandler mainTransfomerHandler = moExportTransformation
                    .setupMainTransfomerHandler(moToExport, referenceProcessor,
                            outputFile);

            Transformer transformer = TRANSFORMER_FACTORY.newTransformer();
            
            TRANSFORMER_FACTORY.setAttribute(FeatureKeys.EXPAND_ATTRIBUTE_DEFAULTS, false);
            
            transformer.transform(new DOMSource(moToExport.getElement()), new SAXResult(
                    mainTransfomerHandler));            

        } catch (TransformerConfigurationException e) {
            throw new RSuiteException(0, e.getLocalizedMessage(), e);
        } catch (TransformerException e) {
            throw new RSuiteException(0, e.getLocalizedMessage(), e);
        } finally {
            Thread.currentThread().setContextClassLoader(tcl);
        }

        return new SubExporterResult(referenceProcessor.getMoToExport(), referenceProcessor.getExportPathMap());

    }
}
