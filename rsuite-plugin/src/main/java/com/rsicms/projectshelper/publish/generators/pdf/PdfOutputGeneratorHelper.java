package com.rsicms.projectshelper.publish.generators.pdf;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class PdfOutputGeneratorHelper {

	public static void copyNonXmlFilesToTemp(File inputFolder, File tempFolder) throws IOException {
	       
        FileUtils.copyDirectory(inputFolder, tempFolder, new FileFilter() {
            
            @Override
            public boolean accept(File pathname) {
                if (pathname.isFile()){
                    String extension = FilenameUtils.getExtension(pathname.getName());
                    
                    if (isXMLFile(extension) ){
                        return false;
                    }
                }
                
                return true;
            }

            private boolean isXMLFile(String extension) {
                return "xml".equalsIgnoreCase(extension) || "dita".equalsIgnoreCase(extension);
            }
        });
        
    }
}
