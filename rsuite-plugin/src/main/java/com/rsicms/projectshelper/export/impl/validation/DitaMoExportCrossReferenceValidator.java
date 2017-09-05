package com.rsicms.projectshelper.export.impl.validation;


public class DitaMoExportCrossReferenceValidator extends
		DefaultMoExportCrossReferenceValidator {

	@Override
	protected LinkTargetParser getLinkTargetParser() {
		return new DitaLinkTargetParser();
	}
}
