package org.theiet.rsuite.datatype;

import static org.theiet.rsuite.journals.datatype.JournalWorkflowType.ARCHIVE;

import org.theiet.rsuite.journals.JournalConstants;

public class ArchiveJournalProvider extends JournalProvider implements JournalConstants {

	@Override
	protected String getJournalQuery () {
		return "/rs_ca_map/rs_ca[rmd:get-type(.) = 'journal'][rmd:get-lmd-value(., '" + LMD_FIELD_JOURNAL_WORFLOW_TYPE + "') = '" + ARCHIVE + "']";
	}
}