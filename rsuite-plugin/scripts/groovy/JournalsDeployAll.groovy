import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*



try {
	rsuite.login();

	println "[INFO] ======== Running:  " + "JournalConfigureLayeredMetadata.groovy";
	evaluate(new File("JournalConfigureLayeredMetadata.groovy"));
	
	
	println "[INFO] ======== Running:  " + "JournalConfigureRoles.groovy";
	evaluate(new File("JournalConfigureRoles.groovy"));

	println "[INFO] ======== Running:  " + "JournalDefineCaTypes.groovy";
	evaluate(new File("JournalDefineCaTypes.groovy"));
	
	println "[INFO] ======== Running:  " + "JournalCreateBrowseStructure.groovy";
	evaluate(new File("JournalCreateBrowseStructure.groovy"));
	
	println "[INFO] ======== Running:  " + "JournalImportAndConfigureDtds.groovy";
	evaluate(new File("JournalImportAndConfigureDtds.groovy"));
	
}finally{
	rsuite.logout();
}
