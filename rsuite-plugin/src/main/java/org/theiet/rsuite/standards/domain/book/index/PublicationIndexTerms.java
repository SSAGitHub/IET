package org.theiet.rsuite.standards.domain.book.index;

import java.util.*;

import org.theiet.rsuite.standards.domain.book.index.datatypes.*;

public class PublicationIndexTerms {

    private Map<String, DitaFileIndexTerms> ditaFilesIndexTerms = new HashMap<String, DitaFileIndexTerms>();
    
    public void addIndexTerm(String fileName, String elementId, IndexTerm indexTerm){
        
        DitaFileIndexTerms ditaFileIndexTerms = ditaFilesIndexTerms.get(fileName);
        
        ditaFileIndexTerms = createIfNotExist(fileName, ditaFileIndexTerms);
        
        ditaFileIndexTerms.addElementIndexTerm(elementId, indexTerm);
    }

    private DitaFileIndexTerms createIfNotExist(String fileName,
            DitaFileIndexTerms ditaFileIndexTerms) {
        if (ditaFileIndexTerms == null){
            ditaFileIndexTerms  = new DitaFileIndexTerms();
            ditaFilesIndexTerms.put(fileName, ditaFileIndexTerms);
        }
        
        return ditaFileIndexTerms;
    }
    
    public DitaFileIndexTerms getIndexTermsForFile(String fileName){
        return ditaFilesIndexTerms.get(fileName);
    }
    
    public List<String> getFilesWithIndex(){
        return new ArrayList<String>(ditaFilesIndexTerms.keySet());
    }
}
