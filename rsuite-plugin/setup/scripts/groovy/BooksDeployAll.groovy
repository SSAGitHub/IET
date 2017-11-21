import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*


rsuite.login();
try {

	println "[INFO] ======== Running:  " + "ConfigureLayeredMetadata.groovy";
	evaluate(new File("setup/scripts/groovy/ConfigureLayeredMetadata.groovy"));
	println "[INFO] ======== Running:  " + "ConfigureRoles.groovy";
	evaluate(new File("setup/scripts/groovy/ConfigureRoles.groovy"));
	//println "[INFO] ======== Running:  " + "ConfigureLocalUsers.groovy";
	//evaluate(new File("setup/scripts/groovy/ConfigureLocalUsers.groovy"));	
	println "[INFO] ======== Running:  " + "BookCaTypes.groovy";
	evaluate(new File("setup/scripts/groovy/BookCaTypes.groovy"));
	println "[INFO] ======== Running:  " + "BookCreateBrowseStructure.groovy";
	evaluate(new File("setup/scripts/groovy/BookCreateBrowseStructure.groovy"));
	println "[INFO] ======== Running:  " + "DeployWorkflows.groovy";
	evaluate(new File("setup/scripts/groovy/DeployWorkflows.groovy"));
	println "[INFO] ======== Running:  " + "ImportAndConfigureDtds.groovy";
	evaluate(new File("setup/scripts/groovy/ImportAndConfigureDtds.groovy"));
}
finally {
rsuite.logout();
}