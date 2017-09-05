package org.theiet.rsuite.journals.domain.article;


import static org.theiet.rsuite.journals.domain.constants.JournalsCaTypes.TYPESETTER;

import java.io.File;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadata;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadataUpdater;
import org.theiet.rsuite.journals.domain.constants.JournalsEvents;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.journals.domain.journal.JournalsFinder;
import org.theiet.rsuite.journals.utils.JournalUtils;
import org.theiet.rsuite.utils.IetUtils;
import org.theiet.rsuite.utils.PubtrackLogger;
import org.w3c.dom.Element;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ContentAssemblyCreateOptions;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.projectshelper.utils.ProjectXpathUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;
import com.rsicms.projectshelper.utils.browse.filters.NodeNameChildMoFilter;
import com.rsicms.rsuite.helpers.upload.DuplicateFilenamePolicy;
import com.rsicms.rsuite.helpers.upload.RSuiteFileLoadHelper;
import com.rsicms.rsuite.helpers.upload.RSuiteFileLoadOptions;
import com.rsicms.rsuite.helpers.upload.RSuiteFileLoadResult;

public class Article {

    private static final String ELEMENT_NAME_ARTICLE = "article";

    private static final String LMD_FIELD_TYPESETTER_UPDATE_TYPE = "typesetter_update_type";

    private static final String LMD_FIELD_JOURNAL_CODE = "journal_code";

    private static final String LMD_FIELD_RECEIVED_TYPESETTER_UPDATE = "received_typesetter_update";

    private static final String LMD_FIELD_AWAITING_TYPESETTER_UPDATES =
            "awaiting_typesetter_updates";

    private static final String LMD_VALUE_INITIAL = "initial";

    private static final String LMD_VALUE_FINAL = "final";

    private static final String LMD_VALUE_YES = "yes";

    private ContentAssembly articleCa;

    private String articleCaId;
    
    private String articleId;

    private ExecutionContext context;

    private XmlApiManager xmlApiManager;

    private ManagedObjectService moSvc;

    private User user;

    private ArticleMetadata articleMetadata;

    public Article(ExecutionContext context, User user, ManagedObject articleContainer) throws RSuiteException{
        ContentAssemblyService contentAssemblyService = context.getContentAssemblyService();
        ContentAssembly articleCa = contentAssemblyService.getContentAssembly(user, articleContainer.getId());
        this.user = user;
        initializeAdditionalFields(context, articleCa);
    }
    
    public Article(ExecutionContext context, User user, String articleCaId) throws RSuiteException{
    	this(context, user, null, articleCaId);
    }
    
    
    public Article(ExecutionContext context, User user, Log logger, String articleCaId)
            throws RSuiteException {
    	//TODO to remove
        ContentAssemblyService contentAssemblyService = context.getContentAssemblyService();
        ContentAssembly articleCa = contentAssemblyService.getContentAssembly(user, articleCaId);
        this.user = user;
        
        initializeAdditionalFields(context, articleCa);
    }

    private void initializeAdditionalFields(ExecutionContext context, ContentAssembly articleCa) {
        this.articleCa = articleCa;
        this.articleCaId = articleCa.getId();
        xmlApiManager = context.getXmlApiManager();
        articleId = articleCa.getDisplayName();
        moSvc = context.getManagedObjectService();
        this.context = context;
        articleMetadata = new ArticleMetadata(articleCa);
    }

    public Article(ExecutionContext context, User user, ContentAssembly articleCa) {
        this.user = user;        
        initializeAdditionalFields(context, articleCa);
    }

    public String getJounralCode() throws RSuiteException {
        return articleCa.getLayeredMetadataValue(LMD_FIELD_JOURNAL_CODE);
    }
    
    public String getJounralShortName() throws RSuiteException {
        return ProjectXpathUtils.getElementValueFromMo(context, getXMLMo(), "/*/front/journal-meta/journal-title-group/abbrev-journal-title");
    }
    
    public Journal getJournal() throws RSuiteException {
    	return JournalsFinder.findJournal(context, user, getJounralCode());
    }
    
    public ContentAssembly getArticleCA(){
    	return articleCa;
    }

    public String getTypesetterUpdateType() throws RSuiteException {

        String typesetterSubmission =
                articleCa.getLayeredMetadataValue(LMD_FIELD_TYPESETTER_UPDATE_TYPE);

        if (StringUtils.isBlank(typesetterSubmission)) {
            typesetterSubmission = LMD_VALUE_INITIAL;
        }

        return typesetterSubmission;
    }

    public ContentAssembly getTypesetterCA() throws RSuiteException{
    	return ProjectBrowserUtils.getChildCaByType(context, articleCa, TYPESETTER.getTypeName());
    }
    
    public ArticleTypesetterContainer getTypesetterContainer() throws RSuiteException{
    	return new ArticleTypesetterContainer(context, user, articleId, getTypesetterCA());
    }
    
    public ContentAssembly getManuscriptCA() throws RSuiteException{
    	return ProjectBrowserUtils.getChildCaByDisplayName(context, articleCa, "Manuscripts");
    }
    
    public ContentAssembly getTypesetterOrCreate() throws RSuiteException {
        ContentAssemblyService caSvc = context.getContentAssemblyService();
        ContentAssemblyCreateOptions options = new ContentAssemblyCreateOptions();
        options.setSilentIfExists(true);
        options.setType(TYPESETTER.getTypeName());
        ContentAssembly typesetterCa =
                caSvc.createContentAssembly(user, articleCaId,
                        TYPESETTER.getDefaultContainerName(), options);
        return typesetterCa;
    }

    public String getArticleCaId() {
        return articleCaId;
    }



    public void logArticleEvent(JournalsEvents event) throws RSuiteException {
        PubtrackLogger.logToProcess(context, user, "ARTICLE", articleId, event);
    }



    public RSuiteFileLoadResult loadTypesetterFiles(File typesetterUpdatePackage)
            throws RSuiteException {
        ContentAssembly typesetterCa = getTypesetterOrCreate();
        RSuiteFileLoadOptions loadOpts = new RSuiteFileLoadOptions(user);
        loadOpts.setDuplicateFilenamePolicy(DuplicateFilenamePolicy.UPDATE);
        RSuiteFileLoadHelper.loadZipContentsToCaNodeContainer(context, typesetterUpdatePackage,
                typesetterCa, loadOpts);

        RSuiteFileLoadResult loadResult = loadOpts.getLoadResult();

        if (loadResult.hasErrors()){
            handleLoadingError(loadResult);
        }
        

        Set<ManagedObject> loadedMos = loadResult.getResultManagedObjects();
        setUpMetadataForLoadedMos(loadedMos);

        return loadResult;
    }

    private void handleLoadingError(RSuiteFileLoadResult loadResult) throws RSuiteException {
        Set<File> failedFiles = loadResult.getFailedFiles();
        StringBuilder errorMessage = new StringBuilder();
        
        if (failedFiles.size() > 0){
            errorMessage = new StringBuilder("Unable to load following files: ");
            
            for (File failedFile : failedFiles){
                errorMessage.append(failedFile.getName()).append("\n");
            }
        }
        
        
        throw new RSuiteException("Errors occured during loading files " + errorMessage.toString());
    }

    public void setUpMetadataForLoadedMos(Set<ManagedObject> loadedMos) throws RSuiteException {
        for (ManagedObject mo : loadedMos) {

            if (!mo.isNonXml()) {
                Element elem = mo.getElement();
                if (elem.getLocalName().equals(ELEMENT_NAME_ARTICLE)) {
                    updateArticleMetadata(elem);                    
                }
            }
        }
    }

    private void updateArticleMetadata(Element elem) throws RSuiteException {
        String doi = JournalUtils.getArticleDOI(xmlApiManager, elem);
        String authorInstitution = JournalUtils.getArticleAuthorInstitution(xmlApiManager, elem);
        String authorCountry = JournalUtils.getArticleAuthorCountry(xmlApiManager, elem);

        ArticleMetadataUpdater metadataUpdater = createArticleMetadataUpdater();
        metadataUpdater.setDoi(doi);
        metadataUpdater.setAuthorInstitution(authorInstitution);
        metadataUpdater.sertAuthorCountry(authorCountry);
        
        metadataUpdater.updateMetadata();

    }
    
    public boolean isInitialTypesetterUpdate() throws RSuiteException {
        String checkFirstTypesetterSubmission =
                articleCa.getLayeredMetadataValue(LMD_FIELD_RECEIVED_TYPESETTER_UPDATE);

        if (StringUtils.isBlank(checkFirstTypesetterSubmission)) {
            return true;
        }

        return false;
    }

    public String getArticleId() {
        return articleId;
    }
    
    public String getShortArticleId() throws RSuiteException {
    	String shortArticleId = getArticleId();
    	return shortArticleId.replaceAll("\\.[A-Z0-9]+$", "");
    }

    public void cleanTypesetterAwaitingStatus() {
        IetUtils.removeMetaDataFieldFromCa(user, moSvc, articleCa,
                LMD_FIELD_AWAITING_TYPESETTER_UPDATES);
    }

    public void setReceivedTypesetterUpdateFlag() throws RSuiteException {
        MetaDataItem item = new MetaDataItem(LMD_FIELD_RECEIVED_TYPESETTER_UPDATE, LMD_VALUE_YES);
        moSvc.setMetaDataEntry(user, articleCaId, item);
    }

    public boolean isFinalTypesetterUpdate() throws RSuiteException {
        String typesetterSubmission = getTypesetterUpdateType();
        return LMD_VALUE_FINAL.equals(typesetterSubmission);
    }

    public ManagedObject getXMLMo() throws RSuiteException {
        return ProjectBrowserUtils.getChildMo(context, getTypesetterOrCreate(), new NodeNameChildMoFilter(
                ELEMENT_NAME_ARTICLE));
    }
    
    
    @Override
    public String toString() {
    	StringBuilder value = new StringBuilder("Article ");
    	value.append(articleId).append(" caId: ").append(articleCaId);
    	return value.toString();
    }
    
    public ArticleMetadataUpdater createArticleMetadataUpdater(){
    	return new ArticleMetadataUpdater(user, context.getManagedObjectService(), this);
    }

	public ArticleMetadata getArticleMetadata() {
		return articleMetadata;
	}

	public void refreshArticleCa() throws RSuiteException {
		articleCa = context.getContentAssemblyService().getContentAssembly(user, articleCaId);
		articleMetadata = new ArticleMetadata(articleCa);
	}
        
	public ArticleContentDetail getArticleContentDetail() throws RSuiteException{
		return new ArticleContentDetail(xmlApiManager.getXPathEvaluator(), getXMLMo().getElement());
	}
}
