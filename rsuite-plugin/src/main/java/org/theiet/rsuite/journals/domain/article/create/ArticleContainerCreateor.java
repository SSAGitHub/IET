package org.theiet.rsuite.journals.domain.article.create;

import java.util.HashMap;
import java.util.Map;

import org.theiet.rsuite.domain.permissions.PermissionsUtils;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.constants.JournalsCaTypes;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.journals.domain.journal.JournalArticles;
import org.theiet.rsuite.journals.utils.JournalWorkflowUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.SecurityService;
import com.rsicms.projectshelper.datatype.RSuiteCaType;
import com.rsicms.projectshelper.utils.ProjectCaUtils;

public class ArticleContainerCreateor {

	private ExecutionContext context;
	
	private User user;
	
	private SecurityService securityService;
	
	private static final String  AUTHOR_CORRECTIONS = JournalsCaTypes.AUTHOR_CORRECTIONS.getDefaultContainerName();
	
	private static final String  TYPESETTER = JournalsCaTypes.TYPESETTER.getDefaultContainerName();
	
	private static final String[] CONTAINER_LIST = {
		"Supplementary Materials",
		"Documentation",
		AUTHOR_CORRECTIONS,
		TYPESETTER,
		"Manuscripts"};

	private static final Map<String, RSuiteCaType> containerTypes = new HashMap<String, RSuiteCaType>();
	{
		containerTypes.put(AUTHOR_CORRECTIONS,  JournalsCaTypes.AUTHOR_CORRECTIONS);
		containerTypes.put(TYPESETTER, JournalsCaTypes.TYPESETTER);
	}
	
	public ArticleContainerCreateor(ExecutionContext context, User user) {
		super();
		this.context = context;
		this.user = user;
		securityService = context.getSecurityService();
	}

	public Article createArticle(Journal journal, String articleId) throws RSuiteException {
		
		checkIfArticleAlreadyExist(articleId);
		JournalArticles journalArticlesContainer = journal.getArticles();;
		ContentAssembly articleCa = createArticleStructure(journalArticlesContainer, articleId);
		
		return new Article(context, user, articleCa);
	}

	private ContentAssembly createArticleStructure(
			JournalArticles journalArticlesContainer, String articleId) throws RSuiteException {
		String articlesCAId = journalArticlesContainer.getArticlesCAId();
		
		ContentAssembly articleCA = ProjectCaUtils.createContentAssembly(context, articlesCAId, articleId,JournalsCaTypes.ARTICLE);
		String articleCAId = articleCA.getId();
		PermissionsUtils.setAdminOnlyDeleteACL(securityService, user, articleCAId);
		
		createArticleSubContainers(articleCAId);
		
		return context.getContentAssemblyService().getContentAssembly(user, articleCAId);
	}

	private void createArticleSubContainers(String articleCAId)
			throws RSuiteException {
		for (String container : CONTAINER_LIST) {
			ContentAssembly containerCa = ProjectCaUtils.createContentAssembly(context, articleCAId, container, containerTypes.get(container));
			String containerCaId = containerCa.getId();
			PermissionsUtils.setDefaultACL(securityService, user, containerCaId);
		}
	}

	private void checkIfArticleAlreadyExist(String articleId) throws RSuiteException {
		
		int articleCount = JournalWorkflowUtils.getArticleCaCount(context, articleId);
		if (articleCount != 0) {
			throw new RSuiteException("Article " + articleId + " exists in database " + articleCount + " time(s)");
		}
		
	}
}
