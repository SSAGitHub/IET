package org.theiet.rsuite.journals.datatype;

public enum JournalWorkflowType {

	ISSUE("Issue"), ARCHIVE("Archive");
	
	private String description;
	
	private JournalWorkflowType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
