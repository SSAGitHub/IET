package org.theiet.rsuite.journals.domain.issues.publish.finalartilces.export;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.journals.domain.article.publish.export.ArticleMoExportHandler;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.export.MoExportHandlerContext;
import com.rsicms.projectshelper.message.ProcessingMessageHandler;

public class IssueFinalArticlesMoExportHandler extends ArticleMoExportHandler {

	private Set<String> containersToSkip = new HashSet<String>();

	private List<String> articles = new ArrayList<String>();

	private ProcessingMessageHandler messageHandler;

	private Set<String> binaryExtensionToSkip = new HashSet<>();

	public IssueFinalArticlesMoExportHandler() {
		containersToSkip.add("Manuscripts");
		containersToSkip.add("Author Corrections");
		containersToSkip.add("Supplementary Materials");
		containersToSkip.add("Documentation");
		containersToSkip.add("Outputs");

		binaryExtensionToSkip.add("pdf");
		binaryExtensionToSkip.add("800");
		binaryExtensionToSkip.add("db");
	}

	@Override
	public void initialize(MoExportHandlerContext moExportHandlerContext)
			throws RSuiteException {
		super.initialize(moExportHandlerContext);

		messageHandler = moExportHandlerContext.getMessageHandler();
	}

	@Override
	public boolean exportMo(ManagedObject mo) {
		boolean parentResult = super.exportMo(mo);

		parentResult = checkIfSkipExport(mo, parentResult);

		try {
			if (isArticleMo(mo) && StringUtils.isNotBlank(isLegalOrInstructPage(mo))) {
				return false;
			}

		} catch (RSuiteException e) {
			messageHandler.error(
					"Problem with checking if the MO " + mo.getId()
							+ " should be exported", e);
		}

		return parentResult;
	}

	protected boolean checkIfSkipExport(ManagedObject mo, boolean parentResult) {
		try {
			if (mo.getObjectType() == ObjectType.CONTENT_ASSEMBLY) {

				String displayName = mo.getDisplayName();

				if (containersToSkip.contains(displayName)) {
					return false;
				}
			} else if (!mo.isNonXml()) {
				String nodeName = getMoElementName(mo);
				if (!"article".equals(nodeName)) {
					return false;
				}

			} else if (binaryFileToSkip(mo)) {
				return false;
			}

		} catch (RSuiteException e) {
			messageHandler.error(
					"Problem with checking if the MO " + mo.getId()
							+ " should be exported", e);
		}

		return parentResult;
	}

	private boolean binaryFileToSkip(ManagedObject mo) throws RSuiteException {
		if (mo.isNonXml()
				&& binaryExtensionToSkip.contains(getFileExtension(mo))) {
			return true;
		}

		return false;
	}

	private String getFileExtension(ManagedObject mo) throws RSuiteException {
		return FilenameUtils.getExtension(mo.getDisplayName().toLowerCase());
	}

	@Override
	public String getMoExportPath(ManagedObject mo, String exportUri)
			throws RSuiteException {
		String exportPath = super.getMoExportPath(mo, exportUri);

		if (isArticleMo(mo)) {
			exportPath = FilenameUtils.getName(exportPath);
			articles.add(exportPath);
		}

		return exportPath;
	}
	
	protected String isLegalOrInstructPage(ManagedObject mo) throws RSuiteException {
		String specialUse = mo.getElement().getAttribute("specific-use");

		if ("instruct_page".equals(specialUse) || "legal_page".equals(specialUse)) {
			return specialUse;
		}

		return null;
	}

	protected boolean isArticleMo(ManagedObject mo) throws RSuiteException {
		return mo.getObjectType() == ObjectType.MANAGED_OBJECT
				&& "article".equals(getMoElementName(mo));
	}

	protected String getMoElementName(ManagedObject mo) throws RSuiteException {
		return mo.getElement().getNodeName();
	}

	@Override
	public void afterExport(File outputFolder) throws RSuiteException {
		super.afterExport(outputFolder);
	}

}
