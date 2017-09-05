package org.theiet.rsuite.standards.domain.book.export.esp;

import static org.theiet.rsuite.books.BooksConstans.LMD_FIELD_BOOK_E_PRODUCT_CODE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.theiet.rsuite.standards.domain.book.StandardsBookEdition;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.rsicms.projectshelper.export.MoExportContainerContext;

class ESPMoToEProductCodeMapper {

	private Map<String, String> contextIdToEproductCode = new HashMap<String, String>();

	private MoExportContainerContext exportContainerContext;

	public ESPMoToEProductCodeMapper(ExecutionContext context, User user,
			MoExportContainerContext exportContainerContext,
			StandardsBookEdition bookEdition) throws RSuiteException {

		this.exportContainerContext = exportContainerContext;

		addMappingForBookEdition(bookEdition);
		addMappingForBookEditionDependencies(context, user, bookEdition);
	}

	private void addMappingForBookEdition(StandardsBookEdition bookEdition)
			throws RSuiteException {
		ContentAssembly bookPublicationCa = bookEdition.getBookPublicationCa();
		String eProductCode = getEPorductCode(bookPublicationCa);
		String typeScriptId = bookEdition.getTypesriptCA().getId();

		contextIdToEproductCode.put(typeScriptId, eProductCode);
		contextIdToEproductCode.put(bookPublicationCa.getId(), eProductCode);
	}

	private String getEPorductCode(ContentAssembly bookEditionCa)
			throws RSuiteException {
		String eProductCode = bookEditionCa
				.getLayeredMetadataValue(LMD_FIELD_BOOK_E_PRODUCT_CODE);
		return eProductCode;
	}

	private void addMappingForBookEditionDependencies(ExecutionContext context,
			User user, StandardsBookEdition bookEdition) throws RSuiteException {

		ContentAssemblyService caService = context.getContentAssemblyService();

		List<String> directBookEditionDependency = bookEdition
				.getDirectBookEditionDependency(user);
		for (String dependencyCaId : directBookEditionDependency) {
			ContentAssembly contentAssembly = caService.getContentAssembly(
					user, dependencyCaId);
			String eproductCode = getEPorductCode(contentAssembly);
			contextIdToEproductCode.put(dependencyCaId, eproductCode);
		}

	}

	public String getEProductCodeForMo(ManagedObject mo) throws RSuiteException {

		String eproductCode = "";

		Set<String> contextCaIds = exportContainerContext.getContextCaId(mo);

		if (contextCaIds.size() > 0) {
			String contextCaId = contextCaIds.iterator().next();
			eproductCode = contextIdToEproductCode.get(contextCaId);
		}

		return eproductCode;
	}

}
