import com.reallysi.rsuite.client.api.*

// -----------------------------------------------------------------------



def roles = ""
def pass = "test"
def email = "npf@theiet.org"


def upsertUser(userId, pass, fullName, email, roles){	
	def user = rsuite.getLocalUser(userId);
	if (user == null) {
		
		println "Adding "+ userId +" (password="+pass+")";
		rsuite.createLocalUser(userId, pass, fullName, email, roles);
	} else {
		//println "Updating "+ userId;
		//rsuite.updateLocalUser(userId, fullName, email, roles);
		//rsuite.setLocalUserPassword(userId, pass);
	}
}


	upsertUser("MetaDataDistributionUser", pass, "Metadata Distribution List", email, roles);
	
	

//===========================================================================
