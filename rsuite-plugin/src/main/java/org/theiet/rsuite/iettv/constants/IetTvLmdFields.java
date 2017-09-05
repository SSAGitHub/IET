package org.theiet.rsuite.iettv.constants;

import com.reallysi.rsuite.api.MetaDataItem;
import com.rsicms.projectshelper.datatype.RSuiteLmd;

public enum IetTvLmdFields implements RSuiteLmd  {

    VIDEO_ID("iettv_video_id"), IS_WITHDRAWN("iettv_video_withdrawn"), CHANNEL_YEAR_ID("iettv_channel_year_id");
    
    private String lmdName;
    
    private IetTvLmdFields(String lmdname) {
        this.lmdName = lmdname;
    }

    public String getLmdName() {
        return lmdName;
    }
    
    public MetaDataItem createMetadataItem(String value){
        return new MetaDataItem(lmdName, value);
    }
    
}

