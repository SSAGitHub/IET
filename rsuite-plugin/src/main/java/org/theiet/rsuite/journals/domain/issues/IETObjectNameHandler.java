package org.theiet.rsuite.journals.domain.issues;

import com.reallysi.rsuite.api.ContentAssemblyNodeContainer;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.helpers.download.RSuiteObjectNameHandler;

public class IETObjectNameHandler implements RSuiteObjectNameHandler {

	@Override
	public String getName(ExecutionContext context, ManagedObject mo, String defaultname) throws RSuiteException {
		if (!mo.isNonXml()){
			return mo.getDisplayName() + ".xml"; 
		}
		
		return mo.getDisplayName();
	}

	@Override
	public String getName(ExecutionContext context, ContentAssemblyNodeContainer caNodeContainer, String defaultname) throws RSuiteException {
		return defaultname;
	}

}
