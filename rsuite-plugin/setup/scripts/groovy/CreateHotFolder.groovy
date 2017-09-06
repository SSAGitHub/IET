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

setUpHotFolder(baseDir + "/" + "importBookFromInpsec", "Import Book from Inpsec Classifiers Workflow");
setUpHotFolder(baseDir + "/" + "importArticleFromManuscriptCentral", "IET Manuscript Ingestion");
setUpHotFolder(baseDir + "/" + "importTypesetterFiles", "IET Typesetter Ingestion");
setUpHotFolder(baseDir + "/" + "importArticleFromInpsec", "IET INSPEC Ingestion");

rsuite.logout();
