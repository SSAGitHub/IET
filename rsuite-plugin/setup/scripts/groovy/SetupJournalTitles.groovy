import com.reallysi.rsuite.remote.api.RSuiteMapList

def journalMapping = [EL:'Electron. Lett.',
	MNL:'Micro Nano Lett.',
	HTL:'Healthcare Technology Letters',
	ETR:'Eng. Technol. Ref.',
	JOE:'J Eng',
	BMT:'IET Biom.',
	CDS:'IET Circuits Devices Syst.',
	CDT:'IET Comput. Digit. Tech.',
	COM:'IET Commun.',
	CTA:'IET Control Theory Appl.',
	CVI:'IET Comput. Vis.',
	EPA:'IET Electr. Power Appl.',
	EST:'IET Electr. Syst. Transp.',
	GTD:'IET Gener. Transm. Distrib.',
	IFS:'IET Inf. Secur.',
	IPR:'IET Image Process.',
	ITS:'IET Intell. Transp. Syst.',
	MAP:'IET Microw. Antennas Propag.',
	NBT:'IET Nanobiotechnol.',
	NET:'IET Netw.',
	OPT:'IET Optoelectron.',
	PEL:'IET Power Electron.',
	RPG:'IET Renew. Power Gener.',
	RSN:'IET Radar Sonar Navig.',
	SEN:'IET Soft.',
	SMT:'IET Sci. Meas. Technol.',
	SPR:'IET Signal Process.',
	SYB:'IET Syst. Biol. ',
	WSS:'IET Wirel. Sens. Syst.',
	HVE:'High Volt.']

query = "/rs_ca_map/rs_ca[rmd:get-type(.) = 'journal']";
rsuite.login();

searchId = rsuite.startSearch("xpath", query).searchId;
results = rsuite.fetchSearchResults(searchId, (long)1, (long)5000);

def getJournalCode(RSuiteMapList mapList) {
	for (int i = 0; i < mapList.size(); i++) {
		def lmdName = mapList.get(i).get("name")

		if (lmdName == "journal_code") {
			return mapList.get(i).get("value")
		}
	}
}

results.each {
	println "\t" + it.id + "\t" + it.displayName;

	def mapList = rsuite.getMetaData(it.id);
	def journalCode = getJournalCode(mapList)
	def journalAbbrvTitle = journalMapping[journalCode]

	if (journalCode == "ELL") {
		rsuite.addMetaData(it.id, "prefix_digital_library_delivery", "EL");
	}
	
	if (journalAbbrvTitle != null) {
		println "Setting lmd val: " +journalAbbrvTitle
		rsuite.addMetaData(it.id, "journal_abbrv_title", journalAbbrvTitle);
	}
}
rsuite.logout();