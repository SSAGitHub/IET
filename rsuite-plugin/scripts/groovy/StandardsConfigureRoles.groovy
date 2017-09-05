import com.reallysi.rsuite.client.api.*
import com.rsicms.groovy.utils.*

def util = new DeployUserRolesUtils(rsuite);

rsuite.login();

try {

util.createRoleIfnotExist("StandardsBookAuthorizedUser", "Standards Book Authorized User");
util.createRoleIfnotExist("StandardsBookProductionController", "Standards Book Production Controller")
util.createRoleIfnotExist("StandardsBookAuthor", "Standards Book Author")
util.createRoleIfnotExist("StandardsBookOwner", "Standards Book Owner")
util.createRoleIfnotExist("ESPDelivery", "ESP Plataform Delivery Role")

} finally {
	rsuite.logout();
}