package org.theiet.rsuite.datamodel.bookpublication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.onix.OnixConstants;

import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;

public class CallArgumentsBookPublicationMapper implements BooksConstans, OnixConstants {

	private static final String PARAM_BOOK_TITLE = "bookTitle";

	public Map<String, String> converFormCallArgumentsToMap(
		CallArgumentList args, List<MetaDataItem> metadaList) {
		
		Map<String, String> argumentsMap = new HashMap<String, String>();
		
		for (MetaDataItem metadata : metadaList){
			addParameterToMap(argumentsMap, metadata.getName(), metadata.getValue());
		}
		
		addTitlesParameters(args, argumentsMap);
		
		return argumentsMap;
		
	}
	
	public Map<String, String> converFormCallArgumentsToMap(
			CallArgumentList args, String[] FORM_PARAMETERS_TO_LMD) {

		Map<String, String> argumentsMap = new HashMap<String, String>();

		for (String formParam : FORM_PARAMETERS_TO_LMD) {
			addFormParamIfNotBlank(args, argumentsMap, formParam);
		}

		addTitlesParameters(args, argumentsMap);
		
		return argumentsMap;
	}

	private void addTitlesParameters(CallArgumentList args,
			Map<String, String> argumentsMap) {
		addFormParamIfNotBlank(args, argumentsMap, PARAM_BOOK_TITLE);
		addFormParamIfNotBlank(args, argumentsMap, ONIX_VAR_BOOK_SUBTITLE);
		addFormParamIfNotBlank(args, argumentsMap, ONIX_VAR_EDITION_NUMBER);
	}

	private void addFormParamIfNotBlank(CallArgumentList args,
			Map<String, String> argumentsMap, String formParam) {
		String value = args.getFirstValue(formParam);
		
		addParameterToMap(argumentsMap, formParam, value);
	}

	private void addParameterToMap(Map<String, String> argumentsMap,
			String formParam, String value) {
		if (value != null && ( LMD_FIELD_ISBN.equalsIgnoreCase(formParam) || LMD_FIELD_E_ISBN.equalsIgnoreCase(formParam))){
			value = value.replaceAll("[^0-9]", "");
		}

		if (StringUtils.isNotBlank(value)) {
			argumentsMap.put(formParam, value);
		}
	}
	
	/**
	 * Generates full title from title, subtitle and edition number
	 * @param argumentsMap
	 * @return full title
	 */
	public String generateFullTitle(Map<String, String> argumentsMap) {
		String title = argumentsMap.get(PARAM_BOOK_TITLE);
		String subTitle = argumentsMap.get(ONIX_VAR_BOOK_SUBTITLE);
		String edition = argumentsMap.get(ONIX_VAR_EDITION_NUMBER);

		StringBuilder fullTitle = new StringBuilder(title);

		concatenateIfNotBlank(fullTitle, subTitle);
		concatenateIfNotBlank(fullTitle, edition);
		return fullTitle.toString();
	}
	
	private void concatenateIfNotBlank(StringBuilder base, String toConcatenate) {
		if (StringUtils.isNotBlank(toConcatenate)) {
			base.append(" ").append(toConcatenate);
		}
	}
}
