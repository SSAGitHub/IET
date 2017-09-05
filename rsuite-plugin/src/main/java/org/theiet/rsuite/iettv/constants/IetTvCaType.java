package org.theiet.rsuite.iettv.constants;

import com.rsicms.projectshelper.datatype.RSuiteCaType;

public enum IetTvCaType implements RSuiteCaType {

    VIDEO_RECORD("iettv_video_record"), CHANNEL("iettv_channel"), YEAR("iettv_year"), 
    VIDEO_DOMAIN("iettv"), RELATED_FILES("iettv_video_related_files", "Related Files"), NOTES("iettv_video_notes", "Notes");

    private String name;
    
    private String defaultContainerName = "";

    private IetTvCaType(String name) {
        this.name = name;
    }

    private IetTvCaType(String name, String defaultContainerName) {
        this.name = name;
        this.defaultContainerName = defaultContainerName;
    }


    public String getDefaultContainerName() {
        return defaultContainerName;
    }

    @Override
    public String getTypeName() {
        return name;
    }

}
