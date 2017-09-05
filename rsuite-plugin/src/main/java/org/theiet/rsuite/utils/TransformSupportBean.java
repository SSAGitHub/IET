/**
* Copyright (c) 2009 Really Strategies, Inc.
*/
package org.theiet.rsuite.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;

import net.sf.saxon.Controller;
import net.sf.saxon.s9api.Destination;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XsltTransformer;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.report.StoredReport;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.api.xml.LoggingSaxonMessageListener;
import com.reallysi.tools.dita.conversion.TransformationOptions;
import com.rsicms.rsuite.helpers.messages.impl.GenericProcessFailureMessage;

/**
 * 
 */
public class TransformSupportBean {

	// FIXME: Rationalize the various TransformSupportBeans that are floating about.

   protected URI xsltUri;
   protected ExecutionContext context;
   private static Log log = LogFactory.getLog(TransformSupportBean.class);
   
   private URIResolver uriResolver;

   /**
    * @param context
    * @throws RSuiteException 
    * 
    */
   public TransformSupportBean(ExecutionContext context, String xsltUrlString) throws RSuiteException {
       this.context = context;
       try {
           xsltUri = new URI(xsltUrlString);
       } catch (URISyntaxException e) {
           String msg = "Failed to construct URI from URI string\"" + xsltUrlString + "\": " + e.getMessage();         
           logAndThrowRSuiteException(log, e, msg);
       }
       
   }

   public TransformSupportBean(
		   ExecutionContext context, 
		   URI xsltUri) {
	   this.xsltUri = xsltUri;
	   this.context = context;
   }

public TransformSupportBean(WorkflowExecutionContext context) {
	this.context = context;
}

public static void logAndThrowRSuiteException(Log log, Exception e,
           String msg) throws RSuiteException {
       log.error(msg);
       throw new RSuiteException(0, msg, e);
   }

   /**
    * @param xsltUrl 
    * @param doc
    * @param resultFile
    * @param params
    * @param logger
    * @param log
    * @param tempDir
    */
   public void applyTransform(ManagedObject mo, File resultFile, Map<String, String> params,
           TransformationOptions options, Log log, File tempDir) throws RSuiteException {
       Element element = mo.getElement();
       if (element == null) {
           throw new RSuiteException(0, "Managed object " + mo.getDisplayName() + "[" + mo.getId() + "] returned null for getElement() method.");
       }
       Document doc = element.getOwnerDocument();
       Source source = new DOMSource(doc);
       Serializer result = new Serializer();
       resultFile.getParentFile().mkdirs();
       result.setOutputFile(resultFile);
       applyTransform(mo.getDisplayName(), source, result, params, options, log, tempDir);
   }

   /**
    * @param xsltUrl 
    * @param doc
    * @param resultFile
    * @param params
    * @param logger
    * @param log
    * @param tempDir
    */
   public void applyTransform(
		   String basefilename, 
		   Document doc, 
		   File resultFile, 
		   Map<String, String> params,
		   TransformationOptions options, 
		   Log log, 
		   File tempDir) throws RSuiteException {

       Source source = new DOMSource(doc);
       try {
           source.setSystemId(resultFile.toURI().toURL().toExternalForm());
       } catch (MalformedURLException e) {
           throw new RSuiteException("MalformedURLException setting system ID for result file " + resultFile.getAbsolutePath());
       }
       Serializer result = new Serializer();
       result.setOutputFile(resultFile);
       applyTransform(
    		   basefilename, 
    		   source, 
    		   result, 
    		   params, 
    		   options, 
    		   log, 
    		   tempDir);
   }

   public void applyTransform(String basefilename, File inputXmlFile, File resultFile, Map<String, String> params,
          TransformationOptions options, Log log, File tempDir)  throws RSuiteException {
       SAXSource saxSource = null;
       try {
           InputSource inSource = new InputSource(new FileInputStream(inputXmlFile));
           // FIXME: Set validation to true to force a failure for testing the logging:
           XMLReader reader = context.getXmlApiManager().getLoggingXMLReader(log, true);
           
            saxSource = new SAXSource(reader, inSource);
       } catch (FileNotFoundException e) {
           String msg = "File not found exception: " + e.getMessage();         
           logAndThrowRSuiteException(log, e, msg);
       }
       
       try {
        saxSource.setSystemId(inputXmlFile.toURI().toURL().toExternalForm());
       }
       catch (MalformedURLException e) {
           String msg = "Malformed URL for " + inputXmlFile.getAbsolutePath() + ": " + e.getMessage();         
           logAndThrowRSuiteException(log, e, msg); 
       }

       Serializer result = new Serializer();
       result.setOutputFile(resultFile);
       
       applyTransform(basefilename, saxSource, result, params, options, log, tempDir);
       
   }


   public void applyTransform(
		   String displayName, 
		   Source xmlSource, 
		   Destination result, 
		   Map<String, String> params,
           TransformationOptions options, 
           Log log, 
           File tempDir) throws RSuiteException {

	   
       boolean storeLog = true;
       LoggingSaxonMessageListener logger = options.getSaxonLogger();
       if (logger == null) {
    	   logger = context.getXmlApiManager().newLoggingSaxonMessageListener(log);
       } else {
    	   storeLog = false; // Assume caller, which provided the logger, will store
    	   // any reports.
       }
       
       XsltTransformer trans = context.getXmlApiManager().getSaxonXsltTransformer(xsltUri, logger);
       setupCustomUriResolver(trans);
       for (String name : params.keySet()) {
           String value = params.get(name);
           log.info("Setting XSLT parameter \"" + name + "\" to \"" + value + "\"");
           trans.setParameter(new QName(name), new XdmAtomicValue(value));
       }       
       for (String name : options.getXsltParamNames()) {
           String value = options.getXsltParameter(name);
           log.info("Setting XSLT parameter \"" + name + "\" to \"" + value + "\"");
           trans.setParameter(new QName(name), new XdmAtomicValue(value));
       }
   
       try {
           trans.setSource(xmlSource);
       } catch (SaxonApiException e) {
           String msg = "Exception setting transform source: " + e.getMessage();           
           logAndThrowRSuiteException(log, e, msg);
       }
       trans.setDestination(result);
       boolean exceptionOccurred = false;
       log.info("Applying XSLT transform \"" + xsltUri.toString() + "\" to input source " + xmlSource.getSystemId());
       try {
           trans.transform();
       } catch (SaxonApiException e) {
           String msg = "Exception from XSLT transform: (" + displayName + ") " + e.getMessage();
           options.addFailureMessage(
        		   new GenericProcessFailureMessage(
        				   "XSLT Transform", 
        				   displayName, 
        				   msg));
           exceptionOccurred = true;
       } finally {
    	   
    	   
    	   
           StoredReport report = null;
    	   if (storeLog) {
        	   String reportFileName = 
        			   displayName + 
        			   "_transform_at_" + 
        					   new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + 
        					    RandomStringUtils.randomAlphabetic(6) + ".txt";
               report = context.getReportManager().saveReport(reportFileName, 
            		   logger.getLogString());
    	   }
           if (exceptionOccurred) {
               log.info("Exceptions occurred.");
        	   options.addFailureMessage(
        			   new GenericProcessFailureMessage(
        					   "Transform", 
        					   displayName, 
        					   "Exceptions performing transform. See stored report for details.",
        					   report)
        			   );   
           } else {
        	   // Not useful to log an info message here.
           }
       }
       
//             try {
//                 FileUtils.deleteDirectory(tempDir);
//             } catch (IOException e) {
//                 log.warn("Failed to delete temporary directory: " + e.getMessage());
//             }
   }

   public void applyTransform(
                              URI xslt,
                              File inputXmlFile,
                              File resultFile,
                              Map<String, String> params,
                              LoggingSaxonMessageListener logger,
                              Log log,
                              File tempDir
                      ) throws RSuiteException {
                          SAXSource saxSource = null;
                          FileInputStream in = null;
                          try {
                              try {
                                  InputSource inSource = new InputSource(
                                          new FileInputStream(inputXmlFile));
                                  // FIXME: Set validation to true to force a failure for
                                  // testing the logging:
                                  XMLReader reader = context.getXmlApiManager().
                                      getLoggingXMLReader(log, true);
                                  saxSource = new SAXSource(reader, inSource);

                              } catch (FileNotFoundException e) {
                                  String msg = "File not found exception: " + e.getMessage();         
                                  logAndThrowRSuiteException(log, e, msg);
                              }

                              Serializer result = new Serializer();
                              result.setOutputFile(resultFile);

                              applyTransform(inputXmlFile.getAbsolutePath(), xslt, saxSource, result, params,
                                      logger, log, tempDir);
                          } finally {
                              if (in != null) {
                                  try {
                                      in.close();
                                  } catch (Exception e) {
                                  }
                              }
                          }
                      }

                      public void applyTransform(
                              String displayName,
                              URI xslt,
                              Source xmlSource,
                              Destination result,
                              Map<String, String> params,
                              LoggingSaxonMessageListener logger,
                              Log log,
                              File tempDir
                      ) throws RSuiteException {
                          XsltTransformer trans = context.getXmlApiManager().getSaxonXsltTransformer(xslt, logger);
                          
                          for (String name : params.keySet()) {
                              String value = params.get(name);
                              log.info("Setting XSLT parameter \"" + name + "\" to \"" + value + "\"");
                              trans.setParameter(new QName(name), new XdmAtomicValue(value));
                          }
                      
                          try {
                              trans.setSource(xmlSource);
                          } catch (SaxonApiException e) {
                              String msg = "Exception setting transform source: " + e.getMessage();           
                              logAndThrowRSuiteException(log, e, msg);
                          }
                          trans.setDestination(result);
                      
                          log.info("Applying XSLT transform \"" + xslt.toString() + "\" to input source " + xmlSource.getSystemId());
                          try {
                              trans.transform();
                          } catch (SaxonApiException e) {
                              String msg = "Exception from XSLT transform: (" + displayName + ") "+ e.getMessage();
                              logAndThrowRSuiteException(log, e, msg);
                          }
                          log.info("Transformation complete.");
                          
//                                try {
//                                    FileUtils.deleteDirectory(tempDir);
//                                } catch (IOException e) {
//                                    log.warn("Failed to delete temporary directory: " + e.getMessage());
//                                }
                      }

   public void applyTransform(Source xmlSource, Destination result,
							Map<String, String> params, LoggingSaxonMessageListener logger, Log wfLog, File tempDir)
							throws RSuiteException {
											
								    XsltTransformer trans = context.getXmlApiManager().getSaxonXsltTransformer(xsltUri, logger);
								    setupCustomUriResolver(trans);
								    
								    for (String name : params.keySet()) {
								    	String value = params.get(name);
								    	wfLog.info("Setting XSLT parameter \"" + name + "\" to \"" + value + "\"");
								        trans.setParameter(new QName(name), new XdmAtomicValue(value));
								    }
								
								    try {
										trans.setSource(xmlSource);
									} catch (SaxonApiException e) {
										String msg = "Exception setting transform source: " + e.getMessage();			
										logAndThrowRSuiteException(wfLog, e, msg);
									}
								    trans.setDestination(result);
								
								    wfLog.info("Applying XSLT transform \"" + xsltUri.toString() + "\" to input source " + xmlSource.getSystemId());
								    try {
										trans.transform();
									} catch (SaxonApiException e) {
										String msg = "Exception from XSLT transform: " + e.getMessage();
										logAndThrowRSuiteException(wfLog, e, msg);
									}
									wfLog.info("Transformation complete.");
									
							//				try {
							//					FileUtils.deleteDirectory(tempDir);
							//				} catch (IOException e) {
							//					wfLog.warn("Failed to delete temporary directory: " + e.getMessage());
							//				}
								}

public void applyTransform(File inputXmlFile, File resultFile, Map<String, String> params,
		LoggingSaxonMessageListener logger, Log log, File tempDir) throws RSuiteException {
		    SAXSource saxSource = null;
		    try {
				InputSource inSource = new InputSource(new FileInputStream(inputXmlFile));
				// FIXME: Set validation to true to force a failure for testing the logging:
				XMLReader reader = context.getXmlApiManager().getLoggingXMLReader(log, true);
				
				 saxSource = new SAXSource(reader, inSource);
			} catch (FileNotFoundException e) {
				String msg = "File not found exception: " + e.getMessage();			
				logAndThrowRSuiteException(log, e, msg);
			}
		    try {
				saxSource.setSystemId(inputXmlFile.toURI().toURL().toExternalForm());
			} catch (MalformedURLException e) {
				throw new RSuiteException(0, "Unexpected MalformedURLException: " + e.getMessage(), e);
			}
		
		    Serializer result = new Serializer();
			result.setOutputFile(resultFile);
			
			applyTransform(saxSource, result, params, logger, log, tempDir);
			
		}

/**
 * @param xsltUrl 
 * @param doc
 * @param resultFile
 * @param params
 * @param logger
 * @param wfLog
 * @param tempDir
 */
public void applyTransform(Document doc, File resultFile, Map<String, String> params,
		LoggingSaxonMessageListener logger, Log wfLog, File tempDir) throws RSuiteException {
			Source source = new DOMSource(doc);
			Serializer result = new Serializer();
			result.setOutputFile(resultFile);
			applyTransform(source, result, params, logger, wfLog, tempDir);
		}

/**
 * @param xsltUrl 
 * @param doc
 * @param resultFile
 * @param params
 * @param logger
 * @param wfLog
 * @param tempDir
 */
public void applyTransform(ManagedObject mo, File resultFile, Map<String, String> params,
		LoggingSaxonMessageListener logger, Log wfLog, File tempDir) throws RSuiteException {
			Element element = mo.getElement();
			if (element == null) {
				throw new RSuiteException(0, "Managed object " + mo.getDisplayName() + "[" + mo.getId() + "] returned null for getElement() method.");
			}
			Document doc = element.getOwnerDocument();
			String docUri = doc.getDocumentURI();
			if (docUri == null || "".equals(docUri.trim())) {
				// FIXME: The need for this is removed in 3.3.2, where the document URI is set by the MO itself.
			    doc.setDocumentURI("rsuite:/res/content/" + mo.getId());
			    docUri = doc.getDocumentURI();
			}
			Source source = new DOMSource(doc);
			source.setSystemId(docUri);
			Serializer result = new Serializer();
			result.setOutputFile(resultFile);
			applyTransform(source, result, params, logger, wfLog, tempDir);
		}

/**
    * Gets a temporary directory.
    * @param deleteOnExit If set to true, directory will be deleted on exit.
    * @return
    * @throws Exception 
    */
   public static File getTempDir(String prefix, boolean deleteOnExit) throws Exception {
       File tempFile = File.createTempFile(prefix, "trash");
       File tempDir = new File(tempFile.getAbsolutePath() + "_dir");
       tempDir.mkdirs();
       tempFile.delete();
       if (deleteOnExit) tempDir.deleteOnExit();   
       return tempDir;
   }
   
   private void setupCustomUriResolver(XsltTransformer transformer) throws RSuiteException{
	   setCustomUriResolverForTransformer(transformer, uriResolver);
   }
   
   public static void setCustomUriResolverForTransformer(XsltTransformer transformer, URIResolver uriResolver) throws RSuiteException{
	   
	   if (uriResolver != null){
		   try{
			   
			   Field field = transformer.getClass().getDeclaredField("controller");
			   field.setAccessible(true);
			   Controller configuration = (Controller) field.get(transformer);
			   configuration.setURIResolver(uriResolver);
			   
		   }catch (Exception e){
			   logAndThrowRSuiteException(log, e, "Unable to set up custom resolver");
		   }
	   } 
   }

	public void setUriResolver(URIResolver uriResolver) {
		this.uriResolver = uriResolver;
	}
   
   

	public static URIResolver createCustomUriResolver(
			ExecutionContext context, final Log log) throws RSuiteException {
		final URIResolver resolver = context.getXmlApiManager().getRSuiteAwareURIResolver();
		   
		   final XMLReader reader = context.getXmlApiManager().getLoggingXMLReader(log, true);
		   
		   URIResolver myResolver = new URIResolver() {
				
				@Override
				public Source resolve(String href, String base) throws TransformerException {
					Source source = resolver.resolve(href, base);
					if (source instanceof SAXSource){
						SAXSource saxSource = (SAXSource)source;
						saxSource.setXMLReader(reader);
					}

					return source;
				}
		   };
		return myResolver;
	}
       

}