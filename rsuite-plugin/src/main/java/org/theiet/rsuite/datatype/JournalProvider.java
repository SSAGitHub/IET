package org.theiet.rsuite.datatype;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.utils.XpathUtils;

import com.reallysi.rsuite.api.DataTypeOptionValue;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.forms.DataTypeProviderOptionValuesContext;
import com.reallysi.rsuite.api.forms.DefaultDataTypeOptionValuesProviderHandler;
import com.reallysi.rsuite.service.SearchService;

public class JournalProvider extends DefaultDataTypeOptionValuesProviderHandler {
	
	public void provideOptionValues(
			DataTypeProviderOptionValuesContext context,
			List<DataTypeOptionValue> optionValues) throws RSuiteException {
		Log log = LogFactory.getLog(JournalProvider.class);
		User user = context.getSession().getUser();
		String journalQuery = getJournalQuery();
		journalQuery = XpathUtils.resolveRSuiteFunctionsInXPath(journalQuery);
		SearchService srchSvc = context.getSearchService();
		List<ManagedObject> caSet = srchSvc.executeXPathSearch(user,
				journalQuery, 1, 0);
		int n = caSet.size();
		log.info("provideOptionValues: query " + journalQuery + " returned " + n);
		optionValues.add(new DataTypeOptionValue("All", "All"));
		TreeMap<String, String> journalMap = new TreeMap<String, String>();
		for (ManagedObject ca: caSet) {
			journalMap.put(ca.getDisplayName(), ca.getId());
		}
		Set<String> keySet = journalMap.keySet();
		for (String displayName : keySet) {
			String caId = journalMap.get(displayName);
			optionValues.add(new DataTypeOptionValue(caId, displayName));
		}
	}

	protected String getJournalQuery () {
		return "/rs_ca_map/rs_ca[rmd:get-type(.) = 'journal']";
	}
}