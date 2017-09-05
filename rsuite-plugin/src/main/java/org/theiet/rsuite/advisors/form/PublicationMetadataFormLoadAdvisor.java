/**
 * Copyright (c) 2012 Really Strategies, Inc.
 */
package org.theiet.rsuite.advisors.form;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.forms.DefaultFormHandler;
import com.reallysi.rsuite.api.forms.FormColumnInstance;
import com.reallysi.rsuite.api.forms.FormInstance;
import com.reallysi.rsuite.api.forms.FormInstanceCreationContext;
import com.reallysi.rsuite.api.forms.FormParameterInstance;
import com.reallysi.rsuite.api.vfs.BrowsePath;

/**
 * Form definition advisor to pre-populate form fields with existing LMD values.
 * 
 */
public class PublicationMetadataFormLoadAdvisor extends DefaultFormHandler {

	private static Log log = LogFactory.getLog(PublicationMetadataFormLoadAdvisor.class);


	/* (non-Javadoc)
	 * @see com.reallysi.rsuite.api.forms.FormDefinitionAdvisor#adjustFormDefinition(com.reallysi.rsuite.api.forms.FormDefinitionContext, com.reallysi.rsuite.api.forms.FormDefinition)
	 */
	@Override
	public void adjustFormInstance(FormInstanceCreationContext context,
			FormInstance form) throws RSuiteException {
		log.info("adjustFormDefinition(): Starting...");
		Session sess = context.getSession();
		String formName = form.getName();
//		for (FormParameterDefinition param : form.getFormParameters()) {
//			log.info("  Param " + param.getName() + "=" + param.getValue());
//		}

		BrowsePath path = context.getBrowsePath();
		
		handleNewBookForm(form, sess, path);
		return;
		
		
		
	}

	@SuppressWarnings("unchecked")
	private void handleNewBookForm(FormInstance form, Session sess,
			BrowsePath path) throws RSuiteException {
		String key = getParametersMapSessionKey(path);

		Map<String, String> paramValues = (Map<String, String>)sess.getAttribute(key);
		List <FormColumnInstance> colums = form.getColumns();
		for (Iterator <FormColumnInstance>iterator = colums.iterator(); iterator.hasNext();) {
			FormColumnInstance colum = (FormColumnInstance) iterator.next();
			List<FormParameterInstance> params = colum.getParams();
			// FIXME: For now just handling case of one entry per form field, but need to work out
			// a general way to capture multiple values for a given field name and then map them back
			// to the form definition.
			for (FormParameterInstance param : params) {
				String name = param.getName();
				if (paramValues != null) {
					String value = paramValues.get(name);
					if (value != null)
						param.addValue(value);
				}
			}
			
			// FIXME: Restore captured datatype-based LMD values.
			
			form.setFormParams(params);
		}		
	}

	public static String getParametersMapSessionKey(BrowsePath path) {
		String uri = path.getUri();
		String key = getParametersMapSessionKey(uri);
		return key;
	}

	public static String getParametersMapSessionKey(String uri) {
		String key = uri + "~PublicationMetadataFields";
		return key;
	}
	

}
