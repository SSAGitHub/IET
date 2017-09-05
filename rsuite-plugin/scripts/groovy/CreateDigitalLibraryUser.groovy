rsuite.login();
rsuite.createLocalUser("DigitalLibrary", "test", "DigitalLibrary", "test.the.iet@gmail.com", "DigitalLibrary");
def properties = [
	"ftp.host" :"vpn.reallysi.com", 
	"ftp.user" :"harv", 
	"ftp.password" :"t1m34w1n3",
	"ftp.folder" :"DigitalLibrary",
];
rsuite.setUserProperties("DigitalLibrary", true, properties);
rsuite.logout();