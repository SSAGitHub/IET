package com.rsicms.projectshelper.utils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.AuthorizationService;

public class ProjectUserUtils {

	private ProjectUserUtils(){
	}
	
    /**
     * Obtains a user with given id
     * 
     * @param context
     *            Execution context
     * @param userId
     *            The rsuite user id
     * @return user user object
     * @throws RSuiteException
     */
    public static User getUser(ExecutionContext context, String userId)
    		throws RSuiteException {
    
    	AuthorizationService authorizationSvc = context
    			.getAuthorizationService();
    
    	User user = authorizationSvc.findUser(userId);
    
    	if (user == null) {
    		throw new RSuiteException("Unable to find user with id: " + userId);
    	}
    
    	return user;
    }

    /**
     * Obtains a email for given users
     * 
     * @param context
     *            Execution context
     * @param userId
     *            The rsuite user id
     * @return user email address
     * @throws RSuiteException
     */
    public static String getUserEmail(ExecutionContext context, String userId)
    		throws RSuiteException {
    	return getUser(context, userId).getEmail();
    }
    
    public static boolean isSystemUser(ExecutionContext context, User user) throws RSuiteException {
    	User systemUser = context.getAuthorizationService().getSystemUser();
    	
    	if (systemUser.getUserId().equals(user.getUserId())){
    		return true;
    	}
    	
    	return false;
    }

}
