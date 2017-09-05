package com.rsicms.projectshelper.export.impl.subexporter;

import com.reallysi.rsuite.api.*;

final class SubExporterHelper {

    public static String getRealMoId(ManagedObject mo) throws RSuiteException{
        if (mo.getTargetId() != null){
            return mo.getTargetId();
        }
        
        return mo.getId();
    }
    

}
