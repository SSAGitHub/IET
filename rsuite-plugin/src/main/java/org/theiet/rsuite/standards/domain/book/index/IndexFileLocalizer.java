package org.theiet.rsuite.standards.domain.book.index;

import java.io.File;

import org.apache.commons.io.*;

import com.reallysi.rsuite.api.RSuiteException;

public class IndexFileLocalizer {

    public File localizeIndexFile(File inputFolder) throws RSuiteException {
        File topicFolder = new File(inputFolder, "topics");
        for (File file : topicFolder.listFiles()){
            if (isIndexFile(file)){
                return file;
            }
        }
        
        throw new RSuiteException("Unable to find index file in " + inputFolder.getAbsolutePath());
        
    }

    private boolean isIndexFile(File file) {
        String fileName = file.getName();
        
        if (file.isFile() && FilenameUtils.getBaseName(fileName).endsWith("index")){
            return true;
        }
        
        return false;
    }





}
