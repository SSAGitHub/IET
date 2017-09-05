package com.rsicms.projectshelper.publish.storage.datatype;

import com.rsicms.projectshelper.datatype.RSuiteCaType;

public enum OutputCaTypes implements RSuiteCaType {
    
    OUTPUT_EVENT("outputEvent"),
    OUTPUT_HISTORY("outputHistory"),
    OUTPUTS("outputs");
    
    private String typeName;

    private OutputCaTypes(String typeName) {
        this.typeName = typeName;
    }



    @Override
    public String getTypeName() {
        return typeName;
    }
    
    public boolean isSameType(String type){
        return typeName.equals(type);
    }

}
