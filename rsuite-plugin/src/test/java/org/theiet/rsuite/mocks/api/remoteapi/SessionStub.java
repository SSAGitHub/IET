package org.theiet.rsuite.mocks.api.remoteapi;

import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.User;

public class SessionStub extends Session {

	public SessionStub(String key, User user) {
		super(key, user); 
	}

}
