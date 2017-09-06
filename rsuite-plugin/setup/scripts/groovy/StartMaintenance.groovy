/**
* Start a workflow process
*/

class Fix {
    def webserviceId
    def description
    Fix(webserviceId,description) {
        this.webserviceId = webserviceId
        this.description = description
    }
}

def fixes = [];
fixes.add(new Fix ("iet.maintenance.iet356" , "IET 356: Duplicated Metadata"));
fixes.add(new Fix ("iet.maintenance.iet468" , "IET 468: Fix corrupted articles MOs"));



 int i = 1;
fixes.each {		
		def wsId = it.webserviceId;
		def desc = it.description;
				
		println " ${i}: ${ it.description}";
	   i++;
		}
		
	  
 BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
   
   println "";
   print "\n + Enter fix number or hit enter to exit:\n > ";
   
   def fixSpecifier = reader.readLine();
   
   
 
  	index = fixSpecifier.toInteger();
	fix = fixes[index - 1];
   
   if ("" == fixSpecifier || fixSpecifier == null || fix == null){
   		println "incorrect number has been provided: ${}";
	    System.exit(-1);
   }
  
   
   println "You entered: $fixSpecifier - ${fix.description}"


rsuite.login();

	def sessionKey = rsuite.getSessionKey();
	def repUrl = rsuite.getRepositoryUri();
	

	//def encodedQuery = URLEncoder.encode(statusQuery.replace("^status^", statusValue), "utf-8");

	def rsuiteURL = "http://" + repUrl + "/rsuite/rest/v1/api/" + fix.webserviceId + "?skey=" + sessionKey	;

	 println " + [DEBUG] rsuiteURl=\"" + rsuiteURL + "\""

	def url = new URL(rsuiteURL);
	def connection = url.openConnection();
	connection.setReadTimeout(1000 * 60 * 60);
	def content = connection.getContent();
	
	println "RESULT:"
	println "${content}"

rsuite.logout();