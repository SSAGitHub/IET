package org.theiet.rsuite.journals.utils;

import org.theiet.rsuite.journals.JournalConstants;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.vfs.BrowsePath;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class JournalBrowserUtils implements JournalConstants{

	public static ContentAssemblyItem getAncestorAritcle(ExecutionContext context, BrowsePath path) throws RSuiteException{		
		return ProjectBrowserUtils.getAncestorCAbyType(context, path, CA_TYPE_ARTICLE);
	}
	
	public static ContentAssemblyItem getAncestorJournal(ExecutionContext context, BrowsePath path) throws RSuiteException{		
		return ProjectBrowserUtils.getAncestorCAbyType(context, path, CA_TYPE_JOURNAL);
	}
	
	public static ContentAssemblyItem getAncestorAritcle(ExecutionContext context, String contextRsuiteId) throws RSuiteException{		
		return ProjectContentAssemblyUtils.getAncestorCAbyType(context, contextRsuiteId, CA_TYPE_ARTICLE);
	}
	
	public static ContentAssemblyItem getAncestorJournal(ExecutionContext context, String contextRsuiteId) throws RSuiteException{		
		return ProjectContentAssemblyUtils.getAncestorCAbyType(context, contextRsuiteId, CA_TYPE_JOURNAL);
	}
}
