package 	;

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
	private String WorkflowDataDir;
	private boolean SimulationMode;
	private String MySQLconnectionString;

	//MySQL stuff
	private Connection connect = null;
    private PreparedStatement preparedStatement = null;


	private static String FILE_TO_BE_REMOVED = "toRemove";
	
	
	public static void main(String args[]) throws Exception, SQLException {

		PropertiesConfiguration config = new PropertiesConfiguration("RetroWorkFlowCleanUp.properties");
		logger = Logger.getLogger(RetroWorkFlowCleanUp.class);

		
		String WorkflowDataDir = (String) config.getString("WorkFlowDataDirectory");
		boolean SimulationMode = config.getBoolean("SimulationMode");
		String MySQLconnectionString = (String) config.getString("MySQLconnectionString");
		
		RetroWorkFlowCleanUp RetroWorkFlowCleanUp = new RetroWorkFlowCleanUp(WorkflowDataDir,SimulationMode,MySQLconnectionString );
		RetroWorkFlowCleanUp.processRetroWorkFlowCleanUp();

	}
	
	public RetroWorkFlowCleanUp(String WorkflowDataDir,boolean SimulationMode, String MySQLconnectionString) {
		
		this.WorkflowDataDir = WorkflowDataDir;
		this.SimulationMode = SimulationMode;
		this.MySQLconnectionString = MySQLconnectionString;
	}

	public RetroWorkFlowCleanUp(Logger logger,String WorkflowDataDir,boolean SimulationMode, String MySQLconnectionString) {
		
		this.WorkflowDataDir = WorkflowDataDir;
		this.SimulationMode = SimulationMode;
		this.logger = logger;
		this.MySQLconnectionString = MySQLconnectionString;
	}

	public void processRetroWorkFlowCleanUp() {

		 	
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hhmmss");
		logger.info("RetroWorkFlowCleanUp Starting :" + dateFormat.format(new Date()));

		// connect to MySQL
		
		try  {
		
			Class.forName("com.mysql.jdbc.Driver");
	        // Setup the connection with the DB
	        connect = DriverManager.getConnection(MySQLconnectionString);

	        Statement statement = connect.createStatement();
	        ResultSet resultSet = statement.executeQuery("select ID_ from jbpm_processinstance WHERE END_ is not null");
            processCompleteWorkFlowInstances(resultSet);


		
		}
		catch (ClassNotFoundException e) {
			logger.info(e.toString());
		}
		catch (Exception e) {
			logger.info(e.toString());
			
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.info(sw.toString());
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
        	
            String WorkFlowFileLocationStr = rsWorkFileRootName.getString("STRINGVALUE_");
            logger.info("rsuiteWorkingFolderPath=" + WorkFlowFileLocationStr);
            
	    	int start = WorkflowDataDir.concat(File.separator).length(); 
	     	// find the next file separator which will be the absolute path to the workflow root directory
	    	int end = WorkFlowFileLocationStr.indexOf(File.separator, start);

	    	//chop the chunk out that represents the workflow root directory
	    	String WorkFlowRootDirStr = WorkFlowFileLocationStr.substring(0, end);

	    	File WorkFlowRootDir = new File(WorkFlowRootDirStr);
	    	
	    	if (WorkFlowRootDir.exists()) {
	    		try {
	    			if (SimulationMode) {
		                logger.info("SIMULATION MODE  -creating marker file in " + WorkFlowRootDir.getAbsolutePath());
	    			}
	    			else {
	    				logger.info("creating marker file in " + WorkFlowRootDir.getAbsolutePath());

	    				File markerFile = new File(WorkFlowRootDir, FILE_TO_BE_REMOVED);
	    				markerFile.createNewFile();
	    			}
	    		}
	    		catch (Exception e) {
	    			logger.error(e.toString(), e);
	    		}	
	    	}
	    	else
	    	{
	    		logger.info("Workflow root " + WorkFlowRootDir.getPath() + " not found");
	    	}
	    }
    }
}
	
