package com.rsicms.batch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
//import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.apache.commons.lang.*;

public class WorkFlowCleanUpTest {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	private File todayFolder;
	private File todayFolderMinus15Days;
	private File todayFolderMinus32Days;
	private File todayFolderMinus366Days;
	private File todayFolderMinus366DaysYear;
	private File todayFolderMinus32DaysYearMonth;

	private File trashFolder;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

	@Before
	public void prepareSampleFolderStructure() throws IOException {

		trashFolder = testFolder.newFolder("workflowTrash");

		DateTime today = new DateTime();
		todayFolder = createDayFolder(trashFolder, today);

		DateTime todayMinus15Days = today.minusDays(15);
		todayFolderMinus15Days = createDayFolder(trashFolder, todayMinus15Days);

		DateTime todayMinus32Days = today.minusDays(32);
		todayFolderMinus32Days = createDayFolder(trashFolder, todayMinus32Days);

		DateTime todayMinus366Days = today.minusDays(366);
		todayFolderMinus366Days = createDayFolder(trashFolder, todayMinus366Days);
		
		// open the previous months folder - test the removal of the month folder
		int month = todayMinus32Days.getMonthOfYear();
		int year = todayMinus32Days.getYear();
				
		String sMonth = StringUtils.leftPad(Integer.toString(month), 2, "0");
		todayFolderMinus32DaysYearMonth = new File (trashFolder,Integer.toString(year) + "/" + sMonth);
		
		// open the previous year folder - test the removal of the year folder
		int yearMinus1 = todayMinus366Days.getYear();
		System.out.println("year=" + yearMinus1);
		
		todayFolderMinus366DaysYear = new File (trashFolder,Integer.toString(yearMinus1));

	}

	private File createDayFolder(File trashFolder, DateTime date) throws IOException {
		String dateFolderPath = dateFormat.format(date.toDate());
		File dayFolder = new File(trashFolder, dateFolderPath);
		dayFolder.mkdirs();
		return dayFolder;
	}

	@Test
	public void do_not_remove_folder_in_dry_run_mode() throws IOException {
		Logger logger = mock(Logger.class);
		//Logger logger = Logger.getLogger(WorkFlowCleanUpTest.class);

		WorkFlowCleanUp workFlowCleanUp = new WorkFlowCleanUp(logger, trashFolder.getAbsolutePath(), 14, false);
		workFlowCleanUp.cleanUpWorkflowFolders();

		assertEquals(true, todayFolder.exists());
		assertEquals(true, todayFolderMinus15Days.exists());
		assertEquals(true, todayFolderMinus32Days.exists());
		assertEquals(true, todayFolderMinus366DaysYear.exists());

		//	verify(logger, times(1)).info(eq("** SIMULATION MODE ** Directory would be deleted"));
	}

	@Test
	public void remove_folders_in_run_mode() throws IOException {
		Logger logger = mock(Logger.class);
		//Logger logger = Logger.getLogger(WorkFlowCleanUpTest.class);

		WorkFlowCleanUp workFlowCleanUp = new WorkFlowCleanUp(logger, trashFolder.getAbsolutePath(), 14, true);
		workFlowCleanUp.cleanUpWorkflowFolders();
		
		assertEquals(true, todayFolder.exists());
		assertEquals(false, todayFolderMinus15Days.exists());
		assertEquals(false, todayFolderMinus32Days.exists());
		assertEquals(false, todayFolderMinus366DaysYear.exists());
		
		//verify(logger, times(1)).info("Day Directory is 30 days old and will be deleted");
	}

	@Test
	public void remove_parent_folder_if_days_empty() throws IOException {
		Logger logger = mock(Logger.class);
		// Logger logger = Logger.getLogger(WorkFlowCleanUpTest.class);

		WorkFlowCleanUp workFlowCleanUp = new WorkFlowCleanUp(logger, trashFolder.getAbsolutePath(), 14, true);
		workFlowCleanUp.cleanUpWorkflowFolders();
		
		assertEquals(false, todayFolderMinus32DaysYearMonth.exists());
		assertEquals(false, todayFolderMinus366DaysYear.exists());
		
		//verify(logger, times(1)).info("Day Directory is 30 days old and will be deleted");
	}
	

}
