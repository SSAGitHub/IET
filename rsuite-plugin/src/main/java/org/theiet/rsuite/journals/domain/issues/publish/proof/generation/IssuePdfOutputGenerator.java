package org.theiet.rsuite.journals.domain.issues.publish.proof.generation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.theiet.rsuite.domain.transforms.IetAntennaHouseHelper;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.datatype.IssueArticles;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueCoverDate;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadata;
import org.theiet.rsuite.journals.domain.issues.publish.common.IssuePublishWorkflowHelper;
import org.theiet.rsuite.journals.domain.issues.publish.common.areatree.AreaTreeParser;
import org.theiet.rsuite.journals.domain.issues.publish.common.areatree.IssueProofInformation;
import org.theiet.rsuite.journals.domain.issues.publish.common.datatype.IssueMap;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.publish.generators.pdf.engines.AntennaHousePdfEngine;
import com.rsicms.projectshelper.publish.workflow.generators.WorkflowAntennaHouseAreaTreePdfOutputGenerator;
import com.rsicms.projectshelper.utils.ProjectTransformationUtils;

public class IssuePdfOutputGenerator extends
		WorkflowAntennaHouseAreaTreePdfOutputGenerator {

	private static final String PASS_ALL = "all";

	private static final String PASS_BODY = "body";

	private static final String PASS_INITIAL = "initial";

	private static final String XSLT_PARAM_TYPE = "type";

	private static final String XSLT_PARAM_ISSUE_TITLE = "issue-title";
	
	private static final String XSLT_PARAM_ISSUE_COVER_DATE = "issue-cover-date";
	
	private static final String XSLT_PARAM_ISSUE_ABBREVIATED_COVER_DATE = "issue-abbreviated-cover-date";

	private static final String XSLT_PARAM_SPECIAL_ISSUE = "special-issue";
	
	private static final String XSLT_PARAM_ISSUE_VOLUME = "issue-volume";
	
	private static final String XSLT_PARAM_ISSUE_NUMBER = "issue-number";
	
	private static final String XSLT_PARAM_ISSUE_MODE = "issue-mode";
	
	private static final String XSLT_PARAM_FIRST_NUMBERED_PAGE = "first-numbered-page-param";
	
	private static final String XSLT_PARAM_SECTION = "section";
	
	private static final String XSLT_URI_ISSUE = "rsuite:/res/plugin/iet/xslt/jats-fo-xsl/issue-fo.xsl";
	
	private static final String XSLT_URI_LEGAL_PAGE = "rsuite:/res/plugin/iet/xslt/jats-fo-xsl/issue-legal-page-fo.xsl";

	private Log logger;

	private String issueCode;

	private Issue issue;

	private Map<String, String> xsltParameters;

	private ExecutionContext context;
	
	@Override
	public void initialize(ExecutionContext context, Log logger,
			Map<String, String> variables) throws RSuiteException {
		super.initialize(context, logger, variables);

		issue = IssuePublishWorkflowHelper.getIssueFromWorkflowContext(context, variables);

		issueCode = issue.getIssueCode();

		xsltParameters = createXsltParameters(issue);
		this.context = context;
	}

	private Map<String, String> createXsltParameters(Issue issue)
			throws RSuiteException {
		
		IssueMetadata issueMetadata = issue.getIssueMetadata();
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(XSLT_PARAM_FIRST_NUMBERED_PAGE,
				issueMetadata.getFirstNumberedPage());
		
		parameters.put(XSLT_PARAM_SPECIAL_ISSUE,
				String.valueOf(issueMetadata.isSpecialIssue()));
		
		parameters.put(XSLT_PARAM_ISSUE_TITLE,
				String.valueOf(issueMetadata.getIssueTitle()));
		
		IssueCoverDate issueCoverDate = issueMetadata.getCoverDate();
		
		parameters.put(XSLT_PARAM_ISSUE_COVER_DATE,
				String.valueOf(issueCoverDate.getCoverDateValue()));
		
		parameters.put(XSLT_PARAM_ISSUE_ABBREVIATED_COVER_DATE,
				String.valueOf(issueCoverDate.getAbbreviatedDate()));
		
		
		parameters.put(XSLT_PARAM_ISSUE_VOLUME,
				String.valueOf(issue.getVolume()));
		
		parameters.put(XSLT_PARAM_ISSUE_NUMBER,
				String.valueOf(issue.getIssueNumber()));
		
		parameters.put(XSLT_PARAM_ISSUE_MODE, "proof-issue");
		
		parameters.put(XSLT_PARAM_SECTION, PASS_BODY);
		parameters.put(XSLT_PARAM_TYPE, "issue-proof");
		
		IssueArticles issueArticles = issue.getIssueArticles();
		parameters.put("journal-short-name", issueArticles.getArticles().get(0).getJounralShortName());
		
		
		return parameters;
	}

	@Override
	protected String getXml2FoXsltUri() {
		return XSLT_URI_ISSUE;
	}

	@Override
	protected Map<String, String> getXml2FoXslParameters() {
		return xsltParameters;
	}

	@Override
	public File generateOutput(File workflowFolder, String moId,
			File inputFileOriginal, File outputFolder) throws RSuiteException {
		File inputFile = new File(inputFileOriginal.getParentFile(),
				IssueMap.getIssueMapFileName());

		AntennaHousePdfEngine formattingEngine = createPdfEngine();
		File outputFile = new File(outputFolder, getOutputFileName());
		File exportFolder = inputFile.getParentFile();
		try {

			File fakeAreaFile = getAreaTreeFile(exportFolder);
			createFakeAreaFile(fakeAreaFile);

			File foFile = createFoUsingAreaFile(exportFolder, inputFile,
					fakeAreaFile);
			
			File areaFile = getAreaTreeFile(exportFolder);
			formattingEngine.generateAreaTreeFileFromFo(logger, foFile,
					areaFile);

			archiveFoFile(exportFolder, foFile, PASS_INITIAL);
			
			foFile = createFoUsingAreaFile(exportFolder, inputFile, areaFile);

			formattingEngine.generateAreaTreeFileFromFo(logger, foFile,
					areaFile);
			
			archiveFoFile(exportFolder, foFile, PASS_BODY);
			
			xsltParameters.put(XSLT_PARAM_SECTION, PASS_ALL);
			
			foFile = createFoUsingAreaFile(exportFolder, inputFile, areaFile);
			formattingEngine.generateAreaTreeFileFromFo(logger, foFile,
					areaFile);
			
//			archiveFoFile(exportFolder, foFile, PASS_ALL);
//			
//			
//			xsltParameters.remove(XSLT_PARAM_SECTION);
//			
//			foFile = createFoUsingAreaFile(exportFolder, inputFile, areaFile);
//			formattingEngine.generateAreaTreeFileFromFo(logger, foFile,
//					areaFile);
			
			
			processIssueFoFile(foFile, areaFile);
			
			formattingEngine.generatePDFFromFo(logger, foFile, outputFile);
			
			generateLegalPDF(formattingEngine, exportFolder, outputFolder);
			generateStandaloneLegalPDF(formattingEngine, exportFolder, outputFolder);
			
		} catch (RSuiteException e) {
			throw new RSuiteException(0,
					createGenerateOutputErrorMessage(inputFile), e);
		} catch (IOException e) {
			throw new RSuiteException(0,
					createGenerateOutputErrorMessage(inputFile), e);
		}finally{
			createDownloadLinks(moId, workflowFolder, exportFolder);
		}

		return outputFile;
	}
	
	private void archiveFoFile(File exportFolder, File foFile, String passName) throws IOException{
		File archiveFo = getFoFileName(exportFolder, passName);
		FileUtils.moveFile(foFile, archiveFo);
	}
	

	private void processIssueFoFile(File foFile, File areaTreeFile) throws RSuiteException {
		AreaTreeParser areaTreeParser = new AreaTreeParser();
		IssueProofInformation areaTree = areaTreeParser.parseAreaTreeFile(areaTreeFile);
		IssueFoFileProcessor.processIssueFoFile(context, foFile, areaTree);
	}

	private void generateLegalPDF(AntennaHousePdfEngine formattingEngine, File exportFolder, File  outputFolder) throws RSuiteException {
		File legalPageXmlFile = new File(exportFolder, "legal_page.xml");
		File legalPageFoFile = new File(exportFolder, "legal_page.fo");
		
		ProjectTransformationUtils.transformDocument(context, legalPageXmlFile, XSLT_URI_LEGAL_PAGE, legalPageFoFile, getXml2FoXslParameters());
		
		File legalPdfFile = new File(outputFolder, issue.getLegalPagePdfFilName());
		formattingEngine.generatePDFFromFo(logger, legalPageFoFile, legalPdfFile);
		
		createDownloadLinkForAFile("legal", exportFolder.getParentFile(), legalPageFoFile, "Link to the legal page fo file");
	}
	
	private void generateStandaloneLegalPDF(
			AntennaHousePdfEngine formattingEngine, File exportFolder,
			File outputFolder) throws RSuiteException {
		File foFile = new File(exportFolder, "instruct_page.fo");
		
		if (foFile.exists()){
			File legalPdfFile = new File(outputFolder, issue.getInstructPagePdfFilName());
			formattingEngine.generatePDFFromFo(logger, foFile, legalPdfFile);			
			createDownloadLinkForAFile("instruct", exportFolder.getParentFile(), foFile, "Link to the instruct page fo file");	
		}		
	}

	@Override
	public String getOutputFileName(String moId, String defaultName) {
		return getOutputFileName();
	}

	private String getOutputFileName() {
		return issueCode + "-ISSUEPROOF" + ".pdf";
	}

	@Override
	protected String getAntennaHouseConfigurationConfiguration()
			throws RSuiteException {
		return IetAntennaHouseHelper.getAntenaHouseConfiguration();
	}

}
