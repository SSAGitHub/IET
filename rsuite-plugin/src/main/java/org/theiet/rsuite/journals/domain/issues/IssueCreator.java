package org.theiet.rsuite.journals.domain.issues;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class IssueCreator implements JournalConstants {

	/**
	 * Creates a new issue for the journal
	 * 
	 * @param context
	 *            the execution context
	 * @param user
	 *            the user object
	 * @param journalCaId
	 *            the journal CA id or CA ref id
	 * @param year
	 *            the issue year
	 * @param volume
	 *            the issue volume
	 * @param issue
	 *            the issue number
	 * @return a newly created issue
	 * @throws RSuiteException
	 */
	public static ContentAssembly createIssue(ExecutionContext context,
			User user, String journalCaId, String year, String volume,
			String issue) throws RSuiteException {

		validate(year, volume, issue);

		ContentAssembly journalCA = ProjectBrowserUtils.getContentAssembly(context,
				user, journalCaId);

		List<Tuple> hierarchyList = new ArrayList<Tuple>();
		hierarchyList.add(new Tuple(CA_TYPE_ISSUES, "Issues"));
		hierarchyList.add(new Tuple(CA_TYPE_YEAR, year));
		hierarchyList.add(new Tuple(CA_TYPE_VOLUME, volume));

		ContentAssembly currentContext = journalCA;

		for (int i = 0; i < hierarchyList.size(); i++) {
			Tuple tuple = hierarchyList.get(i);
			ContentAssembly ca = ProjectBrowserUtils.getChildCaByNameAndType(context,
					currentContext, tuple.getCaType(), tuple.getDisplayName());
			if (ca == null) {
				for (int z = i; z < hierarchyList.size(); z++) {
					tuple = hierarchyList.get(z);
					currentContext = ProjectContentAssemblyUtils.createContentAssembly(context,
							currentContext.getId(), tuple.getDisplayName(),
							tuple.getCaType());
				}

				break;
			} else {
				currentContext = ca;
			}
		}
		
		
		String issueCode = Issue.generateIssueCode(
				journalCA.getLayeredMetadataValue(LMD_FIELD_JOURNAL_CODE),
				year, volume, issue);

		ContentAssembly issueCA = createIssue(context, user, issue,
				currentContext, issueCode ,journalCA.getLayeredMetadataValue(LMD_FIELD_JOURNAL_CODE));

		return issueCA;
	}

	/**
	 * Validates input date
	 * 
	 * @param year
	 *            the year
	 * @param volume
	 *            the volume
	 * @param issue
	 *            the issue
	 * @throws RSuiteException
	 *             if one of argument value is invalid
	 */
	private static void validate(String year, String volume, String issue)
			throws RSuiteException {
		if (StringUtils.isAlpha(year)) {
			throw new RSuiteException("The year must be a number");
		}

		if (StringUtils.isAlpha(volume)) {
			throw new RSuiteException("The volume must be a number");
		}

		if (StringUtils.isAlpha(issue)) {
			throw new RSuiteException("The issue must be a number");
		}
	}

	/**
	 * Creates a new issue
	 * 
	 * @param context
	 *            the execution context
	 * @param issue
	 *            the issue number
	 * @param volumeCA
	 *            the volume content assembly
	 * @return a new issue CA
	 * @throws RSuiteException
	 *             if issue already exists
	 */
	public static ContentAssembly createIssue(ExecutionContext context,
			User user, String issue, ContentAssembly volumeCA, String issueCode, String journalCode)
			throws RSuiteException {
		ContentAssembly issueCA = ProjectBrowserUtils.getChildCaByNameAndType(context,
				volumeCA, CA_TYPE_ISSUE, issue);

		if (issueCA != null) {
			throw new RSuiteException("Issue: " + issue + " already exists");
		}

		issueCA = ProjectContentAssemblyUtils.createContentAssembly(context, volumeCA.getId(),
				issue, CA_TYPE_ISSUE);
		String issueCAId = issueCA.getId();

		ProjectContentAssemblyUtils.createContentAssembly(context, issueCA.getId(),
				CA_NAME_DOCUMENTATION, "");
		ProjectContentAssemblyUtils.createContentAssembly(context, issueCA.getId(),
				CA_NAME_ARTICLES, CA_TYPE_ISSUE_ARTICLES);
		ProjectContentAssemblyUtils.createContentAssembly(context, issueCA.getId(),
				IetConstants.CA_NAME_TYPESETTER, IetConstants.CA_TYPE_TYPESETTER);
		
		ArrayList<MetaDataItem> items = new ArrayList<MetaDataItem>();
		items.add(new MetaDataItem(LMD_FIELD_ISSUE_CODE, issueCode));
		items.add(new MetaDataItem(LMD_FIELD_JOURNAL_CODE, journalCode));
		context.getManagedObjectService().setMetaDataEntries(user,
				issueCAId,
				items);
		
		
		return issueCA;
	}

	private static class Tuple {
		private String caType;

		private String displayName;

		public Tuple(String key, String value) {
			this.caType = key;
			this.displayName = value;
		}

		public String getCaType() {
			return caType;
		}

		public String getDisplayName() {
			return displayName;
		}
	}
}
