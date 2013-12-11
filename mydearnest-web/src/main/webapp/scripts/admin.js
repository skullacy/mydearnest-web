var contextPath = "";
$(document).ready(function (){
	$("select.role_update").change(function () {
		var $T  =$(this);
		$(this).attr("disabled", "disabled");
		$.ajax({
			url: contextPath + "/admin/member/updateRole",
			data: { "userId" : $T.attr("data-userid"), "role" : $T.val() },
			success:function () {
				alert("저장이 완료되었습니다.");
				$T.removeAttr("disabled");
			},
			error: function () {
				alert("잠시 후 다시 시도해 주세요.");
				$T.removeAttr("disabled");
			}
		});
	});
	
});