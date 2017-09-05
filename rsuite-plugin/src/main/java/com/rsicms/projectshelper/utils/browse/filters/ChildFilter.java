package com.rsicms.projectshelper.utils.browse.filters;

import com.reallysi.rsuite.api.*;

public interface ChildFilter{
	boolean accept(ContentAssembly ca) throws RSuiteException;
}