package org.theiet.rsuite.journals.actionhandlers;

import static com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.books.utils.BookUtils;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.utils.JournalUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.AbstractBaseNonLeavingActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.SearchService;

public class AssignTaskToEa extends AbstractBaseNonLeavingActionHandler {

	private static final long serialVersionUID = 1L;
	@Override
	public void execute (WorkflowExecutionContext context) throws Exception {
		Log log = context.getWorkflowLog();
		User user = getSystemUser();
		SearchService srchSvc = context.getSearchService();
		
		TaskInstance taskInstance = context.getTaskInstance();
		String actorId = null;
		String product = context.getVariable(JournalConstants.WF_VAR_PRODUCT);
		String pdName = context.getProcessInstanceSummaryInfo().getProcessDefinitionName();
		if (StringUtils.isBlank(product)) {
			log.info("execute: no product name in workflow");
			return;
		}
		if (product.equals(JournalConstants.WF_VAR_ISSUE) || 
				product.equals(JournalConstants.WF_VAR_ARTICLE)) {
			String journal_code = context.getVariable(JournalConstants.WF_VAR_JOURNAL_ID);
			if (StringUtils.isBlank(journal_code)) {
				log.info("execute: no journal code in workflow");
				return;
			}
			log.info("execute: look up caId for journal " + journal_code);
			String journalCaId = JournalUtils.getJournalCaId(log, user, context, srchSvc, journal_code);
			log.info("execute: got journalCaId " + journalCaId);
			if (StringUtils.isBlank(journalCaId)) {
				log.info("execute: unable to assign task");
				return;
			}
			ContentAssembly journalCa = getCAFromMO(context, user, journalCaId);
			actorId = journalCa.getLayeredMetadataValue(IetConstants.LMD_FIELD_EDITORIAL_ASSISTANT);
		}
		
		else if (product.equals(BooksConstans.WF_VAR_BOOK)) {
			String productCode = context.getVariable(BooksConstans.WF_BOOK_PRODUCT_CODE);
			log.info("execute: look up book for code " + productCode);
			String bookCaId = BookUtils.getBookCaId(log, user, context, productCode);
			if (StringUtils.isBlank(bookCaId)) {
				log.info("execute: No book Ca, unable to assign task");
				return;
			}
			ContentAssembly bookCa = getCAFromMO(context, user, bookCaId);
			if (pdName.equals(JournalConstants.WF_PD_TYPESETTER_INGESTION)) {		
				actorId = bookCa.getLayeredMetadataValue(IetConstants.LMD_FIELD_PRODUCTION_CONTROLLER_USER);
			}
			else if (pdName.equals(BooksConstans.WF_PD_PREPARE_BOOK)) {
				String taskName = context.getTaskInstance().getName();
				if (taskName.equals(BooksConstans.WF_TASK_UPDATE_FILES)) {
					actorId = bookCa.getLayeredMetadataValue(IetConstants.LMD_FIELD_EDITORIAL_ASSISTANT);
				}
				else {
					actorId = bookCa.getLayeredMetadataValue(IetConstants.LMD_FIELD_PRODUCTION_CONTROLLER_USER);
				}
			}
		}
		
		if (StringUtils.isBlank(actorId)) {
			log.info("execute: no actor found");
			return;			
		}
		log.info("execute: assign task to " + actorId);
		try {
			taskInstance.setActorId(actorId);
			log.info("execute: task assigned");
		}
		catch (Exception e) {
			log.info("execute: unable to assign task " + e.getMessage());
		}
	}

}
