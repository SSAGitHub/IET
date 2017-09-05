package org.theiet.maintenance.domain;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.theiet.rsuite.utils.XqueryUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.repository.ComposedXQuery;
import com.reallysi.rsuite.service.ManagedObjectService;

public class Iet763Helper {

	
	public void removeAliasesFromSubMo(RemoteApiExecutionContext context,
			User user, File logFile) throws RSuiteException {
		Map<String, String> variables = new HashMap<String, String>();
		
		ComposedXQuery xqueryObject = XqueryUtils.getXquery("findSubMosWithAlias.xqy",
				variables);
		

		StringBuilder logContent = new StringBuilder();
		
		ManagedObjectService mosvc = context.getManagedObjectService();
		
		String[] results = context.getRepositoryService().queryAsStringArray(xqueryObject);
		
		for (String result : results){
			mosvc.deleteAllAliases(user, result);
			mosvc.refreshSearchView(user, result);
			logContent.append("removed alias for " + result + "\n");
		}

		

		try {
			FileUtils.writeStringToFile(logFile, logContent.toString());
		} catch (IOException e) {
			throw new RSuiteException(0, "Unable to write log file to " + logFile.getAbsolutePath(), e);
		}
	}
	
}
