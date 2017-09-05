package org.theiet.rsuite.standards.webservices;

import java.util.List;

import org.apache.commons.logging.*;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.advisors.form.AddMoFromTemplateFormAdvisor;
import org.theiet.rsuite.utils.ContentDisplayUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.api.remoteapi.result.*;
import com.reallysi.rsuite.service.*;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class AddMoFromTemplateWebService extends DefaultRemoteApiHandler
		implements StandardsBooksConstans {

	private static Log log = LogFactory.getLog(AddMoFromTemplateWebService.class);
	
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {
		try {
			
			ContentAssemblyService caSvc = context.getContentAssemblyService();
			User user = context.getSession().getUser();
			String typescriptCaId = args.getFirstValue(PARAM_RSUITE_ID);
			String templateMoId = args.getFirstValue(AddMoFromTemplateFormAdvisor.FORM_PARAM_TEMPLATES);
			
			String moFileName = args.getFirstValue(AddMoFromTemplateFormAdvisor.FORM_PARAM_NEW_MO_FILE_NAME);
			String uniquenessAliasName = createUniquenessAliasName(context, moFileName, typescriptCaId);
			valideMoAliasFileName(context, user, uniquenessAliasName);
			
			String targetContainerId = getTargetContainerId(typescriptCaId, caSvc, user);
			
			Alias alias = new Alias(uniquenessAliasName, ALIAS_TYPE_FILENAME);
			
			createMoFromTemplate(context, user, templateMoId, targetContainerId, alias);
			
			
			String message =   "<html><body><div><p>The new manage object has been crearted</p>";
			
			return ContentDisplayUtils.getResultWithLabelRefreshing(MessageType.SUCCESS, "Add new template", message, "500", targetContainerId);
		} catch (RSuiteException ex) {
			log.debug(ex.getMessage(), ex);
			return new MessageDialogResult(MessageType.ERROR, "Add new mo from template", 
					 ex.getMessage());
		}		
	}
	
	private String getTargetContainerId(String typescriptCaId,
			ContentAssemblyService caSvc, User user) throws RSuiteException {
		String targetContainerId = typescriptCaId;
		ContentAssembly typescriptCA = caSvc.getContentAssembly(user, typescriptCaId);
		List<? extends ContentAssemblyItem> typescriptChildrens = typescriptCA.getChildrenObjects();
		for (ContentAssemblyItem typescriptChildren : typescriptChildrens) {
			if (typescriptChildren.getDisplayName() != null && typescriptChildren.getDisplayName().equalsIgnoreCase(DISPLAY_NAME_STANDARDS_CONTENT)) {
				targetContainerId = ((ContentAssemblyReference) typescriptChildren).getTargetId();
			}
		}
		return targetContainerId;
	}

	private void createMoFromTemplate(ExecutionContext context, User user,
			String templateMoId, String targetContainerId, Alias alias)
			throws RSuiteException {
		
		ManagedObjectService moSvc = context.getManagedObjectService();
		
		Alias[] aliases =  {alias};
		ObjectCopyOptions copyOptions = new ObjectCopyOptions();
		copyOptions.setAliases(aliases);
		copyOptions.setExternalFileName(alias.getText());
		
		ObjectAttachOptions attachOptions = new ObjectAttachOptions();
		
		ManagedObject mo =  ProjectContentAssemblyUtils.copyAndAttach(context, user,  new VersionSpecifier(templateMoId), targetContainerId, copyOptions, attachOptions);
		
		String newMoId = mo.getTargetId() == null ? mo.getId() : mo.getTargetId();
		
		moSvc.deleteAllAliases(user, newMoId);
		moSvc.setAlias(user, newMoId, alias);
		
		
	}

	private void valideMoAliasFileName(
			RemoteApiExecutionContext context, User user, String nameMoFileName)
			throws RSuiteException {
		ManagedObjectService moSvc = context.getManagedObjectService();
		ManagedObject mo = moSvc.getObjectByAlias(user, nameMoFileName);
		if (mo != null){
			throw new RSuiteException("Managed object with filename " + nameMoFileName + " already exist");
		}
	}
	

	private String createUniquenessAliasName (ExecutionContext context, String moFileName, String typescriptCaId) throws RSuiteException {
		User systemUser = context.getAuthorizationService().getSystemUser();
		ManagedObjectService moServ = context.getManagedObjectService();

		List<ReferenceInfo> refernces = moServ.getDependencyTracker()
				.listAllReferences(systemUser, typescriptCaId);
		
		if (refernces.size() > 0) {
			ReferenceInfo refernceInfo = refernces.get(0);
			String browserUri = refernceInfo.getBrowseUri();
			ContentAssemblyItem ancestorCAItem =  ProjectBrowserUtils.getAncestorCAbyType(
					context, browserUri, StandardsBooksConstans.CA_TYPE_STANDARDS_BOOK_EDITION);

			if (ancestorCAItem != null) {
				moFileName = ancestorCAItem.getLayeredMetadataValue("product_code") + "_" + moFileName;
			}
		}

		return moFileName;
	}
	

}
