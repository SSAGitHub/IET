package org.theiet.rsuite.iettv.domain.datatype;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.iettv.constants.*;
import org.theiet.rsuite.utils.WorkflowUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.*;
import com.rsicms.projectshelper.upload.UploadHelper;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserHelper;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class VideoRecord {

    private static final String PREFIX_IETTV = "iettv";
    
    private static final String FILENAME_PREFIX_IETTV = PREFIX_IETTV + "_";

    private static final String WORKFLOW_NAME_PRODUCTION_DELIVERY =
            "IET TV Video Record Production Delivery";

    private static final String WORKFLOW_NAME_INSPEC_DELIVERY =
            "IET TV Video Record Inspec Delivery";
    
    private static final String WORKFLOW_NAME_CROSSREF_DELIVERY =
            "IET TV Video Record CrossRef Delivery";

    private static final String ELEMENT_NAME_VIDEO = "Video";
    
    private static final String FILENAME_EXTENSION_ZIP = "zip";
    
    private ExecutionContext context;

    private User user;

    private ContentAssembly videoRecordContainer;

    private VideoRecordInspec videoRecordInspec;
    
    private String videoId;
    
    public VideoRecord(ExecutionContext context, User user, String videoContainerId)
            throws RSuiteException {
        ContentAssemblyService caSvc = context.getContentAssemblyService();
        videoRecordContainer = caSvc.getContentAssembly(user, videoContainerId);
        initializeObjectData(context, user);
    }

    public VideoRecord(ExecutionContext context, User user, ContentAssembly videoRecordContainer) throws RSuiteException {
        this.videoRecordContainer = videoRecordContainer;
        initializeObjectData(context, user);
    }

    private void initializeObjectData(ExecutionContext context, User user) throws RSuiteException{
        this.context = context;
        this.user = user;
        videoRecordInspec = new VideoRecordInspec(new ProjectBrowserHelper(context));
        videoId = videoRecordContainer.getLayeredMetadataValue(IetTvLmdFields.VIDEO_ID
                .getLmdName());
    }
    
    public boolean mergeInspecData() throws RSuiteException {
        return videoRecordInspec.mergeVideoInspec(context.getManagedObjectService(), user, videoRecordContainer, getVideoMetadataMo());
    }
    
    public boolean hasInspecData() throws RSuiteException {
        return videoRecordInspec.hasVideoInspec(context, user, videoRecordContainer);
    }

    public void uploadData(VideoRecordIngestionPackage ingestionPackage) throws RSuiteException {
        ContentAssembly relatedVideoFiles = getRelatedFilesContainer();
        UploadHelper.synchronizeContentAssemblyWithFolder(context, user, relatedVideoFiles,
                ingestionPackage.getRealatedFilesFolder());
        updateVideoMetadataMo(user, ingestionPackage);
    }


    private void updateVideoMetadataMo(User user, VideoRecordIngestionPackage ingestionPackage)
            throws RSuiteException {

        File videoXMLFile = ingestionPackage.getVideoXMLFile();
        UploadHelper.upsertFileToContainer(context, user, videoXMLFile, videoRecordContainer);

        setWithdrwanLmd(user, ingestionPackage);
    }

    private void setWithdrwanLmd(User user, VideoRecordIngestionPackage ingestionPackage)
            throws RSuiteException {
        VideoRecordMetadata videoMetadata = ingestionPackage.getVideoMetadata();
        String isWithdrawn = String.valueOf(videoMetadata.isWithdrawn());
        MetaDataItem withdrawnLmd = IetTvLmdFields.IS_WITHDRAWN.createMetadataItem(isWithdrawn);
        context.getManagedObjectService().setMetaDataEntry(user, videoRecordContainer.getId(),
                withdrawnLmd);
    }

    public ContentAssembly getVideoRecordContainer() {
        return videoRecordContainer;

    }

    public ContentAssembly getNotesContainer() throws RSuiteException {
        return ProjectBrowserUtils.getChildCaByType(context, videoRecordContainer, IetTvCaType.NOTES);
    }

    public ManagedObject getVideoMetadataMo() throws RSuiteException {
        return ProjectBrowserUtils.getChildMoByNodeName(context, videoRecordContainer, ELEMENT_NAME_VIDEO);
    }

    public ContentAssembly getRelatedFilesContainer() throws RSuiteException {
        return ProjectBrowserUtils.getChildCaByType(context, videoRecordContainer,
                IetTvCaType.RELATED_FILES);
    }

    public boolean isWithdrawn() throws RSuiteException {
        String isWithdrawn =
                videoRecordContainer.getLayeredMetadataValue(IetTvLmdFields.IS_WITHDRAWN
                        .getLmdName());
        return String.valueOf(true).equalsIgnoreCase(isWithdrawn);
    }
    
    public String getVideoId() {
        return videoId;
    }

    public String createVideoFileName(String extension) {
        return createVideoFileName(FILENAME_PREFIX_IETTV, extension);
    }
    
    public String createVideoInspecPackageFileName() throws RSuiteException {
        return createVideoFileName(getInspecFilePrefix(), FILENAME_EXTENSION_ZIP);
    }
    
    public static String getInspecFilePrefix(){
        return PREFIX_IETTV + "-inspec_";
    }
    
    private String createVideoFileName(String prefix,String extension) {
        
        String extensionPart = "";
        
        if (StringUtils.isNotBlank(extension)){
            extensionPart = "." + extension;
        }
        
        return prefix + getVideoId() + extensionPart;
    }

    public void startProductionDeliveryWorkflow() throws RSuiteException {
        startDeliveryWorkflow(WORKFLOW_NAME_PRODUCTION_DELIVERY);
    }

    public void startInspecDeliveryWorkflow() throws RSuiteException {
        startDeliveryWorkflow(WORKFLOW_NAME_INSPEC_DELIVERY);
    }
    
    public void startCrossRefDeliveryWorkflow() throws RSuiteException {
        startDeliveryWorkflow(WORKFLOW_NAME_CROSSREF_DELIVERY);
    }

	private void startDeliveryWorkflow(String workflowName)
			throws RSuiteException {
		ManagedObject videoRecordContainerMo = getVideoRecordContainerMo();
        WorkflowUtils.startWorkflowWithContext(context, user, videoRecordContainerMo, workflowName);
	}

    private ManagedObject getVideoRecordContainerMo() throws RSuiteException {
        ManagedObjectService moSvc = context.getManagedObjectService();
        return moSvc.getManagedObject(user, videoRecordContainer.getId());
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("rsuite id: " );
        sb.append(videoRecordContainer.getId());
        String videoId = getVideoId();
        sb.append(" video id: ");
        sb.append(videoId);
            
        return sb.toString();
    }
}
