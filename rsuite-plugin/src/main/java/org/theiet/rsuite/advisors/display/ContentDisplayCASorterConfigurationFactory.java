package org.theiet.rsuite.advisors.display;

import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.datamodel.comparators.SortCaBy;

import com.reallysi.rsuite.api.ContentAssemblyNodeContainer;

public class ContentDisplayCASorterConfigurationFactory {

	public static ContentDisplayCASorterConfig getSortingConfiguration(ContentAssemblyNodeContainer containerToSort) {
		
		ContentDisplayCASorterConfig caSorterConfig = new ContentDisplayCASorterConfig();
		
		String caType = containerToSort.getType();
		if (BooksConstans.CA_TYPE_BOOKS_CONTENT_ASSEMBLY.equalsIgnoreCase(caType)){
			caSorterConfig.setSortCaBy(SortCaBy.DISPLAY_NAME);
		}
		
		
		return caSorterConfig;
	}

}
