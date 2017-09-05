package com.rsicms.projectshelper.webservice.result;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.ResponseStatus;

public class JSONRemoteApiResult implements RemoteApiResult {

	protected String content = null;
	protected String contentType = "application/json"; // Default content type
	protected String encoding = "UTF-8"; // Default encoding.
	protected ResponseStatus responseStatus = ResponseStatus.SUCCESS;
	protected String suggestedFileName = null;

	protected InputStream inputStream;

	public JSONRemoteApiResult(byte[] bytes) {
		this.inputStream = new ByteArrayInputStream(bytes);
	}

	public JSONRemoteApiResult(InputStream is) {
		this.inputStream = is;
	}

	public JSONRemoteApiResult(String content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public InputStream getInputStream() throws IOException {
		if (inputStream != null) {
			return inputStream;
		}
		return new ByteArrayInputStream(content.getBytes(this.getEncoding()));
	}

	public Reader getReader() {
		return new StringReader(content);
	}

	public String getLabel() {
		return null;
	}

	public String getSuggestedFileName() {
		return suggestedFileName;
	}

	public void setSuggestedFileName(String suggestedFileName) {
		this.suggestedFileName = suggestedFileName;
	}

	/**
	 * Get the response status currently configured for this result
	 */
	public void setResponseStatus(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}

	/**
	 * Get the response status currently configured for this result
	 */
	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}

}
