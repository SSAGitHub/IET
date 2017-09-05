package org.theiet.rsuite.iettv.actionhandlers.insgestion;

import static org.theiet.rsuite.iettv.actionhandlers.IetTvWorkflowVariables.*;

import java.io.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecordIngestionPackage;
import org.theiet.rsuite.iettv.domain.factories.VideoRecordIngestionPackageFactory;
import org.theiet.rsuite.iettv.domain.ingestion.VideoRecordIngester;
import org.theiet.rsuite.iettv.domain.search.IetTvFinder;
import org.theiet.rsuite.utils.ActionHandlerUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.*;
import com.rsicms.projectshelper.workflow.WorkflowVariables;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class IngestVideoRecordPackageActionHandler extends AbstractNonLeavingActionHandler {

	private static final String FOLDER_NAME_MARK_LOGIC_XML = "MarkLogicXml";
	
    private static final String FOLDER_NAME_RELATED_FILES = "Attachments";
    
    private static final long serialVersionUID = 1L;

	@Override
	public void executeTask(WorkflowExecutionContext executionContext)
			throws Exception {

	    setUpWorkflowVariables(executionContext);
	    
		WorkflowJobContext workflowJobContext = executionContext
				.getWorkflowJobContext();
				
		File packageFolder = ActionHandlerUtils.unzipIngestionPackage(workflowJobContext);		
		
		String videoPackageId =  FilenameUtils.getBaseName(workflowJobContext.getSourceFilePath());
		
		Log workflowLog = executionContext.getWorkflowLog();
		workflowLog.info("Ingesting video record package " + packageFolder.getAbsolutePath());
		
		reorganizePackageFolder(packageFolder);
		
		VideoRecordIngestionPackage videoRecordPackage = VideoRecordIngestionPackageFactory.createVideoRecordPackage(videoPackageId, packageFolder);
		
	    User user = getSystemUser();

	    VideoRecordIngester.insgestVideo(executionContext, user, videoRecordPackage);

	    executionContext.setVariable(VIDEO_ID.getVariableName(), videoRecordPackage.getVideoMetadata().getVideoId());
	}

    private void setUpWorkflowVariables(WorkflowExecutionContext executionContext) throws RSuiteException {
        ContentAssembly ietTvDomainCa = IetTvFinder.findMainDomainContainer(executionContext, getSystemUser());
        
        if (ietTvDomainCa == null){
            throw new RSuiteException("Iet tv domain does not exist");
        }
        executionContext.setVariable(WorkflowVariables.RSUITE_CONTENTS.getVariableName(), ietTvDomainCa.getId());        
    }

    private void reorganizePackageFolder(File packageFolder) throws IOException, RSuiteException {
        File relatedFiles = new File(packageFolder, FOLDER_NAME_RELATED_FILES);
        relatedFiles.mkdir();
        
        for (File folder : packageFolder.listFiles()){
            String folderName = folder.getName();
            
            if (FOLDER_NAME_MARK_LOGIC_XML.equals(folderName)){
            	File[] markLogicFiles = folder.listFiles();
            	
            	if (markLogicFiles.length != 1){
            		throw new RSuiteException("The Mark Logic folder should contain exactly one file. The folder contains: " + markLogicFiles.length + " files");
            	}
            	
                File metadataFile = folder.listFiles()[0];
                File newMetadataFile = new File(metadataFile.getAbsolutePath());
                metadataFile.renameTo(newMetadataFile);
                FileUtils.moveFileToDirectory(newMetadataFile, packageFolder, false);
                folder.delete();
                continue;
            }
            
            if (FOLDER_NAME_RELATED_FILES.equals(folderName)){
                continue;
            }
            
            if (folder.isDirectory()){
            	FileUtils.moveDirectoryToDirectory(folder, relatedFiles, false);	
            }else{
            	FileUtils.moveFileToDirectory(folder, relatedFiles, true);
            }
            
        }
        
    }

}