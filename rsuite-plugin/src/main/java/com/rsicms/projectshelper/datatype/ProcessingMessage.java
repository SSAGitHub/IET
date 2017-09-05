package com.rsicms.projectshelper.datatype;

import java.util.HashMap;
import java.util.Map;

public class ProcessingMessage {

	private String message;

	private String rsuiteId;

	private Map<String, Object> additionalData = new HashMap<String, Object>();

	private ProcessingMessageType messageType;
	
	private Exception exception;

	public ProcessingMessage(Exception exception, String message
			) {
		messageType = ProcessingMessageType.ERROR;
		this.exception = exception;
		this.message = message;
	}
	
	public ProcessingMessage(Exception exception, String message,
			String rsuiteId) {
		messageType = ProcessingMessageType.ERROR;
		this.exception = exception;
		this.message = message;
		this.rsuiteId = rsuiteId;
	}

	public ProcessingMessage(ProcessingMessageType messageType, String message) {
		this.messageType = messageType;
		this.message = message;
	}

	public ProcessingMessage(ProcessingMessageType messageType, String message, String rsuiteId) {
		this.messageType = messageType;
		this.message = message;
		this.rsuiteId = rsuiteId;
	}

	public void addAdditionalData(String dataName, Object data) {
		additionalData.put(dataName, data);
	}

	public String getMessage() {
		return message;
	}

	public String getRsuiteId() {
		return rsuiteId;
	}

	public Map<String, Object> getAdditionalData() {
		return new HashMap<String, Object>(additionalData);
	}

	public ProcessingMessageType getMessageType() {
		return messageType;
	}

	public Exception getException() {
		return exception;
	}

	@Override
	public String toString() {
		return messageType + " : " + message;
	}
}
