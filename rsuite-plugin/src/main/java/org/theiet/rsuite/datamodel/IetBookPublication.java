package org.theiet.rsuite.datamodel;

import org.theiet.rsuite.standards.StandardsBooksConstans;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class IetBookPublication implements StandardsBooksConstans {

	private ContentAssembly bookPublicationCa;

	public IetBookPublication(ContentAssembly bookEditionCa) {
		this.bookPublicationCa = bookEditionCa;
	}

	public IetBookPublication(ExecutionContext context, String bookEditonCaId)
			throws RSuiteException {
		bookPublicationCa = ProjectBrowserUtils
				.getContentAssembly(context, bookEditonCaId);
	}

	public ContentAssembly getBookPublicationCa() {
		return bookPublicationCa;
	}

	public boolean isMetadataAlreadySent() throws RSuiteException {

		String value = bookPublicationCa
				.getLayeredMetadataValue(LMD_FIELD_BOOK_METADATA_SENT);

		if (LMD_VALUE_YES.equalsIgnoreCase(value)) {
			return true;
		}

		return false;
	}
	
	public String getRsuiteId(){
		return bookPublicationCa.getId();
	}
}
