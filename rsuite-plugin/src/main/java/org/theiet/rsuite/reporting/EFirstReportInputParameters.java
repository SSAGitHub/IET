package org.theiet.rsuite.reporting;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
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

public class EFirstReportInputParameters implements JournalConstants {

	private String startDate;

	private String endDate;

	private Set<String> articleTypesToExclude = new HashSet<String>();
	
	public static final String[] SCALAR_ARGS = {REPORT_JOURNAL_CODE};
	
	private Map<String, String> scalarMaps = new HashMap<String, String>();
	
	private String[] filters;
	
	public EFirstReportInputParameters(ReportGenerationContext context,
			CallArgumentList args) throws RSuiteException {
		
		Set<String> argSet = args.getNames();
		
		filters = args.getValuesArray("filter");

		startDate = args.getFirstValue("start", "");
		endDate = args.getFirstValue("end", "");

		setJournalCode(context, args);
		
		if (argSet.contains("exclusions")) {
			articleTypesToExclude.addAll(Arrays.asList(args
					.getValuesArray("exclusions")));
		}

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
	
	private void setJournalCode(ReportGenerationContext context,
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
	
	public Set<String> getArticleTypesToExclude() {
		return articleTypesToExclude;
	}
	
	public String getScalarValue(String key){
		return scalarMaps.get(key);
	}
	
	public String[] getFilters () {
		return filters;
	}

}
