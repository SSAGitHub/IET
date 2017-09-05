package org.theiet.rsuite.datamodel;

import java.util.Map;

import org.theiet.rsuite.IetConstants;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.UserType;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.security.ExecPermission;
import com.reallysi.rsuite.api.security.Role;
import com.rsicms.projectshelper.utils.ProjectUserUtils;

public class ExternalCompanyUser implements User, IetConstants {


	private ExecutionContext context;

	private User user;

	public ExternalCompanyUser(ExecutionContext context, String userId) throws RSuiteException {
		super();
		this.context = context;
		this.user = ProjectUserUtils.getUser(context, userId);
	}
	
		
	public ExternalCompanyUser(ExecutionContext context, User user) {
		super();
		this.context = context;
		this.user = user;
	}

	@Override
	public String getName() {
		return user.getName();
	}

	@Override
	public boolean hasRole(String roleName) {
		return user.hasRole(roleName);
	}

	@Override
	public Role[] getRoles() {
		return user.getRoles();
	}

	@Override
	public boolean hasExecPermission(ExecPermission perm) {
		return user.hasExecPermission(perm);
	}

	@Override
	public ExecPermission[] getExecPermissions() {
		return user.getExecPermissions();
	}

	@Override
	public String getUserId() {
		return user.getUserId();
	}

	@Override
	public String getUserID() {
		return user.getUserID();
	}

	@Override
	public boolean isLocalUser() {
		return user.isLocalUser();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUserDN() {
		return user.getUserDN();
	}

	@Override
	public String getFullName() {
		return user.getFullName();
	}

	@Override
	public String[] getGroups() {
		return user.getGroups();
	}

	@Override
	public String getEmail() {
		return user.getEmail();
	}
	
	public boolean hasExecPermission(String value){
		return true;
	}

	public String getCompanyName(){
		return user.getFullName();
	}
	
	public String getContactFirstName() throws RSuiteException{
		
		Map<String, String> userProperties = context.getUserService().getUserPropertiesCatalog().getProperties(user.getUserId());
		String firstName = userProperties.get(USER_PROP_CONTACT_FIRST_NAME);
		
		return firstName == null ? "" : firstName;
	}


	@Override
	public UserType getUserType() {
		return user.getUserType();
	}

}
