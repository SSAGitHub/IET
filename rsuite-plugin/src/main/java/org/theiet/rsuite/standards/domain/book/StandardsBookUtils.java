package org.theiet.rsuite.standards.domain.book;

import java.util.*;

import org.theiet.rsuite.onix.domain.InitialOnixFileCreator;
import org.theiet.rsuite.standards.StandardsBooksConstans;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public final class StandardsBookUtils implements StandardsBooksConstans {

	private StandardsBookUtils() {
	}

	/**
	 * Create content assembly representing standards book - container for book
	 * editions
	 * 
	 * @param context
	 *            Execution context
	 * @param booksGroupCaId
	 *            Parent container id
	 * @param bookName
	 *            The book name
	 * @param shortTitle
	 *            Short book title
	 * @return Book content assembly
	 * @throws RSuiteException
	 */
	public static ContentAssembly createStandardBookCa(
			ExecutionContext context, String booksGroupCaId, String bookName,
			String shortTitle) throws RSuiteException {
		ContentAssembly bookCa = ProjectContentAssemblyUtils.createContentAssembly(context,
				booksGroupCaId, bookName, CA_TYPE_STANDARDS_BOOK);

		ManagedObjectService moSvc = context.getManagedObjectService();
		User user = context.getAuthorizationService().getSystemUser();

		List<MetaDataItem> metadataItems = new ArrayList<MetaDataItem>();

		metadataItems.add(new MetaDataItem(LMD_FIELD_BOOK_TITLE, bookName));
		metadataItems.add(new MetaDataItem(LMD_FIELD_BOOK_TITLE_SHORT,
				shortTitle));

		moSvc.setMetaDataEntries(user, bookCa.getId(), metadataItems);

		return bookCa;
	}

	/**
	 * Populate Standards Book content assembly with initial structure
	 * 
	 * @param context
	 * @param standardsBookCaId
	 * @return List of ContentAssembly book children
	 * @throws RSuiteException
	 */
	public static List<ContentAssembly> createStandardBookCaStructure(
			ExecutionContext context, String standardsBookCaId) throws RSuiteException {		
		List<ContentAssembly> bookChildren = new ArrayList<ContentAssembly>();
		
		ContentAssembly standarsBookImageCa = ProjectContentAssemblyUtils.createContentAssembly(context,
				standardsBookCaId, CA_NAME_STANDARDS_IMAGES, CA_TYPE_STANDARDS_IMAGES);

		bookChildren.add(standarsBookImageCa);

		return bookChildren;		
	}

	/**
	 * Returns present timestamp in format yyyy-MM-dd HH:mm:ss:SSS
	 * 
	 * @return present timestamp
	 */
	public static String getPresentTimestamp() {
		String value = UK_DATE_FORMAT_TIMESTAMP.format(new Date());
		return value;
	}

	public static ContentAssembly createStandarsBookEdition(
			ExecutionContext context, User user, String editionName,
			String bookCaId, List<MetaDataItem> metadataList,
			Map<String, String> variables) throws RSuiteException {

		ContentAssembly bookCa = ProjectContentAssemblyUtils.createContentAssembly(context,
				bookCaId, editionName,
				StandardsBooksConstans.CA_TYPE_STANDARDS_BOOK_EDITION);

		metadataList.add(new MetaDataItem(LMD_FIELD_CREATE_TIMESTAMP,
				StandardsBookUtils.getPresentTimestamp()));
		metadataList.add(new MetaDataItem(LMD_FIELD_BOOK_STATUS,
				LMD_VALUE_INITIALIZED));

		context.getManagedObjectService().setMetaDataEntries(user,
				bookCa.getId(), metadataList);

		bookCa = createBookStructure(context, user, bookCa.getId());

		ContentAssembly productionInformation = ProjectBrowserUtils
				.getChildCaByDisplayName(context, bookCa,
						CA_NAME_PRODUCTION_DOCUMENTATION);
		
		InitialOnixFileCreator onixCreator = new InitialOnixFileCreator(
				context, user);
		onixCreator.createInitialOnixMo(productionInformation, variables);

		return bookCa;
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
	private static ContentAssembly createBookStructure(ExecutionContext context, User user,
			String bookCaId) throws RSuiteException {

		ContentAssembly editorial = ProjectContentAssemblyUtils.createContentAssembly(context,
				bookCaId, CA_NAME_STANDARDS_EDITORIAL, CA_TYPE_FOLDER);

		ProjectContentAssemblyUtils.createContentAssembly(context, editorial.getId(),
				CA_NAME_STANDARDS_COMMENTS, CA_TYPE_FOLDER);

		ProjectContentAssemblyUtils.createContentAssembly(context, editorial.getId(),
				CA_NAME_STANDARDS_NEXT_EDITION_COMMENTS, CA_TYPE_FOLDER);

		ProjectContentAssemblyUtils.createContentAssembly(context, editorial.getId(),
				CA_NAME_STANDARDS_PRODUCT_INFORMATION, CA_TYPE_FOLDER);

		ProjectContentAssemblyUtils.createContentAssembly(context, editorial.getId(),
				CA_NAME_STANDARDS_CONTRACTS);

		ProjectContentAssemblyUtils.createContentAssembly(context, editorial.getId(),
				CA_NAME_STANDARDS_TYPESCRIPT, CA_TYPE_STANDARDS_TYPESCRIPT);

		ProjectContentAssemblyUtils.createContentAssembly(context, bookCaId,
				CA_NAME_STANDARDS_PRODUCTION_DOCUMENTATION);
		ContentAssembly productionFiles = ProjectContentAssemblyUtils.createContentAssembly(
				context, bookCaId, CA_NAME_STANDARDS_PRODUCTION_FILES);

		String prodCaId = productionFiles.getId();

		ProjectContentAssemblyUtils.createContentAssembly(context, prodCaId,
				CA_NAME_STANDARDS_CORRECTIONS,
				CA_TYPE_STANDARDS_BOOK_CORRECTIONS);
		ProjectContentAssemblyUtils.createContentAssembly(context, prodCaId,
				CA_NAME_STANDARDS_COVER);
		ProjectContentAssemblyUtils.createContentAssembly(context, prodCaId, CA_NAME_FINAL_FILES,
				CA_TYPE_FINAL_FILES);
		ProjectContentAssemblyUtils.createContentAssembly(context, prodCaId,
				CA_NAME_STANDARDS_PRINT_FILES, CA_TYPE_STANDARDS_PRINT_FILES);

		ProjectContentAssemblyUtils.createContentAssembly(context, prodCaId,
				CA_NAME_STANDARDS_PRINTER_INSTRUCTIONS,
				CA_TYPE_STANDARDS_PRINTER_INSTRUCTIONS);

		ProjectContentAssemblyUtils.createContentAssembly(context, bookCaId, "Outputs",
				"outputs");

		return context.getContentAssemblyService().getContentAssembly(user, bookCaId);
	}
	
	public static String convertEditonNumberToOrdinal(int editionNumber){
		
		String suffix = "th";
		
		int modulo100 = editionNumber % 100;
		int modulo10 = editionNumber % 10;
		
		if (modulo100 == 11 || modulo100 == 12 || modulo100 == 13){
			suffix = "th";
		}else if (modulo10 == 1){
			suffix = "st";
		} else if (modulo10 == 2){
			suffix = "nd";
		}else if (modulo10 == 3){
			suffix = "rd";
		}
		
		return editionNumber + suffix;
	}

	public static String getStandardsBookLMD(ExecutionContext context,
			String moId, String lmd) throws RSuiteException {
		String mathmlFont = ProjectContentAssemblyUtils.getLMDFromCATypeAncestor(context, moId, CA_TYPE_STANDARDS_BOOK, lmd);

		return mathmlFont;
	}
}
