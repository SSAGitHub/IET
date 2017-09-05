import com.reallysi.rsuite.client.api.*
import com.rsicms.groovy.utils.*

def util = new DeployUserRolesUtils(rsuite);

rsuite.login();

try {

util.createRoleIfnotExist("IetTvAuthorizedUser", "IET.tv Authorized User");
util.createRoleIfnotExist("IetTvAdministrator", "IET.tv Administrator")
util.createRoleIfnotExist("IetTvInspecClassifier", "IET.tv Inspec Classifier")
util.createRoleIfnotExist("IetTvProduction", "IET.tv Production")
util.createRoleIfnotExist("IetTvCrossRef", "IET.tv CrossRef")

} finally {
	rsuite.logout();
}