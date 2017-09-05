package org.theiet.rsuite.journals.domain.article;

import org.w3c.dom.Element;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.xml.XPathEvaluator;

public class ArticleContentDetail {

	private XPathEvaluator xpathEvaluator;
	
	private Element articleRootElement;

	protected ArticleContentDetail(XPathEvaluator xpathEvaluator, Element articleRootElement) {
		this.xpathEvaluator = xpathEvaluator;
		this.articleRootElement = articleRootElement;
	}

	public String getFirstPage() throws RSuiteException{		
		return xpathEvaluator.executeXPathToString("/article/front/article-meta/fpage/text()", articleRootElement);
	}
	
	public String getLastPage() throws RSuiteException{		
		return xpathEvaluator.executeXPathToString("/article/front/article-meta/lpage/text()", articleRootElement);
	}
	
}
