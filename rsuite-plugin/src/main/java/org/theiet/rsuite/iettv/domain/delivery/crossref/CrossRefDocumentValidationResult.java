package org.theiet.rsuite.iettv.domain.delivery.crossref;

public class CrossRefDocumentValidationResult {

	private boolean isValid;
	
	private String errorMessages;
	
	
	CrossRefDocumentValidationResult(boolean isValid, String errorMessages) {
		this(isValid);
		this.errorMessages = errorMessages;
	}

	CrossRefDocumentValidationResult(boolean isValid) {
		this.isValid = isValid;
	}

	public boolean isValid(){
		return isValid;
	}
	
	public String getValidationErrorMessages(){
		return errorMessages;
	}
}
