package org.theiet.rsuite.journals.domain.article;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ContentAssemblyReference;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ManagedObjectReference;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;

public class ArticleTypesetterContainer {

	private ManagedObject articleXMLMO;

	private ContentAssembly imagesCA;

	private ManagedObject finalPdfMO;

	private ManagedObject proofPdfMO;

	ArticleTypesetterContainer(ExecutionContext context, User user,
			String articleId, ContentAssembly typesetterCA)
			throws RSuiteException {

		ManagedObjectService moSvc = context.getManagedObjectService();
		ContentAssemblyService caSvc = context.getContentAssemblyService();

		traverseTypesetterCA(user, articleId, typesetterCA, moSvc, caSvc);

	}

	private void traverseTypesetterCA(User user, String articleId,
			ContentAssembly typesetterCA, ManagedObjectService moSvc,
			ContentAssemblyService caSvc) throws RSuiteException {
		for (ContentAssemblyItem child : typesetterCA.getChildrenObjects()) {

			if (child instanceof ManagedObjectReference) {
				String moId = ((ManagedObjectReference) child).getTargetId();
				ManagedObject mo = moSvc.getManagedObject(user, moId);
				processManagedObject(articleId, mo);

			} else if (child instanceof ContentAssemblyReference) {
				processContentAssemblyReference(user, caSvc, child);
			}
		}
	}

	private void processManagedObject(String articleId, ManagedObject mo)
			throws RSuiteException {
		if (mo.isNonXml()) {
			processNonXMLMO(articleId, mo);
		} else {
			processXMLMO(articleId, mo);
		}
	}
	
	private void processNonXMLMO(String articleId, ManagedObject mo)
			throws RSuiteException {
		String displayName = mo.getDisplayName();
		if (displayName.toLowerCase().endsWith("final.pdf")) {
			checkIfObjectAlreadyExist(finalPdfMO,
					"More than one final PDF found", articleId);
			finalPdfMO = mo;
		}
		
		if (displayName.toLowerCase().endsWith("proof.pdf")) {
			checkIfObjectAlreadyExist(proofPdfMO,
					"More than one proof PDF found", articleId);
			proofPdfMO = mo;
		}
	}

	private void processXMLMO(String articleId, ManagedObject mo)
			throws RSuiteException {
		String localName = mo.getLocalName();
		if ("article".equals(localName)) {
			checkIfObjectAlreadyExist(articleXMLMO,
					"More than one XML article found", articleId);
			articleXMLMO = mo;
		}
	}

	private void checkIfObjectAlreadyExist(ManagedObject mo,
			String errorMessage, String articleId) throws RSuiteException {
		if (mo != null) {
			throw new RSuiteException(errorMessage + " for article id "
					+ articleId);
		}
	}

	private void processContentAssemblyReference(User user,
			ContentAssemblyService caSvc, ContentAssemblyItem child)
			throws RSuiteException {
		String caId = ((ContentAssemblyReference) child).getTargetId();

		ContentAssembly ca = caSvc.getContentAssembly(user, caId);
		String nodeName = ca.getDisplayName();

		if ("images".equalsIgnoreCase(nodeName)) {
			imagesCA = ca;
		}
	}

	public ManagedObject getArticleXMLMO() {
		return articleXMLMO;
	}

	public ContentAssembly getImagesCA() {
		return imagesCA;
	}

	public ManagedObject getFinalPdfMO() {
		return finalPdfMO;
	}

	public ManagedObject getProofPdfMO() {
		return proofPdfMO;
	}

}
