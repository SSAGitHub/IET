// Groovy script to import and configure the EHS DTDs.

import com.reallysi.rsuite.admin.importer.*
import com.reallysi.rsuite.client.api.*
import com.rsicms.groovy.utils.*

// -----------------------------------------------------------------------

def util = new DeployDTDUtils(rsuite, importerFactory);

def projectDir = new File("src");
def doctypesDir = new File("setup/doctypes");
def srcDir = new File(projectDir, "main/resources/webcontent");
def xsltDir = new File(srcDir, "xslt");
def File catalogFile = new File(doctypesDir, "catalog.xml");
def catalog = catalogFile.getAbsolutePath();
println "catalog=\"" + catalog + "\"";

def namespaceDecls = (String[])[];

//-----------------------------------------------------------------------

println " + [INFO] Logging into RSuite...";



// --------- CUSTOM DTD /TEMPLATE ----------

if (true) {
	schemaType = "DTD";
	def schemaDir = new File(new File(doctypesDir, "nlm_v3"), "book");
	def schemaName = "book3.dtd";
	def publicId = "-//NLM//DTD Book DTD v3.0 20080202//EN";

	def previewXsltFile = new File(new File(xsltDir, "jpub3-preview"), "jpub3-rsuite-preview-shell.xsl");

	moDefList = [
		new ManagedObjectDefinition(['name' : 'book',
			'displayNameXPath':
			'book-meta/book-title-group/book-title',
			'versionable': 'true',
			'reusable': 'true',
			'browsable': 'true']),
		new ManagedObjectDefinition(['name' : 'book-part',
			'displayNameXPath':
			'concat(if (@book-part-type) then concat(@book-part-type, " ") else "", if (@book-part-number) then concat(@book-part-number, ". ") else "", book-part-meta/title-group/title)',
			'versionable': 'true',
			'reusable': 'true',
			'browsable': 'true']),
		new ManagedObjectDefinition(['name' : 'sec',
			'displayNameXPath':
			'concat(if (label) then concat(label, " ") else "", if (title != "") then title else @sec-type)',
			'versionable': 'true',
			'reusable': 'true',
			'browsable': 'true']),
		new ManagedObjectDefinition(['name' : 'app',
			'displayNameXPath':
			'title',
			'versionable': 'true',
			'reusable': 'true',
			'browsable': 'true']),
		new ManagedObjectDefinition(['name' : 'index',
			'displayNameXPath':
			'title',
			'versionable': 'true',
			'reusable': 'true',
			'browsable': 'true']),
	]

	util.loadSchema(schemaType, schemaDir, schemaName, publicId, schemaName, previewXsltFile, namespaceDecls, moDefList, catalog);

	//-----------------------------------------------------------------------

	println " + [INFO] Done."
}

if (false) {	// import being done in JournalImportAndConfigureDtds.groovy
	schemaType = "DTD";
	def schemaDir = new File(new File(doctypesDir, "jats"), "jats-publishing-dtd-0.4");
	def schemaName = "JATS-journalpublishing0.dtd";
	def publicId = "-//NLM//DTD JATS (Z39.96) Journal Publishing DTD v0.4 20110131//EN";

	// FIXME: Set up the proper JATS preview transform.
	def previewXsltFile = new File(new File(xsltDir, "jpub3-preview"), "jpub3-rsuite-preview-shell.xsl");

	moDefList = [
		new ManagedObjectDefinition(['name' : 'article',
			'displayNameXPath':
			'front/article-meta/title-group/article-title',
			'versionable': 'true',
			'reusable': 'true',
			'browsable': 'true']),
		new ManagedObjectDefinition(['name' : 'sec',
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
			'browsable': 'true']),
	]

	util.loadSchema(schemaType, schemaDir, schemaName, publicId, schemaName, previewXsltFile, namespaceDecls, moDefList, catalog);

	//-----------------------------------------------------------------------

	println " + [INFO] Done."
}

if (false) { // DTD no longer required

	schemaType = "DTD";
	def schemaDir = new File(doctypesDir, "AIP-book");
	def schemaName = "AIPbook-base1.dtd";
	def publicId = "-//AIP//DTD XML Book Setup Module 1.0//EN";

	def previewXsltFile = new File(new File(xsltDir, "aip-preview"), "aip-rsuite-preview-shell.xsl");

	moDefList = [
		new ManagedObjectDefinition(['name' : 'ebook',
			'displayNameXPath':
			'book-metadata/titlegrp/title',
			'versionable': 'true',
			'reusable': 'true',
			'browsable': 'true']),
	]

	util.loadSchema(schemaType, schemaDir, schemaName, publicId, schemaName, previewXsltFile, namespaceDecls, moDefList, catalog);

	println " + [INFO] Done."
}
//-----------------------------------------------------------------------

if (false) {
	schemaType = "XMLSchema";
	schemaDir = new File(doctypesDir, "magazine");
	schemaName = "magazine-interchange.xsd";
	publicId = "http://www.iet.com/doctypes/xsd/magazine-interchange"

	previewXsltFile = new File(new File(xsltDir, "magazine-preview"), "magazine-rsuite-preview-shell.xsl");

	moDefList = [
		new ManagedObjectDefinition(
		['name' : 'document',
			'displayNameXPath': 'content/headline',
			'versionable': 'true',
			'reusable': 'true',
			'browsable': 'true']),
	]

	util.loadSchema(schemaType, schemaDir, schemaName, publicId, schemaName, previewXsltFile, namespaceDecls, moDefList, catalog);
}
println " + [INFO] Done."

// -----------------------------------------------------------------------

// End of script              new ManagedObjectDefinition(['name' : 'titleGroup', 'displayNameXPath': 'concat(substring(title[1], 1, 80), "...")', 'versionable': 'true', 'reusable': 'true', 'browsable': 'true']),


