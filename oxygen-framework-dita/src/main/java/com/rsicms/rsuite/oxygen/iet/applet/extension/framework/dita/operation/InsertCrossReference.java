package com.rsicms.rsuite.oxygen.iet.applet.extension.framework.dita.operation;

import static com.rsicms.rsuite.editors.oxygen.applet.common.utils.FrameworkUtils.getFileFromFrameworkResourceFolder;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenComponentRegister;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenEditorContext;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceHandlerDefault;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.LookupMethod;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.extension.operations.InsertReferenceOperation;

public class InsertCrossReference extends InsertReferenceOperation {

	private static Logger logger = Logger.getLogger(InsertCrossReference.class);
	
	@Override
	protected InsertReferenceElement createInsertReferenceElement(
			final AuthorAccess authorAccess) {

		final InsertReferenceElement xrefElement = new InsertReferenceElement(
				"xref", "href");
		xrefElement.setCrossReference(true);
		xrefElement.setLookup(LookupMethod.BROWSE);

		OxygenEditorContext context = OxygenComponentRegister
				.getRegisterOxygenEditorContext(authorAccess);

		OxygenDocument document = context.getDocumentComponent();

		ICmsURI cmsUri = document.getDocumentUri().getCMSUri();

		xrefElement.setTargetNodeWS(cmsUri.getHostURI()
				+ "/rsuite/rest/v1/api/rsuite.oxygen.demo.target.element.list");

		String stylesheetContent = getLocalTargetStylesheet(authorAccess);

		xrefElement.setTargetNodeListStylesheet(stylesheetContent);
		xrefElement.setReferenceHandler(new InsertReferenceHandlerDefault() {

			@Override
			public String getXmlFragment(ISelectedReferenceNode selectedNode,
					String xmlFragment) {

				return xmlFragment;
			}

			@Override
			public String getLinkValue(ISelectedReferenceNode selectedNode,
					String linkValue) {
				return linkValue;
			}
		});

		return xrefElement;
	}

	
	public static String getLocalTargetStylesheet(final AuthorAccess authorAccess) {

		String stylesheetContent = null;
		try {
			InputStream d4p2targetListStylesheet = getFileFromFrameworkResourceFolder(
					authorAccess, "dita", "d4p2targetList.xsl");

			stylesheetContent = IOUtils.toString(d4p2targetListStylesheet);
		} catch (IOException e) {
			OxygenUtils.handleException(logger, e);
		}
		return stylesheetContent;
	}

	@Override
	public String getDescription() {
		return "Infert cross reference";
	}

}
