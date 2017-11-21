// Groovy script to import and configure the EHS DTDs.

import com.reallysi.rsuite.admin.importer.*
import com.reallysi.rsuite.client.api.*
import com.rsicms.groovy.utils.*

// -----------------------------------------------------------------------

def util = new DeployDTDUtils(rsuite, importerFactory);

def projectDir = new File("setup");
def doctypesDir = new File(projectDir, "doctypes");
def srcDir = new File(projectDir, "src");
def xsltDir = new File(srcDir, "xslt");
def File catalogFile = new File(doctypesDir, "catalog.xml");
def catalog = catalogFile.getAbsolutePath();
println "catalog=\"" + catalog + "\"";

//def namespaceDecls = (String[])[];

//-----------------------------------------------------------------------


//-----------------------------------------------------------------------


println "";
println " + [INFO] Importing Onix schema..."
println "";


def schemaDir = new File(doctypesDir, "onix_iet");
def schemaName = "ONIX_BookProduct_3.0_reference_IET.xsd";
def onixSchemaConfiugurationName = "ONIX_BookProduct_3.0_reference_IET_Config.xsd";

def schemaFile = new File(schemaDir, schemaName)
def onixSchemaConfiugurationFile = new File(schemaDir, onixSchemaConfiugurationName)

def namespaceDecls = (String[])[ "onix=" + "http://ns.editeur.org/onix/3.0/reference"];

def moDefList = [
	new ManagedObjectDefinition(
	['name' : '{http://ns.editeur.org/onix/3.0/reference}:ONIXMessage',		
		'displayNameXPath': '/*/*[local-name() ="Product"]/*[local-name() ="RecordReference"]',
		'versionable': 'true',
		'reusable': 'true',
		'browsable': 'true']),
]


def importer = importerFactory.generateImporter("XMLSchema", new SchemaInputSource(schemaFile));
uuid = importer.execute()

importer = importerFactory.generateImporter("XMLSchema", new SchemaInputSource(onixSchemaConfiugurationFile));
uuidConfig = importer.execute()


rsuite.setManagedObjectDefinitions(uuid, false, namespaceDecls, moDefList);
rsuite.setManagedObjectDefinitions(uuidConfig, false, namespaceDecls, moDefList);

def tagmapPreviewXslFile = new File(new File(xsltDir, "onix"), "onix-rsuite-preview-shell.xsl");

println "LOADING preview stylesheet"

if (tagmapPreviewXslFile != null && tagmapPreviewXslFile.exists()) {
	rsuite.loadStylesheetForSchema(uuid, tagmapPreviewXslFile)
	rsuite.loadStylesheetForSchema(uuidConfig, tagmapPreviewXslFile)
}

println " + [INFO] Done."

// -----------------------------------------------------------------------

// End of script              new ManagedObjectDefinition(['name' : 'titleGroup', 'displayNameXPath': 'concat(substring(title[1], 1, 80), "...")', 'versionable': 'true', 'reusable': 'true', 'browsable': 'true']),


