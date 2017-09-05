package org.theiet.rsuite.journals.domain.journal;

import java.util.Map;

import org.theiet.rsuite.journals.domain.article.Article;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ObjectDetachOptions;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class JournalArticles {

	private ContentAssembly articlesCA;
	
	private ContentAssemblyService caService; 
	
	private ObjectDetachOptions detachOptions = new ObjectDetachOptions();
	
	private User user;
	
	private Map<String, String> referenceIdMap;
	
	JournalArticles(User user, ContentAssemblyService caService,
			ContentAssembly articlesCA) throws RSuiteException {
		super();
		this.user = user;
		this.caService = caService;
		this.articlesCA = articlesCA;
		referenceIdMap = ProjectBrowserUtils.getCaRefIdMap(user, caService, articlesCA);
	}

	public void detachArticle(Article article) throws RSuiteException{
		String articleCaId = article.getArticleCaId();
		if (referenceIdMap.containsKey(articleCaId)){
			caService.detach(user, articlesCA.getId(), referenceIdMap.get(articleCaId), detachOptions);
		}
		
	}
	
	public String getArticlesCAId(){
		return articlesCA.getId();
	}
	
}
