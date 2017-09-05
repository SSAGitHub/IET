package org.theiet.rsuite.journals.domain.journal;

import java.util.*;

import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.constants.JournalsCaTypes;
import org.theiet.rsuite.utils.SearchUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class JournalsFinder {
    
    private static final String JOURNALS_NAME_RESEARCH_JOURNAL = "Research Journal";
    
    private static final String JOURNALS_NAME_LETTERS_JOURNAL = "Letters Journal";

    private JournalsFinder(){
    }
    
    public static boolean isResearchJournalArticle(ExecutionContext context, User user, Article article) throws RSuiteException{
        Set<String> resarchJournalsCodes = getResarchJournalsCodes(context, user);
        return resarchJournalsCodes.contains(article.getJounralCode());
    }
    
    public static boolean isLetterJournalArticle(ExecutionContext context, User user, Article article) throws RSuiteException{
        Set<String> resarchJournalsCodes = getLetterJournalsCodes(context, user);
        return resarchJournalsCodes.contains(article.getJounralCode());
    }
    
    public static Set<String> getResarchJournalsCodes(ExecutionContext context, User user) throws RSuiteException{
        ContentAssembly journalsCa = findJournalsCaBaseOnDisplayName(context, user, JOURNALS_NAME_RESEARCH_JOURNAL);
        Set<String> journalCodes = getJournalCodes(context, journalsCa);
        return journalCodes;
    }

    private static Set<String> getJournalCodes(ExecutionContext context, ContentAssembly journalsCa)
            throws RSuiteException {
        Set<String> journalCodes = new HashSet<String>();
        List<ContentAssembly> journalsList = ProjectBrowserUtils.getChildrenCaByType(context, journalsCa, JournalsCaTypes.JOURNAL.getTypeName());
        
        for (ContentAssembly journalCa : journalsList){
            Journal journal = new Journal(context, journalCa);
            journalCodes.add(journal.getJournalCode());
        }
        return journalCodes;
    }
    
    public static Set<String> getLetterJournalsCodes(ExecutionContext context, User user) throws RSuiteException{
        ContentAssembly journalsCa = findJournalsCaBaseOnDisplayName(context, user, JOURNALS_NAME_LETTERS_JOURNAL);
        Set<String> journalCodes = getJournalCodes(context, journalsCa);
        return journalCodes;
    }
    
    public static ContentAssembly findJournalsCaBaseOnDisplayName(ExecutionContext context, User user, String journalsName) throws RSuiteException{
        String xpath = "/rs_ca_map/rs_ca[rmd:get-display-name(.) = '" + journalsName + "' and rmd:get-type(.) = '" + JournalsCaTypes.JOURNALS.getTypeName() + "']";
        return SearchUtils.findSingleCaBasedOnRSX(context, user, xpath);
    }
    
    public static ContentAssembly findJournalCaBaseOnJournalCode(ExecutionContext context, User user, String journalCode) throws RSuiteException{
        String xpath = "/rs_ca_map/rs_ca[rmd:get-lmd-value(., '" + JournalConstants.LMD_FIELD_JOURNAL_CODE + "') = '" + journalCode + "' and rmd:get-type(.) = '" + JournalsCaTypes.JOURNAL.getTypeName() + "']";
        
        return SearchUtils.findSingleCaBasedOnRSX(context, user, xpath);
    }
    
    public static Journal findJournal(ExecutionContext context, User user, String journalCode) throws RSuiteException{
    	ContentAssembly journalCA = findJournalCaBaseOnJournalCode(context, user, journalCode);
    	
    	if (journalCA == null){
    		throw new RSuiteException("Unable to retrieve unique journal object for journal code " + journalCode);	
    	}
    	    	
    	return new Journal(context, journalCA);
    }
}
