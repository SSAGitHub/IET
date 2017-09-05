package org.theiet.rsuite.standards.domain.book.index.datatypes;

import java.util.*;

public class DitaFileIndexTerms {

    private Map<String, ElementIndexTerms> elementToIndexTermsMap = new HashMap<String, ElementIndexTerms>();
    
    public void addElementIndexTerm(String elementId, IndexTerm indexTerm){
        ElementIndexTerms elementIndexTerm = elementToIndexTermsMap.get(elementId);
        
        if (elementIndexTerm == null){
            elementIndexTerm = new ElementIndexTerms(elementId);
            elementToIndexTermsMap.put(elementId, elementIndexTerm);
        }
        
        elementIndexTerm.addIndexTerm(indexTerm);
    }
    
    public List<IndexTerm> getIndexTermsForElement(String elementId){
        ElementIndexTerms elementIndexTerms = elementToIndexTermsMap.get(elementId);
        
        if (elementIndexTerms == null){
            return new ArrayList<IndexTerm>();
        }
        
        return elementToIndexTermsMap.get(elementId).getIndexTerms();
    }
}
