package org.theiet.rsuite.reporting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.journals.JournalConstants;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.report.ReportGenerationContext;
import com.reallysi.rsuite.service.ContentAssemblyService;

public class PublishedIssueReportInputParameters implements JournalConstants {

	private String startDate;

	private String endDate;

	private Set<String> articleTypesToExclude = new HashSet<String>();

	public static final String[] SCALAR_ARGS = { REPORT_JOURNAL_CODE,
		REPORT_YEAR, REPORT_VOLUME_NUMBER, REPORT_ISSUE_NUMBER };
	
	private Map<String, String> scalarMaps = new HashMap<String, String>();
	
	
	public PublishedIssueReportInputParameters(ReportGenerationContext context,
			CallArgumentList args) throws RSuiteException {

		Set<String> argSet = args.getNames();

		startDate = args.getFirstValue("start", "");
		endDate = args.getFirstValue("end", "");

		getJournalCode(context, args);

		scalarMaps.put(REPORT_YEAR, args.getFirstValue(REPORT_YEAR));
		
		scalarMaps.put(REPORT_VOLUME_NUMBER, args.getFirstValue(REPORT_VOLUME_NUMBER));
		
		scalarMaps.put(REPORT_ISSUE_NUMBER, args.getFirstValue(REPORT_ISSUE_NUMBER));

		if (argSet.contains("exclusions")) {
			articleTypesToExclude.addAll(Arrays.asList(args
					.getValuesArray("exclusions")));
		}

		validateReportInputParamaters();
	}

	private void validateReportInputParamaters() throws RSuiteException {
		if (StringUtils.isNotEmpty(scalarMaps.get(REPORT_ISSUE_NUMBER))
				&& StringUtils.isEmpty(scalarMaps.get(REPORT_VOLUME_NUMBER))) {

			throw new RSuiteException("Please provide volume for number");

		}

		if (StringUtils.isNotEmpty(scalarMaps.get(REPORT_VOLUME_NUMBER))
				&& StringUtils.isEmpty(scalarMaps.get(REPORT_YEAR))) {
			throw new RSuiteException("Please provide year for volume");
		}

	}

	private void getJournalCode(ReportGenerationContext context,
			CallArgumentList args) throws RSuiteException {
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		User user = context.getSession().getUser();

		String journalCodeId = args.getFirstValue(REPORT_JOURNAL_CODE);

		if (!"All".equalsIgnoreCase(journalCodeId)) {
			ContentAssembly journalCa = caSvc.getContentAssembly(user,
					journalCodeId);
			String journalCode = journalCa
					.getLayeredMetadataValue(JournalConstants.LMD_FIELD_JOURNAL_CODE);

			scalarMaps.put(REPORT_JOURNAL_CODE, journalCode);
		}
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public Set<String> getArticleTypesToExclude() {
		return articleTypesToExclude;
	}


	public String getScalarValue(String key){
		return scalarMaps.get(key);
	}

}
