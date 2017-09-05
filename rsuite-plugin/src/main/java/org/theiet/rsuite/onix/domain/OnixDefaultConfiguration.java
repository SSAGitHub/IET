package org.theiet.rsuite.onix.domain;

import java.util.*;

import org.theiet.rsuite.onix.OnixConstants;
import org.theiet.rsuite.utils.SearchUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.DependencyTracker;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.*;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class OnixDefaultConfiguration implements OnixConstants {

	private ContentAssembly onixConfigurationsCa;

	private ExecutionContext context;

	private User user;

	private List<String> onixConfigurationsIds = new ArrayList<String>();
	
	private static OnixDefaultConfiguration instance;
	
	private ManagedObject configurationsMo;
	
	private String defaultTemplateMoId;
	
	private String defaultTemplateUri;
	
	private ManagedObjectService moSvc;
	
	public static OnixDefaultConfiguration getInstance(ExecutionContext context) throws RSuiteException{
		if (instance == null){
			instance = new OnixDefaultConfiguration(context);
		}
		return instance;
	}
	
	private OnixDefaultConfiguration(ExecutionContext context)
			throws RSuiteException {
		super();

		ContentAssemblyService caSvc = context.getContentAssemblyService();
		user = context.getAuthorizationService().getSystemUser();

		this.context = context;

		moSvc = context.getManagedObjectService();
		configurationsMo = findOnixConfigurationsMo();
		computeOnixConfigurationIds(configurationsMo);

		this.onixConfigurationsCa = caSvc.getContentAssembly(user,
				configurationsMo.getId());
		
		ManagedObject defaultTemplateMo = findDefaultMo(context);
		
		defaultTemplateUri = generateMoUri(defaultTemplateMo);
		
		
		
	}

	private ManagedObject findDefaultMo(ExecutionContext context)
			throws RSuiteException {
		ManagedObject defaultTemplateMo = ProjectBrowserUtils.getChildMoLmd(context, onixConfigurationsCa, LMD_FIELD_ONIX_CONFIGURATION_TYPE, LMD_VALUE_TEMPLATE);
		
		if (defaultTemplateMo == null){
			throw new RSuiteException(RSuiteException.ERROR_CONFIGURATION_PROBLEM, "There is no default Onix template");
		}
		
		defaultTemplateMoId = defaultTemplateMo.getId();
		return defaultTemplateMo;
	}

	public String getDefaultFilteringConfigurationUri(String outputType)
			throws RSuiteException {
		return OnixRecipientConfiguration.getChildConfigurationUri(context,
				onixConfigurationsCa, LMD_FIELD_ONIX_CONFIGURATION_TYPE, outputType);
	}

	public ContentAssembly getRecipientConfigurationCa() {
		return onixConfigurationsCa;
	}

	

	private ManagedObject findOnixConfigurationsMo() throws RSuiteException {
		
		return SearchUtils.findMoWitRXSsingleCheck(context, user, "/rs_ca_map/rs_ca[rmd:get-type(.) = ('onixConfigurations')]", "There should be only on onix configurations CA.");
	}

	private void computeOnixConfigurationIds(ManagedObject mo)
			throws RSuiteException {

		DependencyTracker dt = context.getManagedObjectService()
				.getDependencyTracker();
		onixConfigurationsIds.add(mo.getId());
		List<ReferenceInfo> rtr = dt.listDirectReferences(user, mo.getId());
		for (ReferenceInfo ri : rtr) {
			onixConfigurationsIds.add(ri.getId());
		}
		Collections.reverse(onixConfigurationsIds);
	}

	public List<String> getOnixConfigurationIds() {

		return onixConfigurationsIds;
	}

	public String getDefaultTemplateUri() throws RSuiteException {
		ManagedObject mo = moSvc.getManagedObject(user, defaultTemplateMoId);
		if (!isMoDefaultTemplate(mo)){
			ManagedObject defaultMo = findDefaultMo(context);
			defaultTemplateUri = generateMoUri(defaultMo);
		}
		return defaultTemplateUri;
	}
	
	public ManagedObject getDefaultTemplateMo() throws RSuiteException {
		ManagedObject mo = moSvc.getManagedObject(user, defaultTemplateMoId);
		if (!isMoDefaultTemplate(mo)){
			mo = findDefaultMo(context);
			
		}
		return mo;
	}
	
	private boolean isMoDefaultTemplate(ManagedObject mo) throws RSuiteException{
		
		String lmdName = LMD_FIELD_ONIX_CONFIGURATION_TYPE;
		String lmdValue = LMD_VALUE_TEMPLATE;
		
		for (MetaDataItem item : mo.getMetaDataItems()){
			
			if (item.getName().equalsIgnoreCase(lmdName) && lmdValue != null && lmdValue.trim().equals(lmdValue)){
				return true;
			}
		}
		return false;
	}
	
	private String generateMoUri(ManagedObject mo) {
		return "rsuite:/res/content/" + mo.getId();
	}

	public ContentAssembly getOnixConfigurationsCa() {
		return onixConfigurationsCa;
	}
	
}
