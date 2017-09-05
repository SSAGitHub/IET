package org.theiet.rsuite.actionhandlers;

import java.io.File;
import java.util.*;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.utils.*;
import org.w3c.dom.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.ContentAssemblyCreateOptions;
import com.reallysi.rsuite.api.workflow.*;
import com.reallysi.rsuite.api.xml.XPathEvaluator;
import com.reallysi.rsuite.service.*;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class BatchCreateBooks extends AbstractBaseActionHandler 
implements BooksConstans {

	private static final long serialVersionUID = 1L;
	private static final String BOOKS_QUERY = "/rs_ca_map/rs_ca[rmd:get-type(.) = 'books']";
	private static final String BOOK_XPATH = "/bookload/book";

	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {
		
		String create = context.getVariable("create");
		Log log = context.getWorkflowLog();
		User user = getSystemUser();
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		ManagedObjectService moSvc = context.getManagedObjectService();
		SearchService srchSvc = context.getSearchService();
		XmlApiManager xmlApiMgr = context.getXmlApiManager();
		XPathEvaluator xPathEval = xmlApiMgr.getXPathEvaluator();
		ContentAssembly booksCa = null;
		List<ManagedObject> caSet = srchSvc.executeXPathSearch(user,
				BOOKS_QUERY, 1, 0);
		int nCa = caSet.size();
		if (nCa != 1) {
			ExceptionUtils.throwWorfklowException(context, "BOOKS_QUERY returned " + nCa);
		} else {
			booksCa = caSvc.getContentAssembly(user, caSet.get(0).getId());
		}
		
		File xmlFile = context.getFileWorkflowObject().getFile();
		Document xmlDoc = xmlApiMgr.getW3CDomFromFile(xmlFile, true);
		
		log.info("execute: get root assembly");
		Element xmlRoot = xmlDoc.getDocumentElement();
		String rootCaName = xmlRoot.getAttribute("dest");
		if (StringUtils.isBlank(rootCaName)) {
			ExceptionUtils.throwWorfklowException(context, "Did not find root ca name");
		}
		
		log.info("execute: get root assembly " + rootCaName);
		ContentAssembly buildCa = ProjectBrowserUtils.getChildCaByNameAndType(context, booksCa, "booksCA", rootCaName);
		if (buildCa == null) {
			ExceptionUtils.throwWorfklowException(context, "Did not find assembly " + rootCaName);
		}
		String buildCaId = buildCa.getId();
		
		Node[] bookNodes = xPathEval.executeXPathToNodeArray(BOOK_XPATH, xmlDoc);
		
		for (Node bookNode : bookNodes) {
			Element bookElem = (Element)bookNode;
			String bookTitle = bookElem.getAttribute("title");
			if (StringUtils.isBlank(bookTitle)) {
				log.error("execute: empty title attribute");
			} else {
				log.info("execute: got Book " + bookTitle);
				HashMap<String, String> attMap = new HashMap<String, String>();
				NodeList lmdNodes = bookElem.getElementsByTagName("lmd");
				for (int n=0; n<lmdNodes.getLength(); n++) {
					Element lmdElem = (Element)lmdNodes.item(n);
					String name = lmdElem.getAttribute("field").replaceAll("&amp;", "&");
					String value = lmdElem.getAttribute("value").replaceAll("&amp;", "&");
					attMap.put(name, value);
					log.info("\t" + name + ":" + value);
				}
				String book_title_short = getValueFromMap(attMap, "book_title_short");
				String author_surname = getValueFromMap(attMap, "author_surname");
				String product_code = getValueFromMap(attMap, "product_code");
				String bookCaName = new String();
				if (book_title_short == null || author_surname == null || product_code == null) {
					log.error("execute: unable to form book name");
				} else {
					bookCaName = product_code + " - " + book_title_short + " - " + author_surname;
					ContentAssembly bookCa = ProjectBrowserUtils.getChildCaByNameAndType(context, buildCa, "book", bookCaName);
					if ("yes".equals(create)) {
						String bookCaId = new String();
						boolean isNewBook = false;
						try {
							if (bookCa == null) {
								bookCa = createContentAssembly(user, log, caSvc, buildCaId, bookCaName, BooksConstans.CA_TYPE_BOOK);
								bookCaId = bookCa.getId();
								log.info("execute: created book " + bookCaName + "(" + bookCaId + ")");
								isNewBook = true;
							}
							else {
								bookCaId = bookCa.getId();
								log.info("execute: updating book " + bookCaName + "(" + bookCaId + ")");
							}
							ArrayList<MetaDataItem> lmdItems = new ArrayList<MetaDataItem>();
							Set<String> keySet = attMap.keySet();
							for (String key : keySet) {
								lmdItems.add(new MetaDataItem(key, attMap.get(key)));
							}
							log.info("execute: setting metadata");
							List<MetaDataItem> oldLmdItems = bookCa.getMetaDataItems();
							for (MetaDataItem item : oldLmdItems) {
								moSvc.removeMetaDataEntry(user, bookCaId, item);
							}
							moSvc.setMetaDataEntries(user, bookCaId, lmdItems);
									
							if (isNewBook) {
								log.info("execute: creating subcontainers");
								createBookStructure(user, log, caSvc, bookCa);
							}
						}
						catch (RSuiteException e) {
							log.error("execute: failure in book creation " + e.getMessage(), e);
						}
					}
					
				}				
			}
		}
	}
	
	private String getValueFromMap(HashMap<String, String> map, String key) {
		if (map.containsKey(key)) {
			String value = map.get(key);
			return value;
		} else {
			return null;
		}
	}
	
	private ContentAssembly createContentAssembly(User user, 
			Log log,
			ContentAssemblyService caService,
			String parentId, 
			String displayName, 
			String type) throws RSuiteException{
		ContentAssemblyCreateOptions options = new ContentAssemblyCreateOptions();
		if (type != null){
			options.setType(type);
		}
		options.setSilentIfExists(true);
		return caService.createContentAssembly(user, parentId, displayName, options);
	}
	
	private void createBookStructure(User user,
			Log log,
			ContentAssemblyService caSvc,
			ContentAssembly bookCa) throws RSuiteException {
		ContentAssembly editorial = createContentAssembly(user, log, caSvc,
				bookCa.getId(), CA_NAME_EDITORIAL, null);

		createContentAssembly(user, log, caSvc, editorial.getId(),
				CA_NAME_PRODUCT_INFORMATION, null);

		createContentAssembly(user, log, caSvc, editorial.getId(),
				CA_NAME_CONTRACTS, null);

		createContentAssembly(user, log, caSvc, bookCa.getId(),
				CA_NAME_PRODUCTION_DOCUMENTATION, null);
		ContentAssembly productionFiles = createContentAssembly(user, log, caSvc,
				bookCa.getId(), CA_NAME_PRODUCTION_FILES, null);

		String prodCaId = productionFiles.getId();

		createContentAssembly(user, log, caSvc, prodCaId, CA_NAME_TYPESCRIPT,
				CA_TYPE_TYPESCRIPT);
		createContentAssembly(user, log, caSvc, prodCaId, CA_NAME_TYPESETTER,
				CA_TYPE_TYPESETTER);
		createContentAssembly(user, log, caSvc, prodCaId, CA_NAME_CORRECTIONS,
				CA_TYPE_BOOK_CORRECTIONS);
		createContentAssembly(user, log, caSvc, prodCaId, CA_NAME_COVER, null);
		createContentAssembly(user, log, caSvc, prodCaId, CA_NAME_FINAL_FILES,
				CA_TYPE_FINAL_FILES);
		createContentAssembly(user, log, caSvc, prodCaId, CA_NAME_PRINT_FILES,
				CA_TYPE_PRINT_FILES);

		createContentAssembly(user, log, caSvc, prodCaId,
				CA_NAME_PRINTER_INSTRUCTIONS, CA_TYPE_PRINTER_INSTRUCTIONS);

	}

}
