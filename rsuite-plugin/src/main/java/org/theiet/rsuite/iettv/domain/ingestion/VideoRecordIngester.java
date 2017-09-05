package org.theiet.rsuite.iettv.domain.ingestion;

import static org.theiet.rsuite.iettv.domain.factories.ChannelYearFactory.*;
import static org.theiet.rsuite.iettv.domain.factories.VideoRecordFactory.*;
import static org.theiet.rsuite.iettv.domain.search.IetTvFinder.*;

import org.theiet.rsuite.iettv.domain.datatype.*;
import org.theiet.rsuite.iettv.domain.validation.IetTvValidator;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class VideoRecordIngester {

    public static VideoRecord insgestVideo(ExecutionContext context, User user,
            VideoRecordIngestionPackage ingestionPackage) throws RSuiteException {

        VideoRecordMetadata videoMetadata = ingestionPackage.getVideoMetadata();

        IetTvValidator.validateFile(ingestionPackage.getVideoXMLFile(), context.getXmlApiManager().getRSuiteAwareEntityResolver());

        VideoRecord videoRecord = findVideoRecordByVideoId(context, user, videoMetadata.getVideoId());

        if (videoRecord == null) {
        	ContentAssembly yearContainer = getOrCreateChannelYearContainer(context, videoMetadata);
            videoRecord = createVideoRecord(context, user, videoMetadata, yearContainer.getId());            
        }
        
        
        boolean initialVideoRecordIngestion = isInitialIngestion(videoRecord);
        
        synchronized (videoMetadata.getVideoId().intern()) {
            videoRecord.uploadData(ingestionPackage);
        }
        
        if (initialVideoRecordIngestion && VideoInspecChecker.inspecRequried(videoMetadata)){
        	videoRecord.startInspecDeliveryWorkflow();        	
        }
        
        if (initialVideoRecordIngestion) {
            videoRecord.startCrossRefDeliveryWorkflow();
        } else {
            videoRecord.mergeInspecData();
        }

        return videoRecord;
    }

    private static boolean isInitialIngestion(VideoRecord videoRecord) throws RSuiteException {
        boolean initialVideoRecordIngestion = false;
        if (videoRecord.getVideoMetadataMo() == null){
            initialVideoRecordIngestion = true;
        }
        return initialVideoRecordIngestion;
    }
}
