import com.reallysi.rsuite.client.api.*
import com.rsicms.groovy.utils.*

def util = new DeployUserRolesUtils(rsuite);

rsuite.login();

try {

util.createRoleIfnotExist("OnixAdministrator", "Onix Administrator");
util.createRoleIfnotExist("OnixBookVendor", "Onix Book Vendor");

} finally {
	rsuite.logout();
}