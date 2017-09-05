package org.theiet.rsuite.journals.domain.issues.datatype.metadata;

import static org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadataFields.*;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.utils.string.csv.CSVBuilder;

class IssueMetadataCSVConventer {

	private IssueMetadataCSVConventer(){
	}
	
	private static String[] lmdToExport = { LMD_FIELD_COVER_DATE,
			LMD_FIELD_TOTAL_PAGINATION, LMD_FIELD_IS_SPECIAL_ISSUE,
			LMD_FIELD_ISSUE_TITLE, LMD_FIELD_INSIDE_FRONT_COVER,
			LMD_FIELD_PRELIMS, LMD_FIELD_INSIDE_BACK_COVER,
			LMD_FIELD_FIRST_NUMBERED_PAGE, LMD_FIELD_LAST_NUMBERED_PAGE,
			LMD_FIELD_ADVERT_PAGES };
	
	static String extractLMDtoCSV(ContentAssembly issueCA)
			throws RSuiteException {
		
		CSVBuilder csv = new CSVBuilder();
		
		for (String lmd : lmdToExport) {
				
			csv.addValue(lmd);
			csv.addValueSeparator();
			
			String value = issueCA.getLayeredMetadataValue(lmd);
			
			csv.addValue(value);
			csv.addNewLine();
		}

		return csv.toString();
	}
	
}
