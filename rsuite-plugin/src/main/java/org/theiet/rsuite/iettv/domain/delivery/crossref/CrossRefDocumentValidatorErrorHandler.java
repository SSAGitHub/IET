package org.theiet.rsuite.iettv.domain.delivery.crossref;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

class CrossRefDocumentValidatorErrorHandler implements ErrorHandler {

	private StringBuilder errorMessages = new StringBuilder();

	@Override
	public void warning(SAXParseException exception) throws SAXException {
	}

	@Override
	public void error(SAXParseException exception) throws SAXException {
		logValidationError(exception);

	}

	private void logValidationError(SAXParseException exception) {
		errorMessages.append("line ")
				.append(exception.getLineNumber()).append(":")
				.append(exception.getColumnNumber())
				.append(" ")
				.append(exception.getMessage());
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		logValidationError(exception);
	}

	boolean hasErrors(){
		if (errorMessages.length() > 0){
			return true;
		}
		
		return false;
	}

	public StringBuilder getErrorMessages() {
		return errorMessages;
	}
	
}
