package org.theiet.rsuite.iettv.domain.factories;

import java.io.File;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecordIngestionPackage;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecordMetadata;
import org.theiet.rsuite.iettv.domain.factories.parsers.VideoRecordMetadataXMLParser;

import com.reallysi.rsuite.api.RSuiteException;

public class VideoRecordIngestionPackageFactory {

    
    private static final String FOLDER_RELATED_FILES = "Attachments";

    public static VideoRecordIngestionPackage createVideoRecordPackage(String videoPackageId, File videoRecordIngestionPackageFolder) throws RSuiteException{        
        File videoRecordXML = localizeVideoRecordXML(videoPackageId, videoRecordIngestionPackageFolder);
        File relatedFileFolder = localizeRelatedFilesForlder(videoRecordIngestionPackageFolder);
        VideoRecordMetadata videoMetada = VideoRecordMetadataXMLParser.praseVideoRecordMetadata(videoRecordXML);
        
        addPrefixToRelatedFiles(relatedFileFolder, videoMetada.getVideoNumber());
        
        return new VideoRecordIngestionPackage(relatedFileFolder, videoMetada, videoRecordXML);
    }

    @SuppressWarnings({"unchecked" })
	private static void addPrefixToRelatedFiles(File relatedFileFolder, String videoNumber) {
    	String prefix = videoNumber + "_";
    	Iterator<File> iterateFiles = FileUtils.iterateFiles(relatedFileFolder, null, true);
    	
    	List<File> filesToRename = new ArrayList<>();
    	
    	while (iterateFiles.hasNext()) {
			File file = iterateFiles.next();
			
			if (file.isFile() && !file.getName().startsWith(prefix)){
				filesToRename.add(file);
			}
		}

    	for (File file : filesToRename){
    		File newFile = new File(file.getParentFile(), prefix + file.getName());
    		file.renameTo(newFile);
    	}
	}

	private static File localizeVideoRecordXML(String videoPackageId, File videoRecordIngestionPackageFolder) throws RSuiteException {
        
        File videoRecordFile = null;
              
        for (File file : videoRecordIngestionPackageFolder.listFiles()){
            if (isXMLFile(file)){                
                videoRecordFile = file;
            }            
        }
        
        if (!videoPackageId.equalsIgnoreCase(FilenameUtils.getBaseName(videoRecordFile.getName()))){
            throw new RSuiteException("The video id from the package name does not match video id from the video xml file");
        }
        
        return videoRecordFile;
    }

    private static boolean isXMLFile(File file) {
        
        if (!file.isFile()){
            return false;
        }
        
        String extension = FilenameUtils.getExtension(file.getName());
        
        if ("xml".equals(extension.toLowerCase())){
           return true; 
        }
        
        return false;
    }

    private static File localizeRelatedFilesForlder(File videoRecordIngestionPackageFolder) {
        return new File(videoRecordIngestionPackageFolder, FOLDER_RELATED_FILES);
    }
}
