package org.theiet.rsuite.iettv.domain.datatype;

import java.util.List;

public class VideoChannel {

    private String category;
	
	private String name;
    
    private String yearChannelIdPrefix;
    
    private boolean mainChannel;
    
    public VideoChannel(String name, String category, boolean mainChannel) {
        this.name = name;
        this.mainChannel = mainChannel;
        this.category = category;
        yearChannelIdPrefix = name.toLowerCase().replaceAll("\\s+", "_") + "_";
    }


    public VideoChannel(Builder builder) {
    	this(builder.name, builder.category, builder.mainChannel);
    }


	public String getName() {
        return name;
    }
    

    public String getCategory() {
		return category;
	}


	public static List<VideoChannel> availableChannels(){
        return null;
    }


    public String getInternName() {
        return name.intern();
    }
    
    public String generateYearChannelId(String year){
        return yearChannelIdPrefix + year;
    }


	public boolean isMainChannel() {
		return mainChannel;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VideoChannel other = (VideoChannel) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder value = new StringBuilder("Channel: ");
		value.append(name).append(" category: ").append(category);
		return value.toString();
	}
	
	 public static class Builder{
		 	private String category;
			
			private String name;
		    
		    private boolean mainChannel = false;
		    
		    public Builder name(String value){
		    	this.name = value.trim();
		    	return this;
		    }
		    
		    public Builder mainChannel(boolean value){
		    	this.mainChannel = value;
		    	return this;
		    }
		    
		    public Builder category(String value){
		    	this.category = value.trim();
		    	return this;
		    }
		    
		    public VideoChannel build(){
		    	return new VideoChannel(this);
		    }
	 }
}
