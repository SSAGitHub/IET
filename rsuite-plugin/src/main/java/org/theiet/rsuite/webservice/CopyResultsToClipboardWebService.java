package org.theiet.rsuite.webservice;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.Basket;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.content.ContentDisplayObject;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.reallysi.rsuite.api.search.Search;
import com.reallysi.rsuite.api.search.SearchHistory;
import com.reallysi.rsuite.api.search.SearchResultSet;
import com.reallysi.rsuite.service.BasketService;

/**
 * Custom RSuite web service to save search results to user's clipboard
 *
 */
public class CopyResultsToClipboardWebService extends DefaultRemoteApiHandler{

    
    private static final String WEB_SERVICE_LABEL="Copy Results to Clipboard";
    
    private static Log log = LogFactory.getLog(CopyResultsToClipboardWebService.class);
   
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
       BasketService basketSvc = context.getBasketService();
       Basket basket = context.getBasketService().getOrCreateClipboard(user);
   
       SearchHistory searchHist = (SearchHistory) sess.getAttribute("SearchHistory");
       if(searchHist!=null)
       {
           Search search = searchHist.getLastSearch();

           SearchResultSet results = search.getResults();

           ContentDisplayObject item;
           ArrayList<String> basketItems = new ArrayList<String>();
           for(int i = 1; i<= results.getCountResults(); i++)
           {
                item = results.getResult(i);
                basketItems.add(item.getId());
           }
           String[] foo = new String[0];
           basketSvc.addObjectsToBasket(user, basket.getId(), basketItems.toArray(foo));
       }
        
       return new MessageDialogResult(MessageType.SUCCESS, WEB_SERVICE_LABEL, 
               "<html><body><div><p>Results saved to clipboard.</p></div></body></html>", "500");
       }
       catch(Exception e)
       {
           return new MessageDialogResult(MessageType.ERROR, WEB_SERVICE_LABEL, "Error: "+e.getMessage());
       }
                                       
    }

    @Override
    public void initialize(RemoteApiDefinition arg0) {
        // TODO Auto-generated method stub
        
    }
    
   

}
