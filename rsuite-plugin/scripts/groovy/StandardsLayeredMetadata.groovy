// Configure layered metadata:
import com.reallysi.rsuite.admin.importer.*
import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*

rsuite.login();
println"Staring ConfigureLayeredMetaDataDefinitionGroovy..."
lmDefs = rsuite.getLayeredMetaDataDefinitionInfos();
fields = [:];

lmDefs.getMapList().each {
	def map = it.convertToMap()
	def fieldName = map["name"];
	fields[map["name"]] = map["type"];
	reportMetaDataMap(map);
}

def reportMetaDataMap(map) {
	def fieldName = map["name"];
	println"Metadata field: \"" + fieldName +"\""
	map.entrySet().each {
		def key = it.getKey();
		if (key !="name") {
			println" " + key +"=" + it.getValue();
		}
	}
}

def addOrReplaceLMDDefinition(lmdName, associatedElements, allowedValues) {
	println" + [INFO] Metadata field \"" + lmdName +":";
	if (fields.containsKey(lmdName)) {
		println" + [INFO]   Field exists, removing existing definition...";
		rsuite.removeLayeredMetaDataDefinition(lmdName);
	}
	println" + [INFO]   Creating new definition for \"" + lmdName +"\"...";
	println" + [INFO]     associated elements:" + associatedElements;
	println" + [INFO]     allowed values:" + allowedValues;
	def lmd = new LayeredMetaDataDefinition(lmdName,"string", associatedElements, allowedValues);
	rsuite.addLayeredMetaDataDefinition(lmd);
}

def addOrReplaceLMDDefinition(lmdName, associatedElements, allowedValues, versioned, allowsMultiple, allowContextual) {
	println" + [INFO] Metadata field \"" + lmdName +":";
	if (fields.containsKey(lmdName)) {
		println" + [INFO]   Field exists, removing existing definition...";
		rsuite.removeLayeredMetaDataDefinition(lmdName);
	}
	println" + [INFO]   Creating new definition for \"" + lmdName +"\"...";
	println" + [INFO]     associated elements:" + associatedElements;
	println" + [INFO]     allowed values:" + allowedValues;
	println" + [INFO]     versioned:" + versioned;
	println" + [INFO]     allowsMultiple:" + allowsMultiple;
	println" + [INFO]     allowContextual:" + allowContextual;
	def lmd = new LayeredMetaDataDefinition(lmdName,"string", versioned, allowsMultiple, allowContextual, associatedElements, allowedValues);	
	rsuite.addLayeredMetaDataDefinition(lmd);
}

def addOrReplaceLMDDefinitionDataType(lmdName, associatedElements, allowedValues, versioned, allowsMultiple, allowContextual, dataType) {
	println" + [INFO] [datatype] Metadata field \"" + lmdName +":";
	if (fields.containsKey(lmdName)) {
		println" + [INFO]   Field exists, removing existing definition...";
		rsuite.removeLayeredMetaDataDefinition(lmdName);
	}
	println" + [INFO]   Creating new definition for \"" + lmdName +"\"...";
	println" + [INFO]     associated elements:" + associatedElements;
	println" + [INFO]     using data type:" + dataType["name"];
	def values = [];

	def lmd = new LayeredMetaDataDefinition(lmdName,"string", versioned, allowsMultiple, allowContextual, associatedElements, allowedValues);
	rsuite.addLayeredMetaDataDefinition(lmd);
	rsuite.setLayeredMetaDataDefinitionDataType(lmdName, dataType["name"]);
}

def addOrReplaceLMDDefinitionDataType(lmdName, associatedElements, dataType) {
	println" + [INFO] [datatype] Metadata field \"" + lmdName +":";
	if (fields.containsKey(lmdName)) {
		println" + [INFO]   Field exists, removing existing definition...";
		rsuite.removeLayeredMetaDataDefinition(lmdName);
	}
	println" + [INFO]   Creating new definition for \"" + lmdName +"\"...";
	println" + [INFO]     associated elements:" + associatedElements;
	println" + [INFO]     using data type:" + dataType["name"];
	def values = [];

	def lmd = new LayeredMetaDataDefinition(lmdName,"string", associatedElements, values);
	rsuite.addLayeredMetaDataDefinition(lmd);
    rsuite.setLayeredMetaDataDefinitionDataType(lmdName, dataType["name"]);
}

println"Updating definitions"

def nonXmlMoTypes = ['nonxml']
def assemblyTypes = ['rs_ca', 'rs_canode']
def moRootElemTypes  = [];
def standardsMoTypes = ['guide_notemap', 'chapter', 'regmap', 'regulation', 'part' , 'topic', 'd4pCover' , 'index']


// Define data types used by LMD:


def datatypeYesNo = new DataTypeDefinition("yesno","string","yes or no","radiobutton","literal");
datatypeYesNo.addItem("yes","yes");
datatypeYesNo.addItem("no","no");
rsuite.setDataTypeDefinition(datatypeYesNo);

/**
 * Define LMD fields:
 */
def xmlMoTypes = ['article', 'book', 'article-meta']

def standardsXmlMoTypes = ['guide_notemap', 'chapter', 'regmap', 'regulation', 'part', "ONIXMessage", 'topic', 'd4pCover']

def ietTvxmlMoTypes = ['Video', 'VideoInspec']

def filesMoElemTypes = nonXmlMoTypes + xmlMoTypes + standardsXmlMoTypes

def allMoElemTypes = assemblyTypes +  nonXmlMoTypes + xmlMoTypes + standardsXmlMoTypes + ietTvxmlMoTypes


addOrReplaceLMDDefinition("_pid", assemblyTypes + nonXmlMoTypes, null, false, false, true)
addOrReplaceLMDDefinition("_md5Hash",  allMoElemTypes, null);
// lmdName, associatedElements, allowedValues, versioned, allowsMultiple, allowContextual


// LMD for"hot folder containers":

addOrReplaceLMDDefinition("_hot_container_process_definition", assemblyTypes, null, false, false, true)
addOrReplaceLMDDefinitionDataType("_is_hot_container_enabled", assemblyTypes, datatypeYesNo)

addOrReplaceLMDDefinition("corporate_contributor", assemblyTypes, null, false, false, false)

addOrReplaceLMDDefinition("genertedBy", assemblyTypes, null, false, false, false)
addOrReplaceLMDDefinition("outputType", assemblyTypes, null, false, false, false)
addOrReplaceLMDDefinition("sourceMoId", assemblyTypes, null, false, false, false)
addOrReplaceLMDDefinition("book_status", assemblyTypes, null, false, false, false)
addOrReplaceLMDDefinition("edition_name", assemblyTypes, null, false, false, false)
addOrReplaceLMDDefinition("book_edition_short", assemblyTypes, null, false, false, false)

addOrReplaceLMDDefinition("iet_author", assemblyTypes, null, false, false, false)
addOrReplaceLMDDefinition("iet_owner", assemblyTypes, null, false, false, false)
addOrReplaceLMDDefinition("icml_transformation", assemblyTypes, null, false, false, false)
addOrReplaceLMDDefinition("pdf_transformation", assemblyTypes, null, false, false, false)
addOrReplaceLMDDefinition("archiveTimestamp", assemblyTypes, null, false, false, false)
addOrReplaceLMDDefinition("createTimestamp", assemblyTypes, null, false, false, false)


addOrReplaceLMDDefinition("has_output", assemblyTypes + standardsMoTypes + ['article'], null, false, false, true)

addOrReplaceLMDDefinition("standards_book_prefix", assemblyTypes, null, false, false, true)

addOrReplaceLMDDefinition("publish_workflow_log", filesMoElemTypes, null, false, false, true)
//addOrReplaceLMDDefinitionDataType("is_removable_publication_result", filesMoElemTypes, datatypeYesNo)
// addOrReplaceLMDDefinitionDataType("publication_with_warnings", filesMoElemTypes, datatypeYesNo)
// addOrReplaceLMDDefinitionDataType("publication_with_errors", filesMoElemTypes, datatypeYesNo)

addOrReplaceLMDDefinitionDataType("is_removable_publication_result", filesMoElemTypes, null, false, false, true, datatypeYesNo)
addOrReplaceLMDDefinitionDataType("publication_with_errors", filesMoElemTypes, null, false, false, true, datatypeYesNo)
addOrReplaceLMDDefinitionDataType("publication_with_warnings", filesMoElemTypes, null, false, false, true, datatypeYesNo)

addOrReplaceLMDDefinition("user_creator", filesMoElemTypes, null, false, false, true)
addOrReplaceLMDDefinition("date_created", filesMoElemTypes, null, false, false, true)

addOrReplaceLMDDefinition("publication_dependency", assemblyTypes, null, false, true, true)

addOrReplaceLMDDefinition("mathml_font", assemblyTypes, null, false, false, true)
