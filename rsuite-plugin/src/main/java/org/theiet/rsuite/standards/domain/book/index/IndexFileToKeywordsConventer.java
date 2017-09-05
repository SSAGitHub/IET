package org.theiet.rsuite.standards.domain.book.index;

import java.io.*;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.theiet.rsuite.standards.domain.book.index.datatypes.DitaFileIndexTerms;
import org.theiet.rsuite.standards.domain.book.index.embed.IndexTermsAsKeywordsEmbed;
import org.theiet.rsuite.standards.domain.book.index.parser.IndexFileParser;

import com.reallysi.rsuite.api.RSuiteException;

public class IndexFileToKeywordsConventer {

    private static IndexFileLocalizer indexFileLocalizer = new IndexFileLocalizer();
    
    public void convertIndexFileToKeywords(File inputFolder) throws RSuiteException {
        
        File tempFolder = new File(inputFolder.getParentFile(), UUID.randomUUID().toString());
        try{
            createTempFolderAndCopyOrignalFiles(inputFolder, tempFolder);
            File indexFile = indexFileLocalizer.localizeIndexFile(inputFolder);
            
            IndexFileParser indexFileParser = new IndexFileParser(indexFile);
            PublicationIndexTerms indexTerms = indexFileParser.parse();
            
            IndexTermsAsKeywordsEmbed conventer = new IndexTermsAsKeywordsEmbed();
            
            List<String> filesWithIndex = indexTerms.getFilesWithIndex();
            
            for (String fileName : filesWithIndex){
                File inputFile = new File(tempFolder, fileName);
                
                if (!inputFile.exists()){
                    continue;
                }
                
                File ouputFile = new File(inputFolder, "topics/" + fileName);
                FileUtils.deleteQuietly(ouputFile);                
                DitaFileIndexTerms fileIndexTerms = indexTerms.getIndexTermsForFile(fileName);                
                conventer.embedIndexTermsAsKeyWords(inputFile, fileIndexTerms, ouputFile);
            }
            
        } catch (IOException e) {
           throw new RSuiteException(-1, "Unable to create temporary fodler for " + inputFolder.getAbsolutePath(), e);
        }finally{
            FileUtils.deleteQuietly(tempFolder);
        }        
        
        
    }

    private void createTempFolderAndCopyOrignalFiles(File inputFolder, File tempFolder)
            throws IOException {
        
        tempFolder.mkdirs();        
        File topicsFolder = new File(inputFolder, "topics");
        
        FileUtils.copyDirectory(topicsFolder, tempFolder, true);        
    }

}
