package com.rsicms.projectshelper.export.impl.validation;

import java.util.Set;

import org.w3c.dom.Element;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.export.MoExportCrossReferenceValidator;

public class DefaultMoExportCrossReferenceValidator implements
		MoExportCrossReferenceValidator {

	private static final String CROSS_REFERENCE_SEPARATOR = "#";

	@Override
	public Set<String> parseReferenceTargets(ManagedObject referncedMo)
			throws RSuiteException {
		LinkTargetParser linkTargetParser = getLinkTargetParser();

		Element moRootElement = referncedMo.getElement();
		return linkTargetParser.parseTargetValues(moRootElement);

	}

	protected LinkTargetParser getLinkTargetParser() {
		return new DefaultLinkTargetParser();
	}

	@Override
	public String getTargetValueFromLink(String attributeValue) {
		int index = attributeValue.indexOf(CROSS_REFERENCE_SEPARATOR);
		if (index > -1) {
			return attributeValue.substring(index + 1);
		}
		return "";
	}

	@Override
	public boolean isCrossReference(String attributeValue) {
		if (attributeValue.contains(CROSS_REFERENCE_SEPARATOR)) {
			return true;
		}
		return false;
	}

}
