package com.rsicms.projectshelper.export;

import java.util.*;

import com.reallysi.rsuite.api.ManagedObject;
import com.rsicms.projectshelper.message.ProcessingMessageHandler;

public class MoExportResult {

	private ProcessingMessageHandler messageHandler;

	
	private Map<String, String> exportPathMapping = new HashMap<String, String>();

	public MoExportResult(ProcessingMessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}



	public ProcessingMessageHandler getMessageHandler() {
		return messageHandler;
	}

    public void addExportMapping(ManagedObject mo, String absolutePath) {
        exportPathMapping.put(mo.getId(), absolutePath);        
    }


    public String getExportedPathForMo(String moId){
        return exportPathMapping.get(moId);
    }

}
