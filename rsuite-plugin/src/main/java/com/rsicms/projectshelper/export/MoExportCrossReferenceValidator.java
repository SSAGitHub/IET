package com.rsicms.projectshelper.export;

import java.util.Set;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;

public interface MoExportCrossReferenceValidator {

	Set<String> parseReferenceTargets(ManagedObject referncedMo) throws RSuiteException;
	
	String getTargetValueFromLink(String attributeValue);
	
	boolean isCrossReference(String attributeValue);
}
