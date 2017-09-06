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

def setUpESPDeliveryUser(util, pass, email){
	def userId = "ESPStandardsBook";
	def properties = ["deliveryPath" :'$rsuiteTempFolder/espDelivery'];
	util.upsertUser(userId, pass, userId, email, "ESPDelivery");
	rsuite.setUserProperties(userId, false, properties);
}

rsuite.login();

	util.upsertUser("StandardsBookAuthorizedUser", pass, "Standards Book Authorized User", email, "StandardsBookAuthorizedUser");
	util.upsertUser("StandardsBookEditorialAssistant", pass, "Standards Book Editorial Assistant", email, "StandardsBookEditorialAssistant");
	util.upsertUser("StandardsBookProductionController", pass, "Standards Book Production Controller", email, "StandardsBookProductionController,StandardsBookAuthorizedUser");
	util.upsertUser("StandardsBookPrinter", pass, "Standards Book Printer", email, "StandardsBookPrinter,StandardsBookAuthorizedUser");	
	util.upsertUser("StandardsBookAuthor", pass, "Standards Book Author", email, "StandardsBookAuthor,StandardsBookAuthorizedUser");
	util.upsertUser("StandardsBookOwner", pass, "Standards Book Owner", email, "StandardsBookOwner,StandardsBookAuthorizedUser");
		
	setUpESPDeliveryUser(util, pass, email);
rsuite.logout();

//===========================================================================
