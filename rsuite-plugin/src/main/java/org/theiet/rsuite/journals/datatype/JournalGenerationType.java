package org.theiet.rsuite.journals.datatype;

public enum JournalGenerationType {

	TYPESETTER("Typesetter"), AUTOMATED("Automated");
	
	private String description;
	
	private JournalGenerationType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
