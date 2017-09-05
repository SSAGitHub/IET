import com.rsicms.groovy.utils.*

// -----------------------------------------------------------------------

def util = new DeployCaUtils(rsuite);

// Types reflecting the IET journals hierarchy:

util.defineContentAssemblyType("journals", "Journals", false, "journal")
util.defineContentAssemblyType("journal", "Journal", false, null)
util.defineContentAssemblyType("articles", "Articles", false, "article")
util.defineContentAssemblyType("article", "Article", false, null)
util.defineContentAssemblyType("authorCorrections", "Author Corrections", false, null)

util.defineContentAssemblyType("journal_archive", "Journal Archive", false, null)
util.defineContentAssemblyType("journal_archive_year", "Journal Archive Year", false, null)
util.defineContentAssemblyType("journal_archive_month", "Journal Archive Month", false, null)

util.defineContentAssemblyType("withdrawnArticles", "Withdrawn Articles", false, null)


util.defineContentAssemblyType("issues", "Issues", false, "year")
util.defineContentAssemblyType("year", "Year", false, "volume")
util.defineContentAssemblyType("volume", "Volume", false, "issue")
util.defineContentAssemblyType("issue", "Issue", false, null)
util.defineContentAssemblyType("issueArticles", "Issue Articles", false, null)
util.defineContentAssemblyType("typesetter", "Typesetter", false, null)

util.defineContentAssemblyType("admin", "Admin", false, null)