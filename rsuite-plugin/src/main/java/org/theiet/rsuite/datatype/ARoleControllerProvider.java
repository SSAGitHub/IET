package org.theiet.rsuite.datatype;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.onix.OnixConstants;
import org.theiet.rsuite.standards.StandardsBooksConstans;

import com.reallysi.rsuite.api.DataTypeOptionValue;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.forms.DataTypeProviderOptionValuesContext;
import com.reallysi.rsuite.api.forms.DefaultDataTypeOptionValuesProviderHandler;
import com.reallysi.rsuite.api.security.UserManager;
import com.reallysi.rsuite.service.AuthorizationService;

public abstract class ARoleControllerProvider extends
		DefaultDataTypeOptionValuesProviderHandler implements JournalConstants, BooksConstans, StandardsBooksConstans, OnixConstants {

	/**
	 * Generate list of authors for article assignment. See references in
	 * rsuite-plugin.xml forms extension.
	 * 
	 * @author Harv Greenberg, Really Strategies, Inc., November 2011 Note: Dec
	 *         1 changed to check pipe-delimited string with multiple roles for
	 *         OR
	 */
	public void provideOptionValues(
			DataTypeProviderOptionValuesContext context,
			List<DataTypeOptionValue> optionValues) throws RSuiteException {

		Map<String, String> deliverList = new TreeMap<String, String>();

		AuthorizationService authSvc = context.getAuthorizationService();
		UserManager userManager = authSvc.getUserManager();

		for (User user : userManager.getUsers()) {

			String userId = user.getUserId();

			if (user != null && user.hasRole(getRoleName())) {

				String fullName = user.getFullName();

				if (deliverList.containsKey(fullName)
						&& !deliverList.get(fullName).equals(userId)) {
					fullName = fullName + " (" + userId + ")";
				}

				deliverList.put(fullName, userId);
			}

		}
		Set<String> nameSet = deliverList.keySet();
		for (String name : nameSet) {
			optionValues.add(new DataTypeOptionValue(deliverList.get(name),
					name));
		}
	}

	protected abstract String getRoleName();

}
