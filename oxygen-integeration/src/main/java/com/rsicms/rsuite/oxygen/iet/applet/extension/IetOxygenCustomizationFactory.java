package com.rsicms.rsuite.oxygen.iet.applet.extension;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

import ro.sync.azcheck.ui.SpellCheckOptions;
import ro.sync.ecss.extensions.api.component.AuthorComponentFactory;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ISchemaAwareCustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.*;
import com.rsicms.rsuite.editors.oxygen.applet.components.IOxygenComponentBuilder;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenAppletStartupParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;

public class IetOxygenCustomizationFactory extends RsuiteCustomizationFactory {

	public IetOxygenCustomizationFactory() throws OxygenIntegrationException {
		super();
	}

	@Override
	public void initialize(OxygenAppletStartupParmaters parameters,
			OxygenOpenDocumentParmaters documentParamters)
			throws OxygenIntegrationException {
		super.initialize(parameters, documentParamters);
	}

	@Override
	public IOxygenComponentBuilder getComponentBuilder(
			AuthorComponentFactory factory, OxygenMainComponent mainComponent) {

		SpellCheckOptions spellCheckOptions = factory.getSpellCheckOptions();
		spellCheckOptions.language = "en_GB";
		factory.setSpellCheckOptions(spellCheckOptions);
		
		return super.getComponentBuilder(factory, mainComponent);
	}

	@Override
	public List<ISchemaAwareCustomizationFactory> getSchamaCustomizationFactories(
			OxygenOpenDocumentParmaters parameters) {
		List<ISchemaAwareCustomizationFactory> factories =new ArrayList<ISchemaAwareCustomizationFactory>();
		factories.add(new IetDitaCustomizationFactory());
		return factories;
	}

	@Override
	public URLStreamHandlerFactory getURLHandlerFactory() {
	    
	   return new URLStreamHandlerFactory() {
            public URLStreamHandler createURLStreamHandler(String protocol) {

                if ("rsuiteimg".equals(protocol)) {
                    System.out.println("IET STREAM URL");
                    return new IetStreamImageURLHandler((RSuiteURI)getCmsURI());
                }

                return null;
            }
        };

	}
	
}
