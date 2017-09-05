import com.rsicms.groovy.utils.*

// -----------------------------------------------------------------------

rsuite.login();
def util = new DeployCaUtils(rsuite);

// Types reflecting the IET journals hierarchy:


util.defineContentAssemblyType("onixRecipient", "Onix Recipient", false, "onixOutput")
util.defineContentAssemblyType("onixConfigurations", "Onix Configurations", false, "onixRecipient")
util.defineContentAssemblyType("onixBookConfiguration", "Onix Book Configuration", false, null)
util.defineContentAssemblyType("onixOutput", "Onix Output Container", false, null)


rsuite.logout();