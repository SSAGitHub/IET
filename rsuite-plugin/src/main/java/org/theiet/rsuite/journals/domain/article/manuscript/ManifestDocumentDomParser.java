package org.theiet.rsuite.journals.domain.article.manuscript;

import com.reallysi.rsuite.api.RSuiteException;

public interface ManifestDocumentDomParser {

	ManifestDocument parseManifestDOM() throws RSuiteException;
}
