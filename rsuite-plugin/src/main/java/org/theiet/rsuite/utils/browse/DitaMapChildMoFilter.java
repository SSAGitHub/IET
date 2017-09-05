package org.theiet.rsuite.utils.browse;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.contenttypes.dita.DitaUtils;
import com.rsicms.projectshelper.utils.browse.filters.ChildMoFilter;

public class DitaMapChildMoFilter implements ChildMoFilter {

	private ExecutionContext context;

	public DitaMapChildMoFilter(ExecutionContext context) {
		this.context = context;
	}

	@Override
	public boolean accept(ManagedObject mo) throws RSuiteException {
		return DitaUtils.isDitaMap(context, mo);
	}

}
