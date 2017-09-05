package com.rsicms.projectshelper.export.impl.subexporter;

import java.util.*;

import com.rsicms.projectshelper.datatype.ManagedObjectWrapper;

public class SubExporterResult {

	private Set<ManagedObjectWrapper> moToProcess;
	
	private Map<String, String> exportPathMap;

	private static SubExporterResult emptyResult = new SubExporterResult();
	
    protected SubExporterResult(Set<ManagedObjectWrapper> moToProcess,
            Map<String, String> exportPathMap) {
        this.moToProcess = moToProcess;
        this.exportPathMap = exportPathMap;
    }
    
    protected SubExporterResult(Set<ManagedObjectWrapper> moToProcess
            ) {
        this(moToProcess, new HashMap<String, String>());
    }
    
    private SubExporterResult(){
        this(new HashSet<ManagedObjectWrapper>(), new HashMap<String, String>());
    }

    public Set<ManagedObjectWrapper> getMoToProcess() {
        return moToProcess;
    }

    public Map<String, String> getExportPathMap() {
        return exportPathMap;
    }


    public static SubExporterResult getEmptyResult(){
        return emptyResult;
    }

}
