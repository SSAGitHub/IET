package org.theiet.rsuite.standards.actionhandlers;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.domain.book.StandardsBookEdition;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.workflow.actions.RSuiteDitaSupportActionHandlerBase;

/**
 * Takes one or more DITA map files and imports each map and its dependencies to
 * RSuite.
 */
public class ArchivePublicationActionHandler extends
		RSuiteDitaSupportActionHandlerBase implements StandardsBooksConstans {

	/** UID. ***/
	private static final long serialVersionUID = -7126092961497026318L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.reallysi.rsuite.api.workflow.AbstractBaseNonLeavingActionHandler#
	 * execute(com.reallysi.rsuite.api.workflow.WorkflowExecutionContext)
	 */
	@Override
	public void execute(WorkflowExecutionContext context)
			throws RSuiteException {

		Log wfLog = context.getWorkflowLog();
		wfLog.info("Try to archive book edition");
		String publicationCaId = context.getVariable(WF_VAR_RSUITE_CONTENTS);

		StandardsBookEdition bookEdition = new StandardsBookEdition(context,
				publicationCaId);

		bookEdition.archiveEdition();

	}

}
