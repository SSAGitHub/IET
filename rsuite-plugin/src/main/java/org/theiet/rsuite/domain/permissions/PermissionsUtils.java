package org.theiet.rsuite.domain.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.theiet.rsuite.standards.StandardsBooksConstans;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.security.ACE;
import com.reallysi.rsuite.api.security.ACL;
import com.reallysi.rsuite.api.security.ContentPermission;
import com.reallysi.rsuite.api.security.Role;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.reallysi.rsuite.service.SecurityService;

public class PermissionsUtils {
	
	public static ACL constructAdminOnlyDeleteACL(SecurityService secSvc) throws RSuiteException {
		Role anyRole = secSvc.constructRole(Role.ROLE_NAME_ANY);
		Role adminRole = secSvc.constructRole(Role.ROLE_NAME_RSUITE_ADMINISTRATOR);
		Set<ContentPermission> anyPermissions = ContentPermission.constructSetForAllPermissions();
		anyPermissions.remove(ContentPermission.DELETE);
		anyPermissions.remove(ContentPermission.ADMIN);
		Set<ContentPermission> adminPermissions = ContentPermission.constructSetForAllPermissions();
		ACE[] aces = new ACE[2];
		aces[0] = secSvc.constructACE(anyRole, anyPermissions);
		aces[1] = secSvc.constructACE(adminRole, adminPermissions);
		return secSvc.constructACL(aces);
	}
	
	public static void setAdminOnlyDeleteACL(SecurityService secSvc,
			User user,
			String caId) throws RSuiteException {
		setAdminOnlyDeleteACL(secSvc, user, caId, null);
	}
	
	public static void setAdminOnlyDeleteACL(SecurityService secSvc,
			User user,
			String caId,
			Log log) throws RSuiteException {
		ACL adminOnlyDeleteACL = constructAdminOnlyDeleteACL(secSvc);
		secSvc.setACL(user, caId, adminOnlyDeleteACL);
	}
	
	public static ACL constructDefaultACL(SecurityService secSvc) throws RSuiteException {
		Role anyRole = secSvc.constructRole(Role.ROLE_NAME_ANY);
		Set<ContentPermission> anyPermissions = ContentPermission.constructSetForAllPermissions();
		ACE[] aces = new ACE[1];
		aces[0] = secSvc.constructACE(anyRole, anyPermissions);
		return secSvc.constructACL(aces);
	}

	public static void setDefaultACL(SecurityService secSvc,
			User user,
			String caId) throws RSuiteException {
		setDefaultACL(secSvc, user, caId, null);
	}
	
	public static void setDefaultACL(SecurityService secSvc,
			User user,
			String caId,
			Log log) throws RSuiteException {
		ACL defaultACL = constructDefaultACL(secSvc);		
		secSvc.setACL(user, caId, defaultACL);		
	}
	
	public static ACL constructAuthorizedRoleACL(SecurityService secSvc, String authorizedRoleName) throws RSuiteException {
		Role anyRole = secSvc.constructRole(Role.ROLE_NAME_ANY);		
		Role stndardsBookAdminRole = secSvc.constructRole(authorizedRoleName);
		Role adminRole = secSvc.constructRole(Role.ROLE_NAME_RSUITE_ADMINISTRATOR);
		
		Set<ContentPermission> anyPermissions = ContentPermission.constructSetForAllPermissions();
		anyPermissions.remove(ContentPermission.LIST);
		anyPermissions.remove(ContentPermission.EDIT);
		anyPermissions.remove(ContentPermission.DELETE);
		anyPermissions.remove(ContentPermission.COPY);
		anyPermissions.remove(ContentPermission.REUSE);
		anyPermissions.remove(ContentPermission.ADMIN);
		
		Set<ContentPermission> stndardsBookAdminPermissions = ContentPermission.constructSetForAllPermissions();
		stndardsBookAdminPermissions.remove(ContentPermission.DELETE);		
		stndardsBookAdminPermissions.remove(ContentPermission.ADMIN);
		
		Set<ContentPermission> adminPermissions = ContentPermission.constructSetForAllPermissions();
		List<ACE> aces = new ArrayList<ACE>();
		aces.add(secSvc.constructACE(anyRole, anyPermissions));
		aces.add(secSvc.constructACE(stndardsBookAdminRole, stndardsBookAdminPermissions));
		aces.add(secSvc.constructACE(adminRole, adminPermissions));
		return secSvc.constructACL(aces.toArray(new ACE[aces.size()]));
	}
	
	public static void setStandarsContainerACL(ManagedObjectService moSvc, SecurityService secSvc,
			User user,
			String moId,
			Log log) throws RSuiteException {
		ACL adminOnlyDeleteACL = constructAuthorizedRoleACL(secSvc, StandardsBooksConstans.ROLE_STANDARDS_BOOK_AUTHORIZED_USER);		
		setACLOnMo(moSvc, secSvc, user, moId, adminOnlyDeleteACL);		
	}
	
	   public static void setIetTvACL(ManagedObjectService moSvc, SecurityService secSvc,
	            User user,
	            String moId,
	            Log log) throws RSuiteException {
	        ACL adminOnlyDeleteACL = constructAuthorizedRoleACL(secSvc, StandardsBooksConstans.ROLE_IET_TV_AUTHORIZED_USER);        
	        
	        setACLOnMo(moSvc, secSvc, user, moId, adminOnlyDeleteACL);
	        
	    }

    private static void setACLOnMo(ManagedObjectService moSvc, SecurityService secSvc, User user,
            String moId, ACL acl) throws RSuiteException {
        ManagedObject mo = moSvc.getManagedObject(user, moId);
		if (StringUtils.isNotBlank(mo.getTargetId())){
			moId = mo.getTargetId();
		}
		
		secSvc.setACL(user, moId, acl);
    }

}