package org.theiet.rsuite.advisors.display;

import java.util.Collections;
import java.util.List;

import org.theiet.rsuite.datamodel.comparators.*;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.content.ContentDisplayObject;

public class ContentDisplayCaSorter  {
	
	private List<ContentDisplayObject> collection;
	
	public ContentDisplayCaSorter (List<ContentDisplayObject> collection) throws RSuiteException {
		this.collection = collection;
	}
	
	public void sort (SortCaBy sortCaBy) throws RSuiteException {
		sort(sortCaBy, SortCaIn.ASC);
	}
	
	public void sort (SortCaBy sortCaBy, SortCaIn sortCaIn)
			throws RSuiteException {
		
		if (collection.size() > 0) {
			Collections.sort(collection, new ContentDisplayObjectComparator(sortCaBy, sortCaIn));
		}
	}
	
}
