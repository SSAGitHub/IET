// Groovy script to load workflows
// -----------------------------------------------------------------------
def projectDir = new File(scriptFile.absolutePath).parentFile.parentFile.parentFile
def workflowDir = new File(projectDir, "workflow")

println " + [INFO] Workflow dir: " + workflowDir.getAbsolutePath();
rsuite.login()


results = workflowImporter.importFromDirectory(workflowDir)

results.entrySet().each {
  println "[INFO] loaded workflow: ${it.key} (${it.value})"
}


rsuite.logout();