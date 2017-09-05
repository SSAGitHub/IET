package org.theiet.rsuite.journals.domain.article;

import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.journals.domain.constants.JournalsCaTypes;
import org.theiet.rsuite.journals.utils.JournalUtils;
import org.theiet.rsuite.utils.XpathUtils;
import org.w3c.dom.Element;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.reallysi.rsuite.service.SearchService;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public final class ArticleFactory {

	private static XPathFactory xPathFactory = XPathFactory.newInstance();

	private ArticleFactory() {
	}

	public static Article getIfArticleContainer(ExecutionContext context, User user, ManagedObject mo) throws RSuiteException{
		
		if (mo.getObjectType() == ObjectType.CONTENT_ASSEMBLY){
			ContentAssemblyService caService = context.getContentAssemblyService();
			ContentAssembly ca = caService.getContentAssembly(user, mo.getId());
			String type = ca.getType();
			
			if (JournalsCaTypes.ARTICLE.isTypeOf(type)){
				return new Article(context, user, ca);
			}
			
		}
		return null;
	}
	
	public static Article getArticleContainerBaseOnArticleId(
			ExecutionContext context, User user, Log logger, String articleId)
			throws RSuiteException {

		SearchService srchSvc = context.getSearchService();
		ManagedObject articleMO = getArticleContainerMo(user,
				createArticleIdQuery(articleId), srchSvc);

		return new Article(context, user, articleMO.getId());

	}

	private static ManagedObject getArticleContainerMo(User user,
			String articleQuery, SearchService srchSvc) throws RSuiteException {

		List<ManagedObject> caSet = srchSvc.executeXPathSearch(user,
				articleQuery, 1, 1);
		int nArticles = caSet.size();

		if (nArticles != 1) {
			String msg = "Article query " + articleQuery + " returned "
					+ nArticles
					+ ". It should be a exactly one article for query: "
					+ articleQuery;
			throw new RSuiteException(msg);
		}

		ManagedObject articleMO = caSet.get(0);
		return articleMO;
	}

	private static String createArticleIdQuery(String articleId) {
		String normaLizedArticleId = JournalUtils.normalizeArticleId(articleId);
		return XpathUtils
				.resolveRSuiteFunctionsInXPath("/rs_ca_map/rs_ca[rmd:get-type(.) = 'article' and rmd:get-display-name(.) = '"
						+ normaLizedArticleId + "']");
	}

	public static Article getAtricleBaseOnArticleXMLMoId(
			ExecutionContext context, User user, String articleXMLMoId)
			throws RSuiteException {
		ManagedObjectService managedObjectService = context
				.getManagedObjectService();
		ManagedObject managedObject = managedObjectService.getManagedObject(
				user, articleXMLMoId);

		String articleDoi = getArticleDoiFromArticleMO(managedObject);
		String articleDoiQuery = createArticleDoiQuery(articleDoi);
		
		ManagedObject articleContainerMo = getArticleContainerMo(user,
				articleDoiQuery, context.getSearchService());
		return new Article(context, user,
				ProjectBrowserUtils.getContentAssembly(context,
						articleContainerMo.getId()));
	}

	private static String getArticleDoiFromArticleMO(ManagedObject managedObject)
			throws RSuiteException {

		try {
			XPath xpath = xPathFactory.newXPath();
			XPathExpression articleIdXpathExpression = xpath
					.compile("/article/front/article-meta/article-id[@pub-id-type='doi']");
			Element articleElement = managedObject.getElement();

			return (String) articleIdXpathExpression.evaluate(articleElement,
					XPathConstants.STRING);

		} catch (XPathExpressionException e) {
			throw new RSuiteException(0, "Unable to find article id in MO "
					+ managedObject.getId());
		}

	}

	private static String createArticleDoiQuery(String articleDoi) {
		return XpathUtils
				.resolveRSuiteFunctionsInXPath("/rs_ca_map/rs_ca[rmd:get-type(.) = 'article' and rmd:get-lmd-value(., 'doi') = '"
						+ articleDoi + "']");
	}
}
