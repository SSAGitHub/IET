import com.reallysi.rsuite.client.api.*
import com.rsicms.groovy.utils.*

def util = new DeployUserRolesUtils(rsuite);




util.createRoleIfnotExist("JournalTypesetter", "Journal Typesetter");
util.createRoleIfnotExist("JournalInspecClassifier", "Journal Inspec Classifier");
util.createRoleIfnotExist("JournalEditorialAssistant", "Journal Editorial Assistant");
util.createRoleIfnotExist("JournalProductionController", "Journal Production Controller");
util.createRoleIfnotExist("JournalDigitalLibrary", "Journal Digital Library");
util.createRoleIfnotExist("JournalIssuePrinter", "Journal Issue Printer");

						          
