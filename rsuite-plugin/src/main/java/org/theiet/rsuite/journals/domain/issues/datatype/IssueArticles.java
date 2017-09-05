package org.theiet.rsuite.journals.domain.issues.datatype;

import java.util.*;

import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.constants.JournalsCaTypes;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.ObjectAttachOptions;
import com.reallysi.rsuite.api.control.ObjectDetachOptions;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class IssueArticles {

	private ExecutionContext context;
	
	private User user;
	
	private ContentAssembly issueArticlesCa;
	
	private ContentAssemblyService caService;
	
	private ObjectAttachOptions attachOpts = new ObjectAttachOptions();
	
	IssueArticles(ExecutionContext context, User user,
			ContentAssembly issueArticlesCa) {
		super();
		this.context = context;
		this.user = user;
		this.issueArticlesCa = issueArticlesCa;
		this.caService = context.getContentAssemblyService();
	}

	public ContentAssembly getArtilcesCA() throws RSuiteException {
		return issueArticlesCa;
	}
	
	public List<Article> getArticles() throws RSuiteException {
		List<ContentAssembly> articleCAs = ProjectBrowserUtils.getChildrenCaByType(context, getArtilcesCA(), JournalsCaTypes.ARTICLE);
		
		List<Article> articles = new ArrayList<>();
		
		for (ContentAssembly articleCA : articleCAs){
			articles.add(new Article(context, user, articleCA));
		}
		
		return articles;
	}

	public void attachArticle(Article article) throws RSuiteException {
		caService.attach(user, issueArticlesCa.getId(), article.getArticleCA(), attachOpts);		
	}
	
}
