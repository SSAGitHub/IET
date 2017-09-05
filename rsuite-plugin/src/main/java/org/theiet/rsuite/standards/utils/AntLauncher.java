package org.theiet.rsuite.standards.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.system.*;

final public class AntLauncher {

	private AntLauncher() {
	}
	
	public static final String MAX_JAVA_MEMORY_PARAM = "maxJavaMemory";
	
	public static CommandExecutionResult executeAntBuild(Log log, String antHome,
			List<String> classPathList, File baseFolder, File buildFile, Properties props) throws RSuiteException {

		File antDir = new File(antHome);
		File antLibDir = new File(antDir, "lib");

		// Build up ant command in an array to avoid any shell interpretation of
		// arguments
		List<String> execCmd = new ArrayList<String>();

		String javaHome = System.getProperty("java.home");
		log.info("java.home=" + javaHome);
		File javaHomeDir = new File(javaHome);
		String javaCmd = new File(new File(javaHomeDir, "bin"), "java")
				.getAbsolutePath();

		String classPath = new File(antLibDir, "ant-launcher.jar")
				.getAbsolutePath();

		log.info("classPath: " + classPath);

		execCmd.add(javaCmd);
		execCmd.add("-Dant.home=" + antHome);

		// If the maxJavaMemory parameter has been specified, use it for
		// the JVM that runs the Ant task as well.
		String maxMemory = props.getProperty(MAX_JAVA_MEMORY_PARAM);
		if (maxMemory != null) {
			execCmd.add("-Xmx" + maxMemory);
		}

		execCmd.add("-jar");
		execCmd.add(classPath);
		if (buildFile != null && buildFile.exists()){
			execCmd.add("-buildfile");
			execCmd.add(buildFile.getAbsolutePath());
		}
		
		
		log.info("Ant properties:");
		log.info("");
		for (Object key : props.keySet()) {
			String propName = (String) key;
			String value = props.getProperty(propName);
			log.info("\n" + propName + "=" + value);
			execCmd.add("-D" + propName + "=" + value);
		}

		// Add any additional jars to the command:
		// These come first so they can override the base
		// jars.
		for (String jarItem : classPathList) {
			File file = new File(jarItem);
			if (!file.isAbsolute()) {
				file = new File(antLibDir, jarItem);
			}
			execCmd.add("-lib");
			execCmd.add(file.getAbsolutePath());

		}
		

		log.info("Ant exec command:" + execCmd);
		log.info("Running Ant process...");
		return SystemUtils.executeOScommand(log, execCmd, baseFolder);
	}

}
