package com.rsicms.projectshelper.export.impl.cache;

import java.util.Map;


public class MoExportCache {

	private Map<String, String> exportPathCache;

	private ReferenceTargetValueCache referenceTargetValuesCache;

	public MoExportCache(Map<String, String> exportPathCache,
	        ReferenceTargetValueCache referenceTargetValuesCache) {
		this.exportPathCache = exportPathCache;
		this.referenceTargetValuesCache = referenceTargetValuesCache;
	}

	public Map<String, String> getExportPathCache() {
		return exportPathCache;
	}

	public ReferenceTargetValueCache getReferenceTargetValuesCache() {
		return referenceTargetValuesCache;
	}
	
	public void addExportPaths(Map<String, String> newExportPaths){
	    exportPathCache.putAll(newExportPaths);
	}

}
