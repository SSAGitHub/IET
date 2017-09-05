package org.theiet.rsuite.onix.onix2lmd;
import java.util.ArrayList;
import java.util.List;


public class Onix2LmdMappingItem {

	private String lmdName;
	
	private boolean isMultivalue;
	
	private List<String> xpaths = new ArrayList<String>();

	public Onix2LmdMappingItem(String lmdName,
			List<String> xpaths) {
		
	}
	
	public Onix2LmdMappingItem(String lmdName, boolean isMultivalue,
			List<String> xpaths) {
		
		this.lmdName = lmdName;
		this.xpaths = xpaths;
		this.isMultivalue = isMultivalue;
	}

	public String getLmdName() {
		return lmdName;
	}

	public boolean isMultivalue() {
		return isMultivalue;
	}

	public List<String> getXpaths() {
		return xpaths;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("lmd: ");
		sb.append(lmdName).append("\n");
		
		sb.append("multivalue: ").append(isMultivalue).append("\n");
		sb.append("xpaths: ").append(xpaths).append("\n");
		
		return sb.toString();
	}
}
