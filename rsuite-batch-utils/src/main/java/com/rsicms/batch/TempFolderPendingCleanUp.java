package com.rsicms.batch;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;



public class TempFolderPendingCleanUp {
	
	private Logger logger = Logger.getLogger(TempFolderPendingCleanUp.class);
	private boolean deleteFiles;
	private File temp_folder_dir;
	private DateTime dtNow = new DateTime();
	private static String FILE_TO_BE_REMOVED = "toRemove";


	public TempFolderPendingCleanUp(Logger logger, String temp_folder, boolean deleteFiles) {
		this(temp_folder, deleteFiles);
		this.logger = logger;
	}

	public TempFolderPendingCleanUp(String temp_folder,  boolean deleteFiles) {
		this.temp_folder_dir = new File(temp_folder);
		this.deleteFiles = deleteFiles;
	}

	public static void main(String args[]) throws Exception {

		PropertiesConfiguration config = new PropertiesConfiguration("TempFolderPendingCleanUp.properties");

		String temp_folder = (String) config.getString("temp_folder");
		boolean deleteFiles = config.getBoolean("delete_files");

		TempFolderPendingCleanUp tempFolderCleanUp = new TempFolderPendingCleanUp(temp_folder, deleteFiles);
		tempFolderCleanUp.completeTempFolderCleanUp();

	}

	public void completeTempFolderCleanUp() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hhmmss");
		System.setProperty("current.date", dateFormat.format(new Date()));

		logger.info("TempFolderPendingCleanUp Starting :" + dtNow.toString());

		try {
			logger.info("temp Directory=" + this.temp_folder_dir);
			logger.info("delete files=" + deleteFiles);
			
			listFilesForYear();
		} catch (Exception ex) {
			logger.error(ex.toString());
			System.exit(0);
		}

		DateTime dtEnd = new DateTime();
		logger.info("TempFolderPendingCleanUp Completed :" + dtEnd.toString());
	}

	public void listFilesForYear() throws IOException {

		for (File fYearFolder : temp_folder_dir.listFiles()) {
			if (fYearFolder.isDirectory()) {
				
				logger.info("Processing Year:" + fYearFolder.getAbsolutePath());
				listFilesForMonth(fYearFolder);
				deleteYearFolderIfEmpty(fYearFolder);
			}
		}
	}

	private void deleteYearFolderIfEmpty(File fYearFolder) throws IOException {
		
		File[] FilesInDirectory = fYearFolder.listFiles();
		
		if (FilesInDirectory.length == 0) {
			
			if (deleteFiles) {
				
				FileUtils.deleteDirectory(fYearFolder);
				logger.info("Year Directory " + fYearFolder.getName() + " is empty and will be deleted");
			} else {
				logger.info("** SIMULATION MODE ** Year Directory " + fYearFolder.getName() + " is empty and will be deleted");
			}
		}
	}

	public void listFilesForMonth(File fYearFolder) throws IOException {

		for (File fMonthFolder : fYearFolder.listFiles()) {
			if (fMonthFolder.isDirectory()) {
				
				processDayFoldersInMonth(fMonthFolder);
				deleteMonthFolderIfEmpty(fMonthFolder);
			}
		}
	}


	private void deleteMonthFolderIfEmpty(File fMonthFolder) throws IOException {
		
		File[] FilesInDirectory = fMonthFolder.listFiles();
		
		if (FilesInDirectory.length == 0) {
			
			if (deleteFiles) {
				
				FileUtils.deleteDirectory(fMonthFolder);
				logger.info("Month Directory " + fMonthFolder.getName() + " is empty and will be deleted");
			} else {
				logger.info("** SIMULATION MODE ** Month Directory " + fMonthFolder.getName() + " is empty and will be deleted");
			}
		}
	}

	private void processDayFoldersInMonth(File fMonthFolder) throws IOException {

		for (File fDayFolder : fMonthFolder.listFiles()) {
			
			if (fDayFolder.isDirectory()) {
				
				logger.info("Processing Day Directories:" + fDayFolder.getAbsolutePath());
				
				processFilesForDay(fDayFolder);
				deleteDayFolderIfEmpty(fDayFolder);
			}
		}
	}
	
	private void deleteDayFolderIfEmpty(File fDayFolder) throws IOException {
		
		File[] FilesInDirectory = fDayFolder.listFiles();

		if (FilesInDirectory.length == 0) {
			
			if (deleteFiles) {
				
				FileUtils.deleteDirectory(fDayFolder);
				logger.info("Day Directory " + fDayFolder.getName() + " is empty and will be deleted");
			} else {
				logger.info("** SIMULATION MODE ** Day Directory " + fDayFolder.getName() + " is empty and will be deleted");
			}
		}
	}
	
	private void processFilesForDay(File fDayFolder) throws IOException {
	
		try {

			for (File fWorkFlowFolder : fDayFolder.listFiles()) {

				if (fWorkFlowFolder.isDirectory()) {
					logger.info("Processing WorkFlow Directory:" + fWorkFlowFolder.getName());
					File workFlowFolderToRemove = new File(fWorkFlowFolder,	FILE_TO_BE_REMOVED);
					removeFolderIfExist(fWorkFlowFolder, workFlowFolderToRemove);
				}
			}
		} 
		catch (Exception ex) {
			logger.error(ex.toString());
		}

	}

	private void removeFolderIfExist(File fWorkFlowFolder, File WorkFlowFolderToRemove) throws IOException {
		if (WorkFlowFolderToRemove.exists()) {

			if (deleteFiles) {
				logger.info("WorkFlow Directory " + fWorkFlowFolder.getAbsolutePath() + " is flagged for deletion and will be deleted");
				FileUtils.deleteDirectory(fWorkFlowFolder);
			} else {
				logger.info("** SIMULATION MODE ** WorkFlow Directory " + fWorkFlowFolder.getAbsolutePath() + " is flagged for deletion and will be deleted");
			}
		}
	}

}
