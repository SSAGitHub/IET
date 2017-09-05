package org.theiet.rsuite.journals.domain.issues.datatype;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.constants.JournalsCaTypes;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadata;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadataUpdater;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.journals.domain.journal.JournalsFinder;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.rsicms.projectshelper.utils.ProjectMoUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class Issue {

	private String year;

	private String volume;

	private String issueNumber;

	private String issueCode;

	private ContentAssembly issueCa;

	private ContentAssembly journalCa;

	private ExecutionContext context;
	
	private User user;
	
	private String issueCaId;
	
	private IssueMetadata issueMetadata;
	
	/**
	 * Constructs issue object
	 * 
	 * @param context
	 *            The web service context. The context node must be issue
	 * @param user
	 *            The user object
	 * @param issueCA
	 *            The issue content assembly
	 * @return the issue code
	 * @throws RSuiteException
	 */
	public Issue(RemoteApiExecutionContext context, CallArgumentList args, User user)
			throws RSuiteException {

		String contextId = ProjectMoUtils.getRealMoId(args.getFirstManagedObject(user));

		initializeIssueObject(context, user, contextId);
	}
	
	public Issue(ExecutionContext context, User user, String issueCaId)
			throws RSuiteException {
		initializeIssueObject(context, user, issueCaId);
	}

	private void initializeIssueObject(ExecutionContext context,
			User user, String contextId) throws RSuiteException {
		issueCa = ProjectBrowserUtils.getContentAssembly(context, user, contextId);

		issueCaId = issueCa.getId();

		this.context = context;
		this.user = user;
		
		issueMetadata = new IssueMetadata(issueCa);
		issueCode = issueMetadata.getIssueCode();
		setUpIssueCodeParts(issueCode);
	}

	private void setUpIssueCodeParts(String issueCode) throws RSuiteException {
		String[] issueCodeParts = null;

		if (StringUtils.isBlank(issueCode)
				|| (issueCodeParts = issueCode.split(JournalConstants.CODE_SEPARATOR)).length != 4) {
			throw new RSuiteException("Issue with id " + issueCa.getId()
					+ " doesn't have a proper issue code lmd");
		}

		issueNumber = issueCodeParts[3];
		volume = issueCodeParts[2];
		year = issueCodeParts[1];
	}

	public String getYear() {
		return year;
	}

	public String getVolume() {
		return volume;
	}

	public String getIssueNumber() {
		return issueNumber;
	}

	/**
	 * Gets issue code
	 * 
	 * @param context
	 *            The web service context. The context node must be issue
	 * @param user
	 *            The user object
	 * @param issueCA
	 *            The issue content assembly
	 * @return the issue code
	 * @throws RSuiteException
	 */
	public String getIssueCode() throws RSuiteException {
		return issueCode;
	}

	public ContentAssembly getIssueCa() {
		return issueCa;
	}


	public ContentAssembly getJournalCa() throws RSuiteException {
		if (journalCa == null){
			String journalCode = getIssueMetadata().getJournalCode();
			journalCa = JournalsFinder.findJournalCaBaseOnJournalCode(context, user, journalCode);
		}
		
		return journalCa;
	}

	public Journal getJournal() throws RSuiteException {
		return new Journal(context, getJournalCa());
	}
	
	
	public ContentAssembly getYearCa() throws RSuiteException{
		return ProjectBrowserUtils.getChildCaByNameAndType(context, getJournalCa(), JournalsCaTypes.YEAR, year);
	}
	

	/**
	 * Gets issue code
	 * 
	 * @param context
	 *            The web service context. The context node must be issue
	 * @param user
	 *            The user object
	 * @param issueCA
	 *            The issue content assembly
	 * @return the issue code
	 * @throws RSuiteException
	 */
	public static String generateIssueCode(String journalCode, String year,
			String volume, String issueNumber) throws RSuiteException {

		StringBuilder sb = new StringBuilder(journalCode);
		sb.append(JournalConstants.CODE_SEPARATOR).append(year)
				.append(JournalConstants.CODE_SEPARATOR).append(volume);
		sb.append(JournalConstants.CODE_SEPARATOR).append(issueNumber);

		return sb.toString();
	}

	public IssueArticles getIssueArticles() throws RSuiteException{
		ContentAssembly issueArticlesCa = ProjectBrowserUtils.getChildCaByType(context, issueCa, JournalsCaTypes.ISSUE_ARTICLES);
		return new IssueArticles(context, user, issueArticlesCa);
	}
	
	public ContentAssembly getTypesetterCA() throws RSuiteException{
		return ProjectBrowserUtils.getChildCaByType(context, issueCa, JournalsCaTypes.TYPESETTER);
	}
	
	public ManagedObject getIssueMo() throws RSuiteException {
		return context.getManagedObjectService().getManagedObject(user, issueCaId);
	}

	public IssueMetadata getIssueMetadata() {
		return issueMetadata;
	}	
	
	public String getInstructPagePdfFilName() throws RSuiteException{
		return getIssueCode() + "_InstForAuth.pdf";
	}
	
	public String getLegalPagePdfFilName() throws RSuiteException{
		return getIssueCode() + "_Legal_Page.pdf";
	}
	
	public IssueMetadataUpdater createIssueMetadataUpdater(){
		return new IssueMetadataUpdater(user, context.getManagedObjectService(), issueCaId);
	}
		
	@Override
	public String toString() {
		StringBuilder value = new StringBuilder("Issue: " + issueCode);
		value.append(" caId: " + issueCaId);
		
		return value.toString();
	};
}
