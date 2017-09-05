package org.theiet.maintenance.webservice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.rsicms.rsuite.helpers.webservice.XmlRemoteApiResult;

/**
 * Custom RSuite web service to save search results to user's clipboard
 *
 */
public class MaintenanceTemplateWS extends DefaultRemoteApiHandler{

    
    private Log log = LogFactory.getLog(this.getClass());
   
    /**
     * Create an MO and save all the search results as a list in the MO
     */
    @Override
    public RemoteApiResult execute(RemoteApiExecutionContext context, CallArgumentList args)
        throws RSuiteException {
        
              
       Session sess = context.getSession();
       User user = sess.getUser();
       
       return new XmlRemoteApiResult("<result />");
       
                                       
    }

}
