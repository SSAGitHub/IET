package com.rsicms.batch;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class RetroWorkFlowCleanUp {
	
	private static Logger logger;
	private String workflowDataDir;
	private boolean simulationMode;
	private String mySQLconnectionString;
	private static String FILE_TO_BE_REMOVED = "toRemove";

	//MySQL stuff
	private Connection connect = null;
    private PreparedStatement preparedStatement = null;

    
	public static void main(String args[]) throws Exception, SQLException {

		PropertiesConfiguration config = new PropertiesConfiguration("RetroWorkFlowCleanUp.properties");
		logger = Logger.getLogger(RetroWorkFlowCleanUp.class);

		
		String workflowDataDir = (String) config.getString("WorkFlowDataDirectory");
		boolean simulationMode = config.getBoolean("SimulationMode");
		String mySQLconnectionString = (String) config.getString("MySQLconnectionString");
		
		RetroWorkFlowCleanUp RetroWorkFlowCleanUp = new RetroWorkFlowCleanUp(workflowDataDir,simulationMode,mySQLconnectionString );
		RetroWorkFlowCleanUp.processRetroWorkFlowCleanUp();

	}
	
	public RetroWorkFlowCleanUp(String workflowDataDir,boolean simulationMode, String mySQLconnectionString) {
		
		this.workflowDataDir = workflowDataDir;
		this.simulationMode = simulationMode;
		this.mySQLconnectionString = mySQLconnectionString;
	}

	public RetroWorkFlowCleanUp(Logger logger,String workflowDataDir,boolean simulationMode, String mySQLconnectionString) {
		
		this.workflowDataDir = workflowDataDir;
		this.simulationMode = simulationMode;
		this.logger = logger;
		this.mySQLconnectionString = mySQLconnectionString;
	}

	public void processRetroWorkFlowCleanUp() {

		 	
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hhmmss");
		logger.info("RetroWorkFlowCleanUp Starting :" + dateFormat.format(new Date()));

		// connect to MySQL
		
		try  {
		
			Class.forName("com.mysql.jdbc.Driver");
	        // Setup the connection with the DB
	        connect = DriverManager.getConnection(this.mySQLconnectionString);

	        Statement statement = connect.createStatement();
	        ResultSet resultSet = statement.executeQuery("select ID_ from jbpm_processinstance WHERE END_ is not null");
            processCompleteWorkFlowInstances(resultSet);


		
		}
		catch (ClassNotFoundException e) {
			logger.error(e, e);
		}
		catch (Exception e) {
			logger.error(e, e);
		}

		logger.info("RetroWorkFlowCleanUp ended :" + dateFormat.format(new Date()));

	
	}
	
	private void processCompleteWorkFlowInstances(ResultSet resultSet) throws SQLException {
		
        while (resultSet.next()) {
            String ID_ = resultSet.getString("ID_");
            logger.info("ID_: " + ID_);
        
            preparedStatement = connect.prepareStatement("select STRINGVALUE_ from jbpm_variableinstance where PROCESSINSTANCE_= ? and NAME_ = 'rsuiteWorkingFolderPath' ");
            preparedStatement.setString(1,ID_ );
            ResultSet rsWorkFileRootName = preparedStatement.executeQuery();

            processCompletedWorkFlowFile(rsWorkFileRootName);
            
        }
    }

	private void processCompletedWorkFlowFile(ResultSet rsWorkFileRootName) throws SQLException {
		
        while (rsWorkFileRootName.next()) {
        	
            String workFlowFileLocationStr = rsWorkFileRootName.getString("STRINGVALUE_");
            logger.info("rsuiteWorkingFolderPath=" + workFlowFileLocationStr);
            
	    	int start = workflowDataDir.concat(File.separator).length(); 
	     	// find the next file separator which will be the absolute path to the workflow root directory
	    	int end = workFlowFileLocationStr.indexOf(File.separator, start);

	    	//chop the chunk out that represents the workflow root directory
	    	String workFlowRootDirStr = workFlowFileLocationStr.substring(0, end);

	    	File workFlowRootDir = new File(workFlowRootDirStr);
	    	
	    	if (workFlowRootDir.exists()) {
	    		try {
	    			if (this.simulationMode) {
		                logger.info("SIMULATION MODE  -creating marker file in " + workFlowRootDir.getAbsolutePath());
	    			}
	    			else {
	    				logger.info("creating marker file in " + workFlowRootDir.getAbsolutePath());

	    				File markerFile = new File(workFlowRootDir, FILE_TO_BE_REMOVED);
	    				markerFile.createNewFile();
	    			}
	    		}
	    		catch (Exception e) {
	    			logger.error(e, e);
	    		}	
	    	}
	    	else
	    	{
	    		logger.info("Workflow root " + workFlowRootDir.getPath() + " not found");
	    	}
	    }
    }
}
	
