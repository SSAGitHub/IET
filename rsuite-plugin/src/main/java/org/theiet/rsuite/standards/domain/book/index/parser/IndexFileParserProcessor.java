package org.theiet.rsuite.standards.domain.book.index.parser;

import java.util.*;

import org.theiet.rsuite.standards.domain.book.index.PublicationIndexTerms;
import org.theiet.rsuite.standards.domain.book.index.datatypes.IndexTermToAdd;

public class IndexFileParserProcessor {

    private Stack<String> indexTermStack = new Stack<String>();

    private int indexIndexEntryLevel = 0;

    private List<IndexTermToAdd> termsToAdd;

    private PublicationIndexTerms indexTerms = new PublicationIndexTerms();

    public void processStartIndexEntry() {

        if (indexIndexEntryLevel == 0) {
            termsToAdd = new ArrayList<IndexTermToAdd>();
        }

        indexIndexEntryLevel++;
        indexTermStack.push("");

    }

    public void processEndIndexEntry() {
        indexIndexEntryLevel--;

        if (!indexTermStack.isEmpty()) {
            indexTermStack.pop();
        }

        if (indexIndexEntryLevel == 0) {
            for (IndexTermToAdd termToAdd : termsToAdd) {
                indexTerms.addIndexTerm(termToAdd.getFileName(), termToAdd.getElementId(),
                        termToAdd.createIndexTerm());
            }

        }
    }

    public void processIndexItem(String fileName, String elementId) {
        IndexTermToAdd termToAdd = new IndexTermToAdd(fileName, elementId, indexTermStack);
        termsToAdd.add(termToAdd);
    }

    public void addTermValue(String termValue) {
        String normalizedTermValue = normalizeTermValue(termValue);
        indexTermStack.set(indexIndexEntryLevel -1, normalizedTermValue);

        if (indexIndexEntryLevel < indexTermStack.size()) {
            updateTermsForIndexToAdd(normalizedTermValue);
        }

    }

    private void updateTermsForIndexToAdd(String normalizedTermValue) {
        for (IndexTermToAdd termToAdd : termsToAdd) {
            termToAdd.setTermValue(indexIndexEntryLevel -1, normalizedTermValue);
        }
    }

    private String normalizeTermValue(String termValue) {
        termValue = termValue.replaceAll("\\n+\\s+", " ");
        termValue.trim();
        if (termValue.endsWith(" -")) {
            termValue = termValue.substring(0, termValue.lastIndexOf(" -"));
        }

        return termValue;
    }

    public PublicationIndexTerms getIndexTerms() {
        return indexTerms;
    }
    
}
