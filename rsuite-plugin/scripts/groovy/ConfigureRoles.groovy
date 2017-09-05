import com.reallysi.rsuite.client.api.*
import com.rsicms.groovy.utils.*

/*def  createRoleIfnotExist(roleName, roleDesc){
	try{
	rsuite.createRole(roleName, "", roleDesc);
	println "Adding "+ roleName +" (desc="+roleDesc+")";
	}catch (Exception e) {
		// ignore
	}
}*/

def util = new DeployUserRolesUtils(rsuite);
	
util.createRoleIfnotExist("BookTypesetter", "Book Typesetter");
util.createRoleIfnotExist("BookPrinter", "Book Printer");
util.createRoleIfnotExist("BookEditorialAssistant", "Book Editorial Assistant");
util.createRoleIfnotExist("BookProductionController", "Book Production Controller")

