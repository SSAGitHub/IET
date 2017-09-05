/**
 * Copyright (c) 2012 Really Strategies, Inc.
 */
package org.theiet.rsuite.books.webservice;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.*;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.books.utils.BookUtils;
import org.theiet.rsuite.domain.permissions.PermissionsUtils;
import org.theiet.rsuite.onix.*;
import org.theiet.rsuite.onix.domain.InitialOnixFileCreator;
import org.theiet.rsuite.utils.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.api.remoteapi.result.*;
import com.reallysi.rsuite.service.SecurityService;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;
import com.rsicms.rsuite.helpers.webservice.RemoteApiHandlerBase;

/**
 * Sets LMD provided through a utility form.
 */
public class CreateBookPublicationWebService extends RemoteApiHandlerBase
		implements BooksConstans, OnixConstants {

	private static String[] FORM_PARAMETERS_TO_LMD = {
			LMD_FIELD_BOOK_PRODUCT_CODE, LMD_FIELD_BOOK_E_PRODUCT_CODE,
			LMD_FIELD_ISBN, LMD_FIELD_E_ISBN, LMD_FIELD_CONTRACT_SIGNED_DATE,
			LMD_FIELD_AUTHOR_FIRST_NAME, LMD_FIELD_AUTHOR_SURNAME,
			LMD_FIELD_BOOK_TITLE_SHORT, LMD_FIELD_BOOK_SERIES_NAME,
			LMD_FIELD_EDITORIAL_ASSISTANT, LMD_FIELD_TYPESETTER_USER,
			LMD_FIELD_PRODUCTION_CONTROLLER_USER };

	/** Web service parameters */

	private static final String PARAM_BOOK_TITLE = "bookTitle";

	private Map<String, String> tmpBookStructure = new HashMap<String, String>();

	/**
	 * 
	 */
	private static Log log = LogFactory
			.getLog(CreateBookPublicationWebService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.reallysi.rsuite.api.remoteapi.RemoteApiHandler#execute(com.reallysi
	 * .rsuite.api.remoteapi.RemoteApiExecutionContext,
	 * com.reallysi.rsuite.api.remoteapi.CallArgumentList)
	 */
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		log.info("execute(): args=" + args.getValuesMap());
		SecurityService secSvc = context.getSecurityService();
		User user = context.getSession().getUser();
		String booksCaId = args.getFirstValue(PARAM_RSUITE_ID);

		try {

			String productCode = checkIfBookAlreadyExist(context, args);

			Map<String, String> variables = converFormCallArgumentsToMap(args); 
			
			ContentAssembly bookCa = createBookCa(context,
					variables, booksCaId);

			String bookCaId = bookCa.getId();

			InitialOnixFileCreator onixCreator = new InitialOnixFileCreator(
					context, user);
			
			ContentAssembly productionInformation = ProjectBrowserUtils
					.getChildCaByDisplayName(context, bookCa,
							CA_NAME_PRODUCTION_DOCUMENTATION);
			


			onixCreator.createInitialOnixMo(productionInformation, variables);

			PubtrackLogger.createProcess(user, context, log,
					BooksConstans.WF_VAR_BOOK, productCode, bookCaId);
			User systemUser = context.getAuthorizationService().getSystemUser();
			PermissionsUtils.setAdminOnlyDeleteACL(secSvc, systemUser,
					bookCaId, log);
		} catch (Exception e) {
			log.error("Unable to create book", e);
			return new MessageDialogResult(MessageType.ERROR,
					"Create New Book", e.getMessage());
		}

		String msg = "A new book has been created";
		return ContentDisplayUtils.getResultWithLabelRefreshing(
				MessageType.SUCCESS, "Create New Book", msg, "500", booksCaId);

	}

	private String checkIfBookAlreadyExist(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {
		String productCode = args.getFirstValue(LMD_FIELD_BOOK_PRODUCT_CODE);

		String id = BookUtils.getBookCaId(log, getUser(context), context,
				productCode);

		if (id != null) {
			throw new RSuiteException("Book with given id already exist");
		}
		return productCode;
	}

	private Map<String, String> converFormCallArgumentsToMap(
			CallArgumentList args) {

		Map<String, String> argumentsMap = new HashMap<String, String>();

		for (String formParam : FORM_PARAMETERS_TO_LMD) {
			addFormParamIfNotBlank(args, argumentsMap, formParam);
		}

		addFormParamIfNotBlank(args, argumentsMap, PARAM_BOOK_TITLE);
		addFormParamIfNotBlank(args, argumentsMap, ONIX_VAR_BOOK_SUBTITLE);
		addFormParamIfNotBlank(args, argumentsMap, ONIX_VAR_EDITION_NUMBER);

		return argumentsMap;
	}

	private void addFormParamIfNotBlank(CallArgumentList args,
			Map<String, String> argumentsMap, String formParam) {
		String value = args.getFirstValue(formParam);
		
		if (value != null && ( LMD_FIELD_ISBN.equalsIgnoreCase(formParam) || LMD_FIELD_E_ISBN.equalsIgnoreCase(formParam))){
			value = value.replaceAll("[^0-9]", "");
		}

		if (StringUtils.isNotBlank(value)) {
			argumentsMap.put(formParam, value);
		}
	}

	/**
	 * Create book content assembly
	 * 
	 * @param context
	 *            The web service context
	 * @param args
	 *            The web service argument list
	 * @param productCode
	 *            the product code
	 * @param booksCaId
	 *            books container CA id
	 * @return created book content assembly
	 * @throws RSuiteException
	 */
	private ContentAssembly createBookCa(RemoteApiExecutionContext context,
			Map<String, String> argumentsMap, String booksCaId)
			throws RSuiteException {

		User user = getUser(context);

		String bookDisplayName = createBookDisplayName(argumentsMap);

		ContentAssembly bookCa = ProjectContentAssemblyUtils.createContentAssembly(context,
				booksCaId, bookDisplayName, BooksConstans.CA_TYPE_BOOK);

		String bookCaId = bookCa.getId();

		List<MetaDataItem> metadataList = new ArrayList<MetaDataItem>();

		for (String lmdName : FORM_PARAMETERS_TO_LMD) {
			String value = argumentsMap.get(lmdName);

			if (value != null) {
				metadataList.add(new MetaDataItem(lmdName, value));
			}
		}

		String fullTitle = generateFullTitle(argumentsMap);

		metadataList.add(new MetaDataItem(BooksConstans.LMD_FIELD_BOOK_TITLE,
				fullTitle));

		metadataList.add(new MetaDataItem(LMD_FIELD_BOOK_METADATA_SENT,
				IetConstants.LMD_VALUE_NO));

		context.getManagedObjectService().setMetaDataEntries(user, bookCaId,
				metadataList);

		createBookStructure(context, bookCa);

		//refresh booksCa object
		bookCa = context.getContentAssemblyService().getContentAssembly(
				user, bookCaId);
		
		return bookCa;
	}

	private String generateFullTitle(Map<String, String> argumentsMap) {
		String title = argumentsMap.get(PARAM_BOOK_TITLE);
		String subTitle = argumentsMap.get(ONIX_VAR_BOOK_SUBTITLE);
		String edition = argumentsMap.get(ONIX_VAR_EDITION_NUMBER);

		StringBuilder fullTitle = new StringBuilder(title);

		concatenateIfNotBlank(fullTitle, subTitle);
		concatenateIfNotBlank(fullTitle, edition);
		return fullTitle.toString();
	}

	private String createBookDisplayName(Map<String, String> argumentsMap) {
		String productCode = argumentsMap.get(LMD_FIELD_BOOK_PRODUCT_CODE);
		String titleShortForm = argumentsMap.get(LMD_FIELD_BOOK_TITLE_SHORT);
		String authorSurname = argumentsMap.get(LMD_FIELD_AUTHOR_SURNAME);

		String bookDisplayName = productCode + " - " + titleShortForm + " - "
				+ authorSurname;
		return bookDisplayName;
	}

	private void concatenateIfNotBlank(StringBuilder base, String toConcatenate) {
		if (StringUtils.isNotBlank(toConcatenate)) {
			base.append(" ").append(toConcatenate);
		}
	}

	/**
	 * Creates book content assembly structure
	 * 
	 * @param context
	 *            execution context
	 * @param bookCa
	 *            book content assembly
	 * @throws RSuiteException
	 */
	private void createBookStructure(ExecutionContext context,
			ContentAssembly bookCa) throws RSuiteException {
		ContentAssembly editorial = createBookStructureWithTmpCache(context,
				bookCa.getId(), CA_NAME_EDITORIAL, null);

		createBookStructureWithTmpCache(context, editorial.getId(),
				CA_NAME_PRODUCT_INFORMATION, null);

		createBookStructureWithTmpCache(context, editorial.getId(),
				CA_NAME_CONTRACTS, null);

		createBookStructureWithTmpCache(context, bookCa.getId(),
				CA_NAME_PRODUCTION_DOCUMENTATION, null);
		ContentAssembly productionFiles = createBookStructureWithTmpCache(
				context, bookCa.getId(), CA_NAME_PRODUCTION_FILES, null);

		String prodCaId = productionFiles.getId();

		createBookStructureWithTmpCache(context, prodCaId, CA_NAME_TYPESCRIPT,
				CA_TYPE_TYPESCRIPT);
		createBookStructureWithTmpCache(context, prodCaId, CA_NAME_TYPESETTER,
				CA_TYPE_TYPESETTER);
		createBookStructureWithTmpCache(context, prodCaId, CA_NAME_CORRECTIONS,
				CA_TYPE_BOOK_CORRECTIONS);
		createBookStructureWithTmpCache(context, prodCaId, CA_NAME_COVER, null);
		createBookStructureWithTmpCache(context, prodCaId, CA_NAME_FINAL_FILES,
				CA_TYPE_FINAL_FILES);
		createBookStructureWithTmpCache(context, prodCaId, CA_NAME_PRINT_FILES,
				CA_TYPE_PRINT_FILES);

		createBookStructureWithTmpCache(context, prodCaId,
				CA_NAME_PRINTER_INSTRUCTIONS, CA_TYPE_PRINTER_INSTRUCTIONS);

	}

	private ContentAssembly createBookStructureWithTmpCache(
			ExecutionContext context, String parentId, String displayName,
			String type) throws RSuiteException {
		ContentAssembly bookContainer = ProjectContentAssemblyUtils.createContentAssembly(context,
				parentId, displayName, type);
		tmpBookStructure.put(bookContainer.getDisplayName(),
				bookContainer.getId());
		return bookContainer;
	}

}
