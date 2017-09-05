package org.theiet.rsuite.journals.domain.issues.publish.proof.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadataUpdater;
import org.theiet.rsuite.journals.domain.issues.publish.common.areatree.AreaTreeParser;
import org.theiet.rsuite.journals.domain.issues.publish.common.areatree.IssueProofInformation;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.publish.datatype.BaseUploadGeneratedOutputsResult;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.publish.datatype.UploadGeneratedOutputsResult;
import com.rsicms.projectshelper.publish.storage.upload.SingleMoUploaderHelper;
import com.rsicms.projectshelper.publish.storage.upload.VersionableGeneratedOutputUploader;

public class IssueGeneratedOutputUploader extends VersionableGeneratedOutputUploader {

	private Issue issue;

	private ExecutionContext context;

	private User user;

	private String workflowProcessId;

	private Log logger;

	public IssueGeneratedOutputUploader(ExecutionContext context, User user,
			Log logger, String workflowProcessId, Issue issue) {
		super(context, user, logger, workflowProcessId);
		this.issue = issue;
		this.context = context;
		this.user = user;
		this.workflowProcessId = workflowProcessId;
		this.logger = logger;

	}
	

	@Override
	public BaseUploadGeneratedOutputsResult uploadGeneratedOutputs(
			OutputGenerationResult outputGenerationResult)
			throws RSuiteException {

		BaseUploadGeneratedOutputsResult result = super.uploadGeneratedOutputs(outputGenerationResult);

		SingleMoUploaderHelper uploadHelper = new SingleMoUploaderHelper(context, user, logger, workflowProcessId, outputGenerationResult);
		List<ManagedObject> additionalMOs = uploadAdditionalPages(uploadHelper, outputGenerationResult);
		result.addAdditionalUploadedMoIds(additionalMOs);
		
		return result;
	}

	private List<ManagedObject> uploadAdditionalPages(
			SingleMoUploaderHelper uploadHelper, OutputGenerationResult outputGenerationResult)
			throws RSuiteException {
		List<ManagedObject> additionalMOs = new ArrayList<>();
		
		File outputFolder = getPdfFile(outputGenerationResult).getParentFile();
		
		ManagedObject uploadInstructPage = uploadInstructPage(uploadHelper, outputFolder);
		addToListIfNotNull(additionalMOs, uploadInstructPage);
		ManagedObject uploadLegalPage = uploadLegalPage(uploadHelper, outputFolder);
		addToListIfNotNull(additionalMOs, uploadLegalPage);
		return additionalMOs;
	}
	
	private void addToListIfNotNull(List<ManagedObject> additionalMOs, ManagedObject additionalPage){
		if (additionalPage != null){
			additionalMOs.add(additionalPage);
		}
	}

	@Override
	public void beforeUpload(OutputGenerationResult ouputGenerationResult)
			throws RSuiteException {
	}

	@Override
	public void afterUpload(UploadGeneratedOutputsResult uploadResult,
			OutputGenerationResult outputGenerationResult)
			throws RSuiteException {

		IssueProofInformation areaTree = getIssueProofInformation(outputGenerationResult);
		updateIssueLmd(areaTree);
		updateIssueArticles(areaTree);
	}

	private void updateIssueLmd(IssueProofInformation issueProof)
			throws RSuiteException {

		IssueMetadataUpdater metadataUpdater = issue.createIssueMetadataUpdater();
		

		metadataUpdater.setTotalPagination(issueProof.getTotalPageNumber());		
		metadataUpdater.setAdvertPages(issueProof.getAdvertPagesToAdd());
		metadataUpdater.setLastNumberedPage(issueProof.getLastNumberedPage());
		metadataUpdater.setInsideFrontCoverLegal();
		metadataUpdater.setPrelims(issueProof.getPrelimsPageNumber());
				
		if (issueProof.shouldExtractInstructPage()){
			metadataUpdater.setInsideBacktCoverInstructions();
		}	
		
		metadataUpdater.updateMetadata();
	}

	private ManagedObject uploadInstructPage(
			SingleMoUploaderHelper uploadHelper, File outputFolder)
			throws RSuiteException {
		File instructPageFile = new File(outputFolder, issue.getInstructPagePdfFilName());
		return uploadAdditionalPage(uploadHelper, instructPageFile);
	}

	private ManagedObject uploadLegalPage(SingleMoUploaderHelper uploadHelper, File outputFolder)
			throws RSuiteException {
		File legalPageFile = new File(outputFolder, issue.getLegalPagePdfFilName());
		return uploadAdditionalPage(uploadHelper, legalPageFile);
	}

	private ManagedObject uploadAdditionalPage(
			SingleMoUploaderHelper uploadHelper, File pageFile)
			throws RSuiteException {

		if (pageFile.exists()) {
			return uploadHelper.uploadGenratedFile(pageFile, getOutputsCa());
		}
		
		return null;
	}

	private ContentAssembly getOutputsCa() throws RSuiteException {
		return issue.getTypesetterCA();
	}

	private IssueProofInformation getIssueProofInformation(
			OutputGenerationResult outputGenerationResult)
			throws RSuiteException {
		File pdfFile = getPdfFile(outputGenerationResult);
		File workflowFolder = pdfFile.getParentFile().getParentFile()
				.getParentFile();
		File areaTreeFile = new File(workflowFolder, "downloads/" + issue.getIssueCa().getId() +  "/area-tree.xml");

		AreaTreeParser areaTreeParser = new AreaTreeParser();
		IssueProofInformation issueProof = areaTreeParser
				.parseAreaTreeFile(areaTreeFile);
		return issueProof;
	}

	private File getPdfFile(OutputGenerationResult outputGenerationResult) {
		File pdfFile = outputGenerationResult.getMoOutPuts().values()
				.iterator().next();
		return pdfFile;
	}

	private void updateIssueArticles(IssueProofInformation areaTree)
			throws RSuiteException {
		IssueArtilcesEnrichment articlesEnrichment = new IssueArtilcesEnrichment(
				context, user);
		articlesEnrichment.enrichIssueArticlesWithIssueData(issue,
				areaTree.getArticlesInformation());
	}

	@Override
	protected ContentAssembly getOutputsCa(String sourceMOid)
			throws RSuiteException {
		return issue.getTypesetterCA();
	}

}
