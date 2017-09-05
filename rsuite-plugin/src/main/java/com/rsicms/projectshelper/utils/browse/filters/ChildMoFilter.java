package com.rsicms.projectshelper.utils.browse.filters;

import com.reallysi.rsuite.api.*;

public interface ChildMoFilter{
	boolean accept(ManagedObject mo) throws RSuiteException;
}