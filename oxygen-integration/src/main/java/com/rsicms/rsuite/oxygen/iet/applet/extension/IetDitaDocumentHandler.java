package com.rsicms.rsuite.oxygen.iet.applet.extension;

import java.io.IOException;

import ro.sync.ecss.extensions.api.AuthorReviewController;
import ro.sync.ecss.extensions.api.component.EditorComponentProvider;
import ro.sync.exml.workspace.api.editor.page.author.WSAuthorComponentEditorPage;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;

public class IetDitaDocumentHandler implements IDocumentHandler {


	@Override
	public void beforeOpenDocument(IDocumentURI arg0,
			OxygenOpenDocumentParmaters arg1, EditorComponentProvider editorComponentProvider) {		
	}

	@Override
	public String modifyDocumentBeforeLoad(String content) throws IOException {
		return content;
	}

	@Override
	public String modifyDocumentBeforeSave(String content) throws IOException {
		return content;
	}

	@Override
	public void afterOpenDocument(IDocumentURI arg0,
			OxygenOpenDocumentParmaters arg1, EditorComponentProvider arg2) {
		
		
		turnOffChangeTrackingForOnix(arg1, arg2);
	}


	protected void turnOffChangeTrackingForOnix(
			OxygenOpenDocumentParmaters arg1, EditorComponentProvider arg2) {
		String schemaId = arg1.getParameterValue(OxygenOpenDocumentParmatersNames.SCHEMA_SYSTEM_ID).toLowerCase();

		WSAuthorComponentEditorPage editorPage = ((WSAuthorComponentEditorPage)arg2.getWSEditorAccess().getCurrentPage());

		AuthorReviewController reviewController = editorPage.getReviewController();
		
		if (reviewController.isTrackingChanges() && schemaId.contains("onix")){
			reviewController.toggleTrackChanges();
		}
	}

}
