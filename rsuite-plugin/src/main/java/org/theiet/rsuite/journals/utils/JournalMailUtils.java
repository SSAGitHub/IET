package org.theiet.rsuite.journals.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.domain.mail.IetMailUtils;
import org.theiet.rsuite.journals.JournalConstants;

import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.security.LocalUserManager;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.rsicms.projectshelper.utils.ProjectUserUtils;

public class JournalMailUtils implements JournalConstants{
	
	private JournalMailUtils() {
	}
	
	public static Map<String, String> setUpVariablesMap(
			ExecutionContext context, User user,
			ContentAssemblyItem journalCa, ContentAssemblyItem articleCa)
			throws RSuiteException {
	
		String authorSalutation = articleCa.getLayeredMetadataValue(LMD_FIELD_AUTHOR_SALUTATION);
		String authorFirstName = articleCa.getLayeredMetadataValue(LMD_FIELD_AUTHOR_FIRST_NAME);
		String authorSurname = articleCa.getLayeredMetadataValue(LMD_FIELD_AUTHOR_SURNAME);
		String authorFullName = authorFirstName + " " + authorSurname;
		String articleTitle = articleCa.getLayeredMetadataValue(LMD_FIELD_ARTICLE_TITLE);
		
		String userName = user.getFullName();
		
		if (ProjectUserUtils.isSystemUser(context, user)){
			userName = ""; 
		}
		
		String editorialAssistantName = getEditorialAssistantName(context, userName, journalCa);
	
		Map<String, String> variables = new HashMap<String, String>();
		variables.put(EMAIL_VAR_JOURNAL_NAME, journalCa.getDisplayName());
		variables.put(EMAIL_VAR_CORRESPONDING_AUTHOR_SALUTATION, authorSalutation);
		variables.put(EMAIL_VAR_PAPER_TITLE, articleTitle);
		variables.put(EMAIL_VAR_MANUSCRIPT_ID, articleCa.getDisplayName());
		variables.put(EMAIL_VAR_EDITORIAL_ASSISTANT_NAME, editorialAssistantName);
		variables.put(EMAIL_VAR_CORRESPONDING_AUTHOR_FULL_NAME, authorFullName);
		variables.put(EMAIL_VAR_CORRESPONDING_AUTHOR_FIRST_NAME, authorFirstName);
		variables.put(EMAIL_VAR_CORRESPONDING_AUTHOR_SURNAME, authorSurname);
		
		return variables;
	}


	private static String getEditorialAssistantName(ExecutionContext context, String userName,
			ContentAssemblyItem journalCaItem) throws RSuiteException {
		User user = context.getAuthorizationService().getSystemUser();
		if (!StringUtils.isBlank(userName) && !userName.equals("none")) {
			return userName;
		}
		
		ContentAssemblyService caSrv = context.getContentAssemblyService();
		ContentAssemblyItem journal = caSrv.getContentAssembly(user, journalCaItem.getId());
		journal.getLayeredMetadataValue(LMD_FIELD_EDITORIAL_ASSISTANT);
		String editorialAssistantName = journal.getLayeredMetadataValue(LMD_FIELD_EDITORIAL_ASSISTANT);
		
		return StringUtils.isBlank(editorialAssistantName) ? userName : editorialAssistantName;
	}

	public static String obtainEmailFrom(ExecutionContext context,
			ContentAssemblyItem journalCa) throws RSuiteException {
		String emailFrom = journalCa
				.getLayeredMetadataValue(LMD_FIELD_JOURNAL_EMAIL);
		if (emailFrom == null) {
			emailFrom = IetMailUtils.obtainEmailFrom();
		}
		return emailFrom;
	}


	public static String getTypesetterEmail(ExecutionContext context,
			ContentAssemblyItem journalCa) throws RSuiteException {
		String typeSetterUser = journalCa
				.getLayeredMetadataValue(IetConstants.LMD_FIELD_TYPESETTER_USER);
		LocalUserManager userManager = context.getAuthorizationService()
				.getLocalUserManager();
		User typesetter = userManager.getUser(typeSetterUser);
	
		if (typesetter == null) {
			throw new RSuiteException(
					"Typesetter is not assinged to the journal");
		}
	
		String recipientEmailAddress = typesetter.getEmail();
		return recipientEmailAddress;
	}

}
