
import com.rsicms.groovy.utils.*

// Groovy script to load workflows
// -----------------------------------------------------------------------
def projectDir = new File(scriptFile.absolutePath).parentFile.parentFile.parentFile
def workflowDir = new File(projectDir, "workflow")



def wfImporter = new DeployWorkflowUtils(rsuite);
wfImporter.importWorkflow(workflowDir)