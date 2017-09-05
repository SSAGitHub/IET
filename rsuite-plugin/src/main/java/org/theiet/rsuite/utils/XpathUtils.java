package org.theiet.rsuite.utils;

public class XpathUtils {

	/**
	 * Resolves rsuite functions
	 * @param xpath
	 * @return
	 */
	public static String resolveRSuiteFunctionsInXPath(String xpath){
		
		String newXpath = xpath.replaceAll("rmd:get-type\\s*\\(\\s*([^\\)\\s]+)\\s*\\)", "$1/mv:metadata/mv:system/mv:ca-type");
		
		newXpath = newXpath.replaceAll("rmd:get-display-name\\s*\\(\\s*([^\\)\\s]+)\\s*\\)", "$1/mv:metadata/mv:system/mv:display-name");
				
		newXpath = newXpath.replaceAll("rmd:get-lmd-value\\s*\\(\\s*([^,\\s]+)\\s*,\\s*(\"|')([^\\']+)(\"|')\\s*\\)", "$1/mv:metadata/mv-lmd:layered/mv-lmd:$3");
		
		return newXpath;

	}

}
