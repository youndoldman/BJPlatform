(function(){
	var rv = -1;
	if (navigator.appName == 'Microsoft Internet Explorer') {
	    var ua = navigator.userAgent;
	    var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
	    if (re.exec(ua) != null)
	        rv = parseFloat(RegExp.$1);
	}
	if (rv > -1) {
	    if (rv < 10.0) {
			alert("你的浏览器太老了，为了能正常访问网站，请使用Chrome、Firefox、IE10.0+等现代浏览器。"+rv);
			window.location.href="blank";
	    }
	}
})();