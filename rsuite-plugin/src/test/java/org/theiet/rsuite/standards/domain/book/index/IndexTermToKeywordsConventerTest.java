package org.theiet.rsuite.standards.domain.book.index;

import java.util.List;

import org.junit.*;
import org.theiet.rsuite.standards.domain.book.index.datatypes.IndexTerm;

public class IndexTermToKeywordsConventerTest {

    @Test
    public void should_convert_one_level_index_term_to_singe_keyword(){
        IndexTerm indexTerm = new IndexTerm("Sample");
        
        List<String> keywords = IndexTermToKeywordsConventer.convertIndexTermToKeywords(indexTerm);
        
        Assert.assertEquals(1, keywords.size());
        Assert.assertEquals("Sample", keywords.get(0));
    }
    
    @Test
    public void should_convert_and_index_term_with_one_sub_term_to_one_keyword(){
        IndexTerm indexTerm = new IndexTerm("Sample", new IndexTerm("resolution"));

        List<String> keywords = IndexTermToKeywordsConventer.convertIndexTermToKeywords(indexTerm);
        
        Assert.assertEquals(1, keywords.size());
        Assert.assertEquals("Sample resolution", keywords.get(0));
    }
    
    @Test
    public void should_convert_and_index_term_with_two_sub_term_to_two_keywords(){
        IndexTerm indexTerm = new IndexTerm("Sample", new IndexTerm("resolution"));
        IndexTerm indexTerm2 = new IndexTerm("Sample", new IndexTerm("restriction"));

        indexTerm.mergeIndexTerm(indexTerm2);
        
        List<String> keywords = IndexTermToKeywordsConventer.convertIndexTermToKeywords(indexTerm);
        
        Assert.assertEquals(2, keywords.size());
        Assert.assertEquals("Sample resolution", keywords.get(0));
        Assert.assertEquals("Sample restriction", keywords.get(1));
    }
    
    @Test
    public void should_convert_and_index_term_with_two_leve_sub_term_to_three_keywords(){
        
        IndexTerm sub2IndexTerm = new IndexTerm("restriction", new IndexTerm("false"));        
        IndexTerm sub2IndexTerm2 = new IndexTerm("restriction", new IndexTerm("true"));
        sub2IndexTerm.mergeIndexTerm(sub2IndexTerm2);
        
        IndexTerm subIndexTerm = new IndexTerm("resolution", sub2IndexTerm);
        IndexTerm indexTerm = new IndexTerm("Sample" , subIndexTerm);
        
        
        List<String> keywords = IndexTermToKeywordsConventer.convertIndexTermToKeywords(indexTerm);
        
        Assert.assertEquals(2, keywords.size());        
        Assert.assertEquals("Sample resolution restriction false", keywords.get(0));
        Assert.assertEquals("Sample resolution restriction true", keywords.get(1));
    }
}
