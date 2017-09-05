package com.rsicms.projectshelper.utils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.service.ManagedObjectService;

public class ProjectMoUtils {

	private ProjectMoUtils(){
	}
	
    public static boolean isContainerMo(ManagedObject mo) throws RSuiteException {
        if (mo.getObjectType() == ObjectType.CONTENT_ASSEMBLY || mo.getObjectType() == ObjectType.CONTENT_ASSEMBLY_NODE) {
            return true;
        }
        return false;
    }
    
	public static boolean isXMLMo(ManagedObject mo) throws RSuiteException {
		if (mo.getObjectType() == ObjectType.MANAGED_OBJECT) {
			return true;
		}
		return false;
	}
	
	public static String getIdWithRevision(ManagedObject mo) throws RSuiteException{
	    String fullId = mo.getId();	    
	    String moRevision = mo.getRevision();
	    
	    moRevision = moRevision == null ? mo.getVersionHistory().getCurrentVersionEntry().getRevisionNumber() +  " (latest)" : moRevision; 
	    
	    fullId += " version: " + moRevision;
	    
	    return fullId;
	}

	public static String buildFileLinkToMo (ManagedObjectService moService, User user, String rsuiteURL, String moFileId) throws RSuiteException {	
		String moId = moService.getContentDisplayObject(user, moFileId).getId();
		String fileLink = rsuiteURL + "/rsuite-cms/inspect/" + moId;
		return fileLink;
	}
	
	public static String getRealMoId(ManagedObject mo) throws RSuiteException{
		if (mo.getTargetId() != null){
			return mo.getTargetId();
		}
		
		return mo.getId();
	}
}
