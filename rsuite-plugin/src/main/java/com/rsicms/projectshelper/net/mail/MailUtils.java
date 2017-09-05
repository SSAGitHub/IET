package com.rsicms.projectshelper.net.mail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.system.MailMessageBean;
import com.reallysi.rsuite.service.MailService;
import com.rsicms.projectshelper.utils.*;

/**
 * Send email to user action handler.
 */
public class MailUtils {

	public static void sentEmail(ExecutionContext context, MailMessage message)
			throws RSuiteException {

		List<String> attachmentsList = null;
		File mailTempFolder = null;

		try {

			if (message.getAttachments() != null
					&& message.getAttachments().size() > 0) {

				mailTempFolder = createTemporaryFolderForAttachments(context);
				attachmentsList = new ArrayList<String>();

				Map<String, byte[]> attachments = message.getAttachments();
				for (String fileName : attachments.keySet()) {
					File attachmentFile = new File(mailTempFolder, fileName);
					FileUtils.writeByteArrayToFile(attachmentFile,
							attachments.get(fileName));
					attachmentsList.add(attachmentFile.getAbsolutePath());
				}
			}
			sentEmail(context, message, attachmentsList);
		} catch (Exception e) {
			throw new RSuiteException(-1, "Unable to sent email", e);
		} finally {
			if (mailTempFolder != null && mailTempFolder.exists()) {
				try {
					FileUtils.deleteDirectory(mailTempFolder);
				} catch (Exception e) {
					// do nothing
				}
			}
		}

	}

	public static void sentMoViaEmail(ExecutionContext context,
			MailMessage message, ManagedObject mo) throws RSuiteException {
		sentMoViaEmail(context, message, mo, false);
	}
	
	public static void sentMoViaEmail(ExecutionContext context,
			MailMessage message, ManagedObject mo, boolean takePROOFWordOff) throws RSuiteException {

		if (mo == null) {
			throw new RSuiteException("MO parameter cannot be null");
		}

		if (mo.getObjectType() == ObjectType.MANAGED_OBJECT_REF) {
			User user = context.getAuthorizationService().getSystemUser();
			mo = context.getManagedObjectService().getManagedObject(user,
					mo.getTargetId());
		}

		String moFileName = getFilenameForMo(mo, takePROOFWordOff);

		try {
			message.addAttachment(moFileName,
					IOUtils.toByteArray(mo.getInputStream()));

		} catch (IOException e) {
			throw new RSuiteException(-1, "Unable to sent email", e);
		}

		sentEmail(context, message);

	}
	
	public static void attachCAsToMessage(ExecutionContext context,
			MailMessage message, List<ContentAssembly> assemblyToAttach) throws RSuiteException, IOException {

		if (assemblyToAttach != null){
			for (ContentAssembly ca : assemblyToAttach){					
				message.addAttachment(ca.getDisplayName() + ".zip", ProjectContentAssemblyUtils.zipContentAssembly(context, ca, true).toByteArray());
			}
		}
	}

	private static File createTemporaryFolderForAttachments(
			ExecutionContext context) {
		String rsuiteHome = context.getConfigurationProperties()
				.getRSuiteHome();
		File tempFolder = new File(rsuiteHome, "temp");

		String uuid = UUID.randomUUID().toString();
		File mailTempFolder = new File(tempFolder, uuid);
		mailTempFolder.mkdirs();
		return mailTempFolder;
	}

	public static void sentEmail(ExecutionContext context, MailMessage message,
			List<String> filesToAttach) throws RSuiteException {

		try {

			message.checkParameters();

			MailService mailSvc = context.getMailService();

			MailMessageBean msg = new MailMessageBean();
			msg.setFrom(message.getMailFrom());
			msg.setSubject(message.getMailSubject());
			msg.setContent(message.getMailBodyText());
			msg.setContentType("text/plain; charset=utf-8");
			
			if (filesToAttach != null && filesToAttach.size() > 0) {
				msg.setFile(filesToAttach);
			}

			for (String to : message.getMailTo()) {
				if (!StringUtils.isBlank(to)) {
					msg.setTo(to);
					mailSvc.send(msg);
				}

			}

		} catch (IOException e) {
			throw new RSuiteException(-1, "Unable to sent email", e);
		}

	}

	private static String getFilenameForMo(ManagedObject mo, boolean takePROOFWordOff) throws RSuiteException {
		String moFileName = (takePROOFWordOff) ? mo.getDisplayName().
				replaceAll("-(?i)PROOF", "") : mo.getDisplayName();
		
		if (mo.isNonXml())
			return moFileName;

		return moFileName + ".xml";
	}

	public static String obtainEmailSubject(ExecutionContext context, String titleProperty)
			throws RSuiteException {
		return ProjectPluginProperties.getPropertyTargetResource(titleProperty, "");
	}

	public static File obtainMailBodyFile(ExecutionContext context, String mailBodyProperty) {
		String mailBodyPath = context.getConfigurationProperties().getProperty(
				mailBodyProperty, "");
		File mailBodyFile = new File(mailBodyPath);
		return mailBodyFile;
	}
}