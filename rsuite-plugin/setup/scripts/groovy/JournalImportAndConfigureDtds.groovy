// Groovy script to import and configure the EHS DTDs.

import com.reallysi.rsuite.admin.importer.*
import com.reallysi.rsuite.client.api.*
import com.rsicms.groovy.utils.*

// -----------------------------------------------------------------------

def util = new DeployDTDUtils(rsuite, importerFactory);

def projectDir = new File("src");
def doctypesDir = new File("setup/doctypes");
def srcDir = new File(projectDir, "main/resources/WebContent");
def xsltDir = new File(srcDir, "xslt");
def File catalogFile = new File(doctypesDir, "catalog.xml");
def catalog = catalogFile.getAbsolutePath();
println "catalog=\"" + catalog + "\"";

def namespaceDecls = (String[])[];

//-----------------------------------------------------------------------

println " + [INFO] Logging into RSuite...";

rsuite.login();

// --------- CUSTOM DTD /TEMPLATE ----------


if (true) {
schemaType = "DTD";
def schemaDir = new File(new File(doctypesDir, "jats"), "jats-publishing-dtd-1.0");
def schemaName = "JATS-journalpublishing1.dtd";
def publicId = "-//NLM//DTD JATS (Z39.96) Journal Publishing DTD v1.0 20120330//EN";
				
// FIXME: Set up the proper JATS preview transform.
def previewXsltFile = new File(new File(xsltDir, "jpub3-preview"), "jpub3-rsuite-preview-shell.xsl");

moDefList = [ 
	new ManagedObjectDefinition(['name' : 'article',
		'displayNameXPath':
		  'front/article-meta/title-group/article-title',
		'versionable': 'true',
		'reusable': 'true',
		'browsable': 'true']),
	
	new ManagedObjectDefinition(['name' : 'article-meta',
		'displayNameXPath':
		  'if (parent::*) then "" else "Inspec classification"',
		'versionable': 'true',
		'reusable': 'true',
		'browsable': 'true']),
	

	/*new ManagedObjectDefinition(['name' : 'sec',
		'displayNameXPath':
		  'concat(if (label) then concat(label, " ") else "", title)',
		'versionable': 'true',
		'reusable': 'true',
		'browsable': 'true']),

	new ManagedObjectDefinition(['name' : 'ref-list',
		'displayNameXPath':
		  'title',
		'versionable': 'true',
		'reusable': 'true',
		'browsable': 'true']),*/


            ]

util.loadSchema(schemaType, schemaDir, schemaName, publicId, schemaName, previewXsltFile, namespaceDecls, moDefList, catalog);

//-----------------------------------------------------------------------

println " + [INFO] Done."
}

// -----------------------------------------------------------------------

// End of script              new ManagedObjectDefinition(['name' : 'titleGroup', 'displayNameXPath': 'concat(substring(title[1], 1, 80), "...")', 'versionable': 'true', 'reusable': 'true', 'browsable': 'true']),


