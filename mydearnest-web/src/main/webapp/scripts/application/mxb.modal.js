
// 모달폼 관련 통합 자바스크립트.
MXB.modal = {

	login: function ()
	{
		if (is_signed) return true;
		$("#dialog_login").dialog({ 
			width: 738, 
			resizable: false, 
			modal: true,
			beforeClose:function (){ } 
		});
	},

	loginByRequired: function ()
	{
		if (is_signed) return true;
		$("#dialog_login").dialog({ 
			width: 738, 
			resizable: false, 
			modal: true, 
			beforeClose:function (){
				$.address.value(context_path + "/index");
			} 
		});
	},
	
	help: function (){
		if (is_signed) return true;
		$("#dialog_login").dialog("destroy");
		var $F = $("#dialog_help").find("iframe");
		$F.attr("src", $F.attr("data-url"));
		$("#dialog_help").dialog({
			width: 652,
			resizable: false,
			modal: true
		});
		return false;
	},
	
	additem: function () {
		if (!is_signed) return true;
		$("#dialog_additem").dialog({
			width: 716,
			resizable: false,
			modal: true
		});
		return false;
	}
	
	
};
$(document).ready(function (){
	if ($("#dialog_additem").size() <= 0) return;
	$("#dialog_additem li[data-mode]").click(function () {
		$("#dialog_additem").dialog("close");
		if($(this).attr("data-mode") == "button") {
			$.address.value(context_path + "/about/button");
		}
		else if($(this).attr("data-mode") == "new") {
			var c = context_path + "/add-item/browse";
			var g = 540;
		    /Safari/.test(navigator.userAgent) && !/Chrome/.test(navigator.userAgent) && (g = 540);
			window.open(c, 
					"pinsbox", 
					"status=no,resizable=yes,scrollbars=yes,personalbar=no,directories=no,location=no,toolbar=no,menubar=no,width=880,height=" + g + ",top=" + (screen.height - 540) / 2 + ",left=" + (screen.width - 880) / 2)
					.focus();
		}
	});
});	