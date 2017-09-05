package com.rsicms.projectshelper.lmd.value;

import java.util.List;

import com.reallysi.rsuite.api.MetaDataItem;
import com.rsicms.projectshelper.datatype.RSuiteLmd;

public final class LmdUtils {

    private LmdUtils(){};
    
    public static void addMetadata(List<MetaDataItem> metadataItems, RSuiteLmd lmd, String value){
        metadataItems.add(createMetadataItem(lmd, value));
    }
    
    public static void addMetadata(List<MetaDataItem> metadataItems, RSuiteLmd lmd, boolean value){
        metadataItems.add(createMetadataItem(lmd, value));
    }
    
    public static MetaDataItem createMetadataItem(RSuiteLmd lmd, String value){
        return new MetaDataItem(lmd.getLmdName(), value);
    }
    
    public static MetaDataItem createMetadataItem(RSuiteLmd lmd, boolean value){
        String stringValue = YesNoLmdValue.convertToValue(value);
        return createMetadataItem(lmd, stringValue);
    }
}
