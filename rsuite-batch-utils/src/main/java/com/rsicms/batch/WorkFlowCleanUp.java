package com.rsicms.batch;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class WorkFlowCleanUp {
	private Logger logger = Logger.getLogger(WorkFlowCleanUp.class);

	private int daysToKeep;
	
	private boolean deleteFiles;

	private File fWorkFlowTrashFolder;

	private DateTime dtNow = new DateTime();

	public WorkFlowCleanUp(Logger logger, String fWorkFlowTrashFolder, int daysToKeep, boolean deleteFiles) {
		this(fWorkFlowTrashFolder, daysToKeep, deleteFiles);
		this.logger = logger;
	}

	public WorkFlowCleanUp(String fWorkFlowTrashFolder, int daysToKeep, boolean deleteFiles) {
		this.fWorkFlowTrashFolder = new File(fWorkFlowTrashFolder);
		this.daysToKeep = daysToKeep;
		this.deleteFiles = deleteFiles;
	}

	public static void main(String args[]) throws Exception {

		PropertiesConfiguration config = new PropertiesConfiguration("WorkFlowCleanUp.properties");

		int daysToKeep = config.getInt("days_to_keep");
		String workflowDir = (String) config.getString("workflow_dir");
		boolean deleteFiles = config.getBoolean("delete_files");

		WorkFlowCleanUp workFlowCleanUp = new WorkFlowCleanUp(workflowDir, daysToKeep, deleteFiles);
		workFlowCleanUp.cleanUpWorkflowFolders();

	}

	public void cleanUpWorkflowFolders() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hhmmss");
		System.setProperty("current.date", dateFormat.format(new Date()));

		logger.info("WorkFlowCleanUp Starting :" + dtNow.toString());

		try {
			logger.info("Days to keep=" + daysToKeep);
			logger.info("Workflow Directory=" + fWorkFlowTrashFolder);
			logger.info("delete files=" + deleteFiles);

			listFilesForYear();
		} catch (Exception ex) {
			logger.error(ex.toString());
			System.exit(0);
		}

		DateTime dtEnd = new DateTime();
		logger.info("WorkFlowCleanUp Completed :" + dtEnd.toString());
	}

	public void listFilesForYear() throws IOException {

		for (File fYearFolder : fWorkFlowTrashFolder.listFiles()) {
			if (fYearFolder.isDirectory()) {
				logger.info("Processing Year:" + fYearFolder.getName());
				listFilesForMonth(fYearFolder);

				// year directory empty - remove it
				File[] FilesInDirectory = fYearFolder.listFiles();

				if (FilesInDirectory.length == 0) {
					if (deleteFiles) {
						FileUtils.deleteDirectory(fYearFolder);
						logger.info("Year Directory " + fYearFolder.getAbsolutePath() + " is empty and will be deleted");
					} else {
						logger.info("** SIMULATION MODE ** Year Directory " + fYearFolder.getAbsolutePath() + " is empty and will be deleted");
					}
				}
			}
		}
	}

	public void listFilesForMonth(File fYearFolder) throws IOException {

		for (File fMonthFolder : fYearFolder.listFiles()) {
			if (fMonthFolder.isDirectory()) {
				processMonthFolder(fMonthFolder);
			}
		}
	}

	private void processMonthFolder(File fMonthFolder) throws IOException {
		logger.info("Processing Month :" + fMonthFolder.getName());
		listFilesForDay(fMonthFolder);

		// month directory empty - remove it
		File[] FilesInDirectory = fMonthFolder.listFiles();
		deleteMonthFolder(fMonthFolder, FilesInDirectory);
	}

	private void deleteMonthFolder(File fMonthFolder, File[] FilesInDirectory) throws IOException {
		if (FilesInDirectory.length == 0) {
			if (deleteFiles) {
				FileUtils.deleteDirectory(fMonthFolder);
				logger.info("Month Directory " + fMonthFolder.getAbsolutePath() + " is empty and will be deleted");
			} else {
				logger.info("** SIMULATION MODE ** Month Directory " + fMonthFolder.getAbsolutePath() + " is empty and will be deleted");
			}
		}
	}

	public void listFilesForDay(File fMonthFolder) throws IOException {

		for (File fDayFolder : fMonthFolder.listFiles()) {
			if (fDayFolder.isDirectory()) {
				logger.info("Processing Day Directories:" + fDayFolder.getName());

				try {
					int iYear = Integer.parseInt(fDayFolder.getParentFile().getParentFile().getName());
					int iMonth = Integer.parseInt(fDayFolder.getParentFile().getName());
					int iDay = Integer.parseInt(fDayFolder.getName());

					// construct localdate from directory name
					DateTime dtDirectory = new DateTime(iYear, iMonth, iDay, 0, 0, 0);
					int age = Days.daysBetween(dtDirectory.withTimeAtStartOfDay(), dtNow.withTimeAtStartOfDay()).getDays();

					if (age > daysToKeep) {
						if (deleteFiles) {
							logger.info("Day Directory ("+ fDayFolder.getAbsolutePath() + ") is " + age + " days old and will be deleted");
							FileUtils.deleteDirectory(fDayFolder);
						} else {
							logger.info("** SIMULATION MODE ** Day Directory ("+ fDayFolder.getAbsolutePath() + ") is " + age + " days old and will be deleted");
						}
					}
				} catch (Exception ex) {
					logger.error(ex.toString());
				}

			}
		}
	}
}
