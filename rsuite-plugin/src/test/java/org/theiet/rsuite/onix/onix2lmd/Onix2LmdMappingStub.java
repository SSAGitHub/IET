package org.theiet.rsuite.onix.onix2lmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.reallysi.rsuite.api.RSuiteException;

public class Onix2LmdMappingStub extends Onix2LmdMapping {

	public Onix2LmdMappingStub() throws RSuiteException {
		super();
	}

	@Override
	protected InputStream getOnix2LmdConfiguration() {
	
		InputStream is = null;
		try {
			is = new FileInputStream(new File("src/main/resources/WebContent/conf/onix/onix2lmd_config.xml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return is;
	}
}
