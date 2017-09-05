package org.theiet.rsuite.standards.domain.book.index.datatypes;

import java.util.*;

public class IndexTerm {

    private String termValue;
    
    private List<IndexTerm> subIndexTerms = new ArrayList<IndexTerm>();

    private int level = 1;
    
    public IndexTerm(String termValue) {
        this.termValue = termValue;
    }
    
    public IndexTerm(String termValue, IndexTerm subIndexTerm){
        this(termValue);
        subIndexTerms.add(subIndexTerm);
        level += subIndexTerm.getLevel();
    }
    
    
    public IndexTerm(String termValue, int level) {
        this(termValue);
        this.level = level;
        //addSubIndexTerms(level -1);
    }

    private void addSubIndexTerms(int level) {
        if (level > 0){
            subIndexTerms.add(new IndexTerm("", level -1));
        }
        
        
    }

    private void addSubIndexTerm(IndexTerm indexTerm){
        
        IndexTerm subIndexTerm = getSubIndexTerm(indexTerm.getIndexTerm());
        
        if (subIndexTerm == null){
            subIndexTerm = new IndexTerm(indexTerm.getIndexTerm());
            subIndexTerms.add(subIndexTerm);
        }
        
        subIndexTerm.addSubIndexTerms(indexTerm);
    }
    
    private IndexTerm getSubIndexTerm(String indexTermValue){
        for (IndexTerm indexTerm : subIndexTerms){
            if (indexTerm.equals(indexTermValue)){
                return indexTerm;
            }
        }
        
        return null;
    }
    
    public boolean canMergeIndexTerm(IndexTerm indexTerm){
        return level == indexTerm.getLevel();
    }
    
    public void mergeIndexTerm(IndexTerm indexTerm){
        
        if (level != indexTerm.getLevel()){
            throw new RuntimeException("Unable to merge index term with different level");
        }
        
        addSubIndexTerms(indexTerm);
    }

    private void addSubIndexTerms(IndexTerm indexTerm) {
        for (IndexTerm subIndexTerm : indexTerm.getSubIndexTerms()){
            addSubIndexTerm(subIndexTerm);
        }
    }


    public String getIndexTerm() {
        return termValue;
    }

    public List<IndexTerm> getSubIndexTerms() {
        return subIndexTerms;
    }

        
    public boolean hasSubIndexTerms(){
        return !subIndexTerms.isEmpty();
    }
    
    @Override
    public String toString() {
        return termValue;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((termValue == null) ? 0 : termValue.hashCode());
        result = prime * result + level;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IndexTerm other = (IndexTerm) obj;
        if (termValue == null) {
            if (other.termValue != null)
                return false;
        } else if (!termValue.equals(other.termValue))
            return false;
        if (level != other.level)
            return false;
        return true;
    }
    
}
