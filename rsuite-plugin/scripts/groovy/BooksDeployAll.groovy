import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*


rsuite.login();
try {

	println "[INFO] ======== Running:  " + "ConfigureLayeredMetadata.groovy";
	evaluate(new File("ConfigureLayeredMetadata.groovy"));
	println "[INFO] ======== Running:  " + "ConfigureRoles.groovy";
	evaluate(new File("ConfigureRoles.groovy"));
	//println "[INFO] ======== Running:  " + "ConfigureLocalUsers.groovy";
	//evaluate(new File("ConfigureLocalUsers.groovy"));	
	println "[INFO] ======== Running:  " + "BookCaTypes.groovy";
	evaluate(new File("BookCaTypes.groovy"));
	println "[INFO] ======== Running:  " + "BookCreateBrowseStructure.groovy";
	evaluate(new File("BookCreateBrowseStructure.groovy"));
	println "[INFO] ======== Running:  " + "DeployWorkflows.groovy";
	evaluate(new File("DeployWorkflows.groovy"));
	println "[INFO] ======== Running:  " + "ImportAndConfigureDtds.groovy";
	evaluate(new File("ImportAndConfigureDtds.groovy"));
}
finally {
rsuite.logout();
}