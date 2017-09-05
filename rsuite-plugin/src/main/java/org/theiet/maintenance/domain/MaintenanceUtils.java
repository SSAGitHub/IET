package org.theiet.maintenance.domain;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;

public class MaintenanceUtils {

	public static  File createMaintenanceLogFile(RemoteApiExecutionContext context,
			String maintenanceIssueId) {
		File logDir = context.getRSuiteServerConfiguration().getLogsDir();
		   
		   
		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
		   
	
			File logFile = new File(logDir, "maintenance/maintenence_" + maintenanceIssueId + "_"
					+ sdf.format(new Date()) + ".log");
		return logFile;
	}

}
