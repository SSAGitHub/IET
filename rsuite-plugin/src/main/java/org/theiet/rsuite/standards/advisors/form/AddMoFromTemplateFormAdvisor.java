package org.theiet.rsuite.standards.advisors.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.datamodel.comparators.DataTypeOptionValueComparator;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.utils.FormsUtils;
import org.theiet.rsuite.utils.StringUtils;
import org.theiet.rsuite.utils.XpathUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.DataTypeOptionValue;
import com.reallysi.rsuite.api.FormControlType;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ManagedObjectReference;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.forms.DefaultFormHandler;
import com.reallysi.rsuite.api.forms.FormColumnInstance;
import com.reallysi.rsuite.api.forms.FormInstance;
import com.reallysi.rsuite.api.forms.FormInstanceCreationContext;
import com.reallysi.rsuite.api.forms.FormParameterInstance;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.SearchService;

public class AddMoFromTemplateFormAdvisor extends DefaultFormHandler
			implements StandardsBooksConstans {

	public static final String FORM_PARAM_NEW_MO_FILE_NAME = "newMoFileName";

	public static final String FORM_PARAM_TEMPLATES = "temlates";  
	
	private static final Log log = LogFactory.getLog(AddMoFromTemplateFormAdvisor.class); 
	
	@Override
	public void adjustFormInstance(FormInstanceCreationContext context,
			FormInstance form) throws RSuiteException {
		
		form.setLabel("Add new MO from template");
		List<FormColumnInstance> cols = new ArrayList<FormColumnInstance>();
		
		//Column 1
		List<FormParameterInstance> formParams = new ArrayList<FormParameterInstance>();		
		

		FormsUtils.addFormTextParameter(formParams, FORM_PARAM_NEW_MO_FILE_NAME, "File name:", null, true, false);
		
		FormParameterInstance templateRadioButton = createTemplateRadioButton(context);
		formParams.add(templateRadioButton);


		FormColumnInstance fci1 = new FormColumnInstance();
		fci1.addParams(formParams);
		fci1.setName("addTemplatetotypescript");
		cols.add(fci1);
		form.setColumns(cols);
	}

	private FormParameterInstance createTemplateRadioButton(
			FormInstanceCreationContext context) throws RSuiteException {
		List<DataTypeOptionValue> reviewsOptionsValues = getTemplateOptionList(context, log);
		
		FormParameterInstance templateRadioButton = new FormParameterInstance();
		templateRadioButton.setFormControlType(FormControlType.RADIOBUTTON);
		templateRadioButton.setRequired(true);
		templateRadioButton.setSortOptions(FormsUtils.SORT_ORDER_AS_PROVIDED);
		templateRadioButton.setName("temlates");
		templateRadioButton.setLabel("Select template");	
		templateRadioButton.setBeforeOptions(reviewsOptionsValues);
		return templateRadioButton;
	}

	private List<DataTypeOptionValue> getTemplateOptionList(FormInstanceCreationContext context, Log log)
			throws RSuiteException {
		List<DataTypeOptionValue> templateOptionsValues = new ArrayList<DataTypeOptionValue>();
		
		SearchService srchSvc = context.getSearchService();
		User user = context.getUser();
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		
		List<ManagedObject> templateSet = getTemplateMOs(log, srchSvc, user);
		
		for (ManagedObject templateCaAsMo: templateSet) {
			ContentAssembly templatesCa = caSvc.getContentAssembly(user, templateCaAsMo.getId());
			List<? extends ContentAssemblyItem> templates = templatesCa.getChildrenObjects();
			for (ContentAssemblyItem template : templates) {
				ManagedObject templateMo = getTemplateMo(context, template);

				if (templateMo == null){
					continue;
				}
				  
					
				templateOptionsValues.add(new DataTypeOptionValue(templateMo.getId(), getTemplateDisplayName(templateMo)));
			}
		}
		
		Collections.sort(templateOptionsValues, new DataTypeOptionValueComparator());
		
		return templateOptionsValues;
	}
	
	private String getTemplateDisplayName(ManagedObject mo) throws RSuiteException{
		StringBuilder sb = new StringBuilder(StringUtils.normalizeSpace(mo.getDisplayName()));
		sb.append(" <").append(mo.getLocalName()).append("> ");
		sb.append("[").append(mo.getId()).append("]");
		
		return sb.toString();
		
	}

	private List<ManagedObject> getTemplateMOs(Log log, SearchService srchSvc,
			User user) throws RSuiteException {
		String templatesQuery = "/rs_ca_map/rs_ca[rmd:get-type(.) = '" + CA_TYPE_STANDARDS_TEMPLATE + "' and " +
				"rmd:get-lmd-value(., '" + LMD_FIELD_STANDARDS_TEMPLATE_ID + "') = '" + LMD_VALUE_STANDARD_TEMPLATES + "']";
		templatesQuery = XpathUtils.resolveRSuiteFunctionsInXPath(templatesQuery);
		log.info("adjustFormInstance: query for available templates\n" + templatesQuery);
		
		
		
		List<ManagedObject> templateSet = srchSvc.executeXPathSearch(user, templatesQuery, 1, 0);
		return templateSet;
	}
	
	private ManagedObject getTemplateMo(ExecutionContext context, ContentAssemblyItem templateCaItem) throws RSuiteException{
		ManagedObject templateMo = null;

		if (templateCaItem.getObjectType() == ObjectType.MANAGED_OBJECT){
			templateMo = (ManagedObject)templateCaItem;
		}
		
		if (templateCaItem.getObjectType() == ObjectType.MANAGED_OBJECT_REF) {
			User user = context.getAuthorizationService().getSystemUser();
			ManagedObjectReference moRef = (ManagedObjectReference)templateCaItem;
			
			templateMo = context.getManagedObjectService().getManagedObject(user , moRef.getTargetId());
		}
		
		return templateMo;
	}
	
}
