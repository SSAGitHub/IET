import com.reallysi.rsuite.client.api.*
import com.rsicms.groovy.utils.*

// -----------------------------------------------------------------------

def util = new DeployUserRolesUtils(rsuite);

def projectDir = new File(scriptFile.absolutePath).parentFile
def roles = "editor,EditorialAssistant,ProductionController"
def pass = "test"
def email = "test.the.iet@gmail.com"


def setUpFtpProperties(userId, targetFolder){
	def properties = ["ftp.host" :"us5.rsuitecms.com", 
		"ftp.user" :"rsuite_iet", 
		"ftp.password" :"rsuite_iet.32", "ftp.folder.book.update": "test/book_update", "ftp.folder.book": targetFolder, "contact.first.name" : "Lukasz"];
	rsuite.setUserProperties(userId, false, properties);
}	

rsuite.login();
	util.upsertUser("BookEditorialAssistant", pass, "Book Editorial Assistant", email, "BookEditorialAssistant");
	util.upsertUser("BookProductionController", pass, "Book Production Controller", email, "BookProductionController");	
	util.upsertUser("BookTypesetter", pass, "Book Typesetter", email, "BookTypesetter");
	util.upsertUser("MetaDataDistributionUser", pass, "Metadata Distribution List", email, "");
	util.upsertUser("MetaDataDistributionStandardsUser", pass, "Metadata Distribution List Standards", email, "");
	
	util.upsertUser("BookPrinter", pass, "Book Printer", email, "BookPrinter");	
	
	util.upsertUser("salesGuest", "sales2014", "Sales Guest", email, "RSuiteAdministrator");
	setUpFtpProperties("BookTypesetter", "test/book");
	setUpFtpProperties("BookPrinter", "test/book_printer");
	
rsuite.logout();

//===========================================================================
