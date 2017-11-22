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


println "Updating definitions"

                          
def nonXmlMoTypes = ['nonxml']
def assemblyTypes = ['rs_ca', 'rs_canode']
def xmlMoTypes = ['article']

 
// Define data types used by LMD:

/**
 * Define LMD fields:
 */
addOrReplaceLMDDefinition("author_email",  assemblyTypes, null);
addOrReplaceLMDDefinition("author_salutation",  assemblyTypes, null);
addOrReplaceLMDDefinition("author_surname",  assemblyTypes, null);
addOrReplaceLMDDefinition("author_first_name",  assemblyTypes, null);
addOrReplaceLMDDefinition("author_institution",  assemblyTypes, null);
addOrReplaceLMDDefinition("author_country",  assemblyTypes, null);
addOrReplaceLMDDefinition("article_title",  assemblyTypes, null);
addOrReplaceLMDDefinition("category",  assemblyTypes, null);
addOrReplaceLMDDefinition("article_type",  assemblyTypes, null);
addOrReplaceLMDDefinition("is_special_issue",  assemblyTypes, null);
addOrReplaceLMDDefinition("special_issue_title",  assemblyTypes, null);
addOrReplaceLMDDefinition("submitted_date",  assemblyTypes, null);
addOrReplaceLMDDefinition("decision_date",  assemblyTypes, null);
addOrReplaceLMDDefinition("export_date",  assemblyTypes, null);
addOrReplaceLMDDefinition("supplementary_material",  assemblyTypes, null);
addOrReplaceLMDDefinition("typeset_pages",  assemblyTypes, null);
addOrReplaceLMDDefinition("online_published_date",  assemblyTypes, null);
addOrReplaceLMDDefinition("pre_published_date",  assemblyTypes, null);
addOrReplaceLMDDefinition("print_published_date",  assemblyTypes, null);
addOrReplaceLMDDefinition("print_journal_issue",  assemblyTypes, null);
addOrReplaceLMDDefinition("awaiting_author_comments",  assemblyTypes, null);
addOrReplaceLMDDefinition("awaiting_typesetter_updates",  assemblyTypes, null);
addOrReplaceLMDDefinition("typesetter_update_type",  assemblyTypes, null);
addOrReplaceLMDDefinition("received_typesetter_update",  assemblyTypes, null);
addOrReplaceLMDDefinition("available",  assemblyTypes, null);
addOrReplaceLMDDefinition("assigned",  assemblyTypes, null);
addOrReplaceLMDDefinition("inspec_required",  assemblyTypes, null);
//ISSUE LMD
addOrReplaceLMDDefinition("cover_date",  assemblyTypes, null);
addOrReplaceLMDDefinition("total_pagination",  assemblyTypes, null);
addOrReplaceLMDDefinition("issue_title",  assemblyTypes, null);
addOrReplaceLMDDefinition("inside_front_cover",  assemblyTypes, null);
addOrReplaceLMDDefinition("inside_back_cover",  assemblyTypes, null);
addOrReplaceLMDDefinition("prelims",  assemblyTypes, null);
addOrReplaceLMDDefinition("first_numbered_page",  assemblyTypes, null);
addOrReplaceLMDDefinition("last_numbered_page",  assemblyTypes, null);
addOrReplaceLMDDefinition("advert_pages",  assemblyTypes, null);
addOrReplaceLMDDefinition("issue_automated_pdf_generation_workflow",  assemblyTypes, null);



//JOURNAL LMD
addOrReplaceLMDDefinition("production_controller",  assemblyTypes, null);
addOrReplaceLMDDefinition("editorial_assistant",  assemblyTypes, null);
addOrReplaceLMDDefinition("typesetter",  assemblyTypes, null);
addOrReplaceLMDDefinition("inspec_classifier", assemblyTypes, null)
addOrReplaceLMDDefinition("journal_code",  assemblyTypes, null);
addOrReplaceLMDDefinition("issue_code",  assemblyTypes, null);
addOrReplaceLMDDefinition("journal_email",  assemblyTypes, null);
addOrReplaceLMDDefinition("open_access",  assemblyTypes, null);
addOrReplaceLMDDefinition("classifications",  assemblyTypes, null);
addOrReplaceLMDDefinition("article_notes",  assemblyTypes, null);
addOrReplaceLMDDefinition("display_article_notes_icon",  assemblyTypes, null);
addOrReplaceLMDDefinition("category_code",  assemblyTypes, null);
addOrReplaceLMDDefinition("journal_worflow_type",  assemblyTypes, null);
addOrReplaceLMDDefinition("journal_generation_type",  assemblyTypes, null);
addOrReplaceLMDDefinition("add_prefix_digital_library_delivery",  assemblyTypes, null);
addOrReplaceLMDDefinition("prefix_digital_library_delivery",  assemblyTypes, null);
addOrReplaceLMDDefinition("journal_issue_printer",  assemblyTypes, null);

addOrReplaceLMDDefinition("submission_type",  assemblyTypes, ['Transfer', 'Direct']);
addOrReplaceLMDDefinition("licence_type", assemblyTypes, ['IET','Other','CC-BY','CC-BY-ND','CC-BY-NC','CC-BY-NC-ND'])

// @MANUEL_BEGIN Comment out logout, the session will be used by other scripts.
// rsuite.logout();
// @MANUEL_END
