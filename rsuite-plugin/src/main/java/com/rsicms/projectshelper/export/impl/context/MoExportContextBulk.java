package com.rsicms.projectshelper.export.impl.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.export.MoExportContainerContext;

public class MoExportContextBulk implements MoExportContainerContext {

	private List<MoExportContainerContext> contextsList = new ArrayList<MoExportContainerContext>();

	private Map<String, String> additionalInfo = new HashMap<String, String>();
	
	public MoExportContextBulk(List<MoExportContainerContext> contextsList) {
		this.contextsList = contextsList;
	}

	@Override
	public String getContextPath(ManagedObject mo) throws RSuiteException {
		String contextPath = null;
		
		for (MoExportContainerContext context : contextsList){
			contextPath = context.getContextPath(mo);
			
			if (contextPath != null){
				break;
			}
		}
		
		return contextPath;
	}

	@Override
	public String getContextMoVersion(ManagedObject mo) throws RSuiteException {
		String moVersion = null;
		
		for (MoExportContainerContext context : contextsList){
			moVersion = context.getContextMoVersion(mo);
			
			if (moVersion != null){
				break;
			}
		}
		
		return moVersion;
	}

	@Override
	public Set<String> getContextCaId(ManagedObject mo) throws RSuiteException {
		
		Set<String> contexCaIds = new HashSet<String>();
		
		for (MoExportContainerContext context : contextsList){
			contexCaIds.addAll(context.getContextCaId(mo));
		}

		return contexCaIds;
	}

	@Override
	public void addAdditionalContextInfo(String key, String value) {
		additionalInfo.put(key, value);		
	}

	@Override
	public String getAdditionalContextInfo(String key) {
		return additionalInfo.get(key);
	}

	@Override
	public String getContextCaId() {
		return null;
	}

	
	
}
