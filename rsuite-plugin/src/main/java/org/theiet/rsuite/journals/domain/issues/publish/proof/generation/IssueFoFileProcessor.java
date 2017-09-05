package org.theiet.rsuite.journals.domain.issues.publish.proof.generation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.theiet.rsuite.journals.domain.issues.publish.common.areatree.IssueProofInformation;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.utils.ProjectTransformationUtils;

public class IssueFoFileProcessor {

	
	private static final String XSLT_PARAM_BLANK_PAGES_TO_ADD = "blank-pages-to-add";
	private static final String XSLT_PARAM_PRELIM_PAGE_URI = "prelim-page-uri";	
	private static final String XSLT_PARAM_EXTRACT_PRELIM_PAGE = "extract-prelim-page";
	
	private static String XSLT_ISSUE_FO_POST_PROCESSOR = "rsuite:/res/plugin/iet/xslt/jats-fo-xsl/issue-fo-post-processor.xsl";
	
	public static void processIssueFoFile(ExecutionContext context, File foFile, IssueProofInformation areaTree) throws RSuiteException {
		Map<String, String> processingParameters = new HashMap<String, String>();
		setUpExtractLegalPage(areaTree, foFile.getParentFile(), processingParameters);
		setUpBlankPages(areaTree, processingParameters);
		
		
		if (processingParameters.size() > 0){
			processIssueFoFile(context, foFile, processingParameters);
		}
	}
	
	private static void processIssueFoFile(ExecutionContext context, File foFile, Map<String, String> paramaters) throws RSuiteException {
		File foFileOrig = prepareInputFile(foFile);
		ProjectTransformationUtils.transformDocument(context, foFileOrig, XSLT_ISSUE_FO_POST_PROCESSOR, foFile, paramaters);		
	}

	private static File prepareInputFile(File foFile) throws RSuiteException {
		String baseFoFileName = FilenameUtils.getBaseName(foFile.getName());
		String baseFoFileExstension = FilenameUtils.getExtension(foFile.getName());
		
		File foFileOrig = new File(foFile.getParentFile(), baseFoFileName + "_orig" + "." + baseFoFileExstension);
		
		try {
			FileUtils.moveFile(foFile, foFileOrig);
		} catch (IOException e) {
			throw new RSuiteException(0, "Unable to rename the fo file: " + foFile, e);
		}
		
		return foFileOrig;
	}

	private static void setUpBlankPages(IssueProofInformation proofIssue,
			Map<String, String> processingParameters) {

		int blankPagesToAdd = proofIssue.getAdvertPagesToAdd();

		if (blankPagesToAdd > 0) {			
			processingParameters.put(XSLT_PARAM_BLANK_PAGES_TO_ADD,
					String.valueOf(blankPagesToAdd));
		}
	}

	private static void setUpExtractLegalPage(IssueProofInformation proofIssue,
			File foFolder, Map<String, String> processingParameters) {

		if (proofIssue.shouldExtractInstructPage()) {
			processingParameters
					.put(XSLT_PARAM_EXTRACT_PRELIM_PAGE, String.valueOf(true));
			processingParameters
			.put(XSLT_PARAM_PRELIM_PAGE_URI, getPrelimFoUri(foFolder));
		}

	}

	private static String getPrelimFoUri(File foFolder) {
		return new File(foFolder, "instruct_page.fo").toURI().toString();
	}
}
