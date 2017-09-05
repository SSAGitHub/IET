// Script to import and configure the project DTDs in RSuite
//
import com.reallysi.rsuite.admin.importer.*
import com.reallysi.rsuite.client.api.*

// -----------------------------------------------------------------------

def otHome = "";
try {
    otHome = "${ditaot}";
} catch (e) {
    otHome = System.getenv("DITA_OT_HOME");
}
if (otHome == "") {
    println "Property ditaot not set and environment variable DITA_OT_HOME is not set. Set to the directory";
    println "that contains your DITA Open Toolkit, e.g., the 'dita/DITA-OT' directory"
    println "under the OxygenXML frameworks/ directory."
    println "e.g. -Dditaot=/opt/rsuite-data/DITA-OT"
    return;
}
println "ditaot is " + otHome
def File otDir = new File(otHome);
def File catalogFile = new File(otDir, "catalog-dita.xml");
def catalog = catalogFile.getAbsolutePath();
println "catalog=\"" + catalog + "\"";

projectDir = new File(scriptFile.absolutePath).parentFile.parentFile.parentFile;
doctypesDir = new File(otDir, "plugins/org.iet.doctypes/doctypes");

def File xsltDir = new File(projectDir, "src/xslt");
println "doctypesDir is " + doctypesDir.getAbsolutePath();

baseTopicTypePubId = "urn:pubid:org.iet:doctypes:dita:";
baseMapTypePubId   = "urn:pubid:org.iet:doctypes:dita:";
previewXslFile = new File(projectDir, "src/xslt/preview/dita-preview-shell.xsl");

def loadAndConfigureTopicDtd(dtdFile, dtdPublicId, topicTypes, otherMoTypes, previewXslFile, catalog)
{
    def moDefList = [];
    topicTypes.each {
        moDefList.add(new ManagedObjectDefinition(['name' : it[1], 
                                                      'displayNameXPath': "if (/d4pCover) then //*[contains(@class, ' topic/navtitle')] else *[contains(@class, ' topic/title')]", 
                                                      'versionable': 'true', 
                                                      'reusable': 'true']))    
    }
    
    otherMoTypes.each { 
    moDefList.add(new ManagedObjectDefinition(['name' : it[0], 
                                               'displayNameXPath': it[1], 
                                               'versionable': 'true', 
                                               'reusable': 'true']))
    }
    
    loadAndConfigureDtd(dtdFile, dtdPublicId, moDefList, previewXslFile, catalog);
}

def loadAndConfigureMapDtd(dtdFile, dtdPublicId, mapType, previewXslFile, catalog)
{
    def moDefList = [];
    moDefList.add(new ManagedObjectDefinition(['name' : mapType, 
       'displayNameXPath': 
           "(if (*[contains(@class, ' topic/title')]) " +
              " then *[contains(@class, ' topic/title')] " +
              " else string(@title))", 
       'versionable': 'true', 
       'reusable': 'true']))
    
    loadAndConfigureDtd(dtdFile, dtdPublicId, moDefList, previewXslFile, catalog);
}

def loadAndConfigureDtd(dtdFile, dtdPublicId, moDefList, previewXslFile, catalog)
    {
    println " + [INFO] Importing DTD " + dtdFile.name + ", public ID \"" + dtdPublicId + "\"...";
    importer = importerFactory.generateImporter("DTD", new SchemaInputSource(dtdFile, dtdFile.name, dtdPublicId));
    importer.setCatalogNames((String[])[catalog])
    uuid = importer.importDtd()
    
    rsuite.setManagedObjectDefinitions(uuid, false, moDefList)
    rsuite.loadStylesheetForSchema(uuid, previewXslFile)

}


topicTypes = [
	//['article', 'article'], 
	['chapter', 'chapter'], 
//	['concept', 'concept'], 
//	['glossentry', 'glossentry'], // WEK: Commenting out glossentry for now, something not working on import 
	['part', 'part'], 
//	['reference', 'reference'], 
//	['sidebar', 'sidebar'], 
//	['subsection', 'subsection'], 
//	['task', 'task'], 
	['index', 'index'],
	['topic', 'topic'],
	['d4pCover', 'd4pCover'],	
	['regulation', 'regulation'], 
];

def mapTypes = [
	['regmap', 'regmap'],
	['guide_notemap', 'guide_notemap'],
	//['pubmap', 'pubmap'],
	//['subjectScheme', 'subjectScheme'],
	 ];

 // Pairs are MO type, display name xpath
def otherMoTypes =  [ 
    ['subsection', "*[contains(@class, ' topic/title ')]"],
  //  ['sidebar',  "*[contains(@class, ' topic/title ')]"],
//    ['fig',  "(if (*[contains(@class, ' topic/title ')]) then *[contains(@class, ' topic/title ')] else concat('Figure: ', substring(., 1, 30), '...'))"],
    //['table',  "(if (*[contains(@class, ' topic/title ')]) then *[contains(@class, ' topic/title ')] else concat('Table: ', substring(., 1, 30), '...'))"],
    //['ul', "concat('UL: ', substring(., 1, 30))"],
    //['ol', "concat('OL: ', substring(., 1, 30))"],
    //['li', "concat('LI: ', substring(., 1, 30))"],
    ['p', "concat('Para: ', substring(., 1, 30))"],
    //['q', "concat('Motto: ', substring(., 1, 30))"],
];

println "Loading topic DTDs...";
topicTypes.each {
    loadAndConfigureTopicDtd(new File(doctypesDir, it[0] + "/dtd/" + it[0] + ".dtd"), 
            baseTopicTypePubId + it[0], 
            topicTypes, 
            otherMoTypes,
            previewXslFile,
            catalog);
    
}

println "Loading map DTDs...";

mapTypes.each {
    loadAndConfigureMapDtd(new File(doctypesDir, it[0] + "/dtd/" + it[0] + ".dtd"), 
            baseMapTypePubId + it[0], 
            it[1], 
            previewXslFile,
            catalog);
    
}

println "Loading technical DTDs...";

//def technicalDoctypes = new File(otDir, "dtd/technicalContent/dtd")
//
//technicalTopicTypes = [
//	['concept', 'Concept'],
//	['task', 'Task'],
//	['task', ''],
//	['task', 'General Task'],	
//	['topic', 'Topic'],
//	['map', 'Map']
//];
//
//technicalTopicTypes.each {
//	if (it[1] != ""){
//		dtdPublicId = "-//OASIS//DTD DITA " + it[1] + "//EN";
//	}else{
//	dtdPublicId = null;
//	}
//	
//	dtdFile = new File(technicalDoctypes,  it[0] + ".dtd");
//	def moDefList = [];
//	
//	moDefList.add(new ManagedObjectDefinition(['name' : it[0],
//		'displayNameXPath': "/*/title",
//		'versionable': 'true',
//		'reusable': 'true']))
//	
//	loadAndConfigureDtd(dtdFile,
//			dtdPublicId,
//			moDefList,
//			previewXslFile,
//			catalog);
//		
//		
//	
//}



  if (true) {
    // NOTE: This doctype is provided by the DITA4Publishers project.
    println "";
    println " + [INFO] Importing style2tagmap.xsd..."
    println "";
    def schemaFile = new File(otDir, "plugins/net.sourceforge.dita4publishers.doctypes/doctypes/style2tagmap/xsd/style2tagmap.xsd")
    def importer = importerFactory.generateImporter("XMLSchema", new SchemaInputSource(schemaFile));
    uuid = importer.execute()
    def moDefList = [];
    
    def namespaceDecls = (String[])[ "s2t=" + "urn:public:dita4publishers.org:namespaces:word2dita:style2tagmap"];
    
    moDefList.add(new ManagedObjectDefinition(['name' : '{urn:public:dita4publishers.org:namespaces:word2dita:style2tagmap}:style2tagmap',
                                               'displayNameXPath': "s2t:title",
                                               'versionable': 'true',
                                               'reusable': 'true']))
    rsuite.setManagedObjectDefinitions(uuid, false, namespaceDecls, moDefList);
    
    def tagmapPreviewXslFile = new File(xsltDir, "style2tagmap/preview/style2tagmap-preview.xsl")
    if (tagmapPreviewXslFile != null && tagmapPreviewXslFile.exists()) {
        rsuite.loadStylesheetForSchema(uuid, tagmapPreviewXslFile)
    }
  } else {
    println "";
    println " + [INFO] Skipping import of style2tagmap doctype."
    println "";
  }
  


// End of script