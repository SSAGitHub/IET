package com.rsicms.rsuite.oxygen.iet.applet.extension.framework.dita.operation;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceHandlerDefault;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.LookupMethod;
import com.rsicms.rsuite.editors.oxygen.applet.extension.operations.InsertReferenceOperation;

public class InsertTopicrefOperation extends InsertReferenceOperation {

	@Override
	protected InsertReferenceElement createInsertReferenceElement(AuthorAccess authorAccess) {
		
		
		InsertReferenceElement referenceElement = new InsertReferenceElement("topicref", "href");
		referenceElement.setLookup(LookupMethod.BROWSE);
		
		referenceElement.setReferenceHandler(new InsertReferenceHandlerDefault() {
			
			@Override
			public String getXmlFragment(ISelectedReferenceNode selectedNode, String xmlFragment) {
				xmlFragment = xmlFragment.replace("/>", " class=\"- map/topicref \"  />");
				return xmlFragment;
			}
			
			@Override
			public String getLinkValue(ISelectedReferenceNode selectedNode, String linkValue) {				
				return linkValue;
			}
		});
		
		return referenceElement;
	}

	@Override
	public String getDescription() {
		return "Infert Dita topic reference";
	}
}
