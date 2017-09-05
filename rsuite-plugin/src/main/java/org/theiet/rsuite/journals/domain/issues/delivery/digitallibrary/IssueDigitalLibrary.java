package org.theiet.rsuite.journals.domain.issues.delivery.digitallibrary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.datamodel.ExternalCompanyUser;
import org.theiet.rsuite.datatype.deliveryUser.DeliveryUser;
import org.theiet.rsuite.datatype.deliveryUser.DeliveryUserFactory;
import org.theiet.rsuite.domain.date.IetDate;
import org.theiet.rsuite.domain.user.UserUtils;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.delivery.digitallibrary.ArticleDigitalLibraryPackageBuilder;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.datatype.IssueArticles;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadata;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadataUpdater;
import org.theiet.rsuite.journals.domain.issues.pubtrack.IssuePubtrackManager;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.utils.StringUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class IssueDigitalLibrary {

	private Log logger = LogFactory.getLog(getClass());

	private ExecutionContext context;

	private User user;

	private static final String PROP_DIGITAL_LIBRARY_FTP_FOLDER_ISSUE = "ftp.folder.issue";

	private static final String DIGITAL_LIBRARY_ISSUE_USER = "DigitalLibraryIssue";

	public IssueDigitalLibrary(ExecutionContext context, User user, Log logger) {
		this.context = context;
		this.user = user;
		this.logger = logger;
	}

	public void deliverIssue(Issue issue) throws RSuiteException {

		validateIssueBeforeDelivery(issue);

		ByteArrayOutputStream issueArchive = createIssueArchive(issue);

		sentIssueArchiveToDigitalLibrary(issue, issueArchive);

		setPublishedOnlineDateLmd(issue);

		logEventToPubtrack(issue);
	}

	private void validateIssueBeforeDelivery(Issue issue)
			throws RSuiteException {
		IssueMetadata issueMetadata = issue.getIssueMetadata();

		String issueCode = issueMetadata.getIssueCode();
		if (StringUtils.isBlank(issueCode)) {
			throw new RSuiteException("Unable to get issue code");
		}

		String journalCode = issueMetadata.getJournalCode();
		if (StringUtils.isBlank(journalCode)) {
			new RSuiteException("Unable to get journal code");
		}

		IssueArticles issueArticles = issue.getIssueArticles();
		ContentAssembly issueArticlesCa = issueArticles.getArtilcesCA();

		if (issueArticlesCa == null) {
			new RSuiteException("No Article CA in issue");
		}
	}

	private ByteArrayOutputStream createIssueArchive(Issue issue)
			throws RSuiteException {
		IssueArticles issueArticles = issue.getIssueArticles();
		List<Article> articles = issueArticles.getArticles();

		ByteArrayOutputStream issueOutputStream = new ByteArrayOutputStream();
		ZipOutputStream issueZipStream = new ZipOutputStream(issueOutputStream);

		for (Article article : articles) {

			addArticleArchiveToIssueArchive(issueZipStream, article);
		}

		IOUtils.closeQuietly(issueZipStream);

		return issueOutputStream;
	}

	private void addArticleArchiveToIssueArchive(ZipOutputStream issueArchive,
			Article article) throws RSuiteException {
		ByteArrayOutputStream articleDLArchive = ArticleDigitalLibraryPackageBuilder
				.createArticleDigitalLibraryArchive(context, user, article);

		String articleZipFileName = ArticleDigitalLibraryPackageBuilder
				.createDigitalLibraryFinalFileName(context,
						article.getArticleId(), "zip");

		byte[] articleBytes = articleDLArchive.toByteArray();
		ByteArrayInputStream articleBais = new ByteArrayInputStream(
				articleBytes);
		ZipEntry issueZipEntry = new ZipEntry(articleZipFileName);
		try {
			issueArchive.putNextEntry(issueZipEntry);
			IOUtils.copy(articleBais, issueArchive);
			issueArchive.closeEntry();
			articleBais.close();
		} catch (IOException e) {
			throw new RSuiteException(0, "Problem with adding " + article
					+ " to issue archive", e);
		}

	}

	private void sentIssueArchiveToDigitalLibrary(Issue issue,
			ByteArrayOutputStream issueArchive) throws RSuiteException {
		byte[] bytes = issueArchive.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

		ExternalCompanyUser digitalLibraryUser = UserUtils.getExtarnalUser(
				context, DIGITAL_LIBRARY_ISSUE_USER);
		DeliveryUser deliveryUser = DeliveryUserFactory.createDeliveryUser(
				context, digitalLibraryUser.getUserId());

		String ftpFileName = createIssueArchiveFileName(issue);

		logger.info("deliverIssue: preparing to deliver issue as "
				+ ftpFileName);
		String targetFolder = deliveryUser
				.getProperty(PROP_DIGITAL_LIBRARY_FTP_FOLDER_ISSUE);
		deliveryUser.deliverToDestination(bais, ftpFileName, targetFolder);
		logger.info("deliverIssue: " + ftpFileName
				+ "transfer complete, setting metadata and updating pubtrack");
	}

	private String createIssueArchiveFileName(Issue issue)
			throws RSuiteException {

		IssueMetadata issueMetadata = issue.getIssueMetadata();
		String issueCode = issueMetadata.getIssueCode();
		Journal journal = new Journal(context, issue.getJournalCa());
		String fixedJournalCode = ArticleDigitalLibraryPackageBuilder
				.getFixedJournalName(journal);
		String ftpFileName = issueCode.replaceFirst(journal.getJournalCode(),
				fixedJournalCode) + ".zip";
		return ftpFileName;
	}

	private void logEventToPubtrack(Issue issue) throws RSuiteException {
		try {
			IssuePubtrackManager.logSendToDigitalLibrary(context, user, issue);
			IssuePubtrackManager.completeIssueProcess(context, user, issue);
		} catch (RSuiteException e) {
			throw new RSuiteException(0, "Failed to log to pubtrack. Issue:  "
					+ issue + " Message: " + e.getMessage(), e);
		}
	}

	private void setPublishedOnlineDateLmd(Issue issue) throws RSuiteException {
		String publishedOnlineDate = IetDate.getCurrentDate();
		IssueMetadataUpdater issueMetadataUpdater = issue
				.createIssueMetadataUpdater();
		issueMetadataUpdater.setOnlinePublishDate(publishedOnlineDate);
		issueMetadataUpdater.updateMetadata();
	}
}
