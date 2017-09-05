package com.rsicms.rsuite.oxygen.iet.applet.extension.framework.dita;

import ro.sync.ecss.extensions.api.node.AuthorElement;
import ro.sync.ecss.extensions.commons.id.GenerateIDElementsInfo;
import ro.sync.ecss.extensions.dita.id.DITAUniqueAttributesRecognizer;

public class IetDITAUniqueAttributesRecognizer extends
		DITAUniqueAttributesRecognizer {

	@Override
	protected GenerateIDElementsInfo getDefaultOptions() {
		GenerateIDElementsInfo generateInfo = super.getDefaultOptions();
		return generateInfo;
	}

	@Override
	protected String getGenerateIDAttributeQName(AuthorElement element,
			String[] arg1, boolean arg2) {
		
		String id = super.getGenerateIDAttributeQName(element, arg1, arg2);

		if ("regulation".equalsIgnoreCase(element.getName())) {
			id = null;
		}
		
		return id;
	}
}
