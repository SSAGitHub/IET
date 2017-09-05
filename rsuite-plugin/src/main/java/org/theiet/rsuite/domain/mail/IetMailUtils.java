package org.theiet.rsuite.domain.mail;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.utils.ProjectPluginProperties;

public class IetMailUtils {

	private static final String PROP_DEFAULT_MAIL_FROM = "iet.journals.mail.default.from";
	
	public static String obtainEmailFrom() throws RSuiteException {
		return  ProjectPluginProperties.getProperty(
				PROP_DEFAULT_MAIL_FROM, "ietdam@theiet.org");
	}

}
