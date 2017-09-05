package org.theiet.rsuite.mocks.api.remoteapi;

import java.util.Map;

import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;

public class CallArgumentListMock extends CallArgumentList {

	public CallArgumentListMock(ExecutionContext context, Map<String, String> parameters) {
		super(context);
		for (String key : parameters.keySet()){
			String value = parameters.get(key);
			add(key, value);
		}
	}
	
	

}
