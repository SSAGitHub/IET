package com.rsicms.projectshelper.message;

import static com.rsicms.projectshelper.datatype.ProcessingMessageType.ERROR;
import static com.rsicms.projectshelper.datatype.ProcessingMessageType.INFO;
import static com.rsicms.projectshelper.datatype.ProcessingMessageType.WARNING;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import com.rsicms.projectshelper.datatype.ProcessingMessage;
import com.rsicms.projectshelper.datatype.ProcessingMessageType;

public class ProcessingMessageHandler {

	private List<ProcessingMessage> messages = new ArrayList<ProcessingMessage>();

	public void addMessage(ProcessingMessage message) {
		messages.add(message);
	}

	public ProcessingMessage error(String message, String rsuiteId) {
		ProcessingMessage processingMessage = new ProcessingMessage(ERROR,
				message, rsuiteId);
		messages.add(processingMessage);
		return processingMessage;
	}

	public ProcessingMessage error(String message) {
		ProcessingMessage processingMessage = new ProcessingMessage(ERROR,
				message);
		messages.add(processingMessage);
		return processingMessage;
	}

	public ProcessingMessage error(String message, Exception e) {
		ProcessingMessage processingMessage = new ProcessingMessage(e, message);
		messages.add(processingMessage);
		return processingMessage;
	}

	public ProcessingMessage info(String message, String rsuiteId) {
		ProcessingMessage processingMessage = new ProcessingMessage(INFO,
				message, rsuiteId);
		messages.add(processingMessage);
		return processingMessage;
	}

	public ProcessingMessage info(String message) {
		ProcessingMessage processingMessage = new ProcessingMessage(INFO,
				message);
		messages.add(processingMessage);
		return processingMessage;
	}

	public ProcessingMessage warning(String message, String rsuiteId) {
		ProcessingMessage processingMessage = new ProcessingMessage(WARNING,
				message, rsuiteId);
		messages.add(processingMessage);
		return processingMessage;
	}

	public ProcessingMessage warning(String message) {
		ProcessingMessage processingMessage = new ProcessingMessage(WARNING,
				message);
		messages.add(processingMessage);
		return processingMessage;
	}

	public boolean hasErrors() {
		return hasMessageType(ERROR);
	}
	
	public boolean hasWarnings() {
		return hasMessageType(WARNING);
	}
	
	private boolean hasMessageType(ProcessingMessageType messageType){

		for (ProcessingMessage message : messages) {
			if (message.getMessageType() == messageType) {
				return true;
			}
		}

		return false;
	}

	public List<ProcessingMessage> getErrorMessages() {		
		return getMessagesByType(ERROR);
	}
	
	public List<ProcessingMessage> getWarningMessages() {		
		return getMessagesByType(WARNING);
	}

	public List<ProcessingMessage> getMessagesByType(
			ProcessingMessageType messageType) {
		List<ProcessingMessage> errorMessages = new ArrayList<ProcessingMessage>();
		for (ProcessingMessage message : messages) {
			if (message.getMessageType() == messageType) {
				errorMessages.add(message);
			}
		}

		return errorMessages;
	}

	public List<ProcessingMessage> getMessages() {
		return new ArrayList<ProcessingMessage>(messages);
	}

	public void writeMessagesToLogger(Log logger) {
		for (ProcessingMessage message : messages) {

			switch (message.getMessageType()) {
			case ERROR:
				logger.error(message.getMessage());
				Exception exception = message.getException();
				if (exception != null) {
					logger.error(exception, exception);
				}
				break;
			case WARNING:
				logger.warn(message.getMessage());
				break;
			default:
				logger.info(message.getMessage());
				break;
			}
		}
	}
}
