package org.theiet.rsuite.journals.actionhandlers;

import static com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils.*;

import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.journals.JournalConstants;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.AbstractBaseActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;

public class IngestionCompleted extends AbstractBaseActionHandler implements BooksConstans {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(WorkflowExecutionContext executionContext)
			throws Exception {
		User user = getSystemUser();
		String rsuiteId = executionContext.getVariable(IetConstants.WF_VAR_RSUITE_CONTENTS);
		
		if (rsuiteId == null){
			return;
		}
		
		String pdName = executionContext.getProcessInstanceSummaryInfo().getProcessDefinitionName();
		ContentAssembly contentAssembly = getCAFromMO(context, user, rsuiteId);
		
		if (pdName.equals(JournalConstants.WF_PD_TYPESETTER_INGESTION)) {
			if (CA_TYPE_BOOK.equals(contentAssembly.getType())) {
				String parentId = ProjectContentAssemblyUtils.getPaterntId(context, user, rsuiteId);
				executionContext.getBrowseService().pruneCacheForSubtree(user, parentId);
			}			
		}	
		
	}

}
