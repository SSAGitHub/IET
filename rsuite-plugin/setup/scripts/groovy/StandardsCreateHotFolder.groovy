import com.reallysi.rsuite.admin.importer.*
import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*

def createDir (path)
{
	File dir = new File(path);

	if	(dir.exists())
	{
		println "The directory \"" + path + "\" already exists.";
		return;
	}

	if	(dir.mkdirs())
	{
		println "Create directory \"" + path + "\".";
		return;
	}

	println "[ERROR] Unable to create directory \"" + path + "\".";
}

def setUpHotFolder (hotfolder, processName)
{
	rsuite.removeHotFoldersByProcessDefinition(processName);
	rsuite.setHotFolder(hotfolder, processName, true);
	createDir(hotfolder);
}

if	(baseDir == null)
{
	println "[ERROR] The \"baseDir\" parameter must be passed. Unable to configure the hot folders.";
	return;
}

rsuite.login();

setUpHotFolder(baseDir + "/" + "importDitaMap", "IET Import DITA Map");

rsuite.logout();
