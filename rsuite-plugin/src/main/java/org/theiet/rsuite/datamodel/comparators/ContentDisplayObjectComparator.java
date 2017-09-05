package org.theiet.rsuite.datamodel.comparators;

import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.advisors.display.*;
import org.theiet.rsuite.journals.JournalConstants;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.content.ContentDisplayObject;

public class ContentDisplayObjectComparator implements Comparator<ContentDisplayObject>, JournalConstants{

	private static Log log = LogFactory.getLog(ContentDisplayObjectComparator.class);
	private SortCaBy sortCaBy;
	private SortCaIn sortCaIn;
	
	public ContentDisplayObjectComparator(SortCaBy sortCaBy, SortCaIn sortCaIn) {
		this.sortCaBy = sortCaBy;
		this.sortCaIn = sortCaIn;
	}
	
	@Override
	public int compare(ContentDisplayObject contentDisplayObject1,
			ContentDisplayObject contentDisplayObject2) {

		return getContentDisplayObjectPosition (contentDisplayObject1, contentDisplayObject2,
				sortCaBy, sortCaIn);					
	}

	private int getContentDisplayObjectPosition(ContentDisplayObject contentDisplayObject1, ContentDisplayObject contentDisplayObject2,
			SortCaBy sortCaBy, SortCaIn sortCaIn) {
		int position = 0;
		if (sortCaIn == SortCaIn.ASC) {
			position = compareContentDisplayObjects(contentDisplayObject1, contentDisplayObject2, sortCaBy);
		} else if (sortCaIn == SortCaIn.DESC) {
			position = -1 * compareContentDisplayObjects(contentDisplayObject1, contentDisplayObject2, sortCaBy);
		}
		
		return position;
	}
	
	private int compareContentDisplayObjects (ContentDisplayObject contentDisplayObject1, ContentDisplayObject contentDisplayObject2, SortCaBy sortCaBy) {
		if (contentDisplayObject1 == null
				&& contentDisplayObject2 == null) {
			return 0;
		} else if (contentDisplayObject1 == null) {
			return -1;
		} else if (contentDisplayObject2 == null) {
			return 1;
		} else {
			String firstParameter = "";
			String secondParameter = "";
			try {
				switch (sortCaBy) {
					case PRODUCT_CODE:
						firstParameter = formatString(getProductCode(contentDisplayObject1));
						secondParameter = formatString(getProductCode(contentDisplayObject2));
						break;

					case DISPLAY_NAME:
						firstParameter = formatString(contentDisplayObject1.getDisplayName());
						secondParameter = formatString(contentDisplayObject2.getDisplayName());
						break;
					case EXPORT_DATE:
						firstParameter = formatString(contentDisplayObject1.getManagedObject().getLayeredMetadataValue(LMD_FIELD_EXPORT_DATE));
						secondParameter = formatString(contentDisplayObject2.getManagedObject().getLayeredMetadataValue(LMD_FIELD_EXPORT_DATE));
						break;
				}
			} catch (RSuiteException ex) {
				log.info("Error getting parameters for ContentDisplayObject comparison", ex);
			}
			
			if (firstParameter == secondParameter ){
				return 0;
			}
			
			if (firstParameter == null){
				return -1;
			}
			
			return firstParameter.compareToIgnoreCase(secondParameter);
		}		
	}
	
	private String formatString (String tobeFormated) {
		if (tobeFormated == null) {
			return "";
		}
		return tobeFormated;
	}
	
	
	private String getProductCode(
			ContentDisplayObject contentDisplayObject) {
		String nameParts[] = contentDisplayObject.getDisplayName()
				.split("-");
		return nameParts[0].trim();
	}
	
}
