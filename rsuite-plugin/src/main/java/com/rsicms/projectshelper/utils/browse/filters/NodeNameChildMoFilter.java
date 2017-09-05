package com.rsicms.projectshelper.utils.browse.filters;

import java.util.HashSet;
import java.util.Set;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;

public class NodeNameChildMoFilter implements ChildMoFilter {

    private Set<String> nodeNames = new HashSet<>();
    
    public NodeNameChildMoFilter(String... nodeNames) {
    	for (String nodeName : nodeNames){
    		this.nodeNames.add(nodeName);
    	}
    }

    @Override
    public boolean accept(ManagedObject mo) throws RSuiteException {
        if (nodeNames.contains(mo.getLocalName())) {
            return true;
        }
        return false;
    }

}
