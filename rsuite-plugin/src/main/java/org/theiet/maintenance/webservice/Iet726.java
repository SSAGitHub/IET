package org.theiet.maintenance.webservice;

import java.util.List;

import org.theiet.rsuite.advisors.display.ContentDisplayCASorterConfig;
import org.theiet.rsuite.advisors.display.ContentDisplayCASorterConfigurationFactory;
import org.theiet.rsuite.advisors.display.ContentDisplayCaSorter;
import org.theiet.rsuite.advisors.display.ContentDisplaySorter;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.utils.SearchUtils;
import org.theiet.rsuite.utils.XpathUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.content.ContentDisplayObject;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.XmlRemoteApiResult;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.reallysi.rsuite.service.SearchService;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;

/**
 * Custom RSuite web service to save search results to user's clipboard
 * 
 */
public class Iet726 extends DefaultRemoteApiHandler implements BooksConstans, JournalConstants {

	/**
	 * 
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		Session sess = context.getSession();
		User user = sess.getUser();
		StringBuffer result = new StringBuffer("<result>");
		
		sortBooks(context, user);
		
		return new XmlRemoteApiResult(result.append("</result>").toString());

	}
	
	private void sortBooks (ExecutionContext context, User user) throws RSuiteException {
		ContentAssemblyService caServ = context.getContentAssemblyService();
		ManagedObjectService moServ = context.getManagedObjectService();
		SearchService srchSvc = context.getSearchService();
		
		String booksCAQuery = "/rs_ca_map/rs_ca[rmd:get-type(.) = 'booksCA']";
		booksCAQuery = XpathUtils.resolveRSuiteFunctionsInXPath(booksCAQuery);
		

		int maxResult = 200;
		
		List<ManagedObject> booksContainerlList = SearchUtils.executeXpathSearch(user, booksCAQuery, srchSvc, maxResult);
		
		for (ManagedObject booksContainerMO : booksContainerlList) {				
			ContentAssembly caParent = caServ.getContentAssembly(user, booksContainerMO.getId());
			String uri = moServ.getDependencyTracker().listDirectReferences(context.getAuthorizationService().getSystemUser(),caParent.getId()).get(0).getBrowseUri();
			List<ContentDisplayObject> collection = moServ.getContainerDisplayObjects(user, caParent.getId(), uri);
			relocateContainers(context, user, caParent, collection);
		}
	}
	

	
	private void relocateContainers (ExecutionContext context, User user, ContentAssembly caParent, List<ContentDisplayObject> collection) throws RSuiteException {
		if (collection.size() > 0) {			
			ContentDisplayCASorterConfig caSorterConfig = ContentDisplayCASorterConfigurationFactory.getSortingConfiguration(caParent);			
			ContentDisplayCaSorter cdCaSorter = new ContentDisplayCaSorter(collection);
			cdCaSorter.sort(caSorterConfig.getSortCaBy());
			
			for (int i = 0; i < collection.size(); i++) {
				ContentAssembly ca = ProjectContentAssemblyUtils.getCAFromMO(context, user, collection.get(i).getId());
				ContentDisplaySorter.relocateCA(context, ca, caParent, i);
			}
		}
	}

	
}
