import com.rsicms.groovy.utils.*

// -----------------------------------------------------------------------

def util = new DeployCaUtils(rsuite);


// Types reflecting the IET journals hierarchy:

util.defineContentAssemblyType("book", "Book", false, null)
util.defineContentAssemblyType("booksCA", "Books", false, "book")
util.defineContentAssemblyType("books", "Books Domain", false, "booksCA")
util.defineContentAssemblyType("bookCorrections", "Book Corrections", false, "bookCorrections")
util.defineContentAssemblyType("finalFiles", "Final Files", false, null)
util.defineContentAssemblyType("printFiles", "Print Files", false, null)
util.defineContentAssemblyType("typescript", "Typescript", false, null)
util.defineContentAssemblyType("printerInstructions", "Printer Instructions", false, null)

