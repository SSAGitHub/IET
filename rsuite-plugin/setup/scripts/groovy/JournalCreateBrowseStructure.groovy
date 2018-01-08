import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*


 
//def projectDir = new File(scriptFile.absolutePath).parentFile.parentFile.parentFile
//def projectDir = new File("/src/test/resources/org/theiet/rsuite")
def projectDir = new File("setup")

def resarchJournalsMap = [
	"Biometrics" : "BMT",
	"Circuits, Devices & Systems" : "CDS",
	"Computers & Digital Techniques" : "CDT",
	"Communications" : "COM",
	"Control Theory & Applications" : "CTA",
	"Computer Vision" : "CVI",
	"Electric Power Applications" : "EPA",
	"Electrical Systems in Transportation" : "EST",
	"Generation, Transmission & Distribution" : "GTD",
	"Information Security" : "IFS",
	"Image Processing" : "IPR",
	"Intelligent Transport Systems" : "ITS",
	"Microwaves, Antennas & Propagation" : "MAP",
	"Nanobiotechnology" : "NBT",
	"Networks" : "NET",
	"Optoelectronics" : "OPT",
	"Power Electronics" : "PEL",
	"Renewable Power Generation" : "RPG",
	"Radar, Sonar & Navigation" : "RSN",
	"Software" : "SEN",
	"Science, Measurement & Technology" : "SMT",
	"Signal Processing" : "SPR",
	"Systems Biology" : "SYB",
	"Wireless Sensor Systems" : "WSS",
	"Journal of Engineering" : "JOE"
	];

def letterJournalsMap = [	
	"Electronics Letters" : "ELL",
	"Micro & Nano Letters" : "MNL"
	];

journalEmails = [
	"BMT" : "iet_bmt@theiet.org",
    "CDS" : "iet_cds@theiet.org",
    "CDT" : "iet_cdt@theiet.org",
    "COM" : "iet_com@theiet.org",
    "CTA" : "iet_cta@theiet.org",
    "CVI" : "iet_cvi@theiet.org",
    "EPA" : "iet_epa@theiet.org",
    "EST" : "iet_est@theiet.org",
    "GTD" : "iet_gtd@theiet.org",
    "IFS" : "iet_ifs@theiet.org",
    "IPR" : "iet_ipr@theiet.org",
    "ITS" : "iet_its@theiet.org",
    "MAP" : "iet_map@theiet.org",
    "NBT" : "iet_nbt@theiet.org",
    "NET" : "iet_net@theiet.org",
    "OPT" : "iet_opt@theiet.org",
    "PEL" : "iet_pel@theiet.org",
    "RPG" : "iet_rpg@theiet.org",
    "RSN" : "iet_rsn@theiet.org",
    "SEN" : "iet_sen@theiet.org",
    "SMT" : "iet_smt@theiet.org",
    "SPR" : "iet_spr@theiet.org",
    "SYB" : "iet_syb@theiet.org",
    "WSS" : "iet_wss@theiet.org",
    "ELL" : "ELetters@theiet.org",
    "MNL" : "micronanoletters@theiet.org"];


editorialAssistant = [
	"BMT" : "SShah",
	"CDS" : "SShah",
	"CDT" : "SShah",
	"COM" : "BGriffiths",
	"CTA" : "JLawrie",
	"CVI" : "KateHarrison",
	"EPA" : "RebeccaRatty",
	"EST" : "KateHarrison",
	"GTD" : "RebeccaRatty",
	"IFS" : "JLawrie",
	"IPR" : "HFaulkner",
	"ITS" : "RebeccaRatty",
	"MAP" : "PRowley",
	"NBT" : "RebeccaRatty",
	"NET" : "KateHarrison",
	"OPT" : "JLawrie",
	"PEL" : "KateHarrison",
	"RPG" : "SShah",
	"RSN" : "RebeccaRatty",
	"SEN" : "HFaulkner",
	"SMT" : "KateHarrison",
	"SPR" : "SShah",
	"SYB" : "SShah",
	"WSS" : "KateHarrison",
	"ELL" : "TBedford",
	"MNL" : "ABond"];

def journalTypes = ["Research Journal" : resarchJournalsMap, "Letters Journal" : letterJournalsMap];
//def journalTypes = ["Letters Journal" : letterJournalsMap];

//rsuite.login();
def root = "/";
def parent = root;
def journalTypesContainer = "Journals";
folderId = createContainerIfNotExist(parent, "/", journalTypesContainer, "folder");
def typeSet= journalTypes.entrySet()


typeSet.each {
	parent = root + journalTypesContainer + "/";
	def journalsContainer = it.key;
	journalsId = createContainerIfNotExist(parent, folderId, journalsContainer, "journals");	
	def jMap = it.value.entrySet();
	
	jMap.each {
		parent = root + journalTypesContainer + "/" + journalsContainer + "/";
		def journalContainer = it.key;
		def journalCode = it.value;
		journalId = createContainerIfNotExist(parent, journalsId, journalContainer, "journal");
		parent = root + journalTypesContainer + "/" + journalsContainer + "/" + journalContainer + "/";
		
		def articlesContainer = "Articles";
		createContainerIfNotExist(parent, journalId, articlesContainer, "articles", journalCode);
		
		def availableContainer = "Available";
		createContainerIfNotExist(parent, journalId, availableContainer, "RXS", journalCode, true);
		
		def issuesContainer = "Issues";
		createContainerIfNotExist(parent, journalId, issuesContainer, "issues", journalCode);
	}	
}

createContainerIfNotExist(root + journalTypesContainer + "/", folderId, "Withdrawn Articles", "withdrawnArticles")
createArticleAdminStructure(projectDir, root + journalTypesContainer + "/", folderId)

def createArticleAdminStructure(projectDir, parent, folderId){
    def staticDir = new File(projectDir, 'sample_data/journals/admin/publish/pdf/static');
    
    adminId = createContainerIfNotExist(parent, folderId, "Admin", "admin");
    parent = parent + "Admin/"
    publishId = createContainerIfNotExist(parent, adminId, "Publish", "folder");
    parent = parent + "Publish/"
    publishPdfId = createContainerIfNotExist(parent, publishId, "Pdf", "folder");
    parent = parent + "Pdf/"
    
    infos = rsuite.findObjectForPath(parent + "static");
    
    publishId = createContainerIfNotExist(parent, publishPdfId, "static", "folder");
    
    if (infos == null){

        staticDir.eachFile() { file ->
            loadFile(file, publishId)
          }
    }    
    
}

def loadFile(file, parentMoid){
    result = rsuite.loadNonXmlFromFile(file, true, true);
    def moid = result.getEntryValue("moid")
    println "load as moid [${moid}]"   
    rsuite.addResourceToAssembly(parentMoid, moid)
}


def createContainerIfNotExist(parentPath, parentId, containerName, containerType){
	return createContainerIfNotExist(parentPath, parentId, containerName, containerType, null, false);
}

def createContainerIfNotExist(parentPath, parentId, containerName, containerType, containerCode){
	return createContainerIfNotExist(parentPath, parentId, containerName, containerType, containerCode, false);
}

def createContainerIfNotExist(parentPath, parentId, containerName, containerType, containerCode, isDynamicContentAssemblyNode){	
	def containerPath = parentPath + containerName;	
	def folderId = null;	
	infos = rsuite.findObjectForPath(containerPath);	
	 
	if (infos == null){
		if (containerType.equals("articles") || containerType.equals("RXS") || containerType.equals("issues")) {
			rsuite.setMetaData(parentId, "journal_code", containerCode);
			rsuite.setMetaData(parentId, "production_controller", "BGriffiths");
			if (editorialAssistant[containerCode] != null){
				rsuite.setMetaData(parentId, "editorial_assistant", editorialAssistant[containerCode]);
			}			
			rsuite.setMetaData(parentId, "typesetter", "ietTypesetter");
			rsuite.setMetaData(parentId, "inspec_classifier", "ietClassifier");			
			rsuite.setMetaData(parentId, "journal_email", journalEmails[containerCode]);
		}
		   
		def folderOptions = new Options();		
		folderOptions.add("type", containerType);
		
		if (isDynamicContentAssemblyNode) {
			query = "/rs_ca_map/rs_ca[./mv:metadata/mv:system/mv:ca-type = 'article' and ./mv:metadata/mv-lmd:layered/mv-lmd:journal_code = '" + containerCode + "' and ./mv:metadata/mv-lmd:layered/mv-lmd:available = 'yes']";
			result = rsuite.createDynamicContentAssemblyNode(parentId, containerName, containerCode, query, null);
		} else {
			result = rsuite.createContentAssembly(parentId, containerName, folderOptions);
		}
		folderId = result["moid"];
		println "[INFO] Created " + containerType + ": " + containerName + " at " + containerPath + " (" + folderId + ") new";   
	}else{
		if (infos.entryList != null){
			for (entry in infos.entryList){				
				if (entry.key.equals("moid")){
					folderId = entry.value
					break
				}
			}
		}		
		println "[INFO] " + containerType + ": " + containerName + " at " + containerPath +  " (" + folderId + ") already exist";
	}
   
	return folderId;
}

//rsuite.logout();