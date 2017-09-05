package com.rsicms.projectshelper.export.impl.exportercontext;

import com.rsicms.projectshelper.export.MoExportContainerContext;
import com.rsicms.projectshelper.export.MoExportHandler;
import com.rsicms.projectshelper.export.MoExportConfiguration;
import com.rsicms.projectshelper.message.ProcessingMessageHandler;

public class MoExportContext {

	private MoExportHandler exportHandler;

	private MoExportConfiguration exportConfiguration;

	private ProcessingMessageHandler messageHandler;

	private MoExportContainerContext exportContainerContext;
	
	public MoExportContext(MoExportConfiguration exportConfiguration) {
		this.exportHandler = exportConfiguration.createMoExportHandler();
		this.exportConfiguration = exportConfiguration;
		this.messageHandler = new ProcessingMessageHandler();
	}

	public MoExportHandler getExportHandler() {
		return exportHandler;
	}

	public MoExportConfiguration getExportConfiguration() {
		return exportConfiguration;
	}

	public ProcessingMessageHandler getMessageHandler() {
		return messageHandler;
	}

	public MoExportContainerContext getExportContainerContext() {
		return exportContainerContext;
	}

	public void setExportContainerContext(
			MoExportContainerContext exportContainerContext) {
		this.exportContainerContext = exportContainerContext;
	}	

}
