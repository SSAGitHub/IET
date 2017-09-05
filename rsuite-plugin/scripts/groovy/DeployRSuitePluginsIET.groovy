// Groovy script to deploy the IET plugins

def projectDir = new File(scriptFile.absolutePath).parentFile.parentFile.parentFile
def targetDir = new File(projectDir, "target");
def libDir = new File(new File(projectDir, "java"), "lib");



rsuite.login();


deployPlugin(new File(targetDir, "iet-plugin.jar"));
//deployPlugin(new File(libDir, "rsuite-workflow-monitor-plugin.jar"));
//deployPlugin(new File(libDir, "rsuite-alias-setting-mo-advisor-plugin.jar"));
deployPlugin(new File(libDir, "rsuite-zip-uploader-plugin.jar"));
//deployPlugin(new File(libDir, "rsuite-zip-downloader-plugin.jar"));
deployPlugin(new File(libDir, "rsuite-lmd-editor-plugin.jar"));

//SERVER SPECIFIC scripts
println "[INFO] ======== Running:  " + "ConfigureLocalUsers.groovy";
evaluate(new File("iet/ConfigureLocalUsers.groovy"));

  rsuite.logout();

def deployPlugin(file) {
	println " + [INFO] Deploying ${file.getName()}...";
	rsuite.deployPlugin(file);
}
