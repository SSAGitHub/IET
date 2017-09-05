package org.theiet.rsuite.books.domain;

import static org.theiet.rsuite.books.BooksConstans.*;

import org.theiet.rsuite.books.utils.BookUtils;
import org.theiet.rsuite.domain.TypesetterIngestionHelper;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.ContentAssemblyCreateOptions;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class BookTypesetterIngestionHelper extends TypesetterIngestionHelper {

	private String bookCaId;

	private String typesetterSubmission;

	private String bookCode;

	public BookTypesetterIngestionHelper(WorkflowExecutionContext context) {
		super(context);
	}

	@Override
	protected String obtainProductCAId(String productCode)
			throws RSuiteException {

		bookCode = productCode;
		bookCaId = BookUtils.getBookCaId(getLogger(), getUser(), getContext(), productCode);

		return bookCaId;
	}

	@Override
	protected String obtainTypesetterSubmission(ContentAssembly productCA)
			throws RSuiteException {

		typesetterSubmission = productCA
				.getLayeredMetadataValue(LMD_FIELD_TYPESETTER_UPDATE_TYPE);
		
		if (productCA.getLayeredMetadataValue(LMD_FIELD_PRINT_PUBLISHED_DATE) != null){
			typesetterSubmission = LMD_VALUE_UPDATE;
		}
		

		MetaDataItem typesetterUpdateType = new MetaDataItem(
				LMD_FIELD_TYPESETTER_UPDATE_TYPE, typesetterSubmission
						+ LMD_VALUE_PROOF_SUFFIX);
		getMoService().setMetaDataEntry(getUser(), productCA.getId(),
				typesetterUpdateType);

		return typesetterSubmission;
	}

	@Override
	protected ContentAssembly obtainTypesetterCA(ContentAssembly productCA)
			throws RSuiteException {

		ContentAssemblyCreateOptions createOpts = new ContentAssemblyCreateOptions();
		createOpts.setSilentIfExists(true);
		ContentAssembly prodFilesCa = getCaService().createContentAssembly(getUser(),
				bookCaId, CA_NAME_PRODUCTION_FILES, createOpts);

		ContentAssembly typesetterAssembly = null;

		if (LMD_VALUE_FINAL.equals(typesetterSubmission)) {
			typesetterAssembly = ProjectBrowserUtils.getChildCaByType(getContext(),
					prodFilesCa, CA_TYPE_FINAL_FILES);
		} else {
			typesetterAssembly = ProjectBrowserUtils.getChildCaByNameAndType(getContext(),
					prodFilesCa, CA_TYPE_TYPESETTER, CA_NAME_TYPESETTER);
		}

		return typesetterAssembly;
	}

	@Override
	protected String getProductId(ContentAssembly productCA)
			throws RSuiteException {
		return bookCode;
	}

}
