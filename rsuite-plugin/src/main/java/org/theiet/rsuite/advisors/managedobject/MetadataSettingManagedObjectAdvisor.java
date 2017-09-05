package org.theiet.rsuite.advisors.managedobject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.books.BooksConstans;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.control.DefaultManagedObjectAdvisor;
import com.reallysi.rsuite.api.control.ManagedObjectAdvisor;
import com.reallysi.rsuite.api.control.ManagedObjectAdvisorContext;
import com.reallysi.rsuite.api.control.MetaDataContainer;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.xml.XPathEvaluator;

/**
 * Sets metadata on load based on simple business rules.
 */
public class MetadataSettingManagedObjectAdvisor extends
		DefaultManagedObjectAdvisor implements ManagedObjectAdvisor {

	public static final String ADVISOR_CONTEXT_ITEM_PRODUCT_CODE = "productCode";
	private static Log log = LogFactory.getLog(MetadataSettingManagedObjectAdvisor.class);

	public void adviseDuringInsert(ExecutionContext context,  
			ManagedObjectAdvisorContext insertContext)
    		throws RSuiteException {
		Element elem = insertContext.getElement();
		log.debug("adviseDuringInsert(): elem=" + elem);
		if (elem != null) {
			addMetadata(context, insertContext, elem);
		}
	}

	public void addMetadata(
			ExecutionContext context, 
			ManagedObjectAdvisorContext advisorContext, 
			Element elem) {
		MetaDataContainer metadata = advisorContext.getMetaDataContainer();
		String localName = elem.getLocalName();
		log.debug("addMetadata(): localName=" + localName);
		if ("book".equals(localName)) {
			log.debug("addMetadata(): Handling a book");
			XPathEvaluator eval = context.getXmlApiManager().getXPathEvaluator();
			String bookIdXPath = "book-meta/book-id";
			try {
				String productCode = eval.executeXPathToString(
						bookIdXPath + "[@pub-id-type='publisher-id']", 
						elem);
				if (!"".equals(productCode) && productCode != null) {
					// Save the product code so we can propagate to descendant
					// MOs.
					advisorContext.getCallContext().put(ADVISOR_CONTEXT_ITEM_PRODUCT_CODE, productCode);
					metadata.addMetaDataItem(BooksConstans.LMD_FIELD_BOOK_PRODUCT_CODE, productCode);
				}
				String doi = eval.executeXPathToString(
						bookIdXPath + "[@pub-id-type='doi']", 
						elem);
				if (!"".equals(doi) && doi != null) {
					metadata.addMetaDataItem(IetConstants.LMD_FIELD_DOI, doi);
				}
			} catch (RSuiteException e) {
				log.error("Unexpected exception getting book ID metadata: " + e.getMessage(), e);
			}
			try {
				getInspecClassifications(context, advisorContext, elem, metadata);
			} catch (RSuiteException e) {
				log.error("Unexpected exception getting Inspec metadata: " + e.getMessage(), e);
			}
			
		} 
		if ("article".equals(localName)) {
			log.debug("addMetadata(): Handling an article");
			XPathEvaluator eval = context.getXmlApiManager().getXPathEvaluator();
			String articleIdXPath = "front/article-meta/article-id";
			try {
				String productCode = eval.executeXPathToString(
						articleIdXPath + "[@pub-id-type='publisher-id']", 
						elem);
				if (!"".equals(productCode) && productCode != null) {
					// Save the product code so we can propagate to descendant
					// MOs.
					advisorContext.getCallContext().put(ADVISOR_CONTEXT_ITEM_PRODUCT_CODE, productCode);
					metadata.addMetaDataItem(BooksConstans.LMD_FIELD_BOOK_PRODUCT_CODE, productCode);
				}
				String doi = eval.executeXPathToString(
						articleIdXPath + "[@pub-id-type='doi']", 
						elem);
				if (!"".equals(doi) && doi != null) {
					metadata.addMetaDataItem(IetConstants.LMD_FIELD_DOI, doi);
				}
			} catch (RSuiteException e) {
				log.error("Unexpected exception getting book ID metadata: " + e.getMessage(), e);
			}
			
		} 
		if ("ebook".equals(localName)) {
			log.debug("addMetadata(): Handling an ebook");
			XPathEvaluator eval = context.getXmlApiManager().getXPathEvaluator();
			String pubIdXPath = "book-metadata/book-identifiers/bookid";
			String doiXPath = "book-metadata/book-identifiers/doi";
			try {
				String productCode = eval.executeXPathToString(
						pubIdXPath, 
						elem);
				if (!"".equals(productCode) && productCode != null) {
					// Save the product code so we can propagate to descendant
					// MOs.
					advisorContext.getCallContext().put(ADVISOR_CONTEXT_ITEM_PRODUCT_CODE, productCode);
					metadata.addMetaDataItem(BooksConstans.LMD_FIELD_BOOK_PRODUCT_CODE, productCode);
				}
				String doi = eval.executeXPathToString(
						doiXPath, 
						elem);
				if (!"".equals(doi) && doi != null) {
					metadata.addMetaDataItem(IetConstants.LMD_FIELD_DOI, doi);
				}
				try {
					getInspecClassifications(context, advisorContext, elem, metadata);
				} catch (RSuiteException e) {
					log.error("Unexpected exception getting Inspec metadata: " + e.getMessage(), e);
				}
			} catch (RSuiteException e) {
				log.error("Unexpected exception getting book ID metadata: " + e.getMessage(), e);
			}
			
		} 
		if ("book-part".equals(localName) ||
		    "sec".equals(localName) ||
		    "app".equals(localName)
		    ) 
		{
			// <book-part id="c1" book-part-type="chapter" book-part-number="1">
			String productCode = (String)advisorContext.getCallContext().get(ADVISOR_CONTEXT_ITEM_PRODUCT_CODE);
			if (productCode != null) {
				metadata.addMetaDataItem(BooksConstans.LMD_FIELD_BOOK_PRODUCT_CODE, productCode);
			}
			String id = elem.getAttribute("id");
			log.debug("addMetadata(): @id=\"" + id + "\"");
			metadata.addMetaDataItem(IetConstants.LMD_FIELD_XML_ID, id);
			try {
				getInspecClassifications(context, advisorContext, elem, metadata);
			} catch (RSuiteException e) {
				log.error("Unexpected exception getting Inspec metadata: " + e.getMessage(), e);
			}
			metadata.addMetaDataItem(IetConstants.LMD_FIELD_WORKFLOW_STATUS, "new");
		}
	}

	private void getInspecClassifications(
			ExecutionContext context, 
			ManagedObjectAdvisorContext advisorContext, 
			Element elem,
			MetaDataContainer metadata) throws RSuiteException {
		XPathEvaluator eval = context.getXmlApiManager().getXPathEvaluator();
		String inspecPrimaryXPathBase = "";
		String inspecSecondaryXPath = "";
		String inspecTextXPath = "";
		String inspecCodeXPath = "";
		if ("book".equals(elem.getLocalName())) {
			inspecPrimaryXPathBase = "book-meta/kwd-group/compound-kwd/compound-kwd-part";
			inspecSecondaryXPath = "book-meta/kwd-group[@kwd-group-type = 'Inspec']/kwd";
			inspecCodeXPath = inspecPrimaryXPathBase + "[@content-type = 'code']";
			inspecTextXPath = inspecPrimaryXPathBase + "[@content-type = 'text']";
		} else if ("book-part".equals(elem.getLocalName())) {
			inspecPrimaryXPathBase = "book-part-meta/kwd-group/compound-kwd/compound-kwd-part";
			inspecSecondaryXPath = "book-part-meta/kwd-group[@kwd-group-type = 'Inspec']/kwd";
			inspecCodeXPath = inspecPrimaryXPathBase + "[@content-type = 'code']";
			inspecTextXPath = inspecPrimaryXPathBase + "[@content-type = 'text']";
		} else if ("sec".equals(elem.getLocalName()) || 
				   "app".equals(elem.getLocalName())
				) {
			inspecPrimaryXPathBase = "sec-meta/kwd-group/compound-kwd/compound-kwd-part";
			inspecSecondaryXPath = "sec-meta/kwd-group[@kwd-group-type = 'Inspec']/kwd";
			inspecCodeXPath = inspecPrimaryXPathBase + "[@content-type = 'code']";
			inspecTextXPath = inspecPrimaryXPathBase + "[@content-type = 'text']";
		} else if ("ebook".equals(elem.getLocalName())) {
			inspecPrimaryXPathBase = "body/content-item/content-item-metadata/vocabs/classification-codes/compound-classification-code/compound-classification-code-part";
			inspecSecondaryXPath = "body/content-item/content-item-metadata/vocabs/keywords/keyword";
			inspecCodeXPath = inspecPrimaryXPathBase + "[@type = 'code']";
			inspecTextXPath = inspecPrimaryXPathBase + "[@type = 'text']";
		}
		log.debug("getInspecClassifications(): element is " + elem.getLocalName() + "");
		log.debug("getInspecClassifications(): xPath=\"" + inspecCodeXPath + "\"");
		// Inspec codes:
		Node[] nodes = eval.executeXPathToNodeArray(
				inspecCodeXPath, 
				elem);
		log.debug("getInspecClassifications(): Got " + nodes.length + " code nodes");
		for (Node node : nodes) {
			String code = node.getTextContent().trim();
			metadata.addMetaDataItem(IetConstants.LMD_FIELD_INSPEC_CODE, code);
		}
		// Inspec text (primary):
		nodes = eval.executeXPathToNodeArray(
				inspecTextXPath, 
				elem);
		log.debug("getInspecClassifications(): Got " + nodes.length + " text nodes");
		for (Node node : nodes) {
			String code = node.getTextContent().trim();
			metadata.addMetaDataItem(IetConstants.LMD_FIELD_INSPEC_TEXT, code);
		}
		// Inspec text (not primary):
		nodes = eval.executeXPathToNodeArray(
				inspecSecondaryXPath, 
				elem);
		log.debug("getInspecClassifications(): Got " + nodes.length + " secondary text nodes");
		for (Node node : nodes) {
			String code = node.getTextContent().trim();
			metadata.addMetaDataItem(IetConstants.LMD_FIELD_INSPEC_TEXT, code);
		}
		
		// 
	}

	/**
	 * Set an alias of type "filename" if not already set.
	 */
	public void adviseDuringUpdate(
			ExecutionContext context,
            ManagedObjectAdvisorContext updateContext)
            throws RSuiteException {
		Element elem = updateContext.getElement();
		if (elem != null) {
			addMetadata(context, updateContext, elem);			
		}
		
	}
	
	
}
