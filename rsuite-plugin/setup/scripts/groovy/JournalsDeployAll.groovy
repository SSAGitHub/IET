import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*



try {
	rsuite.login();

	println "[INFO] ======== Running:  " + "JournalConfigureLayeredMetadata.groovy";
	evaluate(new File("setup/scripts/groovy/JournalConfigureLayeredMetadata.groovy"));
	
	
	println "[INFO] ======== Running:  " + "JournalConfigureRoles.groovy";
	evaluate(new File("setup/scripts/groovy/JournalConfigureRoles.groovy"));

	println "[INFO] ======== Running:  " + "JournalDefineCaTypes.groovy";
	evaluate(new File("setup/scripts/groovy/JournalDefineCaTypes.groovy"));
	
	println "[INFO] ======== Running:  " + "JournalCreateBrowseStructure.groovy";
	evaluate(new File("setup/scripts/groovy/JournalCreateBrowseStructure.groovy"));
	
	println "[INFO] ======== Running:  " + "JournalImportAndConfigureDtds.groovy";
	evaluate(new File("setup/scripts/groovy/JournalImportAndConfigureDtds.groovy"));
	
}finally{
	rsuite.logout();
}
