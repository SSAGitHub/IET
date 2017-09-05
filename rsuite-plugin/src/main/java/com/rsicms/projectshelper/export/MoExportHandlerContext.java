package com.rsicms.projectshelper.export;

import java.io.File;
import java.util.List;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.message.ProcessingMessageHandler;

public interface MoExportHandlerContext {

	public abstract ExecutionContext getContext();

	public abstract User getUser();

	public abstract ProcessingMessageHandler getMessageHandler();

	public abstract List<ManagedObject> getTopExportedMos();

	public abstract File getExportFolder();

	public abstract MoExportContainerContext getExportContainerContext();

}