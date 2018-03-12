package com.rsicms.batch;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;


public class WorkFlowPendingCleanUp {
	
	private static Logger logger;
	private String WorkflowDataDir;
	private boolean delete_files;
	private static String FILE_TO_BE_REMOVED = "toRemove";
	
	
	public static void main(String args[]) throws Exception {

		PropertiesConfiguration config = new PropertiesConfiguration("WorkFlowPendingCleanUp.properties");
		logger = Logger.getLogger(WorkFlowPendingCleanUp.class);
		
		String WorkflowDataDir = (String) config.getString("WorkFlowDataDirectory");
		boolean delete_files = config.getBoolean("delete_files");

		WorkFlowPendingCleanUp WorkFlowPendingCleanUp = new WorkFlowPendingCleanUp(WorkflowDataDir,delete_files);
		WorkFlowPendingCleanUp.completeWorkFlowCleanUp();
	}
	
	public WorkFlowPendingCleanUp(String WorkflowDataDir,boolean delete_files) {
		
		this.WorkflowDataDir = WorkflowDataDir;
		this.delete_files = delete_files;
		
		completeWorkFlowCleanUp();
	}

	public WorkFlowPendingCleanUp(Logger logger,String WorkflowDataDir,boolean delete_files) {
		
		this.WorkflowDataDir = WorkflowDataDir;
		this.delete_files = delete_files;
		this.logger = logger;
		completeWorkFlowCleanUp();
		
	}

	public void completeWorkFlowCleanUp() {
		 	
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hhmmss");

		logger.info("WorkFlowPendingCLeanUp Starting :" + dateFormat.format(new Date()));
		logger.info("WorkflowDataDir=" + WorkflowDataDir);
		logger.info("delete_files=" + delete_files);
		
		processWorkFlowDirectories();
		
		logger.info("WorkFlowPendingCLeanUp Completed :" + dateFormat.format(new Date()));
	}


	private void processWorkFlowDirectories() {
		
		File WorkFlowTempFolder = new File(WorkflowDataDir);

		for (File WorkFlowFolder : WorkFlowTempFolder.listFiles()) {
			if (WorkFlowFolder.isDirectory()) {
				logger.info("Processing workflow folder:" + WorkFlowFolder.getName());
				
				// check for workflow folder to be removed
				File WorkFlowFolderToRemove = new File(WorkFlowFolder, 	FILE_TO_BE_REMOVED);
				if (WorkFlowFolderToRemove.exists()) {
					if (delete_files) {
						deleteWorkFlowDirectory(WorkFlowFolder);
					}
					else {
						logger.info("Simulation mode " + WorkFlowFolder.getName() + " is flagged for removal and would be removed" );
					}
				}
			}
		}
	}
	
	private void deleteWorkFlowDirectory(File WorkFlowFolder) {
		
		try {
			FileUtils.deleteDirectory(WorkFlowFolder);
			logger.info("Live mode " + WorkFlowFolder.getName() + " is flagged for removal and has be removed" );
			
		} catch (Exception ex) {
			logger.error(ex.toString());
		}

	}	

	
}
