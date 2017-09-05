package org.theiet.rsuite.books.webservice;

import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.books.domain.BookMetadaSender;
import org.theiet.rsuite.datamodel.IetBookPublication;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.*;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;

public class ExtractAndSendBookMetadata extends ProjectRemoteApiHandler
		implements BooksConstans {

	private static final String DIALOG_TITLE = "Send book metadata";
	
	@Override
	protected String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		String publicationCaId = getMoSourceId(args);
		
		IetBookPublication book = new IetBookPublication(context, publicationCaId);
		String additionalText = args
				.getFirstValue(EMAIL_VAR_ADDITIONAL_TEXT);

		BookMetadaSender sender = new BookMetadaSender();
		sender.sendBookMetadata(context, book, additionalText);
		
		return "Metadata sent.";
	}

	@Override
	protected String getDialogTitle() {
		return DIALOG_TITLE;
	}
	
	@Override
	protected boolean refreshParent() {
		return true;
	}
}