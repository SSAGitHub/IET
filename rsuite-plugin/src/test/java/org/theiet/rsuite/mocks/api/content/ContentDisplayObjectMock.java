package org.theiet.rsuite.mocks.api.content;

import java.util.Map;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.content.ContentDisplayObject;

public class ContentDisplayObjectMock implements ContentDisplayObject {

	private String label;
	
	@Override
	public void addCustomValue(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAncillaryLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCustomValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getCustomValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject getManagedObject() throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNamespaceURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQname() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRevision() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCheckout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAncillaryLabel(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLabel(String arg0) {
		this.label = arg0;

	}


}
