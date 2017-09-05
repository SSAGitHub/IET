package com.rsicms.rsuite.oxygen.iet.applet.extension.framework.dita.operation;

public class SaveAndGeneratePDFWithChangeTrackingOperation extends SaveAndGeneratePDFOperation {

	@Override
	public String getDescription() {
		return "Save document and generate PDF with change tracking";
	}

	@Override
	protected boolean generateChangeTracking() {
		return true;
	}
	

}
