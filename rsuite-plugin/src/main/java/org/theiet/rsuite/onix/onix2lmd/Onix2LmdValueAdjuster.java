package org.theiet.rsuite.onix.onix2lmd;

import java.util.List;

import com.reallysi.rsuite.api.MetaDataItem;

public interface Onix2LmdValueAdjuster {

	List<MetaDataItem> adjustMetadata(List<MetaDataItem> metadataToAdd, String publicationMoId);
}
