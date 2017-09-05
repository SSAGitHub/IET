package com.rsicms.projectshelper.export.impl.context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.export.MoExportContainerContext;

public class MoExportContextImpl implements MoExportContainerContext{

	private String caId;
	
	private Map<String, String> moIdToMoVersionMapping = new HashMap<String, String>();
	
	private Map<String, String> moIdToPathMapping = new HashMap<String, String>();
	
	private Map<String, String> additionalInfo = new HashMap<String, String>();
	
	protected MoExportContextImpl(String caId, Map<String, String> moIdToMoVersionMapping,
			Map<String, String> moIdToPathMapping) {
		this.moIdToMoVersionMapping = moIdToMoVersionMapping;
		this.moIdToPathMapping = moIdToPathMapping;
		this.caId = caId;
	}

	@Override
	public String getContextPath(ManagedObject mo) throws RSuiteException {
		String moId = getMoId(mo);
		return moIdToPathMapping.get(moId);
	}

	@Override
	public String getContextMoVersion(ManagedObject mo) throws RSuiteException {
		String moId = getMoId(mo);
		return moIdToMoVersionMapping.get(moId);
	}

	public String getMoId(ManagedObject mo) throws RSuiteException {
		String moId = mo.getId();
		if (StringUtils.isNotBlank(mo.getTargetId())){
			moId = mo.getTargetId();
		}
		return moId;
	}

	public String getContextCaId() {
		return caId;
	}

	@Override
	public Set<String> getContextCaId(ManagedObject mo) throws RSuiteException {
		Set<String> contexCaId = new HashSet<String>();
		
		if (moIdToPathMapping.containsKey(getMoId(mo))){
			contexCaId.add(caId);
		}
		
		return contexCaId;
	}

	@Override
	public void addAdditionalContextInfo(String key, String value) {
		additionalInfo.put(key, value);
		
	}

	@Override
	public String getAdditionalContextInfo(String key) {
		return additionalInfo.get(key);
	}
	
}
