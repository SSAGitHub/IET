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

public class WorkFlowCleanUpTest {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	private File todayFolder;

	private File oldFolder;

	private File trashFolder;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

	@Before
	public void prepareSampleFolderStructure() throws IOException {

		trashFolder = testFolder.newFolder("workflowTrash");

		DateTime today = new DateTime();
		todayFolder = createDayFolder(trashFolder, today);

		DateTime oldDate = today.minusDays(30);
		oldFolder = createDayFolder(trashFolder, oldDate);

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

		WorkFlowCleanUp workFlowCleanUp = new WorkFlowCleanUp(logger, trashFolder.getAbsolutePath(), 14, false);
		workFlowCleanUp.cleanUpWorkflowFolders();


		verify(logger, times(1)).info(eq("** SIMULATION MODE ** Directory would be deleted"));
	}

	@Test
	public void remove_folder_in_run_mode() throws IOException {
		Logger logger = mock(Logger.class);

		WorkFlowCleanUp workFlowCleanUp = new WorkFlowCleanUp(logger, trashFolder.getAbsolutePath(), 14, true);
		workFlowCleanUp.cleanUpWorkflowFolders();
		
		assertEquals(false, oldFolder.exists());
		assertEquals(true, todayFolder.exists());
		
		verify(logger, times(1)).info("Day Directory is 30 days old and will be deleted");

	}

}
