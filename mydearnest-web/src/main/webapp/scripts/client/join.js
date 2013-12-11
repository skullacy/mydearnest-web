parent.$("#dialog_popup .popup_frame").css({ maxWidth : "458px" });

$(document).ready(function () {
	
	if ($("input[type='checkbox']").size() > 0) $("input[type='checkbox']").prettyCheckable();
	
	$("#thumbnail").change(function () {
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
	        }
			reader.readAsDataURL(this.files[0]);			
		}
		else {
			$("#thumb_view").attr("src", $(this).val());
		}
	});
	$("#thumbnail").change();
	$("form").submit(function (e) { $(".submit").hide(); });

});