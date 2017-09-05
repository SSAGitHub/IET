package org.theiet.rsuite.standards.domain.publish.validator;

import java.util.Arrays;

public class OnixValidationResult {

	private byte[] content;
	private String fileName;
	private String contentType;
	private boolean validOnixFile;

	public OnixValidationResult(byte[] contentArray, String fileName,
			String contentType, boolean validOnixFile) {

		if (contentArray != null){
			this.content = Arrays.copyOf(contentArray, contentArray.length);
		}

		this.fileName = fileName;
		this.contentType = contentType;
		this.validOnixFile = validOnixFile;
	}

	public byte[] getContent() {
		return content;
	}

	public String getFileName() {
		return fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public boolean isValidOnixFile() {
		return validOnixFile;
	}

}
