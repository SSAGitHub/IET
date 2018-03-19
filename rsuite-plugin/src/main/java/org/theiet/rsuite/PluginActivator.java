package org.theiet.rsuite;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.domain.classloader.CustomLibraryClassLoader;
import org.theiet.rsuite.journals.domain.article.manuscript.acceptance.JournalCustomLibraryFactory;
import org.theiet.rsuite.onix.onix2lmd.Onix2LmdMapping;
import org.theiet.rsuite.standards.domain.publish.mathml.mathmlToImage.MathMlToImageConventerFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.extensions.Plugin;
import com.reallysi.rsuite.api.extensions.PluginLifecycleListener;
import com.rsicms.projectshelper.utils.ProjectPluginProperties;

public class PluginActivator implements PluginLifecycleListener {

	private static Log log = LogFactory.getLog(PluginActivator.class);
	
	@Override
	public void start(ExecutionContext context, Plugin plugin) {
		
		try {
			ProjectPluginProperties.reloadProperties();
			Onix2LmdMapping.reload();
			reloadCustomFactories(context, plugin);
		} catch (RSuiteException e) {
			log.error("Error during plugin start " + e.getMessage(), e);	
		}
	}

	private void reloadCustomFactories(ExecutionContext context, Plugin plugin) throws RSuiteException {
		File rsuiteHomeFolder = context.getRSuiteServerConfiguration().getHomeDir(); 
		
		CustomLibraryClassLoader customLibraryClassLoader = new CustomLibraryClassLoader(plugin.getClassLoader(), rsuiteHomeFolder);
		
		JournalCustomLibraryFactory.reloadFactory(customLibraryClassLoader);
		MathMlToImageConventerFactory.reloadFactory(customLibraryClassLoader);
	}

	@Override
	public void stop(ExecutionContext arg0, Plugin arg1) {
	}

}
