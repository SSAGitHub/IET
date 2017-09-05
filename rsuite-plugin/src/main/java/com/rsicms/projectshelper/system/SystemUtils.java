package com.rsicms.projectshelper.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.RSuiteException;

public final class SystemUtils {

	private SystemUtils() {
	}
	
	public static CommandExecutionResult executeOScommand(Log logger,
			List<String> commandList, File baseFolder) throws RSuiteException {

		return executeOScommand(logger, commandList, baseFolder, true);
	}

	public static CommandExecutionResult executeOScommand(
			List<String> commandList, File baseFolder) throws RSuiteException {

		return executeOScommand(null, commandList, baseFolder, true);
	}
	
	public static CommandExecutionResult executeOScommand(
			List<String> commandList) throws RSuiteException {

		return executeOScommand(null, commandList, null, true);
	}

	private static CommandExecutionResult executeOScommand(Log logger,
			List<String> commandList, File baseFolder,
			boolean redirectErrorStream) throws RSuiteException {
		try {
			ProcessBuilder pb = new ProcessBuilder(commandList);
			if (baseFolder != null){
				pb.directory(baseFolder);
			}
			

			pb.redirectErrorStream(redirectErrorStream);

			Process proc = pb.start();

			StringBuffer resultOutput = new StringBuffer();

			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(logger,
					proc.getErrorStream(), OutputType.ERROR, true, resultOutput);

			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(logger,
					proc.getInputStream(), OutputType.OUTPUT, true,
					resultOutput);

			outputGobbler.start();
			errorGobbler.start();

			// any error???
			int exitVal = proc.waitFor();

			// kick them off

			return new CommandExecutionResult(resultOutput,
					outputGobbler.getOutput(), errorGobbler.getOutput(),
					exitVal);

		} catch (Exception t) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
					"Unable to execute command " + commandList, t);
		}
	}

}

enum OutputType {
	OUTPUT, ERROR;
}

class StreamGobbler extends Thread {
	private InputStream is;

	private OutputType type;

	private StringBuffer output;

	private Log logger;

	private boolean logOutput;

	private StringBuilder localResult = new StringBuilder();

	StreamGobbler(Log logger, InputStream is, OutputType type,
			boolean logOutput, StringBuffer output) {
		this.is = is;
		this.type = type;
		this.logger = logger;
		this.logOutput = logOutput;
		this.output = output;
	}

	public void run() {

		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		try {

			String line = null;
			while ((line = br.readLine()) != null) {
				if (logOutput && logger != null) {
					if (type == OutputType.OUTPUT) {
						logger.info(line);
					} else {
						logger.error(line);
					}
				}

				if (logOutput) {
					output.append(line).append("\n");
				} else {
					localResult.append(line).append("\n");
				}
			}
		} catch (IOException ioe) {
			logger.error(ioe.getMessage(), ioe);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public StringBuilder getOutput() {
		return localResult;
	}

}
