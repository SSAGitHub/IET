package org.theiet.rsuite.standards.webservices;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.domain.book.StandardsBookEdition;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;

public class CopyBookEditionContentWebService extends ProjectRemoteApiHandler
		implements StandardsBooksConstans {

	private static final String DIALOG_TITLE = "Copy Book Edition content";

	private String moToRefresh = null;
	
	@Override
	protected String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		StringBuilder successResponse = new StringBuilder(
				"Content has been copied");
		String publicationCaId = getMoSourceId(args);
		
		StandardsBookEdition tgtBookEdition = new StandardsBookEdition(context,
				publicationCaId);

		String sourceBookEdition = args
				.getFirstValue(FORM_PARAM_SOURCE_BOOK_EDITION);

		if (StringUtils.isEmpty(sourceBookEdition)){
			return "No source book edition has been selected";
		}
		
		User user = context.getAuthorizationService().getSystemUser();

		ContentAssembly sourceBookEditionCa = context
				.getContentAssemblyService().getContentAssembly(user,
						sourceBookEdition);

		StandardsBookEdition srcBookEdition = new StandardsBookEdition(context,
				sourceBookEditionCa);

		tgtBookEdition.copyContentFromAnotherEdition(srcBookEdition);
		moToRefresh = tgtBookEdition.getTypesriptCA().getId();

		String sourcePublicationCaId = sourceBookEditionCa.getId();

		StandardsBookEdition bookEdition = new StandardsBookEdition(context,
				sourcePublicationCaId);

		bookEdition.archiveEdition();
		
		return successResponse.toString() + ".";
	}

	
	@Override
	public String getMoIdToRefresh() {		
		return moToRefresh;
	}
	
	@Override
	protected String getDialogTitle() {
		return DIALOG_TITLE;
	}
}