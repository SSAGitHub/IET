package org.theiet.rsuite.journals.domain.issues;

import static org.theiet.rsuite.journals.JournalConstants.*;

import org.theiet.rsuite.domain.TypesetterIngestionHelper;
import org.theiet.rsuite.journals.utils.JournalUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class IssueTypesetterIngestionHelper extends TypesetterIngestionHelper {

	public IssueTypesetterIngestionHelper(WorkflowExecutionContext context) {
		super(context);
	}

	@Override
	protected String obtainTypesetterSubmission(ContentAssembly productCA)
			throws RSuiteException {
		return productCA
				.getLayeredMetadataValue(LMD_FIELD_TYPESETTER_UPDATE_TYPE);
	}

	@Override
	protected ContentAssembly obtainTypesetterCA(ContentAssembly productCA)
			throws RSuiteException {
		return ProjectBrowserUtils.getChildCaByNameAndType(getContext(), productCA,
				CA_TYPE_TYPESETTER, CA_NAME_TYPESETTER);
	}

	@Override
	protected String obtainProductCAId(String productCode)
			throws RSuiteException {
		return JournalUtils.getIssueCaId(getLogger(), getUser(), getContext(), productCode);
	}

	@Override
	protected String getProductId(ContentAssembly productCA)
			throws RSuiteException {
		return productCA.getLayeredMetadataValue(LMD_FIELD_JOURNAL_CODE);
	}

}
