package com.rsicms.projectshelper.utils;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ContentAssemblyCreateOptions;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.rsicms.projectshelper.datatype.RSuiteCaType;

public class ProjectCaUtils {

	private ProjectCaUtils(){
	};
	
    public static ContentAssembly createContentAssembly(ExecutionContext context, String parentId, String displayName) throws RSuiteException{
        return createContentAssembly(context, parentId, displayName, "");
    }

    public static ContentAssembly createContentAssembly(ExecutionContext context, String parentId, String displayName, RSuiteCaType type) throws RSuiteException{
        return createContentAssembly(context, parentId, displayName, getTypeName(type));
    }

	private static String getTypeName(RSuiteCaType type) {
		return type == null ? null : type.getTypeName();
	}
    
    public static ContentAssembly createContentAssemblySilently(ExecutionContext context, String parentId, String displayName, RSuiteCaType type) throws RSuiteException{
    	ContentAssemblyCreateOptions options = new ContentAssemblyCreateOptions();
    	options.setSilentIfExists(true);
        return createContentAssembly(context, parentId, displayName, getTypeName(type), options);
    }

    public static ContentAssembly createContentAssembly(ExecutionContext context, String parentId, String displayName, String type) throws RSuiteException{
        ContentAssemblyCreateOptions options = new ContentAssemblyCreateOptions();
        return createContentAssembly(context, parentId, displayName, type, options);
    }
    
    private static ContentAssembly createContentAssembly(ExecutionContext context, String parentId, String displayName, String type, ContentAssemblyCreateOptions options) throws RSuiteException{
        User user = context.getAuthorizationService().getSystemUser();
        ContentAssemblyService caService = context.getContentAssemblyService();
        
        if (StringUtils.isNotBlank(type)){
            options.setType(type);
        }
        
        return caService.createContentAssembly(user, parentId, displayName,options);
    }
    
    public static ManagedObject toMO(ExecutionContext context, User user, ContentAssembly ca) throws RSuiteException{
    	return context.getManagedObjectService().getManagedObject(user, ca.getId());
    }
}
