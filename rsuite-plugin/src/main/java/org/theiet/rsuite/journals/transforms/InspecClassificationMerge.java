package org.theiet.rsuite.journals.transforms;

import java.util.HashSet;
import java.util.Set;

import org.theiet.rsuite.journals.domain.article.Article;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.VersionType;
import com.reallysi.rsuite.api.control.ObjectSource;
import com.reallysi.rsuite.api.control.ObjectUpdateOptions;
import com.reallysi.rsuite.api.control.XmlObjectSource;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.xml.XPathEvaluator;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class InspecClassificationMerge {

	private static final String ELEMENT_KWD_GROUP = "kwd-group";
	private static final Set<String> KWD_GROUP_NEXT_SIBLINGS = new HashSet<String>();
	
	static
	{
		KWD_GROUP_NEXT_SIBLINGS.add("funding-group");
		KWD_GROUP_NEXT_SIBLINGS.add("conference");
		KWD_GROUP_NEXT_SIBLINGS.add("counts");
		KWD_GROUP_NEXT_SIBLINGS.add("custom-meta-group");
	}

	public static void mergeInspecClassification(ExecutionContext context,
			Article article) throws RSuiteException {

		
		ContentAssembly typesetterCa = article.getTypesetterCA();
		User user = context.getAuthorizationService().getSystemUser();

		if (typesetterCa == null) {
			throw new RSuiteException("Unable to find typesetter CA");
		}
		

		ManagedObject nlmMo = article.getXMLMo();

		if (nlmMo == null) {
			throw new RSuiteException("Unable to find NLM MO");
		}

		ManagedObject inspecMo = ProjectBrowserUtils.getChildMoByNodeName(context,
				typesetterCa, "article-meta");

		if (inspecMo == null) {
			throw new RSuiteException("Unable to find inpsec MO");
		}

		ManagedObjectService moService = context.getManagedObjectService();
		moService.checkOut(user, nlmMo.getId());

		Element nlmRoot = nlmMo.getElement();

		mergeInspecClassificationElement(context, nlmRoot,
				inspecMo.getElement());

		ObjectSource src = new XmlObjectSource(nlmRoot);

		moService.update(user, nlmMo.getId(), src, new ObjectUpdateOptions());

		moService.checkIn(user, nlmMo.getId(), VersionType.MAJOR,
				"Merge inspec classfication", true);

	}

	public static void mergeInspecClassificationElement(
			ExecutionContext context, Element nlmDocument,
			Element inspecClassification) throws RSuiteException {

		XPathEvaluator xpathEval = context.getXmlApiManager()
				.getXPathEvaluator();

		Element nlmArticleMetadata = (Element) xpathEval.executeXPathToNode(
				"/article/front/article-meta", nlmDocument);

		mergeInspeMetadata(inspecClassification, nlmArticleMetadata);
	}

	private static void mergeInspeMetadata(Element inspecClassification,
			Element nlmArticleMetadata) throws RSuiteException {
		if (nlmArticleMetadata == null) {
			throw new RSuiteException(
					"Unable to find article-meta element in the NLM document");
		}

		Node lastChild = nlmArticleMetadata.getLastChild();

		while (lastChild != null) {

			if (lastChild instanceof Element) {
				String name = getLocalName(lastChild);
				if (!KWD_GROUP_NEXT_SIBLINGS.contains(name)) {
					break;
				}
			}

			lastChild = lastChild.getPreviousSibling();
		}

		// remove existing kwd-group
		while (ELEMENT_KWD_GROUP.equals(getLocalName(lastChild)) || !(lastChild instanceof Element)) {
			Node kwdGroup = lastChild;
			lastChild = lastChild.getPreviousSibling();
			lastChild.getParentNode().removeChild(kwdGroup);
		}

		Node insertContext = lastChild.getNextSibling();
		Document nlmDocument = nlmArticleMetadata.getOwnerDocument();

		NodeList inspecChilds = inspecClassification.getChildNodes();
		for (int i = 0; i < inspecChilds.getLength(); i++) {
			Node child = inspecChilds.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE
					&& ELEMENT_KWD_GROUP.equalsIgnoreCase(getLocalName(child))) {
				Node importedNode = nlmDocument.importNode(child, true);
				if (insertContext != null) {
					nlmArticleMetadata
							.insertBefore(importedNode, insertContext);
				} else {
					nlmArticleMetadata.appendChild(importedNode);
				}
			}
		}
	}
	
	private static String getLocalName(Node node){
		if (node.getLocalName() != null){
			return node.getLocalName();
		}
		
		return node.getNodeName();
	}
}
