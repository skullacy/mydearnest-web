$(document).ready(function (){

	$('#file_upload').uploadify({
		'swf'				: context_path + '/scripts/uploadify/uploadify.swf',
		'uploader'  		: context_path + '/mdn-image/create.ajax;jsessionid=' + session_id,
		'fileObjName' 		: "filedata",
		'buttonText'		: "",
		'height'			: 21,
		'wmode'     		: 'transparent',
		'fileTypeDesc'		: "Image Files..",
		'fileTypeExts' 		: '*.jpg;*.jpeg;*.gif;*.png',
		'auto'      		: true,
		'onUploadSuccess'	: function(file, response) {
			var result = undefined;
			try { eval("result = " + response + ";"); }
			catch(e) { result = undefined; }
			if(result)
			{
				$("#imageSourceId").val(result.imageId);
				var src = context_path + "/mdn-image/thumb/" + result.imageId + "?w=60&h=60&t=crop";
				src += Math.floor(Math.random() * 100);
				$("#person_thumb").attr("src", src);
			}
		},
		'onError'     : function (event,ID,fileObj,errorObj) {
			alert(errorObj.type + ' Error: ' + errorObj.info);
		}
	});
	

	$("*[data-social='true']").click(function (){
		var width = $(this).attr("data-width");
		var height = $(this).attr("data-height");
		var left = (window.screen.width - width) / 2;
		var top = (window.screen.height - height) / 2;
		var popup = window.open($(this).attr("data-url"), "_top", 'width=' + width + ', height=' + height +', left=' + left + ', top=' + top + ', toolbar=no, location=no, directories=no, status=no, menubar=no, resizable=yes, scrollbars=no', false);
		popup.focus();
	});

	$("*[data-social='false']").click(function (){
		var mode = $(this).attr("data-action");
		$("#social-" + mode + "-id").val("");
		$("#social-" + mode + "-accessToken").val("");
		$("#social-" + mode + "-secretToken").val("");
		$("#social-" + mode + "-profile").html("");
		$("*[data-action='" + mode + "'][data-social]").hide();
		$("*[data-action='" + mode + "'][data-social='true']").show();
	});
	
	
	$("#userId").bind("change");
	
});

function socialResultJoin(mode, data, udata, data1, data2) {

	$("#userId").val(data["userId"]);
	$("#userId").change();
	$("#name").val(data["name"]);
	$("#email").val(data["email"]);
	$("input[type='radio'][value='" + data["sex"] + "']").attr("checked", "checked");

	$("#birthYear").val(data["birthYear"]);
	$("#birthMonth").val(data["birthMonth"]);
	$("#birthDay").val(data["birthDay"]);
	$("#country").val(data["country"]);
	
	if (data["imageSourceId"]) {
		$("#imageSourceId").val(data["imageSourceId"]);
		var src = context_path + "/mdn-image/thumb/" + data["imageSourceId"] + "?w=60&h=60&t=crop";
		src += Math.floor(Math.random() * 100);
		$("#person_thumb").attr("src", src);
	}

	$("#social-" + mode + "-id").val(udata);
	$("#social-" + mode + "-accessToken").val(data1);
	$("#social-" + mode + "-secretToken").val(data2);
	$("*[data-action='" + mode + "'][data-social]").hide();
	$("*[data-action='" + mode + "'][data-social='false']").show();
}


function connSocial(type, uid){

	var $P = $("div[data-type='" + type + "']");
	$P.attr("data-uid", uid);
	if(uid){
		$P.find("span.connected").show();
		$P.find("span.notfound").hide();
	}
	else{
		$P.find("span.connected").hide();
		$P.find("span.notfound").show();
	}
	
}


