package org.theiet.rsuite.journals.domain.issues.publish.common.areatree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IssueArticlesInformation {

	private Map<String, IssueArticleInfo> articleInfoMap;
	
	public IssueArticlesInformation(Map<String, IssueArticleInfo> articleInfoMap) {
		this.articleInfoMap = articleInfoMap;
	}

	public List<IssueArticleInfo> getArticlesInfo(){
		return  new ArrayList<IssueArticleInfo>(articleInfoMap.values());		
	}
	
	public IssueArticleInfo getArticleInfo(String doi){
		String normalizedDoi = doi.replaceAll("[^\\p{L}\\d]", "");
		return articleInfoMap.get(normalizedDoi);
	}
}
