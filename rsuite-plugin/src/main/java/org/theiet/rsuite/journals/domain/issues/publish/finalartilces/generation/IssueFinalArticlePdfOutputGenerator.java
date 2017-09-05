package org.theiet.rsuite.journals.domain.issues.publish.finalartilces.generation;

import static org.theiet.rsuite.journals.domain.article.publish.datatype.ArticlePublishWorkflowVariables.PUBLISH_TYPE;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.ArticleFactory;
import org.theiet.rsuite.journals.domain.article.publish.generation.ArticlePdfOutputGenerator;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class IssueFinalArticlePdfOutputGenerator extends
		ArticlePdfOutputGenerator {

	private ExecutionContext context;

	private User user;

	private static final String PUBLISH_TYPE_FINAL = "FINAL";

	@Override
	public void initialize(ExecutionContext context, Log logger,
			Map<String, String> variables) throws RSuiteException {
		this.context = context;
		this.user = context.getAuthorizationService().getSystemUser();
		variables.put(PUBLISH_TYPE.getVariableName(), PUBLISH_TYPE_FINAL);
		super.initialize(context, logger, variables);
	}

	@Override
	protected Map<String, String> getXml2FoXslParameters() {
		Map<String, String> parameters = super.getXml2FoXslParameters();
		parameters.put("issue-mode", "final-issue-article");

		return parameters;
	}

	@Override
	public String getOutputFileName(String moId, String defaultName)
			throws RSuiteException {
		Article article = ArticleFactory.getAtricleBaseOnArticleXMLMoId(
				context, user, moId);
		return article.getShortArticleId() + "-" + PUBLISH_TYPE_FINAL + ".pdf";
	}

}
