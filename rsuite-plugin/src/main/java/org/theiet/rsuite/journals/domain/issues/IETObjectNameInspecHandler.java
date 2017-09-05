package org.theiet.rsuite.journals.domain.issues;

import com.reallysi.rsuite.api.ContentAssemblyNodeContainer;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.helpers.download.RSuiteObjectNameHandler;

public class IETObjectNameInspecHandler implements RSuiteObjectNameHandler {

	private String articleId;
	
	public IETObjectNameInspecHandler(String articleId) {
		super();
		this.articleId = articleId;
	}

	@Override
	public String getName(ExecutionContext context, ManagedObject mo, String defualtName) throws RSuiteException {
		if (!mo.isNonXml()){
			return articleId + ".xml"; 
		}
		
		return mo.getDisplayName();
	}

	@Override
	public String getName(ExecutionContext context, ContentAssemblyNodeContainer caNodeContainer, String defualtName) throws RSuiteException {
		return defualtName;
	}

}
