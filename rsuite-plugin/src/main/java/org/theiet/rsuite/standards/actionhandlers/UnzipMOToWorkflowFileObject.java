package org.theiet.rsuite.standards.actionhandlers;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.utils.ZipUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.AbstractBaseActionHandler;
import com.reallysi.rsuite.api.workflow.FileWorkflowObject;
import com.reallysi.rsuite.api.workflow.MoListWorkflowObject;
import com.reallysi.rsuite.api.workflow.MoWorkflowObject;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;

public class UnzipMOToWorkflowFileObject extends AbstractBaseActionHandler {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private Log classLog = LogFactory.getLog(UnzipMOToWorkflowFileObject.class);

	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {
		Log log = context.getWorkflowLog();
		log.info("Entered to UnzipMoToWO");
		User user = context.getAuthorizationService().getSystemUser();
		ManagedObjectService moSvc = context.getManagedObjectService();

		// Get the ingested content
		String baseWorkFolder = context.getConfigurationProperties()
				.getProperty("rsuite.workflow.baseworkfolder", "/tmp");
		String workFolderPath = baseWorkFolder + "/"
				+ context.getProcessInstance().getId();
		File contentsDir = new File(workFolderPath, "contents");
		contentsDir.mkdirs();
		File ditaDir = new File(workFolderPath, "dita");
		ditaDir.mkdirs();

		MoListWorkflowObject rsuiteContents = context.getMoListWorkflowObject();
		if (rsuiteContents.getMoList().size() != 1) {
			log.info("Unexpected number of workflow objects. Expected exactly one, got "
					+ rsuiteContents.getMoList().size());
			return;
		}

		MoWorkflowObject wo = rsuiteContents.getMoList().get(0);

		ManagedObject mo = moSvc.getManagedObject(user, wo.getMoid());
		String fullFileName = mo.getDisplayName();
		String baseFileName = FilenameUtils.getBaseName(fullFileName);
		String extension = FilenameUtils.getExtension(fullFileName);

		File moFile = new File(contentsDir, fullFileName);
		IOUtils.copy(mo.getInputStream(), new FileOutputStream(moFile));

		File outDir = ZipUtils.unzip(moFile, ditaDir);

		context.setFileWorkflowObject(new FileWorkflowObject(outDir));

		context.setVariable("fullFileName", fullFileName);
		context.setVariable("baseFileName", baseFileName);
		context.setVariable("extension", extension);

		log.info("Removing zip file. " + mo.getDisplayName());
		moSvc.checkOut(getSystemUser(), mo.getId());
		moSvc.destroy(user, mo.getId(), null);

		log.info("Done.");
	}

}
