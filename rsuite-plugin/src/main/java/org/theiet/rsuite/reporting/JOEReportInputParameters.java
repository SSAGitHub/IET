package org.theiet.rsuite.reporting;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.utils.DataTypeUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.DataTypeOptionValue;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.report.ReportGenerationContext;

public class JOEReportInputParameters implements JournalConstants {

	private String startDate;

	private String endDate;

	private Set<String> articleTypesToExclude = new HashSet<String>();

	private String journalCode;

	private List<String> allArchiveJournalCodes;

	public JOEReportInputParameters(ReportGenerationContext context,
			CallArgumentList args) throws RSuiteException {

		Set<String> argSet = args.getNames();

		startDate = args.getFirstValue("start", "");
		endDate = args.getFirstValue("end", "");

		if (argSet.contains("exclusions")) {
			articleTypesToExclude.addAll(Arrays.asList(args
					.getValuesArray("exclusions")));
		}

		String candidateJournalCaId = args.getFirstValue("journalCaId");
		if (StringUtils.isNumeric(candidateJournalCaId)) {
			ContentAssembly journalCa = context.getContentAssemblyService()
					.getContentAssembly(context.getUser(), candidateJournalCaId);
			journalCode = journalCa.getLayeredMetadataValue(LMD_FIELD_JOURNAL_CODE); 
		} else {
			journalCode = candidateJournalCaId; 
		}

		allArchiveJournalCodes = populateArchiveJournalCodes(context);		
	}

	private List<String> populateArchiveJournalCodes(ReportGenerationContext context) throws RSuiteException {
		DataTypeOptionValue[] optionValues = DataTypeUtils.getDataTypeOptionValues(context, context.getUser(), DT_NAME_ARCHIVED_JOURNAL);

		List<String> allArchiveJournalCodes = new ArrayList<String>();
		for (int i = 0; i<optionValues.length; i++) {
			String journalCaId = optionValues[i].getValue();
			if (StringUtils.isNumeric(journalCaId)) {
				allArchiveJournalCodes.add(context.getContentAssemblyService()
						.getContentAssembly(context.getUser(), journalCaId)
							.getLayeredMetadataValue(LMD_FIELD_JOURNAL_CODE));
			}
		}

		return allArchiveJournalCodes;
	}

	public Set<String> getArticleTypesToExclude() {
		return articleTypesToExclude;
	}

	public boolean isBetweenDateRange (String journalManuscriptID, String onlinePublishedDate) throws RSuiteException {
		if (StringUtils.isEmpty(onlinePublishedDate)) {
			return false;
		}
		
		try {
			Date dStartDate = JOEReport.DATE_PICKER_FORMAT.parse(startDate);
			Date dEndDate = JOEReport.DATE_PICKER_FORMAT.parse(endDate);
			Date dOnlinePublishedDate = JOEReport.DATE_PICKER_FORMAT.parse(onlinePublishedDate);

			return dStartDate.compareTo(dOnlinePublishedDate) * dOnlinePublishedDate.compareTo(dEndDate) > 0;
		} catch (ParseException ex) {
			throw new RSuiteException(RSuiteException.ERROR_NOT_DEFINED, "Not expected Online published date format for: " + journalManuscriptID, ex);
		}
	}

	public List<String> getAllArchiveJournalCodes() {
		return allArchiveJournalCodes;
	}

	public String getJournalCode () {
		return journalCode;
	}
}
