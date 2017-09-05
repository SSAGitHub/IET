package com.rsicms.projectshelper.net.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.utils.ProjectPluginProperties;

public class MailMessage {

	private List<String> mailTo = new ArrayList<String>();

	private String mailFrom;

	private String mailSubject;

	private String messageTemplateProperty;

	private String mailBodyText;

	private Map<String, String> variables;
	
	private Map<String, byte[]> attachments = new HashMap<String, byte[]>();

	public MailMessage(String mailFrom, List<String> mailTo, String mailSubject) {
		this.mailTo = mailTo;
		this.mailFrom = mailFrom;
		this.mailSubject = mailSubject;
	}

	public MailMessage(String mailFrom, String mailTo, String mailSubject) {
		this.mailFrom = mailFrom;
		this.mailSubject = mailSubject;
		this.mailTo.add(mailTo);
	}

	public MailMessage(String mailFrom, String mailTo, String mailSubject,
			String messageTemplate, Map<String, String> variables) {
		this.mailFrom = mailFrom;
		this.mailSubject = mailSubject;
		this.mailTo.add(mailTo);
		this.messageTemplateProperty = messageTemplate;
		this.variables = variables;
	}

	public List<String> getMailTo() {
		return mailTo;
	}

	public void setMailTo(List<String> mailTo) {
		this.mailTo = mailTo;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getMailSubject() {
		
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMessageTemplateProperty() {
		return messageTemplateProperty;
	}

	public void setMessageTemplateProperty(String messageTemplateProperty) {
		this.messageTemplateProperty = messageTemplateProperty;
	}

	public void setMailBodyText(String mailBodyText) {
		this.mailBodyText = mailBodyText;
	}

	public Map<String, String> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, String> variables) {
		
		if (mailSubject != null){
			mailSubject = resolveVariables(variables, mailSubject);	
		}
		
		this.variables = variables;
	}

	public String getMailBodyText() throws IOException {

		String mailBody = mailBodyText;

		if (messageTemplateProperty != null) {
			mailBody = ProjectPluginProperties.getPropertyTargetResource(messageTemplateProperty, "", true);
		}

		if (variables != null) {
			mailBody = resolveVariables(variables, mailBody);
			mailSubject = resolveVariables(variables, mailSubject);
		}

		return mailBody;

	}

	public void checkParameters() throws RSuiteException {

		if (StringUtils.isBlank(mailFrom)) {
			throw new RSuiteException("The from parameter cannot be empty");
		}

		if (mailTo == null || mailTo.size() == 0) {
			throw new RSuiteException("The recipient list cannot be empty");
		}
		
		if (mailBodyText == null && messageTemplateProperty == null){
			throw new RSuiteException("Missing mail body");
		}
	}

	public void addAttachment(String fileName, byte[] bytes){
		attachments.put(fileName, bytes);
	}	
	
	public Map<String, byte[]> getAttachments() {
		return attachments;
	}

	private String resolveVariables(Map<String, String> variables,
			String textToResolve) {
		for (String variable : variables.keySet()) {
			
			String variableValue = variables.get(variable);
			
			if (variableValue == null){
				variableValue = "";
			}
			
			if (variable != null && !variable.isEmpty()){
				textToResolve = textToResolve.replaceAll("\\$\\{" + variable
						+ "\\}", variableValue);
			}
			
		}
		return textToResolve;
	}
	
	
}
