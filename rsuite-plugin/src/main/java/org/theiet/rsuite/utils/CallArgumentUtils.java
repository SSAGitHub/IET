package org.theiet.rsuite.utils;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.remoteapi.CallArgumentList;

public class CallArgumentUtils {

	public static String getRsuiteId (CallArgumentList args) {
		String rsuiteId = args.getFirstValue("sourceId");

       if (StringUtils.isBlank(rsuiteId)){
    	   rsuiteId = args.getFirstValue("rsuiteId");
       }

       return rsuiteId;
	}

}
