package org.theiet.rsuite.journals.domain.issues.publish.finalartilces;

import static org.theiet.rsuite.journals.domain.issues.publish.common.datatype.IssuePublishWorkflowVariables.ISSUE_CA_ID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.theiet.rsuite.datamodel.types.IetWorkflows;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.datatype.IssueArticles;
import org.theiet.rsuite.journals.domain.issues.publish.finalartilces.export.IssueFinalArticlesContentExporterConfiguration;
import org.theiet.rsuite.journals.domain.issues.publish.finalartilces.generation.IssueFinalArticlePdfOutputGeneratorConfiguration;
import org.theiet.rsuite.journals.domain.issues.publish.finalartilces.notification.IssueFinalArticleSendNotificationConfiguration;
import org.theiet.rsuite.journals.domain.issues.publish.finalartilces.upload.IssueFinalArticleGeneratedOutputUploaderConfiguration;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.workflow.ProcessInstanceSummaryInfo;
import com.rsicms.projectshelper.publish.workflow.launcher.PublishWorkflowLauncher;
import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;
import com.rsicms.projectshelper.workflow.WorkflowUtils;

public final class IssueFinalArticlesPdfWorkflowLauncher {

	private IssueFinalArticlesPdfWorkflowLauncher() {
	}

	public static ProcessInstanceSummaryInfo launchFinalArticlesPdfWorkflowForIssue(
			RemoteApiExecutionContext context, CallArgumentList args,
			Issue issue) throws RSuiteException {

		User user = context.getSession().getUser();

		Map<RSuiteWorkflowVariable, Object> workflowVariables = WorkflowUtils
				.createCommonWorkflowVariables(context, args);
		return launchIssueFinalArticlesWorkflow(context, user, issue,
				workflowVariables);
	}

	private static ProcessInstanceSummaryInfo launchIssueFinalArticlesWorkflow(
			ExecutionContext context, User user, Issue issue,
			Map<RSuiteWorkflowVariable, Object> workflowVariables)
			throws RSuiteException {

		PublishWorkflowLauncher.Builder builder = new PublishWorkflowLauncher.Builder(
				context, user);
		builder.workflowName(IetWorkflows.IET_PUBLISH_WORKFLOW
				.getWorkflowName());
		IssueArticles issueArticles = issue.getIssueArticles();
		List<Article> articles = issueArticles.getArticles();

		Map<String, String> articleMoIdMap = addContextMOs(builder, articles);
		setUpWorkflowVariables(issue, workflowVariables, builder,
				articleMoIdMap);

		builder.exportConfiguration(IssueFinalArticlesContentExporterConfiguration.class);
		builder.outputGeneratorConfiguration(IssueFinalArticlePdfOutputGeneratorConfiguration.class);
		builder.generatedOutputUploaderConfiguration(IssueFinalArticleGeneratedOutputUploaderConfiguration.class);
		builder.sendNotificationConfiguration(IssueFinalArticleSendNotificationConfiguration.class);
		PublishWorkflowLauncher workflowLauncher = builder.build();
		return workflowLauncher.launchPublishWorkflow();
	}

	private static Map<String, String> addContextMOs(
			PublishWorkflowLauncher.Builder builder, List<Article> articles)
			throws RSuiteException {
		Map<String, String> articleMoIdMap = new HashMap<String, String>();

		for (Article article : articles) {
			ManagedObject articleXMLMo = article.getXMLMo();
			builder.addContextMo(articleXMLMo);
		}
		return articleMoIdMap;
	}

	private static void setUpWorkflowVariables(Issue issue,
			Map<RSuiteWorkflowVariable, Object> workflowVariables,
			PublishWorkflowLauncher.Builder builder,
			Map<String, String> articleMaps) {
		workflowVariables.put(ISSUE_CA_ID, issue.getIssueCa().getId());
		builder.addWorkflowVariables(workflowVariables);
	}
}
