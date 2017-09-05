package com.rsicms.projectshelper.publish.workflow.actionhandlers;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.publish.workflow.configuration.*;

final class ConfigurationFactory {
    
    private ConfigurationFactory(){}

    private static DefaultContentExporterConfiguration defeaultExporterConfiguration = new DefaultContentExporterConfiguration();
    
    public static ContentExporterConfiguration createContentExporterConfiguration(
            String exporterConfigurationClass) throws RSuiteException {

        if (StringUtils.isNotBlank(exporterConfigurationClass)) {
            return (ContentExporterConfiguration)createInstance(exporterConfigurationClass);
        }

        return defeaultExporterConfiguration;
    }
    
    public static OutputGeneratorConfiguration createOutputGeneratorConfiguration(
            String exporterConfigurationClass) throws RSuiteException {

        if (StringUtils.isNotBlank(exporterConfigurationClass)) {
            return (OutputGeneratorConfiguration)createInstance(exporterConfigurationClass);
        }

        throw new RSuiteException("Output generation configuration is required");
    }
    
    public static GeneratedOutputUploaderConfiguration createGeneratedOutputUploaderConfiguration(
            String exporterConfigurationClass) throws RSuiteException {

        if (StringUtils.isNotBlank(exporterConfigurationClass)) {
            return (GeneratedOutputUploaderConfiguration)createInstance(exporterConfigurationClass);
        }

        throw new RSuiteException("Generated output uploader configuration is required");
    }
    
    public static SendNotificationConfiguration createSendNotificationConfiguration(
            String notifierClass) throws RSuiteException {

        if (StringUtils.isNotBlank(notifierClass)) {
            return (SendNotificationConfiguration)createInstance(notifierClass);
        }

        return null;
    }



    private static Object createInstance(String exporterConfigurationClass) throws RSuiteException {
       
            try {
                ClassLoader classLoader = ExportContentActionHandler.class.getClassLoader();
                Class<?> loadClass = classLoader.loadClass(exporterConfigurationClass);
                return loadClass.newInstance();
            } catch (SecurityException e) {
                throw new RSuiteException(0, createInstanceErrorMessage(exporterConfigurationClass), e);
            } catch (ClassNotFoundException e) {
                throw new RSuiteException(0, createInstanceErrorMessage(exporterConfigurationClass), e);
            } catch (InstantiationException e) {
                throw new RSuiteException(0, createInstanceErrorMessage(exporterConfigurationClass), e);
            } catch (IllegalAccessException e) {
                throw new RSuiteException(0, createInstanceErrorMessage(exporterConfigurationClass), e);
            }
        }
    
    private static String createInstanceErrorMessage(String exporterConfigurationClass) {
        return "Unable to create object for '" + exporterConfigurationClass + "'";
    }
}
