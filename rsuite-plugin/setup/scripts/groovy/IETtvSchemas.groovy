
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

//-----------------------------------------------------------------------


println "";
println " + [INFO] Importing IETtv schema..."
println "";


def schemaDir = new File(doctypesDir, "iet_tv");
def schemaName = "iet_tv.xsd";


def schemaFile = new File(schemaDir, schemaName)


def namespaceDecls = (String[])[ "iettv=" + "http://tv.theiet.org/video"];

def moDefList = [
	new ManagedObjectDefinition(
	['name' : 'Video',		
		'displayNameXPath': '/*/BasicInfo/Title',
		'versionable': 'true',
		'reusable': 'true',
		'browsable': 'true']),
    
    new ManagedObjectDefinition(
        ['name' : 'VideoInspec',
            'displayNameXPath':
          'if (parent::*) then "" else "Inspec classification"',
            'versionable': 'true',
            'reusable': 'true',
            'browsable': 'true']),

	new ManagedObjectDefinition(
		['name' : 'IETTV-Inspec',
			'displayNameXPath':
		  'if (parent::*) then "" else "Inspec classification"',
			'versionable': 'true',
			'reusable': 'true',
			'browsable': 'true']),
			
]


def importer = importerFactory.generateImporter("XMLSchema", new SchemaInputSource(schemaFile));
uuid = importer.execute()


rsuite.setManagedObjectDefinitions(uuid, false, null, moDefList);

def tagmapPreviewXslFile = new File(xsltDir, "iet-tv/iet-tv-video-record-rsuite-preview-shell.xsl");

println "TRYING to load preview stylesheet: " + tagmapPreviewXslFile.absolutePath

if (tagmapPreviewXslFile != null && tagmapPreviewXslFile.exists()) {
    println "LOADING preview stylesheet"
	rsuite.loadStylesheetForSchema(uuid, tagmapPreviewXslFile)
}

println " + [INFO] Done."

// -----------------------------------------------------------------------
