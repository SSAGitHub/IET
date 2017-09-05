package org.theiet.rsuite.onix.onix2lmd.adjuster;

import static org.theiet.rsuite.books.BooksConstans.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.onix.onix2lmd.Onix2LmdValueAdjuster;
import org.theiet.rsuite.standards.domain.book.StandardsBookUtils;

import com.reallysi.rsuite.api.MetaDataItem;

public class IetOnix2LmdValueAdjuster implements Onix2LmdValueAdjuster {

	private static final String TITLE_DELIMITER_EDITION = ", ";
	private static final String TITLE_DELIMITER_SUBTITLE = ": ";
	private static final String FULL_TITLE_VALUE_SEPARATOR = ";;;";

	@Override
	public List<MetaDataItem> adjustMetadata(List<MetaDataItem> metadataToAdd,
			String publicationMoId) {

		List<MetaDataItem> adjustedMetadata = new ArrayList<MetaDataItem>();

		for (MetaDataItem metadataItem : metadataToAdd) {

			String metadataName = metadataItem.getName();
			String metadataValue = metadataItem.getValue();

			if (LMD_FIELD_BOOK_TITLE.equals(metadataName)) {
				StringBuilder newTitle = adjustTitle(metadataItem);
				metadataItem.setValue(newTitle.toString());

			}
			
			if ( (LMD_FIELD_ACTUAL_PUB_DATE.equals(metadataName) || LMD_FIELD_REFORECAST_PUB_DATE.equals(metadataName)) && StringUtils.isBlank(metadataValue)){
				continue;
			}

			adjustedMetadata.add(metadataItem);
		}

		return adjustedMetadata;
	}

	public StringBuilder adjustTitle(MetaDataItem metadataItem) {
		String metadataValue = metadataItem.getValue();

		String[] titleParts = metadataValue.split(FULL_TITLE_VALUE_SEPARATOR);

		StringBuilder newTitle = new StringBuilder();

		if (titleParts.length == 3) {
			newTitle.append(titleParts[0]);
			newTitle.append(getSubtitle(titleParts));
			newTitle.append(getEdition(titleParts));
		}
		return newTitle;
	}

	private String getSubtitle(String[] titleParts) {
		String subtitle = titleParts[1].trim();
		String newSubtitle = "";

		if (StringUtils.isNotEmpty(subtitle)) {
			newSubtitle = TITLE_DELIMITER_SUBTITLE + subtitle;
		}

		return newSubtitle;
	}

	private Object getEdition(String[] titleParts) {
		String edition = titleParts[2].trim();
		String newEdition = "";

		if (StringUtils.isNotEmpty(edition) && StringUtils.isNumeric(edition)) {
			int editionNumber = Integer.parseInt(edition);
			newEdition = TITLE_DELIMITER_EDITION
					+ StandardsBookUtils
							.convertEditonNumberToOrdinal(editionNumber)
					+ " Edition";
		}

		return newEdition;
	}
}
