package org.theiet.rsuite.journals.domain.article;

import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ContentAssemblyItemFilter;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.helpers.utils.RSuiteUtils;

public class ArticleContentAssemblyItemFilter implements ContentAssemblyItemFilter {

	private static final String FILTERED_ITEM_DISPLAYED_NAME_IMAGES = "Images";

	private ExecutionContext context;

	public ArticleContentAssemblyItemFilter (ExecutionContext context) {
		this.context = context;
	}

	@Override
	public boolean include(User user, ContentAssemblyItem caItem) throws RSuiteException {
		ManagedObject mo = RSuiteUtils.getRealMo(
				context, user, context.getManagedObjectService()
				.getManagedObject(user, caItem.getId()));

		if ((mo.getObjectType() == ObjectType.CONTENT_ASSEMBLY ||
				mo.getObjectType() == ObjectType.CONTENT_ASSEMBLY_REF ||
						mo.getObjectType() == ObjectType.CONTENT_ASSEMBLY_NODE) &&
				mo.getDisplayName().equalsIgnoreCase(FILTERED_ITEM_DISPLAYED_NAME_IMAGES)) {

			return false;
		}

		return true;
	}

}
