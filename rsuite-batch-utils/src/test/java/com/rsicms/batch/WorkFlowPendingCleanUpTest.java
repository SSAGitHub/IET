package com.rsicms.batch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

public class WorkFlowPendingCleanUpTest {


	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();
	private File TrashFolder;
	private File WorkflowFolderToBeRemoved;
	private File WorkflowFolderNotToBeRemoved;
	private String FILE_TO_BE_REMOVED = "toRemove";
	
	String WorkflowFolderToBeRemovedStr; 
	String WorkflowFolderNotToBeRemovedStr; 

	@Before
	public void prepareSampleFolderStructure() throws IOException {

		TrashFolder = testFolder.newFolder("workflow-data");

		DateTime dt = new DateTime();
		SimpleDateFormat fmt1 = new SimpleDateFormat("yyyyMMdd-hhmmss-10");
		SimpleDateFormat fmt2 = new SimpleDateFormat("yyyyMMdd-hhmmss-20");
		
		WorkflowFolderToBeRemovedStr = fmt1.format(new Date());
		WorkflowFolderNotToBeRemovedStr = fmt2.format(new Date());

		WorkflowFolderToBeRemoved = new File (TrashFolder, WorkflowFolderToBeRemovedStr);
		WorkflowFolderNotToBeRemoved = new File (TrashFolder, WorkflowFolderNotToBeRemovedStr);

		WorkflowFolderToBeRemoved.mkdir();
		WorkflowFolderNotToBeRemoved.mkdir();
		
		File markerFile = new File(WorkflowFolderToBeRemoved, FILE_TO_BE_REMOVED);
		markerFile.createNewFile();
	}
 
	@Test
	public void do_not_remove_folder_in_simulation_mode() throws IOException {
		
		Logger logger = mock(Logger.class);
		boolean delete_files = false;

		WorkFlowPendingCleanUp workFlowCLeanUp = new WorkFlowPendingCleanUp(logger, TrashFolder.getAbsolutePath(),delete_files);
		workFlowCLeanUp.completeWorkFlowCleanUp();

		assertEquals(true, WorkflowFolderToBeRemoved.exists());
		assertEquals(true, WorkflowFolderNotToBeRemoved.exists());

		//verify(logger, times(1)).info(eq("Simulation mode " + WorkflowFolderToBeRemoved + " is flagged for removal and would be removed"));
	}
	
	@Test
	public void do_remove_folder_in_live_mode() throws IOException {
		
		Logger logger = mock(Logger.class);
		boolean delete_files = true;

		WorkFlowPendingCleanUp workFlowCLeanUp = new WorkFlowPendingCleanUp(logger, TrashFolder.getAbsolutePath(),delete_files);
		workFlowCLeanUp.completeWorkFlowCleanUp();

		assertEquals(false, WorkflowFolderToBeRemoved.exists());
		assertEquals(true, WorkflowFolderNotToBeRemoved.exists());

//		verify(logger, times(1)).info(eq("Simulation mode " + WorkflowFolderToBeRemoved + " is flagged for removal and would be removed"));
	}
}
