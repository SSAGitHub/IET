package com.rsicms.projectshelper.utils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public final class ProjectExecutionContextUtils {

    private ProjectExecutionContextUtils(){};
    
    public static String getSkey(ExecutionContext context) throws RSuiteException {
        UserAgent userAgent = new UserAgent("system");
        Session session = context.getSessionService().
        createSession(
                "Realm",
                userAgent,
                "http://" + context.getRSuiteServerConfiguration().getHostName() + 
                ":" + context.getRSuiteServerConfiguration().getPort() +
                "/rsuite/rest/v1",
                context.getAuthorizationService().getSystemUser()
        );
        return session.getKey();
    }
}
