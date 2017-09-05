package org.theiet.rsuite.journals.domain.article.delivery.author;

import org.theiet.rsuite.journals.domain.article.Article;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.net.mail.MailMessage;
import com.rsicms.projectshelper.net.mail.MailUtils;

public class AuthorDelivery {

	private AuthorDelivery(){
	}
	
	public static void sendComplimentaryPDF(ExecutionContext context, User user, Article article) throws RSuiteException{
		ManagedObject mo = AuthorComplimentaryPDF.obtainProofPDF(article);
		sendComplimentaryEmail(context, user, article, mo);		
	}
	
	public static void sendFinalComplimentaryPDF(ExecutionContext context, User user, Article article) throws RSuiteException{
		ManagedObject mo = AuthorComplimentaryPDF.obtainFinalPDF(article);
		sendComplimentaryEmail(context, user, article, mo);		
	}

	protected static void sendComplimentaryEmail(ExecutionContext context,
			User user, Article article, ManagedObject mo)
			throws RSuiteException {
		MailMessage authorMessage = AuthorComplimentaryPDF.createAuthorMailMessage(context, user, article);
		MailUtils.sentMoViaEmail(context, authorMessage, mo, true);
	}
	
}
