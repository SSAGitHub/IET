package org.theiet.rsuite.iettv.domain.datatype;

import java.util.*;


public class VideoRecordMetadata {

    private VideoChannel mainChannel;
    
    private List<VideoChannel> channels = new ArrayList<VideoChannel>();
    
    private String year;
    
    private String videoId;
    
    private String videoNumber;

    private VideoStatus videoStatus;

    private String title;
    
    private String accessionNumber;
    
    public static class Builder{
        
        private VideoChannel mainChannel;
        
        private List<VideoChannel> channels = new ArrayList<VideoChannel>();
        
        private String year;
        
        private String videoId;
        
        private String videoNumber;

        private VideoStatus videoStatus;

        private String title;
        
        private String accessionNumber;
        
        public Builder year(String value){
            this.year = value;
            return this;
        }
        
        public Builder channel(VideoChannel value){
        	
        	if (value.isMainChannel()){
        		mainChannel = value;
        	}
        	
            channels.add(value);
            return this;
        }
        
        public Builder title(String value){
            this.title = value;
            return this;
        }
        
        public Builder videoId(String value){
            this.videoId = value;
            return this;
        }
        
        public Builder videoNumber(String value){
            this.videoNumber = value;
            return this;
        }

        public Builder videoStatus(VideoStatus value) {
			this.videoStatus = value;
			return this;
			
		}
        
        public VideoRecordMetadata build(){
            return new VideoRecordMetadata(this);
        }
        
        public Builder accessionNumber(String value){
            this.accessionNumber = value;
            return this;
        }
    }
    
    private VideoRecordMetadata(Builder builder) {
        mainChannel = builder.mainChannel;
        channels = builder.channels;
        year = builder.year;
        videoId = builder.videoId;
        videoStatus = builder.videoStatus;
        title= builder.title;
        accessionNumber = builder.accessionNumber;
        videoNumber = builder.videoNumber;
    }

    public String getYear(){
        return year;
    }
    
    public List<VideoChannel> getChannels(){
        return channels;
    }
    
    public String getTitle(){
        return title;
    }
    
    
    public VideoChannel getMainChannel(){
        return mainChannel;
    }
    
    public String getVideoId(){
        return videoId;
    }
    
    public VideoStatus getVideoStatus(){
        return videoStatus;
    }

    public boolean isWithdrawn() {
        return videoStatus == VideoStatus.WITHDRAWN;
    }

	public String getAccessionNumber() {
		return accessionNumber;
	}

	public String getVideoNumber() {
		return videoNumber;
	}
    
}
