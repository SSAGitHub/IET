package org.theiet.rsuite.standards.webservices;

import java.util.*;

import org.theiet.rsuite.books.domain.BookMetadaSender;
import org.theiet.rsuite.datamodel.IetBookPublication;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.domain.book.StandardsBookEdition;
import org.theiet.rsuite.utils.WorkflowUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.*;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;

public class StandardsSendBookMetadata extends ProjectRemoteApiHandler implements
		StandardsBooksConstans {

	private static final String DIALOG_TITLE = "Send book metadata";

	@Override
	protected String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		StringBuilder successResponse = new StringBuilder(
				"Metadata has been sent");
		String publicationCaId = getMoSourceId(args);

		StandardsBookEdition bookEdition = new StandardsBookEdition(context,
				publicationCaId);

		String additionalText = args.getFirstValue(EMAIL_VAR_ADDITIONAL_TEXT);

		if (!bookEdition.isMetadataAlreadySent()) {
			startStartStandardsWorkflow(context, args, bookEdition);
			successResponse.append(" and workflow has been started");
			markAsSentMetada(context, bookEdition);
		}

		BookMetadaSender sender = new BookMetadaSender();
		sender.sendBookMetadata(context, bookEdition, additionalText);

		return successResponse.toString() + ".";
	}

	private void markAsSentMetada(ExecutionContext context,
			IetBookPublication bookEdition) throws RSuiteException {
		User user = context.getAuthorizationService().getSystemUser();
		context.getManagedObjectService().setMetaDataEntry(user,
				bookEdition.getRsuiteId(),
				new MetaDataItem(LMD_FIELD_BOOK_METADATA_SENT, LMD_VALUE_YES));
	}

	public void startStartStandardsWorkflow(RemoteApiExecutionContext context,
			CallArgumentList args, IetBookPublication bookEdition)
			throws RSuiteException {
		Map<String, Object> variables = new HashMap<String, Object>();
		ContentAssembly bookEditionCa = bookEdition.getBookPublicationCa();

		variables.put(WF_VAR_PRODUCTION_CONTROLLER,
				bookEditionCa.getLayeredMetadataValue(LMD_FIELD_PRODUCTION_CONTROLLER_USER));
		variables.put(WF_VAR_AUTHOR, bookEditionCa
				.getLayeredMetadataValue(LMD_FIELD_IET_AUTHOR));

		WorkflowUtils.startWorkflowForMoFromWS(context, args,
				WF_NAME_IET_STANDARDS_WORKFLOW, variables);
	}

	@Override
	protected String getDialogTitle() {
		return DIALOG_TITLE;
	}
}