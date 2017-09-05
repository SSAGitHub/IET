package org.theiet.rsuite.iettv.actionhandlers.delivery.crossref;

import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;

public enum IetTvCrossRefWorflowVariables implements RSuiteWorkflowVariable {
	CROSS_REF_PATH;

	@Override
	public String getVariableName() {
		return this.toString().toLowerCase();
	}

	
}
