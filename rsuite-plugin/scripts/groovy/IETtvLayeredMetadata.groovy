// Configure layered metadata:
import com.reallysi.rsuite.admin.importer.*
import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*

rsuite.login();
println"Staring IETtv layered metada..."
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


/**
 * Define LMD fields:
 */

addOrReplaceLMDDefinition("iettv_channel_year_id", assemblyTypes, null, false, false, true)
addOrReplaceLMDDefinition("iettv_video_id", assemblyTypes, null, false, false, true)
addOrReplaceLMDDefinition("iettv_video_withdrawn", assemblyTypes, null, false, false, true)

addOrReplaceLMDDefinition("iettv_parent_doi", assemblyTypes, null, false, false, true)
addOrReplaceLMDDefinition("iettv_registrant_name", assemblyTypes, null, false, false, true)
addOrReplaceLMDDefinition("iettv_portal_administrator", assemblyTypes, null, false, false, true)
addOrReplaceLMDDefinition("iettv_email", assemblyTypes, null, false, false, true)



rsuite.logout();