package org.theiet.rsuite.journals.domain.article.publish.upload;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadataUpdater;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.publish.datatype.UploadGeneratedOutputsResult;
import com.rsicms.projectshelper.publish.storage.upload.VersionableGeneratedOutputUploader;

public class ArticleGeneratedOutputUploader extends VersionableGeneratedOutputUploader{

    private Article article;

    private ManagedObjectService moService;
    
    private User user;
    

    
    public ArticleGeneratedOutputUploader(ExecutionContext context, User user, Log logger, String workflowProcessId, Article article) {
    	super(context, user, logger, workflowProcessId);
        this.article = article;
        this.moService = context.getManagedObjectService();
        this.user = user;
    }
    
    @Override
    public void afterUpload(UploadGeneratedOutputsResult uploadResult,
    		OutputGenerationResult outputGenerationResult)
    		throws RSuiteException {
    		
    	setTypesetterPagesLmd(outputGenerationResult);
    }

	private void setTypesetterPagesLmd(
			OutputGenerationResult outputGenerationResult)
			throws RSuiteException {
		Map<String, File> moOutPuts = outputGenerationResult.getMoOutPuts();
    	
    	for (String moId : moOutPuts.keySet()){
    		File pdfFile = moOutPuts.get(moId);
    		int numberPages = getNumberPages(pdfFile);
    		
    		ArticleMetadataUpdater metadataUpdater = article.createArticleMetadataUpdater();
    		metadataUpdater.setTypesetPages(String.valueOf(numberPages));
    		metadataUpdater.updateMetadata();    		
    	}
	}

	private int getNumberPages(File pdfFile) throws RSuiteException  {
		try{
			PDDocument doc = PDDocument.load(pdfFile);
			return doc.getNumberOfPages();
		}catch (IOException e){
			throw new RSuiteException(0, "Unable to get number of pages for file " + pdfFile.getAbsolutePath(), e);
		}

	}

	@Override
	protected ContentAssembly getOutputsCa(String sourceMOid) throws RSuiteException {
		return article.getTypesetterCA();
	}

    
}
