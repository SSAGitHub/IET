package com.rsicms.projectshelper.export.impl.cache;

import java.util.*;

public class ReferenceTargetValueCache {

    private Map<String, Set<String>> referenceTargetValuesCache = new HashMap<String, Set<String>>();
    
    
    public Set<String> getTargetValuesForMo(String moId){
        return referenceTargetValuesCache.get(moId);
    }
    
    public void cacheTargetValuesForMo(String moId, Set<String> targetValues){
        
        Set<String> targetValuesToAdd =  targetValues;
        if (targetValuesToAdd == null){
            targetValuesToAdd = new HashSet<String>();
        }
        
        referenceTargetValuesCache.put(moId, targetValuesToAdd);
    }
    
    public void addCache(ReferenceTargetValueCache cache){
        referenceTargetValuesCache.putAll(cache.referenceTargetValuesCache);
    }
    
}
