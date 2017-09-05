package com.rsicms.projectshelper.publish.generators.pdf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.publish.generators.OutputGenerator;
import com.rsicms.projectshelper.publish.generators.pdf.engines.AntennaHousePdfEngine;
import com.rsicms.projectshelper.publish.generators.pdf.engines.PdfEngineFactory;
import com.rsicms.projectshelper.utils.ProjectTransformationUtils;

public abstract class AntennaHouseAreaTreePdfOutputGenerator implements OutputGenerator {

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

        File outputFile = new File(outputFolder, getOutputFileName(moId, defaultOutputFileName));

        try {

            AntennaHousePdfEngine formattingEngine = createPdfEngine();

            PdfOutputGeneratorHelper.copyNonXmlFilesToTemp(inputFile.getParentFile(), tempFolder);
            File areaFile =  createAreaFile(tempFolder, inputFile, formattingEngine);
            
            File foFile = createFoUsingAreaFile(tempFolder, inputFile, areaFile);
            
            formattingEngine.generatePDFFromFo(logger, foFile, outputFile);
        } catch (RSuiteException e) {
            throw new RSuiteException(0, createGenerateOutputErrorMessage(inputFile), e);
        } catch (IOException e) {
            throw new RSuiteException(0, createGenerateOutputErrorMessage(inputFile), e);
        }

        return outputFile;
    }
    
    protected AntennaHousePdfEngine createPdfEngine() throws RSuiteException {

        String configuration = getAntennaHouseConfigurationConfiguration();

        if (StringUtils.isEmpty(configuration)) {
            return PdfEngineFactory.createAntennaHouseEngine(context);
        }

        return PdfEngineFactory.createAntennaHouseEngine(context,
                getAntennaHouseConfigurationConfiguration());
    }
    
    private File createAreaFile(File tempFolder, File inputFile,
            AntennaHousePdfEngine formattingEngine) throws IOException, RSuiteException {
        
        File areaFile = getAreaTreeFile(tempFolder);
        createFakeAreaFile(areaFile);
        
        
        Map<String, String> transfromParameters = createTransformationParameters(areaFile);
        File foFile = transformXMLFileToFO(context, inputFile, tempFolder, transfromParameters);

        FileUtils.deleteQuietly(areaFile);

        formattingEngine.generateAreaTreeFileFromFo(logger, foFile, areaFile);
        FileUtils.moveFile(foFile, getInitialFoFile(tempFolder));
        
        return areaFile;
    }

    protected File getAreaTreeFile(File tempFolder) {
        return new File(tempFolder, "area-tree.xml");
    }

    protected File getFoFileName(File tempFolder, String passName) {
        return new File(tempFolder, String.format("temp_%s.fo", passName));
    }

    protected void createFakeAreaFile(File areaFile) throws IOException {
        FileUtils.writeStringToFile(areaFile, "<FakeAreaFile />", "utf-8");

    }

    protected File createFoUsingAreaFile(File tempFolder, File inputFile, File areaFile)
            throws RSuiteException, IOException {
        Map<String, String> transfromParameters = createTransformationParameters(areaFile);
        File foFile = transformXMLFileToFO(context, inputFile, tempFolder, transfromParameters);
        
        return foFile;
    }

    private Map<String, String> createTransformationParameters(File areaFile) {
        Map<String, String> transfromParameters = getXml2FoXslParameters();
        transfromParameters.put("area-tree-uri", areaFile.toURI().toString());
        return transfromParameters;
    }

    protected String createGenerateOutputErrorMessage(File inputFile) {
        return "Unable to generate output for " + inputFile.getAbsolutePath();
    }

    protected abstract String getXml2FoXsltUri();

    protected abstract String getAntennaHouseConfigurationConfiguration() throws RSuiteException;

    private File transformXMLFileToFO(ExecutionContext context, File xmlFile, File tempFolder,
            Map<String, String> paramaters) throws RSuiteException {

        File foFile = getFoFile(tempFolder);

        try {
            FileWriter writer = new FileWriter(foFile);
            ProjectTransformationUtils.transformDocument(context, xmlFile,
                    getXml2FoXsltUri(), writer, paramaters);
        } catch (IOException e) {
            throw new RSuiteException(0, "Unable to create writer for " + foFile.getAbsolutePath());
        }

        return foFile;
    }

    protected File getInitialFoFile(File tempFolder){
    	return getFoFileName(tempFolder, "initial");
    }
    
    protected File getFoFile(File tempFolder) {
        return new File(tempFolder, "temp_with_area_tree.fo");
    }

    protected Map<String, String> getXml2FoXslParameters() {
        return new HashMap<String, String>();
    }

    protected abstract String getOutputFileName(String moId, String defaultName) throws RSuiteException;
    
    @Override
    public void beforeGenerateOutput(File tempFolder, String moId,
    		File inputFile, File outputFolder) throws RSuiteException {
    	
    }
    
    @Override
    public void afterGenerateOutput(File tempFolder, String moId,
    		File inputFile, File outputFolder) throws RSuiteException {
    }
}
