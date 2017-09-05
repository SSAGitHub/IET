package com.rsicms.projectshelper.export;

import java.util.Map;
import java.util.Set;

import com.rsicms.projectshelper.datatype.ManagedObjectWrapper;
import com.rsicms.projectshelper.export.impl.cache.*;

public class ProcessResult {

	private Set<ManagedObjectWrapper> moToProcess;

	private Map<String, String> exportPathMap;

	private ReferenceTargetValueCache referenceTargetValuesCache;

	public ProcessResult(Set<ManagedObjectWrapper> moToProcess,
			MoExportCache moExportCache) {
		this.moToProcess = moToProcess;
		this.exportPathMap = moExportCache.getExportPathCache();
		this.referenceTargetValuesCache = moExportCache
				.getReferenceTargetValuesCache();
	}

	public Set<ManagedObjectWrapper> getMoToProcess() {
		return moToProcess;
	}

	public Map<String, String> getExportPathMap() {
		return exportPathMap;
	}

	public ReferenceTargetValueCache getReferenceTargetValuesCache() {
		return referenceTargetValuesCache;
	}

}
