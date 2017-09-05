package com.rsicms.rsuite.oxygen.iet.applet.extension.framework.dita.operation;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceHandlerDefault;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.LookupMethod;
import com.rsicms.rsuite.editors.oxygen.applet.extension.operations.InsertReferenceOperation;

public class InsertReference extends InsertReferenceOperation {

	@Override
	protected InsertReferenceElement createInsertReferenceElement(AuthorAccess authorAccess) {
		
		
		InsertReferenceElement imageElement = new InsertReferenceElement("xref", "href");
		imageElement.setLookup(LookupMethod.BROWSE);
		
		imageElement.setReferenceHandler(new InsertReferenceHandlerDefault() {
			
			@Override
			public String getXmlFragment(ISelectedReferenceNode selectedNode, String xmlFragment) {
				IReposiotryResource resource = selectedNode.getRepositoryResource();
				xmlFragment = xmlFragment.replace("/>", ">" + resource.getDisplayText() + "</xref>");
				return xmlFragment;
			}
			
			@Override
			public String getLinkValue(ISelectedReferenceNode selectedNode, String linkValue) {
				return linkValue;
			}
		});
		
		return imageElement;
	}

	@Override
	public String getDescription() {
		return "Infert reference";
	}
}
