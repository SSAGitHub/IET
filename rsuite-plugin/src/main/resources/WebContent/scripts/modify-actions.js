window.onload = function()
				{
					changeLoginLogo ();
				};
                
function changeLoginLogo () {
	var logo = document.getElementsByClassName("logo")[0];
	if (typeof logo != 'undefined'){
		logo.setAttribute("src","/rsuite-cms/plugin/iet/images/IET-Logo-no-strapline.gif");
	}
}

function openLinkInNewWindow (link, title) {
	window.open(link, title, 'height=500,width=500,scrollbars=yes');
	var closeMenuAttemptInterval = setInterval(
		function(){
			this.RSuite.view.Menu.removeAll()
		},
		250
	); 
	
	setTimeout(
		function(){
			clearInterval(closeMenuAttemptInterval)
		},
		1000
	)
}

//function changeAppLogo () {
//	var applogDIV = document.getElementsByClassName("app-logo ember-view")[0];
//	if (typeof applogDIV != 'undefined'){
//		var applogo = applogDIV.getElementsByTagName("img")[0];
//		if (typeof applogDIV != 'undefined'){
//			applogo.setAttribute("src","/rsuite-cms/plugin/iet/images/IET-Logo-no-strapline.gif");
//			applogo.setAttribute("style","width: 100px; height: 20px;");
//		}
//	}
//}