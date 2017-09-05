package org.theiet.rsuite.journals.domain.article.delivery.author;

import java.util.Map;

import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.ArticleTypesetterContainer;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadata;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.journals.utils.JournalMailUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.net.mail.MailMessage;
import com.rsicms.projectshelper.net.mail.MailUtils;

class AuthorComplimentaryPDF {

	private static final String PROP_COMPLIMENTARY_COPY_MAIL_BODY = "iet.journals.complimentary.copy.mail.body";
	
	private static final String PROP_COMPLIMENTARY_COPY_MAIL_TITLE = "iet.journals.complimentary.copy.mail.title";
	
	static MailMessage createAuthorMailMessage(
			ExecutionContext context, User user, Article article) throws RSuiteException {

		Journal journal = article.getJournal();
		
		Map<String, String> variables = JournalMailUtils.setUpVariablesMap(
				context, user, journal.getJournalCa(), article.getArticleCA());
		

		ArticleMetadata articleMetadata = article.getArticleMetadata();
		String recipientEmailAddress  = articleMetadata.getAuthorEmail();

		String mailSubject = MailUtils.obtainEmailSubject(context,
				PROP_COMPLIMENTARY_COPY_MAIL_TITLE);
		
		String emailFrom = JournalMailUtils.obtainEmailFrom(context,
				journal.getJournalCa());
		

		MailMessage message = new MailMessage(emailFrom, recipientEmailAddress,
				mailSubject);
		message.setMessageTemplateProperty(PROP_COMPLIMENTARY_COPY_MAIL_BODY);
		message.setVariables(variables);

		return message;
	}
	
	static ManagedObject obtainProofPDF(Article article) throws RSuiteException {

		ArticleTypesetterContainer typesetterContainer = article.getTypesetterContainer();
		ManagedObject proofPDF = typesetterContainer.getProofPdfMO();
		
		if (proofPDF == null){
			throw new RSuiteException("Unable to find PDF proof");
		}
		
		return proofPDF;
	}
	
	static ManagedObject obtainFinalPDF(Article article) throws RSuiteException {

		ArticleTypesetterContainer typesetterContainer = article.getTypesetterContainer();
		ManagedObject finalPDF = typesetterContainer.getFinalPdfMO();
		
		if (finalPDF == null){
			throw new RSuiteException("Unable to find final PDF");
		}
		
		return finalPDF;
	}
}
