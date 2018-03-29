import com.reallysi.rsuite.client.api.*
import com.rsicms.groovy.utils.*

// -----------------------------------------------------------------------

def util = new DeployUserRolesUtils(rsuite);

def projectDir = new File(scriptFile.absolutePath).parentFile
def roles = "editor,EditorialAssistant,ProductionController"
def pass = "test"
def email = "test.the.iet@gmail.com"

println("baseDir=  ${baseDir}");
def baseDir = "";
try {
    baseDir = "${baseDir}";
} catch (e) {
    baseDir = System.getenv("RSUITE_FTP_DIR");
}

def setUpFtpProperties(userId, targetFolder){
	def properties = [
				"ftp.host" :"us5.rsuitecms.com", 
				"ftp.user" :"rsuite_iet", 
				"ftp.password" :"rsuite_iet.32",
				"ftp.folder.onix.validation.succeeded": "test/onix/validation/succeeded",
				"ftp.folder.onix": targetFolder,
				"contact.first.name" : "Esteban"
			];
	rsuite.setUserProperties(userId, false, properties);
}	

def setUpLocalProperties(userId, targetFolder){    
    def properties = [
        "deliveryPath" :baseDir + targetFolder,
        "contact.first.name" : "Mark",
        "type" : "local"
    ];
	rsuite.setUserProperties(userId, false, properties);
}

rsuite.login();

	util.upsertUser("OnixAdministrator", pass, "Onix Administrator", email, "OnixAdministrator");
	util.upsertUser("testOnixFtpUser", pass, "Onix Book Vendor", email, "OnixBookVendor");
	
//	setUpFtpProperties("testOnixFtpUser", "/onix");
	setUpLocalProperties("testOnixFtpUser", "/onix");
	
rsuite.logout();

//===========================================================================
