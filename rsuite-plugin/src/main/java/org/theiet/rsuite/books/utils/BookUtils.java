package org.theiet.rsuite.books.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.datamodel.ExternalCompanyUser;
import org.theiet.rsuite.utils.XpathUtils;

import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.SearchService;
import com.rsicms.projectshelper.utils.ProjectUserUtils;

public class BookUtils implements BooksConstans {

	/**
	 * Gets the journal caId from the journal code.
	 * 
	 * @param user2
	 * @param log
	 * 
	 * @param log
	 * @param user
	 * @param context
	 * @param srchSvc
	 * @param productCode
	 * @return the journal caId or null is query does not return exactly one
	 *         result
	 * @throws RSuiteException
	 */
	public static String getBookCaId(Log log, User user,
			ExecutionContext context, String productCode)
			throws RSuiteException {
		SearchService searchScv = context.getSearchService();

		String bookQuery = "/rs_ca_map/rs_ca[rmd:get-type(.) = 'book' and rmd:get-lmd-value(., '"
				+ BooksConstans.LMD_FIELD_BOOK_PRODUCT_CODE
				+ "') = '"
				+ productCode + "']";
		
		bookQuery = XpathUtils.resolveRSuiteFunctionsInXPath(bookQuery);
		
		List<ManagedObject> caSet = searchScv.executeXPathSearch(user,
				bookQuery, 1, 0);
		int books = caSet.size();

		if (books > 1){
			throwExceptionForMultipleCaWithTheSameProductCode(productCode,
					caSet);
		}
		
		if (books == 0) {
			return null;
		}else {
			return caSet.get(0).getId();
		}
	}

	/**
	 * @param productCode
	 * @param caSet
	 * @throws RSuiteException
	 */
	protected static void throwExceptionForMultipleCaWithTheSameProductCode(
			String productCode, List<ManagedObject> caSet)
			throws RSuiteException {
		List<String> caIds = new ArrayList<String>();
		for (ManagedObject mo :caSet){
			caIds.add(mo.getId());
		}
		
		throw new RSuiteException("There is more than one CA with product code " + productCode + " ca ids: " + caIds);
	}

	public static String getUserIdFormLmd(ContentAssemblyItem bookCa, String userLmdName)
			throws RSuiteException {
		String typeSetterUserId = bookCa
				.getLayeredMetadataValue(userLmdName);

		if (StringUtils.isBlank(typeSetterUserId)) {
			throw new RSuiteException(userLmdName + "for the book is not defined");
		}
		return typeSetterUserId;
	}

	//TODO move to common utils
	public static ExternalCompanyUser getTypeSetterUser(ExecutionContext context,
			ContentAssemblyItem bookCa) throws RSuiteException {		
		User user = ProjectUserUtils.getUser(context, getUserIdFormLmd(bookCa, LMD_FIELD_TYPESETTER_USER));
		return new ExternalCompanyUser(context, user);
	}
	
	public static ExternalCompanyUser getTypeSetterUser(ExecutionContext context,
			ContentAssemblyItem bookCa, String userLmdName) throws RSuiteException {		
		User user = ProjectUserUtils.getUser(context, getUserIdFormLmd(bookCa, userLmdName));
		return new ExternalCompanyUser(context, user);
	}
}
