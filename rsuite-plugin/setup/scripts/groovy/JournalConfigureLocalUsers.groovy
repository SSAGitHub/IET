import com.reallysi.rsuite.client.api.*
import com.rsicms.groovy.utils.*

// -----------------------------------------------------------------------

def util = new DeployUserRolesUtils(rsuite);

def projectDir = new File(scriptFile.absolutePath).parentFile
def roles = "Typesetter"
def pass = "test"
def email = "test.the.iet@gmail.com"
//def email = "ietteam@techset.co.uk"


rsuite.login();

try {

	util.upsertUser("ietTypesetter", pass, "IET Typesetter", email, "JournalTypesetter");
	util.upsertUser("ietClassifier", pass, "IET Inspec Classifier", email, "JournalInspecClassifier");
	util.upsertUser("EditorialAssistant", "edAssist", "Editorial Assistant", email, "JournalEditorialAssistant");
	util.upsertUser("ProductionController", "prodCont", "Production Controller", email, "JournalProductionController");
	util.upsertUser("DigitalLibraryArticle", pass, "DigitalLibraryArticle", email, "JournalDigitalLibrary");
	util.upsertUser("DigitalLibraryIssue", pass, "DigitalLibraryIssue", email, "JournalDigitalLibrary");
	util.upsertUser("JournalIssuePrinter", pass, "JournalIssuePrinter", email, "JournalIssuePrinter");
		
		
		
	setUpFtpProperties("ietTypesetter", ["iet.typesetter.ftp.manuscriptFolder" : "test/", "iet.typesetter.ftp.articleFolder" : "test/updates", "iet.typesetter.ftp.issueFolder" : "test/issues"]);
	setUpFtpProperties("ietClassifier", ["iet.inspec.ftp.folder" : "test/inspec"]);
	setUpFtpProperties("DigitalLibraryArticle", ["ftp.folder.article" :"test/DigitalLibraryArticle"]);
	setUpFtpProperties("DigitalLibraryIssue", ["ftp.folder.issue" :"test/DigitalLibraryIssue"]);
	setUpFtpProperties("JournalIssuePrinter", ["ftp.folder.main" :"test/JournalIssuePrinter"]);
	
	
} finally {
	rsuite.logout();
}


def setUpFtpProperties(userId, additionalProps){
	def properties = ["ftp.host" :"us5.rsuitecms.com", 
		"ftp.user" :"rsuite_iet", 
		"ftp.password" :"rsuite_iet.32", "contact.first.name" : "Lukasz", "type" : "ftp"];
	
	if (additionalProps != null){
		properties.putAll(additionalProps);
	}	
	
	rsuite.setUserProperties(userId, false, properties);
}


//===========================================================================
