package com.rsicms.projectshelper.utils.browse;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class ProjectBrowserHelper {

	private ExecutionContext context;
	
	public ProjectBrowserHelper(ExecutionContext context) {
		super();
		this.context = context;
	}


	public ManagedObject getChildMoByNodeName(
			ContentAssembly contextCa, final String... nodeNames) throws RSuiteException {
		
		return ProjectBrowserUtils.getChildMoByNodeName(context, contextCa, nodeNames);
	}	
}
