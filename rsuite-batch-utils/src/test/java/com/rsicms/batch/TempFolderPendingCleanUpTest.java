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
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

public class TempFolderPendingCleanUpTest {


	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();
	private File temp_folder;
	private File workflowFolderToBeRemoved;
	private File workflowFolderNotToBeRemoved;
	private String FILE_TO_BE_REMOVED = "toRemove";
	
	private File workFlowOldFolderYear;
	private File workFlowOldFolderMonth;
	private File workFlowOldFolderDay;
	

	@Before
	public void prepareSampleFolderStructure() throws IOException {

		temp_folder = testFolder.newFolder("workflow-data");

		DateTime dt = new DateTime();
		SimpleDateFormat fmtYear = new SimpleDateFormat("yyyy");
		SimpleDateFormat fmtMonth = new SimpleDateFormat("MM");
		SimpleDateFormat fmtDay = new SimpleDateFormat("dd");
		
		String workFlowFolderYear = fmtYear.format(new Date());
		String workFlowFolderMonth = fmtMonth.format(new Date());
		String workFlowFolderDay = fmtDay.format(new Date());
		
		File fworkFlowFolderYear = new File(temp_folder, workFlowFolderYear);
		File fworkFlowFolderMonth = new File(fworkFlowFolderYear, workFlowFolderMonth);
		File fworkFlowFolderDay = new File(fworkFlowFolderMonth, workFlowFolderDay);
		
		fworkFlowFolderYear.mkdir();
		fworkFlowFolderMonth.mkdir();
		fworkFlowFolderDay.mkdir();
		
		// to be removed - create marker file
		workflowFolderToBeRemoved = new File(fworkFlowFolderDay, "toBeRemoved" );
		workflowFolderToBeRemoved.mkdir();
		
		File markerFile = new File(workflowFolderToBeRemoved, FILE_TO_BE_REMOVED);
		markerFile.createNewFile();
		
		// not to be removed - do not create marker file 
		workflowFolderNotToBeRemoved = new File(fworkFlowFolderDay, "NotToBeRemoved" );
		workflowFolderNotToBeRemoved.mkdir();
		
		//create an empty directory structure for testing day, month and year directory deletion 
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.add(Calendar.YEAR, -1);
		Date dLastYear = cal.getTime();
		
		String lastYear = fmtYear.format(dLastYear);
		
		workFlowOldFolderYear = new File(temp_folder, lastYear);
		workFlowOldFolderMonth = new File(fworkFlowFolderYear, "12");
		workFlowOldFolderDay = new File(fworkFlowFolderMonth, "31");

		workFlowOldFolderYear.mkdir();
		workFlowOldFolderMonth.mkdir();
		workFlowOldFolderDay.mkdir();

	}
 
	@Test
	public void do_not_remove_folder_in_simulation_mode() throws IOException {
		
		Logger logger = mock(Logger.class);
		boolean deletefiles = false;

		TempFolderPendingCleanUp tempFolderCleanUp = new TempFolderPendingCleanUp(logger, temp_folder.getAbsolutePath(),deletefiles);
		tempFolderCleanUp.completeTempFolderCleanUp();

		assertEquals(true, workflowFolderToBeRemoved.exists());
		assertEquals(true, workflowFolderNotToBeRemoved.exists());

		//verify(logger, times(1)).info(eq("Simulation mode " + WorkflowFolderToBeRemoved + " is flagged for removal and would be removed"));
	}

	@Test
	public void do_remove_folder_in_live_mode() throws IOException {
		
		Logger logger = mock(Logger.class);
		boolean deletefiles = true;

		TempFolderPendingCleanUp tempFolderCleanUp = new TempFolderPendingCleanUp(logger, temp_folder.getAbsolutePath(),deletefiles);
		tempFolderCleanUp.completeTempFolderCleanUp();

		assertEquals(false, workflowFolderToBeRemoved.exists());
		assertEquals(true, workflowFolderNotToBeRemoved.exists());

		assertEquals(false, workFlowOldFolderYear.exists());
		assertEquals(false, workFlowOldFolderMonth.exists());
		assertEquals(false, workFlowOldFolderDay.exists());

//		verify(logger, times(1)).info(eq("Simulation mode " + WorkflowFolderToBeRemoved + " is flagged for removal and would be removed"));
	}
}
