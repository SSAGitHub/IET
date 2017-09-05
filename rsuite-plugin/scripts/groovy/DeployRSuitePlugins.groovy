// Groovy script to deploy the IET plugins

def projectDir = new File(scriptFile.absolutePath).parentFile.parentFile.parentFile
def targetDir = new File(projectDir, "target");
def libDir = new File(new File(projectDir, "java"), "lib");



rsuite.login();


deployPlugin(new File(targetDir, "iet-plugin.jar"));
deployPlugin(new File(libDir, "rsuite-lmd-editor-plugin.jar"));

// These jars are provided by RSI and fetched from the RSI 
// Ivy repository.
//
// If you modify this list, please update the project
// documentation in the rsuite-setup.xml file. Also
// update the deploy-3rd-party-rsuite-plugins-locally
// Ant task in the project build.xml file.
if (false) {
  deployPlugin(new File(libDir, "rsuite-workflow-monitor-plugin.jar"));
  deployPlugin(new File(libDir, "rsuite-alias-manipulator-plugin.jar"));
  deployPlugin(new File(libDir, "rsuite-alias-setting-mo-advisor-plugin.jar"));
  deployPlugin(new File(libDir, "rsuite-dev-plugin.jar"));
  deployPlugin(new File(libDir, "rsuite-zipuploader-plugin.jar"));
  deployPlugin(new File(libDir, "rsuite-zip-downloader-plugin.jar"));
  //deployPlugin(new File(libDir, "rsuite-workflow-visualizer-plugin.jar"));
  //deployPlugin(new File(libDir, "rsuite-simple-workflow-reporter-plugin"));
}

rsuite.logout();

//SERVER SPECIFIC
//println "[INFO] ======== Running:  " + "ConfigureLocalUsers.groovy";
//evaluate(new File("ConfigureLocalUsers.groovy"));

def deployPlugin(file) {
	println " + [INFO] Deploying ${file.getName()}...";
	rsuite.deployPlugin(file);
}
