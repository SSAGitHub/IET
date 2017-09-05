package org.theiet.rsuite.journals.domain.issues.datatype.metadata;

import static org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadataFields.LMD_FIELD_COVER_DATE;
import static org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadataFields.LMD_FIELD_FIRST_NUMBERED_PAGE;
import static org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadataFields.LMD_FIELD_ISSUE_CODE;
import static org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadataFields.LMD_FIELD_ISSUE_AUTOMATED_PDF_GENERATION_WORKFLOW;
import static org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadataFields.LMD_FIELD_ISSUE_TITLE;
import static org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadataFields.LMD_FIELD_IS_SPECIAL_ISSUE;
import static org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadataFields.LMD_FIELD_JOURNAL_CODE;
import static org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadataFields.LMD_FIELD_PRINT_PUBLISHED_DATE;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;

public class IssueMetadata {

	private ContentAssembly issueCa;

	public IssueMetadata(ContentAssembly issueCa) {
		this.issueCa = issueCa;
	}

	public String getFirstNumberedPage() throws RSuiteException {
		return getLmd(LMD_FIELD_FIRST_NUMBERED_PAGE);
	}

	public String getIssueCode() throws RSuiteException {
		return getLmd(LMD_FIELD_ISSUE_CODE);
	}

	public String getJournalCode() throws RSuiteException {
		return getLmd(LMD_FIELD_JOURNAL_CODE);
	}

	public IssueCoverDate getCoverDate() throws RSuiteException {
		return new IssueCoverDate(getLmd(LMD_FIELD_COVER_DATE));
	}
	
	private String getLmd(String lmdName) throws RSuiteException {
		return issueCa.getLayeredMetadataValue(lmdName);
	}

	public boolean isSpecialIssue() throws RSuiteException {
		String specialIssueValue = getLmd(LMD_FIELD_IS_SPECIAL_ISSUE);
		return getBooleanWithDefaultFalse(specialIssueValue);
	}

	private boolean getBooleanWithDefaultFalse(String lmdValue) {
		return getBooleanFromLmdValue(lmdValue, false);
	}
	
	private boolean getBooleanFromLmdValue(String lmdValue, boolean resultForEmptyValue) {
		
		if (StringUtils.isEmpty(lmdValue)){
			return resultForEmptyValue;
		}
		
		if (Boolean.parseBoolean(lmdValue)
				|| "yes".equals(lmdValue)) {
			return true;
		}
		
		return false;
	}
	
	public boolean isAutomaticPdfGenerationWorkflow() throws RSuiteException {
		String automatedPdfWorkflow = getLmd(LMD_FIELD_ISSUE_AUTOMATED_PDF_GENERATION_WORKFLOW);
		return getBooleanWithDefaultFalse(automatedPdfWorkflow);
	}

	public String getIssueTitle() throws RSuiteException {
		return getLmd(LMD_FIELD_ISSUE_TITLE);
	}
	
	public String getPrintPublishedDate() throws RSuiteException {
		return getLmd(LMD_FIELD_PRINT_PUBLISHED_DATE);	
	}
	
	public String getLmdInCSV() throws RSuiteException{
		return IssueMetadataCSVConventer.extractLMDtoCSV(issueCa);
	}
	
	
}
