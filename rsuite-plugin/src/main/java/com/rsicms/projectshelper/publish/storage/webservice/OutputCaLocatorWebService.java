package com.rsicms.projectshelper.publish.storage.webservice;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.reallysi.rsuite.api.remoteapi.result.XmlRemoteApiResult;
import com.reallysi.rsuite.service.ManagedObjectService;

/**
 * Custom RSuite web service to save search results to user's clipboard
 *
 */
public class OutputCaLocatorWebService extends DefaultRemoteApiHandler{

    
    private static final String WEB_SERVICE_LABEL="Copy Results to Clipboard";
    
    private static Log log = LogFactory.getLog(OutputCaLocatorWebService.class);
   
    /**
     * Create an MO and save all the search results as a list in the MO
     */
    @Override
    public RemoteApiResult execute(RemoteApiExecutionContext context, CallArgumentList args)
        throws RSuiteException {
        
       try
       {
       Session sess = context.getSession();
       User user = sess.getUser();
       
       String rsuiteId = args.getFirstValue("rsuiteId");
       
       ManagedObjectService moService = context.getManagedObjectService();
       
       String alias =  "output" + rsuiteId;
       String aliasType = "outputSource";
       
       String moId = "";
       
       List<ManagedObject> moList = moService.getObjectsByAlias(user, "output" + rsuiteId);
       
       if (moList.size() > 1){
    	   throw new RSuiteException("Alias " + alias + " type: " + aliasType + " should be unique");
       }
       
       if (moList.size() == 1){
    	   ManagedObject mo = moList.get(0);
    	   moId = mo.getId();
       }
       
       String response = "<mo id='" + moId + "'/>";
       
       
       return new XmlRemoteApiResult(response);
        
      
       }
       catch(Exception e)
       {
           return new MessageDialogResult(MessageType.ERROR, WEB_SERVICE_LABEL, "Error: "+e.getMessage());
       }
                                       
    }

    
    
   

}
