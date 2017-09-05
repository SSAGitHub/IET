package org.theiet.rsuite.iettv.domain.factories;

import static org.theiet.rsuite.utils.IetUtils.*;

import org.theiet.rsuite.iettv.constants.*;
import org.theiet.rsuite.iettv.domain.datatype.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;

public class VideoRecordFactory {

    public static VideoRecord createVideoRecord(ExecutionContext context, User user, VideoRecordMetadata videoMetadata, String yearContainerId) throws RSuiteException{
        ContentAssembly videoRecordContainer = createVideoRecordContainer(context, user, videoMetadata, yearContainerId);
        return new VideoRecord(context, user, videoRecordContainer);
    }
    
  public static ContentAssembly createVideoRecordContainer(ExecutionContext context, User user, VideoRecordMetadata videoMetadata, String yearContainerId) throws RSuiteException{                
        
        ContentAssembly videoRecordCa = ProjectContentAssemblyUtils.createContentAssembly(context, yearContainerId, videoMetadata.getTitle(), IetTvCaType.VIDEO_RECORD);
        String videoRecordCaId = videoRecordCa.getId();
                
        ProjectContentAssemblyUtils.createContentAssembly(context, videoRecordCaId, IetTvCaType.RELATED_FILES.getDefaultContainerName(), IetTvCaType.RELATED_FILES);
        ProjectContentAssemblyUtils.createContentAssembly(context, videoRecordCaId, IetTvCaType.NOTES.getDefaultContainerName(), IetTvCaType.NOTES);       
        
        context.getManagedObjectService().setMetaDataEntry(user, videoRecordCaId, IetTvLmdFields.VIDEO_ID.createMetadataItem(videoMetadata.getVideoId()));
        
        return context.getContentAssemblyService().getContentAssembly(user, videoRecordCaId);
    }
}
