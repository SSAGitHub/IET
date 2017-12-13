package com.rsicms.batch;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;



public class WorkFlowPendingDeleteCleanUp {
	
	private static Logger logger;

	private String WorkflowDataDir;
	private boolean SimulationMode;
	private DateTime dtNow = new DateTime();

	private static String FILE_TO_BE_REMOVED = "toRemove";
	
	
	public static void main(String args[]) throws Exception {

		PropertiesConfiguration config = new PropertiesConfiguration("WorkFlowPendingDeleteCleanUp.properties");
		logger = Logger.getLogger(WorkFlowPendingDeleteCleanUp.class);

		
		String WorkflowDataDir = (String) config.getString("WorkFlowDataDirectory");
		boolean SimulationMode = config.getBoolean("SimulationMode");

		WorkFlowPendingDeleteCleanUp WorkFlowPendingDeleteCleanUp = new WorkFlowPendingDeleteCleanUp(WorkflowDataDir,SimulationMode);
		WorkFlowPendingDeleteCleanUp.completeWorkFlowCleanUp();

	}
	
	public WorkFlowPendingDeleteCleanUp(String WorkflowDataDir,boolean SimulationMode) {
		
		this.WorkflowDataDir = WorkflowDataDir;
		this.SimulationMode = SimulationMode;
		
		completeWorkFlowCleanUp();
		
	}

	public WorkFlowPendingDeleteCleanUp(Logger logger,String WorkflowDataDir,boolean SimulationMode) {
		
		this.WorkflowDataDir = WorkflowDataDir;
		this.SimulationMode = SimulationMode;
		this.logger = logger;
		completeWorkFlowCleanUp();
		
	}

	public void completeWorkFlowCleanUp() {

		 	
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hhmmss");
		logger.info("WorkFlowPendingDeleteCleanUp Starting :" + dateFormat.format(new Date()));

		logger.info("WorkflowDataDir=" + WorkflowDataDir);
		logger.info("SimulationMode=" + SimulationMode);
		
		processWorkFlowDirectories();
		
		DateTime dtEnd = new DateTime();
		logger.info("WorkFlowPendingDeleteCleanUp Completed :" + dateFormat.format(new Date()));
	}


	private void processWorkFlowDirectories() {
		
		File WorkFlowTempFolder = new File(WorkflowDataDir);

		for (File WorkFlowFolder : WorkFlowTempFolder.listFiles()) {
			if (WorkFlowFolder.isDirectory()) {
				logger.info("Processing workflow folder:" + WorkFlowFolder.getName());
				
				// check for workflow folder to be removed
				File WorkFlowFolderToRemove = new File(WorkFlowFolder, 	FILE_TO_BE_REMOVED);
				if (WorkFlowFolderToRemove.exists()) {
					if (SimulationMode) {
						logger.info("Simulation mode " + WorkFlowFolder.getName() + " is flagged for removal and would be removed" );
					}
					else {
						deleteWorkFlowDirtectory(WorkFlowFolder);
					}
				}
			}
		}
	}
	
	private void deleteWorkFlowDirtectory(File WorkFlowFolder) {
		
		try {
			FileUtils.deleteDirectory(WorkFlowFolder);
			logger.info("Live mode " + WorkFlowFolder.getName() + " is flagged for removal and has be removed" );
			
		} catch (Exception ex) {
			logger.error(ex.toString());
		}

	}	

	
}
