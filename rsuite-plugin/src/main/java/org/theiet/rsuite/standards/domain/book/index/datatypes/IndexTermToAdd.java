package org.theiet.rsuite.standards.domain.book.index.datatypes;

import java.util.*;

public class IndexTermToAdd {

    private List<String> termValues = new ArrayList<String>();

    private String fileName;

    private String elementId;


    public IndexTermToAdd(String fileName, String elementId, Stack<String> indexTermStack) {
        this.fileName = fileName;
        this.elementId = elementId;
        for (String indexTerm : indexTermStack) {
            termValues.add(indexTerm);
        }
    }

    public void setTermValue(int level, String value) {
        termValues.set(level, value);
    }

    public IndexTerm createIndexTerm() {

        IndexTerm indexTerm = null;

        for (int i = termValues.size() - 1; i >= 0; i--) {
            String indexTermValue = termValues.get(i);
            indexTerm = createNewIndexTerm(indexTerm, indexTermValue);
        }

        return indexTerm;
    }

    private IndexTerm createNewIndexTerm(IndexTerm indexTerm, String indexTermValue) {
        IndexTerm newIndexTerm;
        if (indexTerm != null) {
            newIndexTerm = new IndexTerm(indexTermValue, indexTerm);
        }else{
            newIndexTerm = new IndexTerm(indexTermValue);
        }
        return newIndexTerm;
    }

    public String getFileName() {
        return fileName;
    }

    public String getElementId() {
        return elementId;
    }
    
    @Override
    public String toString() {
        return elementId + " " + termValues;
    }
    
}
