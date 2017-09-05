package com.rsicms.projectshelper.export;

import java.util.Set;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;

public interface MoExportContainerContext {

	String getContextPath(ManagedObject mo) throws RSuiteException;
	
	String getContextMoVersion(ManagedObject mo) throws RSuiteException;
	
	Set<String> getContextCaId(ManagedObject mo) throws RSuiteException;
	
	void addAdditionalContextInfo(String key, String value);
	
	String getAdditionalContextInfo(String key);
	
	String getContextCaId(); 
}
