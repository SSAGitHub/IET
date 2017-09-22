package com.rsicms.projectshelper.workflow;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.mockito.Mockito;

import com.reallysi.rsuite.api.ConfigurationProperties;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.api.workflow.WorkflowJobContext;

public class WorkflowUtilsTest {

	@Test
	public void test_getMainWorkflowFolder() {
		
		WorkflowExecutionContext context = mock(WorkflowExecutionContext.class);
		WorkflowJobContext jobContext = mock(WorkflowJobContext.class);
		ConfigurationProperties configurationProperties = mock(ConfigurationProperties.class);
		
		when(configurationProperties.getProperty(anyString(), anyString())).thenReturn("/test/workflowBase");
		when(jobContext.getWorkFolderPath()).thenReturn("/test/workflowBase/workflowFolder/test/work");
		when(context.getWorkflowJobContext()).thenReturn(jobContext);		
		when(context.getConfigurationProperties()).thenReturn(configurationProperties );
		
		File mainWorkflowFolder = WorkflowUtils.getMainWorkflowFolder(context);
		
		assertEquals(new File("C:/").getAbsolutePath(), mainWorkflowFolder.getAbsolutePath());
	}

}
