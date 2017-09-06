import com.rsicms.groovy.utils.*

// -----------------------------------------------------------------------

rsuite.login();
def util = new DeployCaUtils(rsuite);

// Types reflecting the IET journals hierarchy:


util.defineContentAssemblyType("standardsBookGroup", "Standards Books Group", false, null)
util.defineContentAssemblyType("standards", "Standards Domain", false, "standardsBookGroup")
util.defineContentAssemblyType("template", "Standards Templates", false, "standards")

util.defineContentAssemblyType("standardsBookEdition", "Standards Book Edition", false, null)
util.defineContentAssemblyType("standardsBook", "Standards Book", false, null)
util.defineContentAssemblyType("standardsImages", "Standards Images", false, null)

util.defineContentAssemblyType("bookCorrections", "Book Corrections", false, "bookCorrections")
util.defineContentAssemblyType("finalFiles", "Final Files", false, null)
util.defineContentAssemblyType("printFiles", "Print Files", false, null)
util.defineContentAssemblyType("standardsTypescript", "Standards Typescript", false, null)
util.defineContentAssemblyType("printerInstructions", "Printer Instructions", false, null)

util.defineContentAssemblyType("outputs", "Outputs", false, null)
util.defineContentAssemblyType("outputEvent", "Output", false, null)
util.defineContentAssemblyType("standardsPublishHistory", "Publishing History", false, null)

util.defineContentAssemblyType("hotfolder", "Hotfolder", false, null)

rsuite.logout();