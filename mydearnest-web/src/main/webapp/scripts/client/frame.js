var isMaximal = undefined;
var statusLastId = -1;
var currentMode = "";

function refind_form(id) {
	var c = context_path + "/add-item/refind?id=" + id;
	var g = 540;
    /Safari/.test(navigator.userAgent) && !/Chrome/.test(navigator.userAgent) && (g = 540);
	var addform = window.open(c, 
			"pinsbox", 
			"status=no,resizable=yes,scrollbars=yes,personalbar=no,directories=no,location=no,toolbar=no,menubar=no,width=880,height=" + g + ",top=" + (screen.height - 540) / 2 + ",left=" + (screen.width - 880) / 2);
	
	addform.focus();
	setTimeout(addform.focus, 1000);
}


shortcut.add("F5",function() { MXB.changeURI(); });
MXB.bind("page_ready", function (jqXHR){

//	$(".global_nb li").removeClass("selected");
//	$(".global_nb li.menu0" + getHeader(jqXHR, "mxb-menu_status")).addClass("selected");				

	$("#header").removeClass("lock");
});

function getCookie(cName) {
    cName = cName + '=';
    var cookieData = document.cookie;
    var start = cookieData.indexOf(cName);
    var cValue = '';
    if(start != -1){
         start += cName.length;
         var end = cookieData.indexOf(';', start);
         if(end == -1)end = cookieData.length;
         cValue = cookieData.substring(start, end);
    }
    return unescape(cValue);
}

function setCookie(cName, cValue){
    var expire = new Date();
    expire.setDate(expire.getDate() + 365);
    cookies = cName + '=' + escape(cValue) + '; path=/ '; // 한글 깨짐을 막기위해 escape(cValue)를 합니다.
    if(typeof cDay != 'undefined') cookies += ';expires=' + expire.toGMTString() + ';';
    document.cookie = cookies;
}

$(document).ready(function (){

	$(window).resize();
	$("#status_command li[data-mode]").click(function (){
		if ($(this).attr("data-required-login") == "true" && !is_signed) {
			MXB.modal.login();
			return;
		}
		wallSettings($(this).attr("data-mode"));
		$("#status_command li[data-mode]").removeClass("on");
		$(this).addClass("on");
	});
	
	$("#status_command li[data-action]").click(function (){
		if (getCookie("status-folding") != "NO") {
			setCookie("status-folding", "NO");
			$(this).removeClass("on");
		}
		else {
			setCookie("status-folding", "YES");
			$(this).addClass("on");
		}
		$(window).resize();
	});
	
});
