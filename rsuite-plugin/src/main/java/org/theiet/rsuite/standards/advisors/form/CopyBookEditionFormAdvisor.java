package org.theiet.rsuite.standards.advisors.form;

import java.util.*;

import org.theiet.rsuite.datamodel.comparators.DataTypeOptionValueComparator;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.domain.book.StandardsBook;
import org.theiet.rsuite.utils.FormsUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.forms.*;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class CopyBookEditionFormAdvisor extends DefaultFormHandler implements
		StandardsBooksConstans {

	@Override
	public void adjustFormInstance(FormInstanceCreationContext context,
			FormInstance form) throws RSuiteException {

		
		form.setLabel("Copy Book Edition Content");
		List<FormColumnInstance> cols = new ArrayList<FormColumnInstance>();

		// Column 1
		List<FormParameterInstance> formParams = new ArrayList<FormParameterInstance>();

		String currentEditionId = getCurrentEditionId(context);
		
		ContentAssemblyItem bookCa = ProjectBrowserUtils.getAncestorCAbyType(context, 
					getArgumentFromFormContext(context, ARGS_RSUITE_BROWSE_URI), CA_TYPE_STANDARDS_BOOK);

		StandardsBook book = new StandardsBook(context, bookCa.getId());

		List<DataTypeOptionValue> reviewsOptionsValues = getTemplateOptionList(
		context, book, currentEditionId);
		
		FormParameterInstance templateRadioButton = createTemplateRadioButton(
				context, reviewsOptionsValues);
		formParams.add(templateRadioButton);

		FormColumnInstance fci1 = new FormColumnInstance();
		fci1.addParams(formParams);
		fci1.setName("copyBookEditionContent");
		cols.add(fci1);
		form.setColumns(cols);
	}

	public String getCurrentEditionId(FormInstanceCreationContext context)
			throws RSuiteException {
		
		
		String currentEditionId = getArgumentFromFormContext(context,
				PARAM_RSUITE_ID);
		
		ContentAssemblyItem currentEditionCaItem =	 ProjectContentAssemblyUtils.getCAItem(context, context.getUser(), currentEditionId);
		currentEditionId = currentEditionCaItem.getId();
		return currentEditionId;
	}

	public String getArgumentFromFormContext(
			FormInstanceCreationContext context, String argumentName) {
		CallArgumentList callArgumentList = context.getArgs();
		return callArgumentList.getFirstValue(argumentName);
	}

	private FormParameterInstance createTemplateRadioButton(
			FormInstanceCreationContext context, List<DataTypeOptionValue> reviewsOptionsValues)
			throws RSuiteException {

		FormParameterInstance templateRadioButton = new FormParameterInstance();
		templateRadioButton.setFormControlType(FormControlType.RADIOBUTTON);
		templateRadioButton.setRequired(true);
		templateRadioButton.setSortOptions(FormsUtils.SORT_ORDER_AS_PROVIDED);
		templateRadioButton.setName(FORM_PARAM_SOURCE_BOOK_EDITION);
		templateRadioButton.setLabel("Select source Book Edition");
		templateRadioButton.setBeforeOptions(reviewsOptionsValues);
		return templateRadioButton;
	}

	private List<DataTypeOptionValue> getTemplateOptionList(
			FormInstanceCreationContext context, StandardsBook book, String currentEditionId)
			throws RSuiteException {
		List<DataTypeOptionValue> templateOptionsValues = new ArrayList<DataTypeOptionValue>();

		List<ContentAssembly> bookEditionList = book.getBookEditions();

		for (ContentAssembly bookEditionCa : bookEditionList) {
			
			if (currentEditionId.equalsIgnoreCase(bookEditionCa.getId())){
				continue;
			}
			
			templateOptionsValues.add(new DataTypeOptionValue(bookEditionCa
					.getId(), bookEditionCa.getDisplayName()));
		}

		Collections.sort(templateOptionsValues,
				new DataTypeOptionValueComparator());

		return templateOptionsValues;
	}

}
