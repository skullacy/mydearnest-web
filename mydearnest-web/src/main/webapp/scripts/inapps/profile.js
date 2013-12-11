
inAppScript = {
		
	is_follwable: null,
	run : (function (){
		$("span.btn_pack[data-follow]").bind("click", inAppScript.onCurrentUserFollow);
		$("a.folder_delete").css("cursor", "pointer").bind("click", function () {
			if (confirm("정말 선택하신 폴더를 삭제하시겠습니까?\n파인드 한 이미지가 전부 삭제됩니다.")) {
				$.ajax({
					url: $(this).attr("data-url"),
					type: "POST",
					success:function () {
						alert("삭제되었습니다.");
						location.reload();
					},
					error: function () {
						alert("서버와 연결이 올바르지 않습니다.");
					}
				})
			}
		});
		
	}),

	dispose: (function (){
		// 제거시 이벤트핸들링도 싹 제거
		// 상단에 정보에 팔로우 부분 컨트롤제거
		$("span.btn_pack[data-follow]").unbind("click");
		$(".folder_delete").unbind("click");
	}),
	
	// 사용자 팔로우 시
	onCurrentUserFollow: function (e){
		if (MXB.isSigned())
		{
			$("span.btn_pack[data-follow]").hide();
			$("span.btn_pack[data-follow]").addClass("loading");
			/*
			$.ajax({
				url: $(this).attr("data-path"),
				type: "POST",
				success: function (){
					MXB.changeURI();
				},
				error: function (){
					alert("잠시 후 다시 시도해 주세요.");
					$("#cmd_follow span.btn_pack").show();
					$("#cmd_follow").removeClass("loading");
				}
				
			});*/
		}
		
		e.preventDefault();
		return false;
	},
	
	follow: {
		//사용자 팔로워/팔로잉 부분
		loaded: function (){
			$("#paging_content *[data-button]").unbind("click");
			$("#paging_content *[data-button]").bind("click", inAppScript.follow.onClick);
		},
		
		onClick: function (e){

			if (MXB.isSigned())
			{
				var $E = $(this);
				$E.hide();
				
				$.ajax({
					url: $E.attr("data-url"),
					type: "POST",
					success: function (){
						MXB.pager.putParam("a", "b");
					},
					error: function (){
						alert("잠시 후 다시 시도해 주세요.");
						$E.show();
					}
				});
				
			}
		}
		
	}
	
};

if (inAppScript.run) inAppScript.run();