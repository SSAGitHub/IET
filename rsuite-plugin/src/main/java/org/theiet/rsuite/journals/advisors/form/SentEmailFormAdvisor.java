package org.theiet.rsuite.journals.advisors.form;

import java.util.Iterator;
import java.util.List;

import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.utils.JournalBrowserUtils;

import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.forms.DefaultFormHandler;
import com.reallysi.rsuite.api.forms.FormColumnInstance;
import com.reallysi.rsuite.api.forms.FormInstance;
import com.reallysi.rsuite.api.forms.FormInstanceCreationContext;
import com.reallysi.rsuite.api.forms.FormParameterInstance;
import com.reallysi.rsuite.api.vfs.BrowsePath;

public class SentEmailFormAdvisor extends DefaultFormHandler implements JournalConstants {

	@Override
	public void adjustFormInstance(FormInstanceCreationContext context,
			FormInstance formInstance) throws RSuiteException {
		
		BrowsePath path =  context.getBrowsePath();
		ContentAssemblyItem articleCA = null;
		if (path == null) {
			articleCA = JournalBrowserUtils.getAncestorAritcle(context, context.getArgs().getFirstValue("rsuiteId"));
		} else {
			articleCA = JournalBrowserUtils.getAncestorAritcle(context, path);
		}
				
		if (articleCA != null){
			List <FormColumnInstance> colums = formInstance.getColumns();
			for (Iterator <FormColumnInstance>iterator = colums.iterator(); iterator.hasNext();) {
				FormColumnInstance colum = (FormColumnInstance) iterator.next();
				List<FormParameterInstance> parameters = colum.getParams();
				for (FormParameterInstance parameter : parameters){
					if ("emailAddress".equals(parameter.getName())){
						String authorEmail = articleCA.getLayeredMetadataValue(LMD_FIELD_AUTHOR_EMAIL);
						if (authorEmail != null){
							parameter.addValue(authorEmail);
						}
						
					}
				}
			}			
		}else{
			List <FormColumnInstance> colums = formInstance.getColumns();
			for (Iterator <FormColumnInstance>iterator = colums.iterator(); iterator.hasNext();) {
				FormColumnInstance colum = (FormColumnInstance) iterator.next();
				List<FormParameterInstance> parameters = colum.getParams();
				parameters.clear();		
				formInstance.setInstructions("This option works only for files that belongs to an journal article");
			}
		}
		
		

		
	}

}
