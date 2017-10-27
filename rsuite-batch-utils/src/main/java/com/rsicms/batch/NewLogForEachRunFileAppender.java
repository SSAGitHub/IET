package com.rsicms.batch;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorCode;


public class NewLogForEachRunFileAppender extends FileAppender {

	public NewLogForEachRunFileAppender() {
	}
	
	public NewLogForEachRunFileAppender(Layout layout, String filename,
			boolean append, boolean bufferedIO, int bufferSize)
			throws IOException {
		super(layout, filename, append, bufferedIO, bufferSize);
	}
	
	public NewLogForEachRunFileAppender(Layout layout, String filename,
			boolean append) throws IOException {
		super(layout, filename, append);
	}
	
	public NewLogForEachRunFileAppender(Layout layout, String filename)
			throws IOException {
		super(layout, filename);
	}
	
	public void activateOptions() {
		if (fileName != null) {
			try {
				fileName = getNewLogFileName();
				setFile(fileName, fileAppend, bufferedIO, bufferSize);
			} 
			catch (Exception e) {
				errorHandler.error("Error while activating log options", e,ErrorCode.FILE_OPEN_FAILURE);
			}
		}
	}
	
	private String getNewLogFileName() {
	
		File logFile = new File(fileName);
		String fileName = logFile.getName();
	
		int dotIndex = fileName.indexOf(".");
	
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-kkmmss");
		String newFileName = fileName.substring(0, dotIndex) + "-" + dateFormat.format(new Date()) + "." + "log";
	
		return logFile.getParent() +  File.separator +  newFileName;
	}
}