package org.theiet.rsuite.standards.domain.book.export;

import static org.theiet.rsuite.standards.StandardsBooksConstans.*;

import java.util.List;

import org.theiet.rsuite.standards.domain.book.StandardsBookEdition;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.export.MoExportContainerContext;
import com.rsicms.projectshelper.export.impl.context.MoExportContainerContextFactory;

public class BookEditionExportHelper {

	public static MoExportContainerContext createMoExportContainerContext(
			ExecutionContext context, User user,
			StandardsBookEdition bookEdition) throws RSuiteException {

		ContentAssembly typesriptCA = bookEdition.getTypesriptCA();

		List<String> editionDependencies = bookEdition
				.getDirectBookEditionDependency(user);
		editionDependencies.add(typesriptCA.getId());

		MoExportContainerContext exportContainerContext = MoExportContainerContextFactory
				.createMoExportContainerContext(context, editionDependencies);
		exportContainerContext.addAdditionalContextInfo(VAR_BOOK_EDITION_ID,
				bookEdition.getRsuiteId());

		return exportContainerContext;
	}
}
