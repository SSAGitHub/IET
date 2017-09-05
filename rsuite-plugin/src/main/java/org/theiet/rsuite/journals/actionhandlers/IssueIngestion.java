package org.theiet.rsuite.journals.actionhandlers;

import static org.theiet.rsuite.IetConstants.*;

import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.books.domain.BookTypesetterIngestionHelper;
import org.theiet.rsuite.domain.TypesetterIngestionHelper;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.issues.IssueTypesetterIngestionHelper;
import org.theiet.rsuite.utils.ExceptionUtils;

import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractActionHandler;


public class IssueIngestion extends AbstractActionHandler {

	
	private static final long serialVersionUID = 1L;
	/*
	 * This action handler is used for ingestion of both books and journal issues
	 * ingestions from the typesetter. The business processes are similar
	 * (load of zip to a typesetter folder). The typesetter ingestion workflow
	 * forks depending on whether the zip is named ARTICLE- (TypesetterIngestion)
	 * or BOOK-/ISSUE- (this class). This class contains slightly different
	 * business logic for issue or book.
	 */

	@Override
	public void executeTask (WorkflowExecutionContext context) throws Exception {
		
		
		String product = context.getVariable(WF_VAR_PRODUCT);
		
		TypesetterIngestionHelper ingestionHelper = null;
		
		if (JournalConstants.WF_VAR_ISSUE.equals(product)) {
			ingestionHelper = new IssueTypesetterIngestionHelper(context);
		}else if (BooksConstans.WF_VAR_BOOK.equals(product)){
			ingestionHelper = new BookTypesetterIngestionHelper(context);
		}else {
			ExceptionUtils.throwWorfklowException(context, "Unsupported product " + product);
		}
		
		ingestionHelper.loadTypesetterFiles();
		
	}
		
}
