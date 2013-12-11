var contextPath = "";

String.prototype.trim = function() {
	return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
};

var printObj = typeof JSON != "undefined" ? JSON.stringify : function(obj) {
	var arr = [];
  $.each(obj, function(key, val) {
    var next = key + ": ";
    next += $.isPlainObject(val) ? printObj(val) : val;
    arr.push( next );
  });
  return "{ " +  arr.join(", ") + " }";
};
 
$(document).ready(function (){
	var tpl = ($("#folderTpl").val().replace(/%s/, "folderId").replace(/%s/, "folderId"));
	$("#folder_view").html(tpl);
	$("#folderId").selectbox();
	
	$("#dialog_folder form").submit(function (e) {
		
		if(!$("input[name='name']", this).val()) {
			alert("폴더이름을 입력해주세요.");
			e.preventDefault();
			return false;
		}

		$("#dialog_folder .btn_set .btn_pack").hide();
		$("#dialog_folder .btn_set .loading").show();
		
		$.ajax({
			url: $(this).attr("action"),
			type: $(this).attr("method"),
			dataType: "json",
			data: $(this).serialize(),
			success:function (data) {
				$("#folder_view").html("");
				$("#folder_view").html($("#folderTpl").val());
				$("#folder_view select option:eq(0)").after("<option value=\"" + data.folder_id + "\" selected=\"selected\">" + data.folder_name + "</option>");
				$("#folder_view select").selectbox().change();

				$("#dialog_folder .btn_set .btn_pack").show();
				$("#dialog_folder .btn_set .loading").hide();
				$("#dialog_folder").dialog("destroy");
				
				$("#dialog_folder input[type='text']").val("");
				$("#dialog_folder textarea").val("");
			}
		});
		
		e.preventDefault();
		return false;
	});
	
	$("#thumbnail").change(function (){
		if (!$(this).val()) return;
		var ext = $(this).val().substring($(this).val().lastIndexOf(".") + 1).toLowerCase();
		if (ext != "jpg" && ext != "gif" && ext != "png" && ext != "jpeg") {
			alert("이미지 파일이 아닙니다.");
			return;
		}
		
		if ($(this).val().indexOf("fake") > 0) {
			var reader = new FileReader();
	        reader.onload = function (e) {
	            $("#thumb_view").attr("src", e.target.result);
				$("#thumb_view").load();
	        }
			reader.readAsDataURL(this.files[0]);			
		}
		else {
			$("#thumb_view").attr("src", $(this).val());
			$("#thumb_view").load();
		}
		
		$("#thumb_button").hide();
	});
	
	$("#thumb_view").load(function () {
		$("#thumb_view").css({"maxWidth" : "auto", "maxHeight" : "auto"});
		if ($("#thumb_view").width() > $("#thumb_view").height()) {
			$("#thumb_view").css({"maxWidth" : "100%"});
		}
		else {
			$("#thumb_view").css({"maxHeight" : "100%"});
		}
		$("#thumb_view").css({
			marginTop: -($("#thumb_view").height() / 2) + "px",
			marginLeft: -($("#thumb_view").width() / 2) + "px"
		});
		$("#thumb_view").css("display", "block");
	});
	
	$("#thumbnail").change();
	$(".selectbox").selectbox({ effect: "slide", speed: 200 });
	
	$(window).resize(function () {
		if ($("#thumb_case").height() > 0)
			$(".thumb_container").css({ "height" : $("#thumb_case").height() });
		$("#thumb_view").load();
	});
	$(window).resize();
});

