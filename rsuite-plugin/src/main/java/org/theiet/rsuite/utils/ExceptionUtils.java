package org.theiet.rsuite.utils;

import org.theiet.rsuite.IetConstants;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;

public final class ExceptionUtils implements IetConstants {

	private ExceptionUtils() {
	}

	public static void throwRsuiteException(Throwable e) throws RSuiteException {
		throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
				e.getMessage(), e);
	}

	public static void throwRsuiteException(String message, Exception e)
			throws RSuiteException {
		throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
				message, e);
	}

	public static RSuiteException createRsuiteException(Throwable e) {
		return new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
				e.getMessage(), e);
	}

	public static RSuiteException createRsuiteException(String message,
			Exception e) {
		return new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
				message, e);
	}

	public static void throwWorfklowException(WorkflowExecutionContext context,
			Exception cause, String errorMsg) throws RSuiteException {
		context.setVariable(WF_VAR_ERROR_MSG,
				constructWfReportLink(errorMsg, context.getProcessInstanceId()));
		if (cause == null) {
			throw new RSuiteException(errorMsg);
		} else {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
					errorMsg, cause);
		}
	}

	public static void throwWorfklowException(WorkflowExecutionContext context,
			String errorMsg) throws Exception {
		throwWorfklowException(context, null, errorMsg);
	}

	public static Object constructWfReportLink(String msg, String pId) {
		StringBuffer sb = new StringBuffer(msg).append(" see ")
				.append("<a href=\"").append(REST_V1_URL_ROOT)
				.append("/workflow/process/log/").append(pId)
				.append("?skey=RSUITE-SESSION-KEY\"")
				.append(" target=\"_blank\"").append(">workflow log</a>");
		return sb.toString();
	}

}
