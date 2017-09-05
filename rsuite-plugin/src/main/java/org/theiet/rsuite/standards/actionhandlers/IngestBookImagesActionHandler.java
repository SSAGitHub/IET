package org.theiet.rsuite.standards.actionhandlers;

import static org.theiet.rsuite.standards.StandardsBooksConstans.*;

import java.io.File;

import org.theiet.rsuite.standards.domain.book.StandardsBook;
import org.theiet.rsuite.standards.domain.image.ImageFilesIngestionHelper;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class IngestBookImagesActionHandler extends AbstractNonLeavingActionHandler  {

	/** UID. */
	private static final long serialVersionUID = -1173651099140530832L;

	@Override
	public void executeTask(WorkflowExecutionContext executionContext)
			throws Exception {
		
		
			String bookCaId = executionContext.getVariable(WF_VAR_BOOK_CA_ID);
			String imagesBaseFolderPath = executionContext.getVariable(WF_VAR_IMAGES_DIRECTORY);
			String bookPrefix = executionContext.getVariable(WF_VAR_BOOK_PREFIX);
					
			StandardsBook standardsBook = new StandardsBook(context, bookCaId);			
			ContentAssembly bookImagesCA = standardsBook.getImagesCA();
		
			File imagesBaseFolder = new File(imagesBaseFolderPath);
			
			ImageFilesIngestionHelper ingestionHelper = new ImageFilesIngestionHelper(executionContext, executionContext.getWorkflowLog());
			
			ingestionHelper.ingestImages(imagesBaseFolder, bookImagesCA, bookPrefix);
		
	}


	

	
	
}
