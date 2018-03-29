package org.theiet.rsuite.eventhandlers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.event.Event;
import com.reallysi.rsuite.api.event.events.WorkflowCompletedEventData;
import com.reallysi.rsuite.api.event.events.WorkflowEventData;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.system.RSuiteServerConfiguration;
import com.rsicms.projectshelper.workflow.WorkflowTrashUtils;

import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;



public class WorkFlowCompletionEventHandlerTest {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();
	public File workFlowTempDir;
	public File workFlowDir;
	public File workflowTrashDir;
	public File workflowUnableToMoveFile;
	
	public static final String WORKFLOWINSTANCE = "123456";
	public static String FILE_TO_BE_REMOVED = "toRemove";
	public static final String ROOT_WORKFLOW_TRASH_DIR = "workflowTrash/";

	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

	@Before
	public void prepareSampleFolderStructure() throws IOException {
		
		workFlowTempDir = testFolder.newFolder("workflowTempfolder");
		workFlowDir = new File(workFlowTempDir, WORKFLOWINSTANCE);
		workflowTrashDir = new File(workFlowTempDir, ROOT_WORKFLOW_TRASH_DIR  + dateFormat.format(new Date()) + "/def_id_" + WORKFLOWINSTANCE + "/");
		
		workFlowTempDir.mkdirs();
		workFlowDir.mkdirs();	
		workflowUnableToMoveFile = new File(workFlowDir, FILE_TO_BE_REMOVED);
	}
	
	@Test
	public void test_file_gets_moved() throws Exception {
	
		WorkFlowCompletionEventHandler eventHandler = new WorkFlowCompletionEventHandler();
		eventHandler.moveWorkFlowFolderToTrash(workFlowTempDir,WORKFLOWINSTANCE, workFlowDir );
		
		assertEquals(false, workFlowDir.exists());
		assertEquals(true, workflowTrashDir.exists());

	}

	@Test
	public void test_file_does_not_get_moved() throws Exception {
		
 		WorkFlowCompletionEventHandler eventHandler = new WorkFlowCompletionEventHandler();
		WorkFlowCompletionEventHandler eventHandlerSpy = Mockito.spy(eventHandler);
		
		WorkflowTrashUtils WorkflowTrashUtils = new WorkflowTrashUtils();
		WorkflowTrashUtils WorkflowTrashUtilsSpy = Mockito.spy(WorkflowTrashUtils);

		Mockito.doReturn(WorkflowTrashUtilsSpy).when(eventHandlerSpy).getWorkflowTrashUtils();

		doThrow(new IOException()).when(WorkflowTrashUtilsSpy).moveDirectory(Mockito.any(File.class),  Mockito.any(File.class));

		eventHandlerSpy.moveWorkFlowFolderToTrash(workFlowTempDir, WORKFLOWINSTANCE, workFlowDir);
		
		assertEquals(true, workFlowDir.exists());
		assertEquals(true, workflowTrashDir.exists());
		assertEquals(true, workflowUnableToMoveFile.exists());
  
	}

	@Test(expected=RSuiteException.class)
	public void test_file_move_generates_RSuiteException() throws Exception {
		
		
		WorkFlowCompletionEventHandler eventHandler = new WorkFlowCompletionEventHandler();
		WorkFlowCompletionEventHandler eventHandlerSpy = Mockito.spy(eventHandler);
		
		WorkflowTrashUtils WorkflowTrashUtils = new WorkflowTrashUtils();
		WorkflowTrashUtils WorkflowTrashUtilsSpy = Mockito.spy(WorkflowTrashUtils);

		Mockito.doReturn(WorkflowTrashUtilsSpy).when(eventHandlerSpy).getWorkflowTrashUtils();

		doThrow(new IOException()).when(WorkflowTrashUtilsSpy).moveFolderToTrash(Mockito.any(File.class), anyString(),  Mockito.any(File.class));

		eventHandlerSpy.moveWorkFlowFolderToTrash(workFlowTempDir, WORKFLOWINSTANCE, workFlowDir);
		
		assertEquals(true, workFlowDir.exists());
		assertEquals(true, workflowTrashDir.exists());
		assertEquals(false, workflowUnableToMoveFile.exists());
	
	}	
	

}

	