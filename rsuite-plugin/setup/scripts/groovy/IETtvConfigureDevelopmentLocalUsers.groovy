import com.reallysi.rsuite.client.api.*
import com.rsicms.groovy.utils.*

// -----------------------------------------------------------------------

def util = new DeployUserRolesUtils(rsuite);

def projectDir = new File(scriptFile.absolutePath).parentFile
def roles = "editor,EditorialAssistant,ProductionController"
def pass = "test"
def email = "test.the.iet@gmail.com"


def setUpFtpProperties(userId, targetFolder){
	def properties = [
				"ftp.host" :"us5.rsuitecms.com", 
				"ftp.user" :"rsuite_iet", 
				"ftp.password" :"rsuite_iet.32",
				"ftp.folder.main": targetFolder,
				"contact.first.name" : "Esteban",
                "type" : "ftp"
			];
	rsuite.setUserProperties(userId, false, properties);
}	

def setUpLocalProperties(userId, targetFolder){    
    def properties = [
        "deliveryPath" :targetFolder,
        "contact.first.name" : "Esteban",
        "type" : "local"
    ];
rsuite.setUserProperties(userId, false, properties);
}

rsuite.login();

	util.upsertUser("IETtvAdministrator", pass, "IETtv Administrator", email, "IetTvAdministrator");

    util.upsertUser("IetTvInspecUser", pass, "IetTv Inspec User", email, "IetTvInspecClassifier");
    
    util.upsertUser("IetTvProductionUser", pass, "IetTv Production User", email, "IetTvProduction");
    util.upsertUser("IetTvCrossRefUser", pass, "IetTv CrossRef User", email, "IetTvCrossRef");
    
    setUpFtpProperties("IetTvInspecUser", "test/iettv_inspec");
    setUpLocalProperties("IetTvProductionUser", "/tmp/iettv_production")
    setUpLocalProperties("IetTvCrossRefUser", "/tmp/iettv_crossref")

rsuite.logout();

//===========================================================================
