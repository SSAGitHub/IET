package com.rsicms.projectshelper.system;

public class CommandExecutionResult {

	private StringBuilder output;
	
	private StringBuilder error;

	private int resultCode;
	
	private StringBuffer result;

	public CommandExecutionResult(StringBuffer result, StringBuilder output, StringBuilder error,
			int resultCode) {
		super();
		this.output = output;
		this.error = error;
		this.resultCode = resultCode;
		this.result = result;
	}

	public StringBuilder getOutput() {
		return output;
	}

	public StringBuilder getError() {
		return error;
	}

	public int getResultCode() {
		return resultCode;
	}

	public StringBuffer getResult() {
		return result;
	}
	

}
