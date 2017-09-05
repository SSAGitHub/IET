package org.theiet.rsuite.iettv.domain.inspec;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecord;
import org.theiet.rsuite.iettv.domain.search.IetTvFinder;
import org.theiet.rsuite.iettv.domain.validation.IetTvValidator;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.upload.UploadHelper;

public class VideoInspecLoader {

    private static final String FILENAME_EXTENSION_ZIP = "zip";
    
    private ExecutionContext context;
    
    private User user;
    
    private VideoRecord videoRecord;
    
    public VideoInspecLoader(ExecutionContext context, User user, String packageName) throws RSuiteException {
        this.context = context;
        this.user = user;
        String baseName = FilenameUtils.getBaseName(packageName);
        validatePackageName(packageName, baseName);
        obtainVideoRecord(context, user, baseName);
    }
    
    public void loadVideoInspec(File inspecFolder) throws RSuiteException{
        File fileToUpload = localizeInspecFile(inspecFolder);
        ContentAssembly videoCa = videoRecord.getVideoRecordContainer();
        IetTvValidator.validateFile(fileToUpload, context.getXmlApiManager().getRSuiteAwareEntityResolver());
        UploadHelper.upsertFileToContainer(context, user, fileToUpload, videoCa);   

    }

    private File localizeInspecFile(File inspecFolder) throws RSuiteException {
        for (File file : inspecFolder.listFiles()){
            String fileName = file.getName();
            String extension = FilenameUtils.getExtension(fileName);
            if (fileName.contains(videoRecord.getVideoId()) && "xml".equalsIgnoreCase(extension)){
                return file;
            }
        }
        
        throw new RSuiteException("Unable to find inpec file for video " + videoRecord.getVideoId() + " in folder " + inspecFolder.getAbsolutePath());        
    }

    private void validatePackageName(String packageName, String baseName) throws RSuiteException {
        String extension = FilenameUtils.getExtension(packageName);
        
        if (!FILENAME_EXTENSION_ZIP.equalsIgnoreCase(extension)){
            throw new RSuiteException("Invalid package name " + packageName + " expected extension " + FILENAME_EXTENSION_ZIP);
        }
        
        String packagePrefix = VideoRecord.getInspecFilePrefix();
        
        if (!baseName.startsWith(packagePrefix)){
            throw new RSuiteException("Invalid package name " + packageName + " expected file name prefix " + packagePrefix);
        }
       
    }
    
    private void obtainVideoRecord(ExecutionContext context, User user, String baseName)
            throws RSuiteException {
        String videoId = baseName.replace(VideoRecord.getInspecFilePrefix(), "");
        
        videoRecord = IetTvFinder.findExistingVideoRecordByVideoId(context, user, videoId);
        
    }

    public VideoRecord getVideoRecord() {
        return videoRecord;
    }
    
}
