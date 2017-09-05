package org.theiet.rsuite.standards.domain.book.index.datatypes;

import java.util.*;

public class ElementIndexTerms {

    private String elementId;
    
    private List<IndexTerm> indexTerms = new ArrayList<IndexTerm>();
    
    
    public ElementIndexTerms(String elementId) {
        this.elementId = elementId;
    }

    public void addIndexTerm(IndexTerm indexTerm){

        IndexTerm existingIndexTerm = findIndexTerm(indexTerm);
        
        if (existingIndexTerm == null || !existingIndexTerm.canMergeIndexTerm(indexTerm)){
            existingIndexTerm = createNewIndexTerm(indexTerm);
        }
        
        existingIndexTerm.mergeIndexTerm(indexTerm);

                
    }

    private IndexTerm createNewIndexTerm(IndexTerm indexTerm) {
        IndexTerm existingIndexTerm;
        existingIndexTerm = new IndexTerm(indexTerm.getIndexTerm(), indexTerm.getLevel());
        indexTerms.add(existingIndexTerm);
        return existingIndexTerm;
    }

    private IndexTerm findIndexTerm(IndexTerm indexTermToFind) {
        for (IndexTerm indexTerm : indexTerms){
            if (indexTerm.equals(indexTermToFind)){
                return indexTerm;
            }
        }
        
        return null;
    }

    public String getElementId() {
        return elementId;
    }

    public List<IndexTerm> getIndexTerms() {
        return new ArrayList<IndexTerm>(indexTerms);
    }
    
    
}
