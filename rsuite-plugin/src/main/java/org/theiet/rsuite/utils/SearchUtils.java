package org.theiet.rsuite.utils;

import java.util.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.*;
import com.rsicms.projectshelper.datatype.*;

public class SearchUtils {

    public static ManagedObject findMoWithRXSsingleCheck(SearchService searchSvc, User user, String rsuiteXpath) throws RSuiteException {

        String resolvedXpath = XpathUtils.resolveRSuiteFunctionsInXPath(rsuiteXpath);

        List<ManagedObject> results = searchSvc.executeXPathSearch(user, resolvedXpath,
                0, 1);

        if (results.size() > 0) {
            return results.get(0);

        }

        return null;
    }
    //TODO fix the name
	public static ManagedObject findMoWitRXSsingleCheck(ExecutionContext context, User user, String query, String notSingleErrorMsg) throws RSuiteException {
		SearchService searchSvc = context.getSearchService();
		

		String resolvedXquery = XpathUtils.resolveRSuiteFunctionsInXPath(query);

		List<ManagedObject> results = searchSvc.executeXPathSearch(user, resolvedXquery,
				0, 2);

		if (results.size() != 1) {
			throw new RSuiteException(
					notSingleErrorMsg +  " Found: "
							+ results.size());

		}

		return results.get(0);
	}

	public static List<ManagedObject> executeXpathSearch(User user,
			String query, SearchService srchSvc, int maxResult)
			throws RSuiteException {
		int start = 1;
		
		String resolvedXquery =  XpathUtils.resolveRSuiteFunctionsInXPath(query);

		return srchSvc.executeXPathSearch(user,
				resolvedXquery, start, maxResult);
	}

	public static ContentAssembly findCaBasedOnLmd(ExecutionContext context, User user, RSuiteLmd lmd, String lmdValue) throws RSuiteException{
	    List<String> values = new ArrayList<String>();
	    values.add(lmdValue);
	    
	    return findCaBasedOnLmd(context, user, lmd, values);
	}
	
	public static ContentAssembly findCaBasedOnLmd(ExecutionContext context, User user, RSuiteLmd lmd, List<String> lmdValues) throws RSuiteException{
        String xpath = generateSearchCaBasedOnLmdQuery(lmd.getLmdName(), lmdValues);
        
        return findSingleCaBasedOnRSX(context, user, xpath);
    }
	
	public static ContentAssembly findCaBasedOnCaType(ExecutionContext context, User user, RSuiteCaType caType) throws RSuiteException{
        String xpath = generateSearchCaBasedOnTypeQuery(caType.getTypeName());
        return findSingleCaBasedOnRSX(context, user, xpath);
    }
    
	public static ContentAssembly findSingleCaBasedOnRSX(ExecutionContext context, User user,
             String xpath) throws RSuiteException {
	    SearchService searchSvc = context.getSearchService();
        List<ManagedObject> searchResult = executeXpathSearch(user,
                xpath, searchSvc, 1);
        
        if (searchResult.size() == 0){
            return null;
        }
        
        ContentAssemblyService caSvc = context.getContentAssemblyService();
        
        return caSvc.getContentAssembly(user, searchResult.get(0).getId());
    }
	
	public static List<ManagedObject> findCaBasedOnLmd(SearchService srchSvc, User user, String lmdName, List<String> lmdValues) throws RSuiteException{
		
		String query = generateSearchCaBasedOnLmdQuery(lmdName, lmdValues);
		
		return executeXpathSearch(user,
				query, srchSvc, 1000);
	}

	protected static String generateSearchCaBasedOnLmdQuery(String lmdName,
			List<String> lmdValues) {
	    
	    String possibleValues = createPossibleValuesXpathParameter(lmdValues);
		
		String query = "/rs_ca_map/rs_ca[rmd:get-lmd-value(., '" + lmdName + "') = " + possibleValues + "]";
		return query;
	}
	
	   private static String generateSearchCaBasedOnTypeQuery(String caType) {
	        String query = "/rs_ca_map/rs_ca[./mv:metadata/mv:system/mv:ca-type = '" + caType + "']";
	        return query;
	    }

    private static String createPossibleValuesXpathParameter(List<String> lmdValues) {
        String possibleValues = ""; 
	    
	    if (lmdValues.size() == 1){
	       possibleValues = "'" + lmdValues.get(0)  + "'"; 
	    }else{
	        possibleValues = createValueSequenceFromList(lmdValues);   
	    }
        return possibleValues;
    }

    private static String createValueSequenceFromList(List<String> lmdValues) {
        StringBuilder possibleValues = new StringBuilder("(");
		
		for (Iterator<String> iterator = lmdValues.iterator(); iterator.hasNext();) {
			String value =  iterator.next();
			
			possibleValues.append("'").append(value).append("'");
			
			if (iterator.hasNext()){
				possibleValues.append(", ");
			}
		}
		
		possibleValues.append(")");
        return possibleValues.toString();
    }
}
