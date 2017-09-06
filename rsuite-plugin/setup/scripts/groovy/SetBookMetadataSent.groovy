query = "/rs_ca_map/rs_ca[rmd:get-type(.) = 'book']";
rsuite.login();
count = 0;
searchId = rsuite.startSearch("xpath", query).searchId;
results = rsuite.fetchSearchResults(searchId, (long)1, (long)5000);
results.each {	
	println count++ + "\t" + it.id + "\t" + it.displayName;
	try {
		rsuite.addMetaData(it.id, "book_metadata_sent", "no");
		println "\tSet LMD";
	}
	catch (e) {
		println "Exception " + e;
	}
}
rsuite.logout();