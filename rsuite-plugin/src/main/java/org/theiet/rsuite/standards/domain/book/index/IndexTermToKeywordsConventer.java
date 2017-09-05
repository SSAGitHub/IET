package org.theiet.rsuite.standards.domain.book.index;

import java.util.*;

import org.theiet.rsuite.standards.domain.book.index.datatypes.IndexTerm;

public class IndexTermToKeywordsConventer {

    
    public static List<String> convertIndexTermToKeywords(IndexTerm indexTerm){
        List<String> keywords = new ArrayList<String>();
        
        if (!indexTerm.hasSubIndexTerms()){
            keywords.add(indexTerm.getIndexTerm());   
        }
        
        for (IndexTerm subIndexTerm : indexTerm.getSubIndexTerms()){
            List<String> subKeywords = convertIndexTermToKeywords(subIndexTerm);
            
            for (String subKeyword : subKeywords){
                keywords.add(indexTerm.getIndexTerm() + " " + subKeyword);
            }
            
        }
        
        return keywords;
    }
}
