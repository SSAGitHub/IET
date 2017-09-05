package org.theiet.rsuite.advisors.display;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.advisors.display.IetContentDisplayAdvisor.DisplayCaCondition;
import org.theiet.rsuite.datamodel.BrowserTreeStatusIcon;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.standards.StandardsBooksConstans;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;

public class ContentDisplayCaLabelAdjuster implements StandardsBooksConstans {

	private ManagedObject mo;

	private StringBuffer label;

	private StringBuffer title;

	private StringBuffer tempTitle = new StringBuffer();
	
	private boolean adjustedLabel;

	public ContentDisplayCaLabelAdjuster(ManagedObject mo, StringBuffer label, StringBuffer title) {
		this.mo = mo;
		this.label = label;
		this.title = title;
	}

	public void adjustLabel(String lmdName, String expectedLmdValue,
			DisplayCaCondition conditon, String labelText)
			throws RSuiteException {

		boolean adjustLabel = ifLabelNeedToBeAdjusted(lmdName,
				expectedLmdValue, conditon);

		if (adjustLabel) {
			label.append(labelText);
		}
		
	}

	public void addLabelIcon(String lmdName, String expectedLmdValue,
			DisplayCaCondition conditon, BrowserTreeStatusIcon icon)
			throws RSuiteException {

		boolean adjustLabel = ifLabelNeedToBeAdjusted(lmdName,
				expectedLmdValue, conditon);

		if (adjustLabel) {
			addDisplayIcon(icon);
		}

	}

	private void addDisplayIcon(BrowserTreeStatusIcon icon) {
		String imageURL = constructImageLink(
				IetConstants.RSUITE_SESSION_KEY, icon);
		label.append(" " + imageURL);
	}
	
	public void addLabelIcon(boolean conditon, BrowserTreeStatusIcon icon)
			throws RSuiteException {
		
		if (conditon){
			addDisplayIcon(icon);
			adjustedLabel = true;
		}
	}

	public void addLabelTitle(String lmdName, String lmdFieldName, String expectedLmdValue,
			DisplayCaCondition conditon, String title) 
					throws RSuiteException {
			
		boolean adjustLabel = ifLabelNeedToBeAdjusted(lmdName,
				expectedLmdValue, conditon);

		if (adjustLabel) {			
			if (tempTitle.toString().length() > 0) {
				tempTitle.append("&#10;");
			}

			tempTitle.append(lmdFieldName + ": " + title);
		}

	}

	private boolean ifLabelNeedToBeAdjusted(String lmdName,
			String expectedLmdValue, DisplayCaCondition conditon)
			throws RSuiteException {
		String lmdValue = mo.getLayeredMetadataValue(lmdName);

		boolean adjustLabel = false;

		switch (conditon) {
		case EQUALS:
			if (expectedLmdValue == lmdValue || (expectedLmdValue != null &&  expectedLmdValue.equals(lmdValue))) {
				adjustLabel = true;
			}
			break;

		case NOT_EQUALS:

			if (!StringUtils.isBlank(lmdValue)
					&& !lmdValue.equals(expectedLmdValue)) {
				adjustLabel = true;
			}

			break;

		case NOT_BLANK:
			if (!StringUtils.isBlank(lmdValue)) {
				adjustLabel = true;
			}

		}

		adjustedLabel = (adjustedLabel || adjustLabel);

		return adjustLabel;
	}

	public boolean isAdjustedLabel() {
		return adjustedLabel;
	}

	private String constructImageLink(String skey, BrowserTreeStatusIcon icon) {
		return constructImageLink(skey, icon.getIconFileName(),
				icon.getStatusTitle(), icon.getLink());
	}

	private String constructImageLink(String skey, String imageFilename,
			String title, String link) {
		StringBuffer sb = new StringBuffer();
		sb.append("<img src=\"")
				.append(JournalConstants.REST_V1_URL_ROOT)
				.append("/static/iet/images/")
				.append(imageFilename)
				.append("?")
				.append("skey=")
				.append(skey)
				.append("\"")
				.append(" height=\"16\" width=\"16\" style=\"margin-bottom:3px; height:16px; width:16px;\"")
				.append(StringUtils.isBlank(link) ? "": " onclick=\"openLinkInNewWindow('" + link + "', '" + title + "')\"")
				.append(" title=\"").append(title).append("\" ").append("/>");
		
		return sb.toString();
	}

	public String getNewLabel () {
		String htmlBrowseTreeNode = "<div title=\"TITLE\">LABEL</div>";
		title.append("&#10;" + tempTitle.toString());
		htmlBrowseTreeNode = htmlBrowseTreeNode.replace("TITLE", title.toString()).replace("LABEL", label.toString());

		return htmlBrowseTreeNode;
	}
}
