package org.theiet.rsuite.books.datatype;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.books.utils.BookUtils;
import org.theiet.rsuite.datamodel.ExternalCompanyUser;

import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class BookRequestDTO implements BooksConstans {

	private String targetFileName;
	
	private String targetFolderProperty;

	public String getTargetFolderProperty() {
		return targetFolderProperty;
	}

	private Map<String, String> emailVariables;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public BookRequestDTO(ExecutionContext context, ContentAssemblyItem bookCa,
			User currentUser, String targetFolderProperty, String targetFileName) throws RSuiteException {

		this.targetFolderProperty = targetFolderProperty;
		this.targetFileName = targetFileName;

		setUpVariableMap(context, bookCa, currentUser);
		
		emailVariables.put(EMAIL_VAR_FTP_FILE_NAME, targetFileName);
	}

	public String getTargetFileName() {
		return targetFileName;
	}

	public Map<String, String> getEmailVariables() {
		return emailVariables;
	}

	public void addEmailVariable(String name, String value) {
		emailVariables.put(name, value);
	}

	private Map<String, String> setUpVariableMap(ExecutionContext context,
			ContentAssemblyItem bookCa, User currentUser)
			throws RSuiteException {
		emailVariables = new HashMap<String, String>();

		emailVariables.put(EMAIL_VAR_PRODUCT_CODE,
				bookCa.getLayeredMetadataValue(LMD_FIELD_BOOK_PRODUCT_CODE));
		emailVariables.put(EMAIL_VAR_BOOK_TITLE,
				bookCa.getLayeredMetadataValue(LMD_FIELD_BOOK_TITLE));

		String firstName = bookCa
				.getLayeredMetadataValue(LMD_FIELD_AUTHOR_FIRST_NAME);
		String surname = bookCa
				.getLayeredMetadataValue(LMD_FIELD_AUTHOR_SURNAME);

		emailVariables.put(EMAIL_VAR_AUTHOR_FULL_NAME, firstName + " "
				+ surname);

		emailVariables.put(EMAIL_VAR_SERIES_NAME,
				bookCa.getLayeredMetadataValue(LMD_FIELD_BOOK_SERIES_NAME));

		emailVariables.put(EMAIL_VAR_PRODUCTION_CONTROLLER,
				currentUser.getFullName());

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 3);
		Date deadline = calendar.getTime();

		emailVariables.put(EMAIL_VAR_DEADLINE, dateFormat.format(deadline));

		ExternalCompanyUser typeSetterUser = BookUtils.getTypeSetterUser(
				context, bookCa);

		emailVariables.put(EMAIL_VAR_COMPANY_NAME,
				typeSetterUser.getCompanyName());
		emailVariables.put(EMAIL_VAR_CONTACT_NAME,
				typeSetterUser.getContactFirstName());

		return emailVariables;
	}
}
