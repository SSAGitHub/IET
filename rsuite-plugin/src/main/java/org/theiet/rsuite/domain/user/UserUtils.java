package org.theiet.rsuite.domain.user;

import org.theiet.rsuite.datamodel.ExternalCompanyUser;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.utils.ProjectUserUtils;

public class UserUtils {

	public static ExternalCompanyUser getExtarnalUser(
			ExecutionContext context,
			String userProp) throws RSuiteException {
		User user = ProjectUserUtils.getUser(context, userProp);
		return new ExternalCompanyUser(context, user);
	}

}
