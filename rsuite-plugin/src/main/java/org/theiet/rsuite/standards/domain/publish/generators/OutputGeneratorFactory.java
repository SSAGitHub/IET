package org.theiet.rsuite.standards.domain.publish.generators;

import static org.theiet.rsuite.standards.domain.publish.generators.OutputGeneratorType.*;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.publish.generators.OutputGenerator;

public final class OutputGeneratorFactory{

    private OutputGeneratorFactory(){};
    
	public static OutputGenerator getOutputGenerator(OutputGeneratorType generatorId) throws RSuiteException{
		
		if (DITA_2_PDF == generatorId){
			return new Dita2PdfGenerator();
		}else if (XML_2_ICML == generatorId){
			return new Xml2IcmlGenerator();
		}
		
		throw new RSuiteException(generatorId + " is unsupported generator");
	}

}
