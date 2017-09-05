package org.theiet.rsuite.standards.domain.publish.validator;

import org.theiet.rsuite.utils.StringUtils;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class OnixBookErrorHandler implements ErrorHandler {

	private final static String WARNING_TYPE_MESSAGE = "WARNING";
	private final static String ERROR_TYPE_MESSAGE = "ERROR";
	private final static String FATAL_ERROR_TYPE_MESSAGE = "FATAL_ERROR";

	private StringBuffer errorMessage = new StringBuffer();

	private boolean hasError = false;

	public String getErrorMessage() {
		return errorMessage.toString();
	}

	@Override
	public void warning(SAXParseException exception) throws SAXException {
		errorLogging(exception, WARNING_TYPE_MESSAGE);
	}

	@Override
	public void error(SAXParseException exception) throws SAXException {
		errorLogging(exception, ERROR_TYPE_MESSAGE);
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		errorLogging(exception, FATAL_ERROR_TYPE_MESSAGE);
	}

	private String buildMessage(SAXParseException exception, String errorType) {
		StringBuffer detailedMessage = new StringBuffer();
		detailedMessage.append("Error Type: ").append(errorType);
		detailedMessage.append(StringUtils.NEW_LINE_SEPARATOR);
		detailedMessage.append("Line, Column: ").append(exception.getLineNumber());
		detailedMessage.append(", ").append(exception.getColumnNumber());
		detailedMessage.append(StringUtils.NEW_LINE_SEPARATOR).append(
				 "LocalizedMessage: ");
		detailedMessage.append(exception.getLocalizedMessage());

		return detailedMessage.toString();
	}

	private void errorLogging(SAXParseException exception, String errorType) {
		String message = buildMessage(exception, errorType);
		hasError = true;
		errorMessage.append(message).append(StringUtils.NEW_LINE_SEPARATOR);
	}

	public boolean isHasError() {
		return hasError;
	}

}
