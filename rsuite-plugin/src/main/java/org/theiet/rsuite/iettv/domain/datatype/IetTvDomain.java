package org.theiet.rsuite.iettv.domain.datatype;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;

public class IetTvDomain {

	private ContentAssembly ietTvDomainCa;

	public IetTvDomain(ContentAssembly ietTvDomainCa) {
		this.ietTvDomainCa = ietTvDomainCa;
	}
	
	public String getParentDoi() throws RSuiteException{
		return ietTvDomainCa.getLayeredMetadataValue("iettv_parent_doi");
	}
	
	public String getRegistrantName() throws RSuiteException{
		return ietTvDomainCa.getLayeredMetadataValue("iettv_registrant_name");
	}
	
	public String getEmail() throws RSuiteException{
		return ietTvDomainCa.getLayeredMetadataValue("iettv_email");
	}
	
	public String getPortalAdministrator() throws RSuiteException{
		return ietTvDomainCa.getLayeredMetadataValue("iettv_portal_administrator");
	}
	
	
	
}
