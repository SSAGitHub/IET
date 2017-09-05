package com.rsicms.rsuite.oxygen.iet.applet.extension.framework.dita.operation;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenComponentRegister;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenEditorContext;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.LookupMethod;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.extension.operations.InsertReferenceOperation;
import com.rsicms.rsuite.oxygen.iet.applet.extension.framework.dita.operation.InsertCrossReference;
			 			 
public class InsertConrefOperation extends InsertReferenceOperation {

	@Override
	public String getDescription() {		
		return "Insert a DITA content reference RSUITE";
	}

	@Override
	protected InsertReferenceElement createInsertReferenceElement(
			AuthorAccess authorAccess) {
		InsertReferenceElement conref = new InsertReferenceElement("", "conref");
		conref.setConfRef(true);
		conref.setLookup(LookupMethod.BROWSE);
		
		OxygenEditorContext context = OxygenComponentRegister
				.getRegisterOxygenEditorContext(authorAccess);
		
		OxygenDocument document = context.getDocumentComponent();
		
		 ICmsURI cmsUri = document.getDocumentUri().getCMSUri();
		
		conref.setTargetNodeWS(cmsUri.getHostURI()
				+ "/rsuite/rest/v1/api/rsuite.oxygen.demo.target.element.list");


		conref.setTargetNodeListStylesheet(InsertCrossReference.getLocalTargetStylesheet(authorAccess));
				
		
		return conref;
	}
}
