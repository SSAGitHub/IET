/**
* Start a workflow process
*/


rsuite.login();

def list = rsuite.getProcessDefinitionInfos();

println "Starting."

def procDefs = [];
list.each {
   procDefs.add(it);
}

def DONE = false;

println "Starting loop"
while (!DONE) {
   int i = 1;
   for (prodDef in procDefs) {
	   println " ${i}: ${procDefs[i - 1].name}";
	   i++;
   }
   

   BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
   
   println "";
   print "\n + Enter workflow name or number or hit enter to exit:\n > ";
   
   def workflowSpecifier = reader.readLine();
   
   println "You entered: $workflowSpecifier"
   
   if ("" == workflowSpecifier || workflowSpecifier == null)
	   break;
   
   def workflowName = null;
   try {
	   index = workflowSpecifier.toInteger();
	   procDef = procDefs[index - 1];
	   workflowName = procDef.name;
   } catch ( e) {
	   workflowName = workflowSpecifier;
   }

   println "Starting workflow process for definitn \"${workflowName}\"..."
   def pId = rsuite.startProcessInstance(workflowName);
   println "Process ID = ${pId}"
   println "";
	   
}
rsuite.logout();