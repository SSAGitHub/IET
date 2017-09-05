package org.theiet.rsuite.datamodel;

public class BrowserTreeStatusIcon {

	private String iconFileName;

	private String statusTitle;

	private String link;

	public BrowserTreeStatusIcon(String iconFileName){
		this(iconFileName, "", "");
	}
	
	public BrowserTreeStatusIcon(String iconFileName, String statusTitle){
		this(iconFileName, statusTitle, "");
	}
	
	public BrowserTreeStatusIcon(String iconFileName, String statusTitle, String link) {
		super();
		this.iconFileName = iconFileName;
		this.statusTitle = statusTitle;
		this.link = link;
	}

	public String getIconFileName() {
		return iconFileName;
	}

	public String getStatusTitle() {
		return statusTitle;
	}

	public String getLink () {
		return link;
	}

}
