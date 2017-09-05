package org.theiet.rsuite.journals.domain.issues.publish.proof.generation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.theiet.rsuite.journals.domain.issues.publish.common.areatree.AreaTreeParser;
import org.theiet.rsuite.journals.domain.issues.publish.common.areatree.IssueProofInformation;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.utils.ProjectTransformationUtils;

class IssueBlankPagesAdder {

	private IssueBlankPagesAdder(){
	}
	
	private static String XSLT_BLANK_PAGES_ADDER = "rsuite:/res/plugin/iet/xslt/jats-fo-xsl/issue-add-blank-pages.xsl";

	public static void addMissingBlankPages(ExecutionContext context, File foFile, File areaTreeFile) throws RSuiteException{
	
		AreaTreeParser areaTreeParser = new AreaTreeParser();
		IssueProofInformation areaTree = areaTreeParser.parseAreaTreeFile(areaTreeFile);
		
		int blankPagesToAdd = areaTree.getAdvertPagesToAdd();
		
		if (blankPagesToAdd > 0){
			addBlankPages(context, foFile, blankPagesToAdd);
		}
		if (areaTree.getTocPageNumber() == 1){
			//extractLegalPage();
		}
	}

	private static void addBlankPages(ExecutionContext context, File foFile, int blankPagesToAdd) throws RSuiteException {
		
		Map<String, String> paramaters = createTransformParamaters(blankPagesToAdd);
		File foFileOrig = prepareInputFile(foFile);
		ProjectTransformationUtils.transformDocument(context, foFileOrig, XSLT_BLANK_PAGES_ADDER, foFile, paramaters);		
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
	
	private static Map<String, String> createTransformParamaters(int blankPagesToAdd){
		Map<String, String> paramaters = new HashMap<String, String>();
		paramaters.put("blank-pages-to-add", String.valueOf(blankPagesToAdd));
		
		return paramaters;
	}

	
}
