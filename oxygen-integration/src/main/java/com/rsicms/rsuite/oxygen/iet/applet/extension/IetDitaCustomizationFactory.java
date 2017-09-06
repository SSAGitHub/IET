package com.rsicms.rsuite.oxygen.iet.applet.extension;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.RSuiteDefaultSchemaCustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;

public class IetDitaCustomizationFactory extends RSuiteDefaultSchemaCustomizationFactory {

	

	@Override
	public IDocumentHandler createDocumentHandler(IDocumentURI arg0,
			OxygenOpenDocumentParmaters arg1) {
		return new IetDitaDocumentHandler();
	}


	@Override
	public boolean matchSchema(String arg0) {
		return true;
	}

}
