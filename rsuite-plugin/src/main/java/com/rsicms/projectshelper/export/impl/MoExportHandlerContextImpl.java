package com.rsicms.projectshelper.export.impl;

import java.io.File;
import java.util.List;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.export.*;
import com.rsicms.projectshelper.export.impl.exportercontext.MoExportContext;
import com.rsicms.projectshelper.message.ProcessingMessageHandler;

public class MoExportHandlerContextImpl implements MoExportHandlerContext {

	private ExecutionContext context;
	
	private User user;

	private List<ManagedObject> topExportedMos;
	
	private File exportFolder;

	private MoExportContext moExportContext;

	public MoExportHandlerContextImpl(ExecutionContext context, User user,
	        List<ManagedObject> topExportedMo, File exportFolder,
			MoExportContext moExportContext) {
		super();
		this.context = context;
		this.user = user;
		this.topExportedMos = topExportedMo;
		this.exportFolder = exportFolder;
		this.moExportContext = moExportContext;
	}

	/* (non-Javadoc)
	 * @see com.rsicms.projectshelper.export.impl.MoExportHandlerContext#getContext()
	 */
	@Override
	public ExecutionContext getContext() {
		return context;
	}

	/* (non-Javadoc)
	 * @see com.rsicms.projectshelper.export.impl.MoExportHandlerContext#getUser()
	 */
	@Override
	public User getUser() {
		return user;
	}

	/* (non-Javadoc)
	 * @see com.rsicms.projectshelper.export.impl.MoExportHandlerContext#getMessageHandler()
	 */
	@Override
	public ProcessingMessageHandler getMessageHandler() {
		return moExportContext.getMessageHandler();
	}

	/* (non-Javadoc)
	 * @see com.rsicms.projectshelper.export.impl.MoExportHandlerContext#getTopExportedMo()
	 */
	@Override
	public List<ManagedObject> getTopExportedMos() {
		return topExportedMos;
	}

	/* (non-Javadoc)
	 * @see com.rsicms.projectshelper.export.impl.MoExportHandlerContext#getExportFolder()
	 */
	@Override
	public File getExportFolder() {
		return exportFolder;
	}

	/* (non-Javadoc)
	 * @see com.rsicms.projectshelper.export.impl.MoExportHandlerContext#getExportContainerContext()
	 */
	@Override
	public MoExportContainerContext getExportContainerContext() {
		return moExportContext.getExportContainerContext();
	}

}
