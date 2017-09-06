// Configure layered metadata:
import com.reallysi.rsuite.admin.importer.*
import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*

rsuite.login();
println"Staring Onix layered metada..."
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
def moRootElemTypes  = ['ONIXMessage'];


/**
 * Define LMD fields:
 */
def onixMoTypes = ['ONIXMessage']
addOrReplaceLMDDefinition("onix_configuration_type", onixMoTypes, null, false, true, true)
addOrReplaceLMDDefinition("product_format", assemblyTypes, null, true, true, true)
addOrReplaceLMDDefinition("onix_recipient", assemblyTypes, null, true, true, true)
addOrReplaceLMDDefinition("onix_ready", assemblyTypes, null, true, true, true)
addOrReplaceLMDDefinition("onix_book_vendor", assemblyTypes, null, true, true, true)
addOrReplaceLMDDefinition("onix_message_number", assemblyTypes, null, true, true, true)

addOrReplaceLMDDefinition("onix_publication_rsuite_id", moRootElemTypes, null, false, false, true)


addOrReplaceLMDDefinition("onix_last_sync_timestamp", assemblyTypes, null, false, false, true)
addOrReplaceLMDDefinition("onix_last_sync_failed", assemblyTypes, null, false, false, true)

rsuite.logout();