package org.theiet.rsuite.iettv.domain.factories;

import org.theiet.rsuite.iettv.constants.IetTvCaType;
import org.theiet.rsuite.iettv.constants.IetTvLmdFields;
import org.theiet.rsuite.iettv.domain.datatype.VideoChannel;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecordMetadata;
import org.theiet.rsuite.iettv.domain.search.IetTvFinder;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;

public class ChannelYearFactory {

    public static ContentAssembly getOrCreateChannelYearContainer(ExecutionContext context,
            VideoRecordMetadata videoMetadata) throws RSuiteException {

        User systemUser = context.getAuthorizationService().getSystemUser();
        
        ContentAssembly yearContainer =
                IetTvFinder.findChannelYearContainer(context, systemUser, videoMetadata.getMainChannel(),
                        videoMetadata.getYear());

        if (yearContainer != null) {
            return yearContainer;
        }

        return createChannelYearContainer(context, systemUser, videoMetadata);
    }

    private static ContentAssembly createChannelYearContainer(ExecutionContext context,
            User systemUser, VideoRecordMetadata videoMetadata) throws RSuiteException {
        
        VideoChannel mainChannel = videoMetadata.getMainChannel();
        String year = videoMetadata.getYear();

        ContentAssembly channelContainer =
                IetTvFinder.findChannelContainer(context, systemUser, mainChannel);

        ContentAssembly yearContainer = null;

        synchronized (mainChannel.getInternName()) {


            yearContainer = IetTvFinder.findChannelYearContainer(context, systemUser, mainChannel, year);
            if (yearContainer == null) {
                yearContainer =
                        ProjectContentAssemblyUtils.createContentAssembly(context, channelContainer.getId(), year,
                                IetTvCaType.YEAR);

                MetaDataItem yearIdLmd =
                        new MetaDataItem(IetTvLmdFields.CHANNEL_YEAR_ID.getLmdName(),
                                mainChannel.generateYearChannelId(year));
                context.getManagedObjectService().setMetaDataEntry(systemUser,
                        yearContainer.getId(), yearIdLmd);
            }

        }

        return yearContainer;

    }

}
