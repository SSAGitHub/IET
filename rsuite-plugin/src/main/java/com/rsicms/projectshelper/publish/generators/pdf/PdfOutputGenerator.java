package com.rsicms.projectshelper.publish.generators.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.publish.generators.OutputGenerator;
import com.rsicms.projectshelper.publish.generators.pdf.engines.PdfFromattingEngine;
import com.rsicms.projectshelper.utils.ProjectTransformationUtils;

public abstract class PdfOutputGenerator implements OutputGenerator {

    private ExecutionContext context;
    
    private Log logger;
    
    @Override
    public void initialize(ExecutionContext context, Log logger, Map<String, String> variables)
            throws RSuiteException {
            this.context = context;
            this.logger = logger;
    }
    
    @Override
    public File generateOutput(File tempFolder, String moId, File inputFile, File outputFolder)
            throws RSuiteException {

        String defaultOutputFileName = FilenameUtils.getBaseName(inputFile.getName()) + ".pdf";
        
        File outputFile = new File(outputFolder, getOutputFileName(defaultOutputFileName));
        
        try{
            File foFile = transformXMLFileToFO(context, inputFile, tempFolder);
            PdfOutputGeneratorHelper.copyNonXmlFilesToTemp(inputFile.getParentFile(), tempFolder);
            
            PdfFromattingEngine formattingEngine = getFormattingEngine();
            formattingEngine.generatePDFFromFo(logger, foFile, outputFile);
        }catch (RSuiteException e) {
            throw new RSuiteException(0, createGenerateOutputErrorMessage(inputFile),e );
        } catch (IOException e) {
            throw new RSuiteException(0, createGenerateOutputErrorMessage(inputFile),e );
        }
        
        return outputFile;
    }

    

    private String createGenerateOutputErrorMessage(File inputFile) {
        return "Unable to generate output for " + inputFile.getAbsolutePath();
    }

    protected abstract String getXml2FoXsltUri();

    protected abstract PdfFromattingEngine getFormattingEngine();
    
    private File transformXMLFileToFO(ExecutionContext context, File xmlFile,
            File tempFolder) throws RSuiteException {

        File foFile = new File(tempFolder, "temp.fo");

        try {
            FileWriter writer = new FileWriter(foFile);
            ProjectTransformationUtils.transformDocument(context.getXmlApiManager(), new FileInputStream(xmlFile),
                    getXml2FoXsltUri(), writer, getXml2FoXslParameters());
        } catch (IOException e) {
            throw new RSuiteException(0, "Unable to create writer for " + foFile.getAbsolutePath());
        }

        return foFile;
    }

    protected Map<String, String> getXml2FoXslParameters() {
        return new HashMap<String, String>();
    }

    protected abstract String getOutputFileName(String defaultName);
}
