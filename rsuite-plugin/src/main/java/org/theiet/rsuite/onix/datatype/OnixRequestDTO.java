package org.theiet.rsuite.onix.datatype;

import java.io.File;

public class OnixRequestDTO {

	private String targetFolderProperty;
	private String targetFileName;
	private String onixExternalUser;
	private File file;

	public void setTargetFolderProperty(String targetFolderProperty) {
		this.targetFolderProperty = targetFolderProperty;
	}

	public void setTargetFileName(String targetFileName) {
		this.targetFileName = targetFileName;
	}

	public void setOnixExternalUser(String onixExternalUser) {
		this.onixExternalUser = onixExternalUser;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getTargetFolderProperty() {
		return targetFolderProperty;
	}

	public String getTargetFileName() {
		return targetFileName;
	}

	public String getOnixExternalUser() {
		return onixExternalUser;
	}

	public File getFile() {
		return file;
	}
	
}
