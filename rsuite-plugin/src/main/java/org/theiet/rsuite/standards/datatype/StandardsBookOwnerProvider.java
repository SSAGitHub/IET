package org.theiet.rsuite.standards.datatype;

import org.theiet.rsuite.datatype.ARoleControllerProvider;

public class StandardsBookOwnerProvider extends ARoleControllerProvider  {

	@Override
	protected String getRoleName() {
		return ROLE_STANDARDS_BOOK_OWNER;
	}

}
