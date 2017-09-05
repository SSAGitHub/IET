package org.theiet.rsuite.iettv.domain.datatype;


public enum VideoStatus {

    PUBLISHED("Published"),  WITHDRAWN( "Withdrawn");
    
    private String statusName;
    
    private VideoStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
    
    
}
