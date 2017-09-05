package org.theiet.rsuite.journals.domain.issues.notification.typesetter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadata;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.datatype.IssueArticles;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadata;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.journals.utils.JournalMailUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.net.mail.MailMessage;
import com.rsicms.projectshelper.net.mail.MailUtils;

public class TypesetterNotification {

	private static final String PROP_ISSUE_TYPESETTER_PASS_FOR_PRESS_MAIL_BODY = "iet.journals.issue.typesetter.pass.for.press.mail.body";

	private static final String PROP_ISSUE_TYPESETTER_PASS_FOR_PRESS_MAIL_TITLE = "iet.journals.issue.typesetter.pass.for.press.mail.title";

	private static final String PROP_ISSUE_TYPESETTER_REQUEST_MAIL_BODY = "iet.journals.issue.typesetter.request.mail.body";

	private static final String PROP_ISSUE_TYPESETTER_REQUEST_MAIL_TITLE = "iet.journals.issue.typesetter.request.mail.title";

	private static final String PROP_ISSUE_TYPESETTER_FINAL_REQUEST_MAIL_BODY = "iet.journals.issue.typesetter.final.request.mail.body";

	private static final String PROP_ISSUE_TYPESETTER_FINAL_REQUEST_MAIL_TITLE = "iet.journals.issue.typesetter.final.request.mail.title";

	private TypesetterNotification() {
	}

	public static void sendPassForPressNotification(ExecutionContext context,
			User user, Issue issue) throws RSuiteException {

		try {

			MailMessage message = TypesetterNotification.createMailMessage(
					context, user, issue,
					PROP_ISSUE_TYPESETTER_PASS_FOR_PRESS_MAIL_TITLE,
					PROP_ISSUE_TYPESETTER_PASS_FOR_PRESS_MAIL_BODY);

			Journal journal = issue.getJournal();
			message.getMailTo().add(journal.getJournalProductionUser().getEmail());
			
			Map<String, String> variables = message.getVariables();
			addListOfPapersVariable(context, variables, issue);

			String csvContent = TypesetterCsv.createPassForPressCsv(issue);			

			message.addAttachment(issue.getIssueCode() + ".csv",
					csvContent.getBytes("utf-8"));

			MailUtils.sentEmail(context, message);
		} catch (IOException | RSuiteException e) {
			throw new RSuiteException(0, "Unable to send email for issue "
					+ issue, e);
		}
	}

	public static void sendFinalRequestNotification(ExecutionContext context,
			User user, Issue issue) throws RSuiteException {
		try {
			MailMessage authorMessage = createMailMessage(context, user, issue,
					PROP_ISSUE_TYPESETTER_FINAL_REQUEST_MAIL_TITLE,
					PROP_ISSUE_TYPESETTER_FINAL_REQUEST_MAIL_BODY);

			MailUtils.sentEmail(context, authorMessage);

		} catch (IOException | RSuiteException e) {
			throw new RSuiteException(0, "Unable to send email for issue "
					+ issue, e);
		}
	}

	public static void sendRequestNotification(ExecutionContext context,
			User user, Issue issue)
			throws RSuiteException, IOException {

		try {
			MailMessage message = TypesetterNotification.createMailMessage(
					context, user, issue,
					PROP_ISSUE_TYPESETTER_REQUEST_MAIL_TITLE,
					PROP_ISSUE_TYPESETTER_REQUEST_MAIL_BODY);

			Map<String, String> variables = message.getVariables();
			addListOfPapersVariable(context, variables, issue);

			IssueMetadata issueMetadata = issue.getIssueMetadata();

			String csvContent = issueMetadata.getLmdInCSV();

			message.addAttachment(issue.getIssueCode() + ".csv",
					csvContent.getBytes("utf-8"));

			MailUtils.sentEmail(context, message);

		} catch (IOException | RSuiteException e) {
			throw new RSuiteException(0, "Unable to send email for issue "
					+ issue, e);
		}
	}

	private static Map<String, String> addListOfPapersVariable(
			ExecutionContext context, Map<String, String> variables, Issue issue)
			throws RSuiteException {

		IssueArticles issueArticles = issue.getIssueArticles();

		StringBuilder articleList = new StringBuilder();

		for (Article article : issueArticles.getArticles()) {
			ArticleMetadata articleMetadata = article.getArticleMetadata();
			articleList.append(article.getArticleId());
			articleList.append(" ").append(articleMetadata.getArticleTitle());
			articleList.append("\n");
		}

		variables.put("ListOfPapers", articleList.toString());

		return variables;
	}

	private static MailMessage createMailMessage(ExecutionContext context,
			User user, Issue issue, String mailSubjectProp, String mailBodyProp)
			throws RSuiteException, IOException {

		ContentAssembly journalCa = issue.getJournalCa();

		Map<String, String> variables = setUpVariablesMap(context, user,
				journalCa, issue);

		String emailFrom = JournalMailUtils.obtainEmailFrom(context, journalCa);

		String mailSubject = MailUtils.obtainEmailSubject(context,
				mailSubjectProp);

		Journal journal = issue.getJournal();

		List<String> recipients = new ArrayList<String>();
		recipients.add(journal.getTypestter().getEmail());
		recipients.add(journal.getProductionController().getEmail());

		MailMessage message = new MailMessage(emailFrom, recipients,
				mailSubject);
		message.setMessageTemplateProperty(mailBodyProp);
		message.setVariables(variables);

		return message;
	}

	private static Map<String, String> setUpVariablesMap(
			ExecutionContext context, User user, ContentAssemblyItem journalCa,
			Issue issue) throws RSuiteException {

		Map<String, String> variables = new HashMap<String, String>();
		variables.put(JournalMailUtils.EMAIL_VAR_JOURNAL_NAME,
				journalCa.getDisplayName());
		variables.put(JournalMailUtils.EMAIL_VAR_EDITORIAL_ASSISTANT_NAME,
				user.getFullName());

		StringBuilder issueDetails = new StringBuilder(issue.getYear());
		issueDetails.append("/").append(issue.getVolume());
		issueDetails.append("/").append(issue.getIssueNumber());

		variables.put("IssueDetails", issueDetails.toString());

		return variables;
	}
}
