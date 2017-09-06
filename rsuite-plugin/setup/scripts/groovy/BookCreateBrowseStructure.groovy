import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*

//rsuite.login();

def booksMoId = createContainerIfNotExist("/", "/",  "Academic Books", "books")

createContainerIfNotExist("/Academic Books/", booksMoId, "All", "booksCA")
createContainerIfNotExist("/Academic Books/", booksMoId, "Contracted", "booksCA")
createContainerIfNotExist("/Academic Books/", booksMoId, "In Production", "booksCA")
createContainerIfNotExist("/Academic Books/", booksMoId, "Published", "booksCA")
createContainerIfNotExist("/Academic Books/", booksMoId, "Cancelled", "booksCA")
 
 
 def createContainerIfNotExist(parentPath, parentId, containerName, type){
	 
	 def containerPath = parentPath + containerName;
	 
	 def folderId = null;	 
	 
	 infos = rsuite.findObjectForPath(containerPath);
	 
	 if (infos == null){
	
		 def folderOptions = new Options();
		 
		 folderOptions.add("type", type);
		 
		 result = rsuite.createContentAssembly(parentId, containerName, folderOptions);
		 folderId = result["moid"];
		 println "[INFO] Created container " + containerName + " " + folderId;
	
	 }else{
		 if (infos.entryList != null){
			 for (entry in infos.entryList){
				 
				 if (entry.key.equals("moid")){
					 folderId = entry.value
					 break
				 }
			 }
		 }
		 
		 println "[INFO] Container " + containerPath +  "  already exist";
	 }
	
	 return folderId; 	 
 }

//rsuite.logout();