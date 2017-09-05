package org.theiet.rsuite.utils;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.DataTypeOptionValue;
import com.reallysi.rsuite.api.FormControlType;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.forms.FormInstanceCreationContext;
import com.reallysi.rsuite.api.forms.FormParameterInstance;
import com.reallysi.rsuite.api.xml.XPathEvaluator;
import com.reallysi.rsuite.service.XmlApiManager;


/**
 * Collection of string-related utility methods.
 */
public class FormsUtils
{
	private static Log log = LogFactory.getLog(FormsUtils.class);
	
	private static String CLASSNAME = "FormsUtils";
	public static final String SORT_ORDER_ASCENDING = "asc";
	public static final String SORT_ORDER_DESCENDING = "desc";
	public static final String SORT_ORDER_NATURAL = "natural";
	public static final String SORT_ORDER_AS_PROVIDED = "dontSort";

	public static void addFormLabelParameter(List<FormParameterInstance> params, String name, String label, String[] values) {
		createFormParameter(params, name, label, null, values, FormControlType.LABEL, null, null, SORT_ORDER_AS_PROVIDED, null, null, false);
	}

	public static void addFormCheckboxParameter(List<FormParameterInstance> params, String name, String label, List<DataTypeOptionValue> options, String[] values, String sortOrder, Boolean allowMultiple, Boolean required, Boolean readOnly) {
		createFormParameter(params, name, label, null, values, FormControlType.CHECKBOX, null, options, sortOrder, allowMultiple, required, readOnly);
	}

	public static void addFormCheckboxParameter(List<FormParameterInstance> params, String name, String label, String dataTypeName, String[] values, String sortOrder, Boolean required, Boolean readOnly) {
		createFormParameter(params, name, label, null, values, FormControlType.CHECKBOX, dataTypeName, null, sortOrder, null, required, readOnly);
	}

	public static void addFormSelectParameter(List<FormParameterInstance> params, String name, String label, List<DataTypeOptionValue> options, String[] values, String sortOrder, Boolean required, Boolean readOnly) {
		createFormParameter(params, name, label, null, values, FormControlType.SELECT, null, options, sortOrder, null, required, readOnly);
	}

	public static void addFormSelectParameter(List<FormParameterInstance> params, String name, String label, String dataTypeName, String[] values, String sortOrder, Boolean required, Boolean readOnly) {
		createFormParameter(params, name, label, null, values, FormControlType.SELECT, dataTypeName, null, sortOrder, null, required, readOnly);
	}

	public static void addFormMultiselectParameter(List<FormParameterInstance> params, String name, String label, List<DataTypeOptionValue> options, String[] values, String sortOrder, Boolean required, Boolean readOnly) {
		createFormParameter(params, name, label, null, values, FormControlType.MULTISELECT, null, options, sortOrder, null, required, readOnly);
	}

	public static void addFormMultiselectParameter(List<FormParameterInstance> params, String name, String label, String dataTypeName, String[] values, String sortOrder, Boolean required, Boolean readOnly) {
		createFormParameter(params, name, label, null, values, FormControlType.MULTISELECT, dataTypeName, null, sortOrder, null, required, readOnly);
	}

	public static void addFormTaxonomyParameter(List<FormParameterInstance> params, String name, String label, List<DataTypeOptionValue> options, String[] values, Boolean allowMultiple, Boolean required, Boolean readOnly) {
		createFormParameter(params, name, label, null, values, FormControlType.fromName("taxonomy"), null, options, SORT_ORDER_AS_PROVIDED, allowMultiple, required, readOnly);
	}

	public static void addFormTaxonomyParameter(List<FormParameterInstance> params, String name, String label, String dataTypeName, String[] values, Boolean allowMultiple, Boolean required, Boolean readOnly) {
		createFormParameter(params, name, label, null, values, FormControlType.fromName("taxonomy"), dataTypeName, null, SORT_ORDER_AS_PROVIDED, allowMultiple, required, readOnly);
	}

	public static void addFormDateParameter(List<FormParameterInstance> params, String name, String label, String value, Boolean required, Boolean readOnly) {
		createFormParameter(params, name, label, value, null, FormControlType.DATEPICKER, null, null, SORT_ORDER_AS_PROVIDED, false, required, readOnly);
	}

	public static void addFormTextParameter(List<FormParameterInstance> params, String name, String label, String value, Boolean required, Boolean readOnly) {
		createFormParameter(params, name, label, value, null, FormControlType.INPUT, null, null, SORT_ORDER_AS_PROVIDED, false, required, readOnly);
	}

	public static void addFormTextParameter(List<FormParameterInstance> params, String name, String label, String dataTypeName, String value, Boolean required, Boolean readOnly) {
		createFormParameter(params, name, label, value, null, FormControlType.INPUT, dataTypeName, null, SORT_ORDER_AS_PROVIDED, false, required, readOnly);
	}

	public static void addFormRadioButtonParameter(List<FormParameterInstance> params, String name, String label, List<DataTypeOptionValue> options, String[] values, String sortOrder, Boolean required, Boolean readOnly) {
		createFormParameter(params, name, label, null, values, FormControlType.RADIOBUTTON, null, options, sortOrder, false, required, readOnly);
	}

	public static void addFormRadioButtonParameter(List<FormParameterInstance> params, String name, String label, String dataTypeName, String[] values, Boolean required, String sortOrder, Boolean readOnly) {
		createFormParameter(params, name, label, null, values, FormControlType.RADIOBUTTON, dataTypeName, null, sortOrder, false, required, readOnly);
	}

	public static void addFormTextAreaParameter(List<FormParameterInstance> params, String name, String label, String value, Boolean required, Boolean readOnly) {
		createFormParameter(params, name, label, value, null, FormControlType.TEXTAREA, null, null, SORT_ORDER_AS_PROVIDED, false, required, readOnly);
	}

	public static void addFormTextAreaParameter(List<FormParameterInstance> params, String name, String label, String[] values, Boolean required, Boolean readOnly) {
		createFormParameter(params, name, label, null, values, FormControlType.TEXTAREA, null, null, SORT_ORDER_AS_PROVIDED, false, required, readOnly);
	}

	public static void addFormSubmitButtonsParameter(List<FormParameterInstance> params, String name, List<DataTypeOptionValue> options) {
		createFormParameter(params, name, null, null, null, FormControlType.SUBMITBUTTON, null, options, SORT_ORDER_AS_PROVIDED, false, false, false);
	}

	public static void addFormHiddenParameter(List<FormParameterInstance> params, String name, String value) {
		createFormParameter(params, name, null, value, null, FormControlType.HIDDEN, null, null, SORT_ORDER_AS_PROVIDED, false, false, false);
	}

	public static void underConstruction(List<FormParameterInstance> params) {
		addFormLabelParameter(params, "underConstruction", "This form has not yet been built.", null);
	}

	private static void createFormParameter(List<FormParameterInstance> params,
			String name, String label, 
			String value, String[] values, 
			FormControlType controlType, 
			String dataTypeName, List<DataTypeOptionValue> options, String sortOrder,
			Boolean allowMultiple, Boolean required, Boolean readOnly) {
		FormParameterInstance param = new FormParameterInstance();
		log.info(CLASSNAME + " Creating form parameter " + name + "/" + label);
		param.setName(name);
		if (null != label && !label.isEmpty())
			param.setLabel(label);
		if (null != values) {
			for (String val : values) {
				param.addValue(val);
			}
		}
		if (null != value && !value.isEmpty())
			param.addValue(value);
		if (null != allowMultiple && allowMultiple == true)
			param.setAllowMultiple(true);
		if (null != required && required != false) {
			param.setRequired(required);
			param.setValidationErrorMessage("This field is required.");
		}
		if (null != dataTypeName && !dataTypeName.isEmpty())
			param.setDataTypeName(dataTypeName);
		if (null != options && options.size() > 0)
			param.setBeforeOptions(options);
		if (readOnly == true)
			param.setReadOnly(true);
		param.setFormControlType(controlType);
		param.setSortOptions(sortOrder);
		params.add(param);
	}

	public static String getXmlValue(FormInstanceCreationContext context, ManagedObject mo, String xpath) {
		XmlApiManager xmlApiManager = context.getXmlApiManager();
		XPathEvaluator evaluator = xmlApiManager.getXPathEvaluator();

		String value = "";
		try {
			value = evaluator.executeXPathToString(
							xpath,
							mo.getElement().getOwnerDocument());
		} catch (RSuiteException e) {
			log.error(CLASSNAME + " Error getting XPath value " + e.getMessage());
		}
		return value;
	}
	
	public static String[] getXmlValues(FormInstanceCreationContext context, ManagedObject mo, String xpath) {
		XmlApiManager xmlApiManager = context.getXmlApiManager();
		XPathEvaluator evaluator = xmlApiManager.getXPathEvaluator();

		try {
			String[] values = evaluator.executeXPathToStringArray(
							xpath,
							mo.getElement().getOwnerDocument());
			return values;
		} catch (RSuiteException e) {
			log.error(CLASSNAME + " Error getting XPath value " + e.getMessage());
			return null;
		}
	}
	

}

