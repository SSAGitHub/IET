package org.theiet.maintenance.webservice;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.theiet.rsuite.utils.XpathUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.pubtrack.Process;
import com.reallysi.rsuite.api.pubtrack.ProcessMetaDataItem;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.service.PubtrackManager;
import com.reallysi.rsuite.service.SearchService;
import com.rsicms.rsuite.helpers.webservice.XmlRemoteApiResult;

/**
 * Custom RSuite web service to save search results to user's clipboard
 * 
 */
public class ArticlePubtrackAddObjectId extends DefaultRemoteApiHandler {


	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		Session sess = context.getSession();
		User user = sess.getUser();
		PubtrackManager ptMgr = context.getPubtrackManager();
		
		JbpmContext jbpmContext = JbpmConfiguration.getInstance()
				.createJbpmContext();
		
		SearchService searchScv = context.getSearchService();
		
		String resultWs = "";
int counter = 0;

int removeCounter = 0;

		
		try {
			String queryString = getSqlQuery();

			Query query = jbpmContext.getSession().createQuery(queryString);

			Iterator reslutIterator = query.list().iterator();
			
			while (reslutIterator.hasNext()) {
				String pubtrackProcessId = String.valueOf(((Long) reslutIterator.next()));
				Process process = ptMgr.getProcess(user, pubtrackProcessId);
				String rxs = createRXSSearchForArticle(process);
				
				rxs = XpathUtils.resolveRSuiteFunctionsInXPath(rxs);
				
				
				List<ManagedObject> result = searchScv.executeXPathSearch(user,
						rxs, 1, 0);
				
				if (result.size() == 1){
					addObjectIdToProcess(user, ptMgr, process, result);
					counter++;
				}else if (result.size() == 0){
					removeCounter++;
					ptMgr.removeProcess(user, pubtrackProcessId);
				}
				

				
				
				 
			}
			

		} finally {
			jbpmContext.close();
		}

		resultWs += "modified: " + counter + " processes" + ". Removed " + removeCounter + " processes for missing ca articles";

		
		return new XmlRemoteApiResult("<result>" + resultWs + "</result>");

	}

	public String createRXSSearchForArticle(Process process) {
		String externalId = process.getExternalId();
		
		String articleId = externalId.replace("IET_ARTICLE_", "");
		
		
		String rxs = "/rs_ca_map/rs_ca[rmd:get-type(.) = 'article' and rmd:get-display-name(.) = '" + articleId + "']";
		return rxs;
	}

	public void addObjectIdToProcess(User user, PubtrackManager ptMgr,
			Process process, List<ManagedObject> result) throws RSuiteException {
		String objectId = result.get(0).getId();
		Set metaData = process.getMetaData();
		
		ProcessMetaDataItem item = new ProcessMetaDataItem();
		item.setProcess(process);
		item.setName("OBJECT_ID");
		item.setValue(objectId);
		
		metaData.add(item);
		process.setMetaData(metaData);
		
		ptMgr.updateProcess(user, process);
	}

	public String getSqlQuery() {
		String queryString = "SELECT p.id FROM com.reallysi.rsuite.api.pubtrack.Process p" +

		" WHERE p.id NOT IN (" +

		" select pm.process" +

		" FROM com.reallysi.rsuite.api.pubtrack.ProcessMetaDataItem pm" +

		" where pm.name = 'OBJECT_ID'" +

		") and p.name='IET_ARTICLE'" +

		" and p.dtCompleted	 is not null" +

		" and p.externalId LIKE 'IET_ARTICLE%' order by dt_started";
		return queryString;
	}

}
