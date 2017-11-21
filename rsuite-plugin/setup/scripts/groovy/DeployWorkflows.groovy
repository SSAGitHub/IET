
import com.rsicms.groovy.utils.*

// Groovy script to load workflows
// -----------------------------------------------------------------------
def projectDir = new File("setup");
def workflowDir = new File(projectDir, "workflow")

println("projectDir=" + projectDir);
println("workflowDir=" + workflowDir);

def wfImporter = new DeployWorkflowUtils(rsuite);
wfImporter.importWorkflow(workflowDir)