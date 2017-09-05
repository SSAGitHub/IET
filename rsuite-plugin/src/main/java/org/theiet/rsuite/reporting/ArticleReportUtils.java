package org.theiet.rsuite.reporting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadata;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadataFields;
import org.theiet.rsuite.utils.StringUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.service.ContentAssemblyService;

public class ArticleReportUtils {
	
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
	private static final SimpleDateFormat HQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
//	List of lmd fields to filter in article report. 
//	NOTE: It is assumed that the lmd name is the same 
//	as the parameter in the report form, otherwise it will not match
	private static ArrayList<String>filterList = new ArrayList<String>();
	
	static {
		filterList.add(IssueMetadataFields.LMD_FIELD_IS_SPECIAL_ISSUE);
		filterList.add(JournalConstants.LMD_FIELD_INSPEC_REQUIRED);
		filterList.add(JournalConstants.LMD_FIELD_OPEN_ACCESS);
	}

	public static String getHQL(Log log, 
			CallArgumentList args,
			ContentAssemblyService caSvc,
			User user) throws RSuiteException {
		String startDate = args.getFirstValue("startDate");
		String endDate = args.getFirstValue("endDate");
		String journalCaId = args.getFirstValue("journalCaId");
		String status = args.getFirstValue("status");
		String articleId = args.getFirstValue("articleid");
		
		StringBuilder hqlBuffer = new StringBuilder("from Process p where p.name=" +
		"'IET_ARTICLE'");
		
		if (status.equals("complete")) {
			hqlBuffer.append(" and p.dtCompleted is not null");
		}
		else if (status.equals("wip")) {
			hqlBuffer.append(" and p.dtCompleted is null");
		}
		
		if (!StringUtils.isBlank(articleId)) {
			if (articleId.contains("%")) {
				hqlBuffer.append(" and p.metaData.name='PRODUCT_ID' and");
				hqlBuffer.append(" p.metaData.value like'" + articleId + "'");
			}
			else {
				hqlBuffer.append(" and p.metaData.name='PRODUCT_ID' and");
				hqlBuffer.append(" p.metaData.value='" + articleId + "'");
			}
		}

	    String hqlStartDate = null;
	    String hqlEndDate = null;
	    if (startDate != null && !"".equals(startDate)) {
	    	try {
				Date date = DATE_FORMATTER.parse(startDate);
				hqlStartDate = HQL_DATE_FORMAT.format(date);
			} catch (ParseException e) {
				log.error(e);
			}
	    }
	    if (endDate != null && !"".equals(endDate)) {
	    	try {
				Date date = DATE_FORMATTER.parse(endDate);
				hqlEndDate = HQL_DATE_FORMAT.format(date);
			} catch (ParseException e) {
				log.error(e);
			}
	    }
	    if (hqlStartDate != null) {
	    	hqlBuffer.append(" and p.dtStarted >= '" + hqlStartDate + "'"); 
	    }
	    if (hqlEndDate != null) {
	    	hqlBuffer.append(" and p.dtStarted <= '" + hqlEndDate + "'"); 
	    }
	    if (journalCaId != null && !"".equals(journalCaId)) {
			if (!journalCaId.equals("All")) {
				hqlBuffer.append(" and (p.metaData.name='JOURNAL_CODE' and");
				hqlBuffer.append(" p.metaData.value='" + caSvc.getContentAssembly(user, journalCaId).getLayeredMetadataValue(JournalConstants.LMD_FIELD_JOURNAL_CODE) + "')");
			}
	    }
		return hqlBuffer.toString();
	}
	
	public static boolean passFilter(CallArgumentList args,
			ContentAssembly articleCa) throws RSuiteException {
		String[] filters = args.getValuesArray("filter");
		
		return passFilter(filters, articleCa);
	}
	
	public static boolean passFilter(String[] filters,
			ContentAssembly articleCa) throws RSuiteException {
		boolean passFilter = true;
		for (String filter : filters) {
			String lmdValue = articleCa.getLayeredMetadataValue(filter);
			if (!StringUtils.isBlank(lmdValue)) {
				if (!lmdValue.toLowerCase().equals("yes")) {
					passFilter = false;
				}
			}
		}
		return passFilter;
	}
}