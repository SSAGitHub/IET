package org.theiet.rsuite.iettv.domain.datatype;

import java.io.File;

public class VideoRecordIngestionPackage {

    private VideoRecordMetadata videoMetadata;
    
    private File realatedFilesFolder;
    
    private File videoXMLFile;
    
    public VideoRecordIngestionPackage(File realatedFilesFolder, VideoRecordMetadata videoMetadata, File videoXMLFile) {
        this.videoMetadata = videoMetadata;
        this.realatedFilesFolder = realatedFilesFolder;
        this.videoXMLFile = videoXMLFile;
    }

    public VideoRecordMetadata getVideoMetadata() {
        return videoMetadata;
    }

    public File getRealatedFilesFolder() {
        return realatedFilesFolder;
    }
    
    public File getVideoXMLFile(){
        return videoXMLFile;
    }
    
    
}
