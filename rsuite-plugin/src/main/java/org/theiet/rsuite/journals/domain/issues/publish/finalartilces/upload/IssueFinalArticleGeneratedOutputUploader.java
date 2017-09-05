package org.theiet.rsuite.journals.domain.issues.publish.finalartilces.upload;

import static org.theiet.rsuite.journals.domain.issues.publish.common.datatype.IssuePublishWorkflowVariables.ISSUE_CA_ID;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.ArticleFactory;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.delivery.printer.IssuePrinterDelivery;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.publish.datatype.UploadGeneratedOutputsResult;
import com.rsicms.projectshelper.publish.storage.upload.VersionableGeneratedOutputUploader;

public class IssueFinalArticleGeneratedOutputUploader extends
		VersionableGeneratedOutputUploader {

	private ExecutionContext context;

	private User user;

	private String issueCaId;
	
	private Log logger;
	
	public IssueFinalArticleGeneratedOutputUploader(
			WorkflowExecutionContext context, User user, Log workflowLog) {
		super(context, user, workflowLog, context.getProcessInstanceId());
		this.context = context;
		this.user = user;
		this.logger = workflowLog;
		issueCaId = (String) context.getWorkflowVariables().get(ISSUE_CA_ID.getVariableName());
	}


	@Override
	protected ContentAssembly getOutputsCa(String articleMoId)
			throws RSuiteException {
		Article article = ArticleFactory.getAtricleBaseOnArticleXMLMoId(
				context, user, articleMoId);
		return article.getTypesetterCA();
	}

	@Override
	public void afterUpload(UploadGeneratedOutputsResult uploadResult,
			OutputGenerationResult outputGenerationResult)
			throws RSuiteException {
		Issue issue = new Issue(context, user, issueCaId);
		
		logger.info("Deliver issue to the printer...");
		IssuePrinterDelivery printerDelivery = new IssuePrinterDelivery(context);
		printerDelivery.deliverIssuePdf(issue);
	}
}

