package org.theiet.rsuite.books.webservice;

import java.util.List;

import org.theiet.rsuite.books.BooksConstans;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

/**
 * An abstract class for book typesetter request: sends context assembly via FTP
 * and sends email to typesetter
 * 
 */
public abstract class ABookTypesetterRequestWebService extends
		AExternalRequestWebService {

	protected String getUserLmdName() {
		return LMD_FIELD_TYPESETTER_USER;
	}

	protected String getTargetFileName(ContentAssemblyItem bookCa)
			throws RSuiteException {
		return BooksConstans.FILENAME_PREFIX_BOOK
				+ bookCa.getLayeredMetadataValue(BooksConstans.LMD_FIELD_BOOK_PRODUCT_CODE)
				+ ".zip";
	}
	
	@Override
	protected List<ContentAssembly> getAssemblyToAttach(
			ExecutionContext context, ContentAssemblyItem bookCa)
			throws RSuiteException {
		return null;
	}
}
