package com.rsicms.projectshelper.lmd.value;

public enum YesNoLmdValue {
    YES("yes"), NO("no");
    
    private String value;

    private YesNoLmdValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public static String convertToValue(boolean flag){
        if (flag){
            return YES.value;
        }
        
        return NO.value;
    }
}
