package org.theiet.rsuite.actionhandlers;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.utils.PubtrackLogger;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.AbstractBaseActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;

public class InitiateBooks extends AbstractBaseActionHandler implements BooksConstans {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {
		Log log = context.getWorkflowLog();
		User user = getSystemUser();
		
		String bookCaId = context.getVariable(WF_VAR_RSUITE_CONTENTS);
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		ContentAssembly productCa = caSvc.getContentAssembly(user, bookCaId);
		String productId = productCa.getLayeredMetadataValue(LMD_FIELD_BOOK_PRODUCT_CODE);
		context.setVariable(BooksConstans.WF_BOOK_PRODUCT_CODE, productId);
		context.setVariable(JournalConstants.WF_VAR_PRODUCT, WF_VAR_BOOK);
		log.info("execute: set " + BooksConstans.WF_BOOK_PRODUCT_CODE + " to " + productId);
		
		PubtrackLogger.logToProcess(user, context, log, WF_VAR_BOOK, productId, PUBTRACK_PREPARE_BOOK_WORKFLOW_STARTED);
	}

}