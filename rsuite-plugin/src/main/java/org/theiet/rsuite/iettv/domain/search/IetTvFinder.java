package org.theiet.rsuite.iettv.domain.search;

import org.theiet.rsuite.iettv.constants.*;
import org.theiet.rsuite.iettv.domain.datatype.*;
import org.theiet.rsuite.utils.SearchUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class IetTvFinder {

    
        public static VideoRecord findExistingVideoRecordByVideoId(ExecutionContext context, User user, String videoId) throws RSuiteException{
            ContentAssembly videoRecordContainer = SearchUtils.findCaBasedOnLmd(context, user, IetTvLmdFields.VIDEO_ID, videoId);
            if (videoRecordContainer == null){
                throw new RSuiteException("There is no video record with id " + videoId);
            }
            
            return new VideoRecord(context, user, videoRecordContainer);
        }
        
        public static VideoRecord findVideoRecordByVideoId(ExecutionContext context, User user, String videoId) throws RSuiteException{
            ContentAssembly videoRecordContainer = SearchUtils.findCaBasedOnLmd(context, user, IetTvLmdFields.VIDEO_ID, videoId);
            if (videoRecordContainer == null){
                return null;
            }
            
            return new VideoRecord(context, user, videoRecordContainer);
        }
        
        public static ContentAssembly findChannelContainer(ExecutionContext context, User user, VideoChannel videoChannel) throws RSuiteException{
            String xpath = "/rs_ca_map/rs_ca[rmd:get-display-name(.) = '" + videoChannel.getName() + "' and rmd:get-type(.) = '" + IetTvCaType.CHANNEL.getTypeName() + "']";
            ContentAssembly channelContainer = SearchUtils.findSingleCaBasedOnRSX(context, user, xpath);
            
            if (channelContainer == null){
                throw new RSuiteException("Unable to find channel container with for a name: " + videoChannel.getName());
            }
            
            return channelContainer;
        }
        
        public static ContentAssembly findChannelYearContainer(ExecutionContext context, User user, VideoChannel videoChannel, String year) throws RSuiteException{
            String yearId = videoChannel.generateYearChannelId(year);
            return SearchUtils.findCaBasedOnLmd(context, user, IetTvLmdFields.CHANNEL_YEAR_ID, yearId);
        }
        
        public static ContentAssembly findMainDomainContainer(ExecutionContext context, User user) throws RSuiteException{
            return SearchUtils.findCaBasedOnCaType(context, user, IetTvCaType.VIDEO_DOMAIN);
        }
        
        public static IetTvDomain findIetTvDomain(ExecutionContext context, User user) throws RSuiteException{
            return new IetTvDomain(SearchUtils.findCaBasedOnCaType(context, user, IetTvCaType.VIDEO_DOMAIN));
        }
}
