package org.theiet.rsuite.journals.domain.issues.publish.finalartilces.upload;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.storage.upload.GeneratedOutputUploader;
import com.rsicms.projectshelper.publish.workflow.configuration.GeneratedOutputUploaderConfiguration;

public class IssueFinalArticleGeneratedOutputUploaderConfiguration implements
		GeneratedOutputUploaderConfiguration {

	@Override
	public GeneratedOutputUploader getGeneratedOutputUploader(
			WorkflowExecutionContext context, User user) throws RSuiteException {



		return new IssueFinalArticleGeneratedOutputUploader(context, user, context.getWorkflowLog());
	}

}
