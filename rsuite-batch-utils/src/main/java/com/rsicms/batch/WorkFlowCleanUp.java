package com.rsicms.batch;

import java.io.*;
import java.util.*;
import java.text.*;
import org.apache.commons.configuration.PropertiesConfiguration; 
import javax.naming.ConfigurationException;
import org.apache.commons.lang.exception.NestableException;
import org.apache.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.joda.time.*;


public class WorkFlowCleanUp 
{
	private static Logger logger;
	private static String WORKFLOW_DIR;
	private static String DAYS_TO_KEEP;
	private static boolean DELETE_FILES;
	private static DateTime dtNow = null;


	public static void main (String args[]) 
	{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hhmmss");
        System.setProperty("current.date", dateFormat.format(new Date()));
		logger = Logger.getLogger(WorkFlowCleanUp.class);

		dtNow = new DateTime();
		logger.info("WorkFlowCleanUp Starting :" + dtNow.toString());

		try
		{
			PropertiesConfiguration config = new PropertiesConfiguration("WorkFlowCleanUp.properties");
			
			DAYS_TO_KEEP = (String) config.getProperty("days_to_keep");
			WORKFLOW_DIR = (String) config.getProperty("workflow_dir");
			DELETE_FILES = Boolean.parseBoolean((String) config.getProperty("delete_files"));

			logger.info("Days to keep=" + DAYS_TO_KEEP);
			logger.info("Workflow Directory=" + WORKFLOW_DIR);
			logger.info("delete files=" + DELETE_FILES);

			File fWorkFlowTrashFolder = new File (WORKFLOW_DIR);
			listFilesForYear(fWorkFlowTrashFolder);
		}
		catch (Exception ex)
		{
			logger.error(ex.toString());
			System.exit(0);
		}
		
		DateTime dtEnd = new DateTime();
		logger.info("WorkFlowCleanUp Completed :" + dtEnd.toString());

	}

	public static void listFilesForYear(File fWorkFlowTrashFolder) throws IOException
	{
		
		for (File fYearFolder : fWorkFlowTrashFolder.listFiles()) 
		{
			if (fYearFolder.isDirectory()) 
			{
				logger.info("Processing Year:" + fYearFolder.getName());
				listFilesForMonth(fYearFolder);
				
				// year directory empty - remove it
				File[] FilesInDirectory = fYearFolder.listFiles();
				
				if (FilesInDirectory.length == 0) 
				{
					if (DELETE_FILES)
					{
						FileUtils.deleteDirectory(fYearFolder);
						logger.info("Year Directory " + fYearFolder.getName() + " is empty and will be deleted");
					}
					else
					{	
						logger.info("** SIMULATION MODE ** Directory would be deleted");
					}
				}
			}
		}
	}
	
	public static void listFilesForMonth(File fYearFolder) throws IOException
	{
		
		for (File fMonthFolder : fYearFolder.listFiles()) 
		{
			if (fMonthFolder.isDirectory()) 
			{
				logger.info("Processing Month:" + fMonthFolder.getName());
				listFilesForDay(fMonthFolder);
				
				// month directory empty - remove it
				File[] FilesInDirectory = fMonthFolder.listFiles();
				
				if (FilesInDirectory.length == 0) 
				{
					if (DELETE_FILES)
					{	
						FileUtils.deleteDirectory(fMonthFolder);
						logger.info("Month Directory " + fMonthFolder.getName() + " is empty and will be deleted");
					}
					else
					{
						logger.info("** SIMULATION MODE ** Directory would be deleted");
					}
				}
			}
		}
	}
	
	public static void listFilesForDay(File fMonthFolder) throws IOException
	{
		
		for (File fDayFolder : fMonthFolder.listFiles()) 
		{
			if (fDayFolder.isDirectory()) 
			{
				logger.info("Processing Day Directories:" + fDayFolder.getName());
				
				try
				{
					int iYear = Integer.parseInt(fDayFolder.getParentFile().getParentFile().getName());
					int iMonth = Integer.parseInt(fDayFolder.getParentFile().getName());
					int iDay = Integer.parseInt(fDayFolder.getName());
				
					// construct localdate from directory name
					DateTime dtDirectory = new DateTime(iYear, iMonth, iDay,0,0,0);
					int age = Days.daysBetween(dtDirectory.withTimeAtStartOfDay(),dtNow.withTimeAtStartOfDay()).getDays();
					
					if (age > Integer.parseInt(DAYS_TO_KEEP))
					{
						if (DELETE_FILES)
						{
							logger.info("Day Directory is " + age + " days old and will be deleted");
							FileUtils.deleteDirectory(fDayFolder);
						}
						else
						{
							logger.info("** SIMULATION MODE ** Directory would be deleted");
						}
					}
				}
				catch (Exception ex)
				{
					logger.error(ex.toString());
				}
		
			}
		}
	}	
}
