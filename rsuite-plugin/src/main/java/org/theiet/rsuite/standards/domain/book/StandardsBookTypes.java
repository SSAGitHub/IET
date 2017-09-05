package org.theiet.rsuite.standards.domain.book;

import com.rsicms.projectshelper.datatype.RSuiteCaType;

public enum StandardsBookTypes implements RSuiteCaType {

	CA_TYPE_STANDARDS_BOOK("standardsBook"), CA_TYPE_STANDARDS_BOOK_EDITION("standardsBookEdition");

	private String typeName;

	private StandardsBookTypes(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String getTypeName() {
		return typeName;
	}
}
