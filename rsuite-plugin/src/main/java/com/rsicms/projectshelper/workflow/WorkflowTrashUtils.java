package com.rsicms.projectshelper.workflow;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;

public class WorkflowTrashUtils {

	public static String ROOT_WORKFLOW_TRASH_DIR = "workflowTrash/";
	public static String FILE_TO_BE_REMOVED = "toRemove";
	
	public void moveFolderToTrash(File tempFolder, String processId, File folderToRemove) throws IOException{
		
		
		if (folderToRemove != null){
			File trashFolder = createWorkflowTrashFolder(tempFolder, processId);
			try {
				moveDirectory(folderToRemove, trashFolder);
			}
			catch (IOException e) {
				File markerFile = new File(folderToRemove, FILE_TO_BE_REMOVED);
				markerFile.createNewFile();
			}
		}
	}


	public void moveDirectory(File FolderToRemove, File TrashFolder)
			throws IOException {
		FileUtils.moveDirectoryToDirectory(FolderToRemove, TrashFolder, true);
	}
	
	
	private File createWorkflowTrashFolder(File TempFolder, String ProcessId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
		
		File tempDir = new File(TempFolder, ROOT_WORKFLOW_TRASH_DIR  
				+ sdf.format(new Date()) + "def_id_" + ProcessId + "/");
		
		if (!tempDir.exists()){
			tempDir.mkdirs();
		}
		
		return tempDir;
	}
	
	public static File moveFolderToTrash(WorkflowExecutionContext context, File folderToRemomve) throws IOException{
		
		if (folderToRemomve != null){
			File trashFolder = createWorkflowTrashFolder(context);
			FileUtils.moveDirectoryToDirectory(folderToRemomve, trashFolder, true);
			return new File(trashFolder, folderToRemomve.getName());
		}
		
		return null;
	}
	
	
	private static File createWorkflowTrashFolder(WorkflowExecutionContext context) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
		File mainTempDir = context.getRSuiteServerConfiguration().getTmpDir();
	
		String processDefinitionId = String.valueOf(context.getProcessInstance().getProcessDefinition().getId());
		
		File tempDir = new File(mainTempDir, ROOT_WORKFLOW_TRASH_DIR  
				+ sdf.format(new Date()) + "def_id_" + processDefinitionId + "/");
		
		if (!tempDir.exists()){
			tempDir.mkdirs();
		}
		
		return tempDir;
	}
}
