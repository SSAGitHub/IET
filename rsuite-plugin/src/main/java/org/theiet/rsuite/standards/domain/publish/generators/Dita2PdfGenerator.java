package org.theiet.rsuite.standards.domain.publish.generators;

import java.io.*;
import java.util.*;

import org.apache.commons.io.*;
import org.apache.commons.logging.Log;
import org.theiet.rsuite.standards.constans.PublishWorkflowContans;
import org.theiet.rsuite.standards.dita.*;
import org.theiet.rsuite.standards.utils.DitaOTUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.api.xml.DitaOpenToolkit;
import com.reallysi.tools.dita.PluginVersionConstants;
import com.rsicms.projectshelper.publish.generators.OutputGenerator;
import com.rsicms.projectshelper.publish.workflow.PublishWorkflowUtils;

public class Dita2PdfGenerator implements DitaAntProperties,
		PublishWorkflowContans, OutputGenerator {

	private Properties props;

	private DitaOpenToolkit toolkit;
	
	private Log logger;

    private ExecutionContext context;

	@Override
	public void initialize(ExecutionContext context, Log logger,
			Map<String, String> variables) throws RSuiteException {

		
		toolkit = DitaOTUtils.getToolkit(context, getVariableWithDefault(variables, WF_VAR_OPEN_TOOLKIT_NAME, "default"));
		props = new Properties();
		setPropertyIfVariableSpecified(props, TRANSTYPE, variables,
				WF_VAR_TRANSTYPE);
		this.logger = logger;
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see org.theiet.rsuite.standards.domain.publish.OutputGenerator#generateOutPut(org.apache.commons.logging.Log, java.io.File, java.io.File, java.io.File)
	 */
	@Override
	public File generateOutput(File tempFolder, String moId, File inputFile,
			File outputDir) throws RSuiteException {
		
		
	    logger.info("Using rsuite-dita-support version "
				+ PluginVersionConstants.getVersion() + ".");
		
		
		File processingFolder = new File(tempFolder, "ot-folder/" + moId);
		processingFolder.mkdirs();
		
		DitaAntLauncher.runDita2PDF(logger, toolkit.getPath(), inputFile,
				outputDir, processingFolder, props);

		String mapNamePart = FilenameUtils.getBaseName(inputFile.getName());
		String pdfFilename = mapNamePart + ".pdf";
		File pdfFile = new File(outputDir, pdfFilename);

		createLinkToFoForWorklowContext(tempFolder, processingFolder);
		
		return pdfFile;
	}

	private void createLinkToFoForWorklowContext(File workflowFolder, File processingFolder) throws RSuiteException {
        if (context instanceof WorkflowExecutionContext){
            File fileToDwonload = copyFileToDownloadsFolders(workflowFolder, processingFolder);
            WorkflowExecutionContext wfContext = (WorkflowExecutionContext)context;
            String link = PublishWorkflowUtils.constructDownloadFilesLink(
                    wfContext.getProcessInstanceId(), fileToDwonload.getName(), "Link to the fo file");
            wfContext.getWorkflowLog().info(link);
        }
        
    }

    private File copyFileToDownloadsFolders(File workflowFolder, File processingFolder)
            throws RSuiteException {
        try{
            File foFile = new File(processingFolder, "temp/topic.fo");    
            File downloadsFolder = new File(workflowFolder, "downloads");
            downloadsFolder.mkdirs();
            FileUtils.copyFileToDirectory(foFile, downloadsFolder, true);
            File fileToDwonload = new File(downloadsFolder, foFile.getName());
            return fileToDwonload;    
        }catch (IOException e){
            throw new RSuiteException(0, e.getLocalizedMessage(), e);
        }
        
    }

    public String getVariableWithDefault(Map<String, String> variables, String variable, String defaultValue){
		if (variables != null && variables.get(variable) != null){
			return  variables.get(variable);
		}
		
		return defaultValue;
	}
	
	public void setPropertyIfVariableSpecified(Properties props,
			String toolkitParamName, Map<String, String> variables,
			String variable) {
		String value = variables.get(variable);
		if (value != null && !"".equals(value.trim())) {
			props.setProperty(toolkitParamName, value);
		}
	}

	@Override
	public void afterGenerateOutput(File tempFolder, String moId,
			File inputFile, File outputFolder) throws RSuiteException {
	}

	@Override
	public void beforeGenerateOutput(File tempFolder, String moId,
			File inputFile, File outputFolder) throws RSuiteException {
	}

}
