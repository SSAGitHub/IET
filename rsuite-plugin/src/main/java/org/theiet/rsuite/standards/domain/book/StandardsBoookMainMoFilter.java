package org.theiet.rsuite.standards.domain.book;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.contenttypes.dita.DitaUtils;
import com.rsicms.projectshelper.contenttypes.jatsv1.JatsUtils;
import com.rsicms.projectshelper.utils.browse.filters.ChildMoFilter;

public class StandardsBoookMainMoFilter implements ChildMoFilter {

	private ExecutionContext context;

	public StandardsBoookMainMoFilter(ExecutionContext context) {
		this.context = context;
	}

	@Override
	public boolean accept(ManagedObject mo) throws RSuiteException {	    
		return DitaUtils.isDitaMap(context, mo) || JatsUtils.isJatsArticle(mo);
	}

}
