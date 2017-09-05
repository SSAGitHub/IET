package org.theiet.rsuite.journals.domain.constants;

import com.rsicms.projectshelper.datatype.RSuiteCaType;

public enum JournalsCaTypes implements RSuiteCaType {


	AUTHOR_CORRECTIONS("authorCorrections", "Author Corrections"),
    TYPESETTER("typesetter", "Typesetter"),
    WITHDRAWN_ARTICLES("withdrawnArticles"),
    ARTICLE("article"),
    ARTICLES("articles"),
    JOURNAL_ARCHIVE("journal_archive"),
    JOURNAL_ARCHIVE_YEAR("journal_archive_year"),
    JOURNAL_ARCHIVE_MONTH("journal_archive_month"),
    JOURNAL("journal"),
    JOURNALS("journals"),
    YEAR("year"),
    VOLUME("volume"),
    ISSUE("issue"),
    ISSUES("issues"),
    ISSUE_ARTICLES("issueArticles");

    private String name = "";
    
    private String defaultContainerName = "";

    private JournalsCaTypes(String name) {
        this.name = name;
    }

    private JournalsCaTypes(String name, String defaultContainerName) {
        this.name = name;
        this.defaultContainerName = defaultContainerName;
    }


    public String getDefaultContainerName() {
        return defaultContainerName;
    }

    @Override
    public String getTypeName() {
        return name;
    }

    public boolean isTypeOf(String type){
    	return name.equalsIgnoreCase(type);
    }
}
