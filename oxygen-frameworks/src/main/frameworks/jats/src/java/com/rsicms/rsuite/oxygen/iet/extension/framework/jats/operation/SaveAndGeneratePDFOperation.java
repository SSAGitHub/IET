package com.rsicms.rsuite.oxygen.iet.extension.framework.jats.operation;

import java.util.UUID;

import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.ArgumentDescriptor;
import ro.sync.ecss.extensions.api.ArgumentsMap;
import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorOperation;
import ro.sync.ecss.extensions.api.AuthorOperationException;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenComponentRegister;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenEditorContext;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.HttpUtils;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.DialogHelper;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;

public class SaveAndGeneratePDFOperation implements AuthorOperation {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public String getDescription() {
		return "Save document and generate PDF";
	}

	@Override
	public void doOperation(AuthorAccess authorAccess, ArgumentsMap argumentsMap)
			throws IllegalArgumentException, AuthorOperationException {

		try {
			OxygenEditorContext context = OxygenComponentRegister
					.getRegisterOxygenEditorContext(authorAccess);

			context.getMainComponent().saveActiveDocument();

			OxygenDocument activeDocumentComponent = context.getMainComponent()
					.getActiveDocumentComponent();

			sendRequestToGeneratePDF(activeDocumentComponent);

			DialogHelper dialogHelper = new DialogHelper(context.getMainFrame());
			dialogHelper.showInformationMessage("Publish process has been started");

		} catch (Exception e) {
			OxygenUtils.handleExceptionUI(logger, e);
		}
	}

	public void sendRequestToGeneratePDF(OxygenDocument activeDocumentComponent)
			throws OxygenIntegrationException {

		String editedDocumentId = activeDocumentComponent.getDocumentUri()
				.getEditedDocumentId();

		OxygenOpenDocumentParmaters openDocumentParameters = activeDocumentComponent
				.getOpenDocumentParameters();

		String moRefId = openDocumentParameters
				.getParameterValue(OxygenOpenDocumentParmatersNames.MO_REFERENCE_ID);

		ICmsURI cmsUri = activeDocumentComponent.getDocumentUri().getCMSUri();
		String hostURI = cmsUri.getHostURI();

		String uri = hostURI
				+ "/rsuite/rest/v1/api/iet.journals.publish.article?"
				+ cmsUri.getSessionKeyParam() + "&rsuiteId[0]=" + editedDocumentId
				+ "&sourceId[0]=" + moRefId + "&" + UUID.randomUUID().toString()
				+ "&type=" + "final";

		HttpUtils.sendGetRequest(uri);
	}

	@Override
	public ArgumentDescriptor[] getArguments() {
		return null;
	}

}
