package org.theiet.rsuite.standards.domain.book.index;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.theiet.rsuite.standards.domain.book.index.*;
import org.theiet.rsuite.standards.domain.book.index.datatypes.*;

public class PublicationIdexTermsTest {

    @Test
    public void should_add_an_index_term_for_empty_index_term_list() {
        PublicationIndexTerms indexTerms = new PublicationIndexTerms();
        
        String fileName = "test.dita";
        IndexTerm indexTerm = new IndexTerm("testTerm");
        indexTerms.addIndexTerm(fileName, "r411", indexTerm);
        
        
        DitaFileIndexTerms termsForFile = indexTerms.getIndexTermsForFile(fileName);
        assertNotNull(termsForFile);
        
        List<IndexTerm> indexTermsForElement = termsForFile.getIndexTermsForElement("r411");
        assertTrue(indexTermsForElement.size() == 1);
        
        IndexTerm actualIndexTerm = indexTermsForElement.get(0);
        assertEquals("testTerm", actualIndexTerm.getIndexTerm());
    }

    @Test
    public void should_add_a_sub_index_term_to_existing_term(){
        
        PublicationIndexTerms indexTerms = new PublicationIndexTerms();
        
        String elementId = "r411";
        String fileName = "test.dita";
        
        IndexTerm indexTerm = new IndexTerm("testTerm");
        indexTerms.addIndexTerm(fileName, elementId, indexTerm);

        IndexTerm indexTerm2 = new IndexTerm("testTerm", new IndexTerm("subTerm1"));
        
        
        indexTerms.addIndexTerm(fileName, elementId, indexTerm2);
        
        DitaFileIndexTerms termsForFile = indexTerms.getIndexTermsForFile(fileName);
        List<IndexTerm> termsForElement = termsForFile.getIndexTermsForElement(elementId);
        
        assertEquals(2, termsForElement.size());
        
        IndexTerm actualIndexTerm = termsForElement.get(1);
        assertEquals("subTerm1", actualIndexTerm.getSubIndexTerms().get(0).getIndexTerm());
    }
    
    @Test
    public void should_add_a_sub_index_term_to_existing_term_with_another_subTerm(){
        
        PublicationIndexTerms indexTerms = new PublicationIndexTerms();
        
        String elementId = "r411";
        String fileName = "test.dita";
        
        IndexTerm indexTerm = new IndexTerm("testTerm", new IndexTerm("subTerm1"));
        indexTerms.addIndexTerm(fileName, elementId, indexTerm);
        

        IndexTerm indexTerm2 = new IndexTerm("testTerm", new IndexTerm("subTerm2"));
        //indexTerm2.addSubIndexTerm();        
        indexTerms.addIndexTerm(fileName, elementId, indexTerm2);
        
        DitaFileIndexTerms termsForFile = indexTerms.getIndexTermsForFile(fileName);
        List<IndexTerm> termsForElement = termsForFile.getIndexTermsForElement(elementId);
        
        IndexTerm actualIndexTerm = termsForElement.get(0);
        
        assertEquals(2, actualIndexTerm.getSubIndexTerms().size());
        assertEquals("subTerm1", actualIndexTerm.getSubIndexTerms().get(0).getIndexTerm());
        assertEquals("subTerm2", actualIndexTerm.getSubIndexTerms().get(1).getIndexTerm());
    }
}
