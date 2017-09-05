package org.theiet.rsuite.actionhandlers;

import java.util.Calendar;
import java.util.Date;

import org.theiet.rsuite.IetConstants;

import com.reallysi.rsuite.api.workflow.AbstractBaseActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;

public class SetUKDateVariable extends AbstractBaseActionHandler {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {
		Date today = Calendar.getInstance().getTime();
		String dateString = IetConstants.UK_DATE_FORMAT.format(today);
		context.setVariable(IetConstants.WF_DATE_STRING, dateString);
	}

}
