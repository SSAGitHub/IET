// Configure layered metadata:
import com.reallysi.rsuite.admin.importer.*
import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*


// -----------------------------------------------------------------------



// @MANUEL_BEGIN Comment out login, the session already exists.
// rsuite.login();
// @MANUEL_END

lmDefs = rsuite.getLayeredMetaDataDefinitionInfos();
fields = [:];


def yesno = new DataTypeDefinition("yesno", "string", "yes or no", "select", "literal");
yesno.addItem("Yes", "yes");
yesno.addItem("No", "no");
rsuite.setDataTypeDefinition(yesno);

lmDefs.getMapList().each {

  def map = it.convertToMap() 
  def fieldName = map["name"];
  fields[map["name"]] = map["type"];
}

def addOrReplaceLMDDefinition(lmdName, associatedElements, allowedValues) {
  println " + [INFO] Metadata field \"" + lmdName + ":";
  if (fields.containsKey(lmdName)) {
    println " + [INFO]   Field exists, removing existing definition...";
    rsuite.removeLayeredMetaDataDefinition(lmdName);
  }
  println " + [INFO]   Creating new definition for \"" + lmdName + "\"...";
  println " + [INFO]     associated elements: " + associatedElements;
  println " + [INFO]     allowed values: " + allowedValues;
  def lmd = new LayeredMetaDataDefinition(lmdName, "string", associatedElements, allowedValues);
  rsuite.addLayeredMetaDataDefinition(lmd);
}

def addOrReplaceLMDDefinition(lmdName, versioned, allowsMultiple, associatedElements, allowedValues) {
	println " + [INFO] Metadata field \"" + lmdName + ":";
	if (fields.containsKey(lmdName)) {
	  println " + [INFO]   Field exists, removing existing definition...";
	  rsuite.removeLayeredMetaDataDefinition(lmdName);
	}
	println " + [INFO]   Creating new definition for \"" + lmdName + "\"...";
	println " + [INFO]     associated elements: " + associatedElements;
	println " + [INFO]     allowed values: " + allowedValues;
	def lmd = new LayeredMetaDataDefinition(lmdName, "string", versioned, allowsMultiple,  associatedElements, allowedValues);					
	rsuite.addLayeredMetaDataDefinition(lmd);
  }
                          
println "Updating definitions"

                          
def nonXmlMoTypes = ['nonxml']
def assemblyTypes = ['rs_ca', 'rs_canode']
def xmlMoTypes = ['article', 'book', 'article-meta']

def standardsXmlMoTypes = ['guide_notemap', 'chapter', 'regmap', 'regulation', 'part']

 def allMoElemTypes = assemblyTypes +  nonXmlMoTypes + xmlMoTypes + standardsXmlMoTypes
 
 addOrReplaceLMDDefinition("status", false, false, assemblyTypes, null)

/* new metadata */
addOrReplaceLMDDefinition("product_code", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("isbn", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("book_title", false, false,  assemblyTypes, null)
addOrReplaceLMDDefinition("book_series_name", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("book_title_short", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("printer", false, false, assemblyTypes, null)

addOrReplaceLMDDefinition("e_isbn", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("e_product_code", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("contract_signed_date", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("book_metadata_sent", false, false, assemblyTypes, null)

addOrReplaceLMDDefinition("vs_isbn", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("vs_product_code", false, false, assemblyTypes, null)

addOrReplaceLMDDefinition("doi", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("price_pound", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("price_dollar", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("commissioning_editor", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("pub_date_catalogue", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("replacement", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("extent", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("trim_size", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("format", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("work_order", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("initial_print_run", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("initial_reorder_level", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("stage_due_complete", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("proposal_presentation_date", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("contracted_ts_delivery_date", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("actual_ts_delivery_date", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("reforecast_pub_date", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("actual_pub_date", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("notes", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("inspec_required", false, false, assemblyTypes, null)
addOrReplaceLMDDefinition("additional_authors", false, true, assemblyTypes, null)
addOrReplaceLMDDefinition("templateId", false, false, assemblyTypes, null)

// @MANUEL_BEGIN Comment out logout, the session will be used by other scripts.
// rsuite.logout();
// @MANUEL_END

/* End of Script */                          
