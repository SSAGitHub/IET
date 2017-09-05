package org.theiet.rsuite.journals.domain.journal;

import com.reallysi.rsuite.api.remoteapi.CallArgumentList;

public class JournalCreateDTO {

	private static final String PARAM_INPSEC_CLASSIFIER = "inpsecClassifier";
	private static final String PARAM_JOURNAL_EMAIL = "journalEmail";
	private static final String PARAM_JOURNAL_NAME = "journalName";
	private static final String PARAM_JOURNAL_CODE = "journalCode";
	private static final String PARAM_PRODUCATION_CONTROLLER = "producationController";
	private static final String PARAM_EDITORIAL_ASSISTANT = "editorialAssistant";
	private static final String PARAM_JOURNAL_WORFLOW_TYPE = "journal_worflow_type";	
	private static final String PARAM_TYPESETTER = "typesetter";
	private static final String PARAM_PREFIX_DIGITAL_LIBRARY_DELIVERY = "add_prefix_digital_library_delivery";

	private String journalCode;

	private String journalName;

	private String journalEmail;

	private String editorialAssistant;

	private String producationController;

	private String typesetter;

	private String inpsecClassifier;
	
	private String workflowType;
	
	private String addPrefixForDigitalLibraryDelivery;

	public JournalCreateDTO(CallArgumentList args) {
		journalCode = args.getFirstValue(PARAM_JOURNAL_CODE);
		journalName = args.getFirstValue(PARAM_JOURNAL_NAME);
		journalEmail = args.getFirstValue(PARAM_JOURNAL_EMAIL);
		editorialAssistant = args.getFirstValue(PARAM_EDITORIAL_ASSISTANT);
		producationController = args
				.getFirstValue(PARAM_PRODUCATION_CONTROLLER);
		typesetter = args.getFirstValue(PARAM_TYPESETTER);
		inpsecClassifier = args.getFirstValue(PARAM_INPSEC_CLASSIFIER);
		
		workflowType = args.getFirstValue(PARAM_JOURNAL_WORFLOW_TYPE);
		addPrefixForDigitalLibraryDelivery = args.getFirstValue(PARAM_PREFIX_DIGITAL_LIBRARY_DELIVERY);

	}

	public String getJournalCode() {
		return journalCode;
	}

	public void setJournalCode(String journalCode) {
		this.journalCode = journalCode;
	}

	public String getJournalName() {
		return journalName;
	}

	public void setJournalName(String journalName) {
		this.journalName = journalName;
	}

	public String getJournalEmail() {
		return journalEmail;
	}

	public void setJournalEmail(String journalEmail) {
		this.journalEmail = journalEmail;
	}

	public String getEditorialAssistant() {
		return editorialAssistant;
	}

	public void setEditorialAssistant(String editorialAssistant) {
		this.editorialAssistant = editorialAssistant;
	}

	public String getProducationController() {
		return producationController;
	}

	public void setProducationController(String producationController) {
		this.producationController = producationController;
	}

	public String getTypesetter() {
		return typesetter;
	}

	public void setTypesetter(String typesetter) {
		this.typesetter = typesetter;
	}

	public String getInpsecClassifier() {
		return inpsecClassifier;
	}

	public void setInpsecClassifier(String inpsecClassifier) {
		this.inpsecClassifier = inpsecClassifier;
	}

	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	public String getAddPrefixForDigitalLibraryDelivery() {
		return addPrefixForDigitalLibraryDelivery;
	}

	public void setAddPrefixForDigitalLibraryDelivery(
			String addPrefixForDigitalLibraryDelivery) {
		this.addPrefixForDigitalLibraryDelivery = addPrefixForDigitalLibraryDelivery;
	}
	

}
