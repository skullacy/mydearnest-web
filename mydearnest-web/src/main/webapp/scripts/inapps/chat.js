
inAppScript = {
		
	selectedRooms:undefined,
	chat_last_id:undefined,
		
	run : (function (){
		$(window).bind("resize", inAppScript.resize);
		$(window).resize();
		if ($("#selectRoom").val()) inAppScript.selectedRooms = $("#selectRoom").val();
		inAppScript.getRooms();
		inAppScript.getChat();
		
		$("#frm_write").bind("submit", inAppScript.submit);
		$("textarea#content").bind("keyup", function (event) {
			if (event.which == 13) {
				$("#frm_write").submit();
			}
		});
	}),
	
	resize: (function (){
		$(".message_lst_div").css("height", ($(window).height() - 300) + "px");
	}),

	dispose: (function (){
		$("#frm_write").unbind("submit");
		$(window).unbind("resize", inAppScript.resize);
		MXB.loop.removeQue("chat");
		MXB.loop.removeQue("chat_room");
	}),
	
	getRooms: (function (){
		$("#rooms li").unbind("click");
		$("#rooms").load($("#rooms").attr("data-url"), function (){
			$("#rooms li").css("cursor", "pointer").bind("click", function () {
				inAppScript.selectedRooms = $(this).attr("data-room-flag");
				$("#rooms li").removeClass("selected");
				$(this).addClass("selected");
				inAppScript.getChat();
			});

			if (inAppScript.selectedRooms) {
				var v = inAppScript.selectedRooms;
				$("#rooms li[data-room-flag='" + v + "']").addClass("selected");
			}
			else if ($("#rooms li").size() > 0) {
				$("#rooms li:first-child").bind("click");
			}
			MXB.loop.addQue("chat_room", inAppScript.reloadRoom, 5);
		});
	}),
	
	getChat: (function () {
		if (!inAppScript.selectedRooms) {
			$("#no_chat").show();
			$("#chat_list").hide();
			return;
		}
		$("#no_chat").hide();
		$("#chat_list").show();
		$("#chat_list").load(context_path + "/mypage/message/talk.ajax", "user=" + inAppScript.selectedRooms, function (){
			$(".message_lst_div").scrollTop(99999999);
			inAppScript.chat_last_id = $("#chat_list li:last-child").attr("data-id");
			MXB.loop.addQue("chat", inAppScript.reloadData, 1);
		});
	}),
	
	submit: (function (e){
		
		if (!$("textarea#content").val().trim()) {
			alert("내용을 입력하세요.");
		}
		else {
			$.ajax({
				url: $(this).attr("action"),
				type: $(this).attr("method"),
				data: {"user": inAppScript.selectedRooms, "content": $("textarea#content").val() },
				success: function (html) {
					$("#chat_list").append(html);
					$(".message_lst_div").scrollTop(99999999);
				},
				error: function () {
					alert("서버와 연결이 올바르지 않습니다.\n잠시 후 다시 시도해주세요.");
				}
			});
		}
		
		$("textarea#content").val("");
		if (e) e.preventDefault();
		return false;
	}),
	
	reloadData: (function (){
		if (!inAppScript.chat_last_id) return;
		
		var prefId = inAppScript.chat_last_id;
		$.ajax({
			url: context_path + "/mypage/message/talk.ajax",
			data: {"user": inAppScript.selectedRooms,  "last_id" : inAppScript.chat_last_id},
			type: "POST",
			success: function (html){
				if (!html.trim()) return;
				if (prefId != inAppScript.chat_last_id) return;
				$("#chat_list").append(html);
				$(".message_lst_div").scrollTop(99999999);
				$("#chat_list li.generated").remove();
				inAppScript.chat_last_id = $("#chat_list li:last-child").attr("data-id");
			}
			
		});
		
	}),
	
	reloadRoom: (function (){
		$("#rooms").load($("#rooms").attr("data-url"), function (){
			$("#rooms li").unbind("click");
			$("#rooms li").css("cursor", "pointer").bind("click", function () {
				inAppScript.selectedRooms = $(this).attr("data-room-flag");
				$("#rooms li").removeClass("selected");
				$(this).addClass("selected");
				inAppScript.getChat();
			});

			if (inAppScript.selectedRooms) {
				var v = inAppScript.selectedRooms;
				$("#rooms li[data-room-flag='" + v + "']").addClass("selected");
			}
			else if ($("#rooms li").size() > 0) {
				$("#rooms li:first").click();
			}
		});

		if (inAppScript.selectedRooms) {
			var v = inAppScript.selectedRooms;
			$("#rooms li[data-room-flag='" + v + "']").addClass("selected");
		}
	})
	
	
		
};

if (inAppScript.run) inAppScript.run();