package org.theiet.rsuite.oxygen;

import java.util.ArrayList;
import java.util.List;

import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.OxygenIntegrationAdvisorDefault;

public class IetOxygenAppletAdvisor extends OxygenIntegrationAdvisorDefault {

	
	@Override
	public List<String> getCustomJars() {
		List<String> frameworkJar = new ArrayList<String>();
		frameworkJar.add("oxygen/frameworks.zip.jar");
		frameworkJar.add("oxygen/iet-oxygen.jar");
		return frameworkJar;
	}
	
	@Override
	public String getOxygenCustomizationClass() {
		return "com.rsicms.rsuite.oxygen.iet.applet.extension.IetOxygenCustomizationFactory";
	}
	
	@Override
	protected boolean addMathFlowSupport() {
		return true;
	}
}
