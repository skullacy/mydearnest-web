
// 而⑦뀓�ㅽ듃硫붾돱 �듯빀 �먮컮�ㅽ겕由쏀듃
// MXB.listener.init();
// MXB.listener.addHandle(obj);
MXB.listener = {

	init: function (){

		$("#listener_info").bind("mouseenter", MXB.listener.lock);
		$("#listener_info").bind("mouseleave", MXB.listener.unlock);

		$("#listener_info a").unbind("click");
		$("#listener_info a").bind("click", MXB.contextMenu.itemClick);

		$("#listener_info").bind("click", function (e){

				
			var albumId = $(this).attr("data-albumid");
			var musicId = $(this).attr("data-musicid");
			
			MXB.listener.pop_view(albumId, musicId);

			e.stopPropagation();
			e.preventDefault();
			return false;
		});
		
		$("#listener_list .more").bind("click", function (){
			if($(this).hasClass("loading")) return;

			$("#listener_list .more").addClass("loading");
			
			var albumId = $("#listener_list ul").attr("data-albumid");
			var musicId = $("#listener_list ul").attr("data-musicid");
			
			var sendData = {
					dummy : Math.random() * 99999,
					page : parseInt($("#listener_list ul").attr("data-page")) + 1
			};

			if(albumId) sendData.albumId = albumId;
			else if(musicId) sendData.musicId = musicId;

			$.ajax({
				url : context_path + "listener/details/list.ajax",
				type : "GET",
				data : sendData,
				success: function (data){
				
					$("#listener_list ul").attr("data-page", sendData.page);
				
					$("#listener_list .list ul").append(data);
					$("#listener_list .list ul a").unbind("click");
					$("#listener_list .list ul a").bind("click", MXB.listener.itemClick);
					if ($("#listener_list ul li.close").size() <= 0)
						$("#listener_list .more").show();
					else
						$("#listener_list .more").hide();

					$("#listener_list .more").removeClass("loading");
				}
			});
			
		});

	},
	
	pop_view: function (albumId, musicId){

		$("#listener_list .list").hide();
		$("#listener_list .list ul").html("");
		$("#listener_list .summary").html("");
		$("#listener_list .summary").addClass("loading");
		$("#listener_list").dialog({ modal: true, resizable: false, width: 370 });
		
		$("#listener_list .list ul").removeAttr("data-page");
		$("#listener_list .list ul").removeAttr("data-albumid");
		$("#listener_list .list ul").removeAttr("data-musicid");
		$("#listener_list .list ul").attr("data-page", 0);
		$("#listener_list .list ul").attr("data-albumid", albumId);
		$("#listener_list .list ul").attr("data-musicid", musicId);
		
		var sendData = {
				dummy : Math.random() * 99999
		};
		
		if(albumId) sendData.albumId = albumId;
		else if(musicId) sendData.musicId = musicId;
		
		$.ajax({
			url : context_path + "listener/details.ajax",
			type : "GET",
			data : sendData,
			success: function (data){
				$("#listener_list .summary").removeClass("loading");
				$("#listener_list .summary").html(data);
				$("#listener_list .list").slideDown(200, function (){ 
					$("#listener_list .more").click(); 
				});
			}
		});
	},
	
	itemClick: function (e){
		$("#listener_list").dialog("destroy");
		MXB.handle.procLink($(this).attr("href"));
		e.stopPropagation();
		e.preventDefault();
		return false;
	},
	
	addHandle: function (obj){
		
		obj.bind("mouseenter", MXB.listener.objEnter);
		obj.bind("mouseleave", MXB.listener.objLeave);
		
		obj.attr("data-listener-hover", "N");
	},
	
	removeHandle: function (obj){

		obj.unbind("mouseenter");
		obj.unbind("mouseleave");

		obj.removeAttr("data-listener-hover");
		
	},
	
	isLockMode: false,
	
	lock: function (){ MXB.listener.isLockMode = true; },
	unlock: function (){ 
		MXB.listener.isLockMode = false; 
		var _T = $(this).data("target");
		if(_T && $(_T).attr("data-listener-hover") != "Y")
		{
			$(this).hide();
			$("#listener_info").data("target", undefined);
		}
	},
	
	objEnter:function (e){

		if ($(this).attr("data-disabled") == "true") return;
		
		$(this).attr("data-listener-hover", "Y");

		$("#listener_info").data("target", this);
		MXB.listener.isLockMode = false;
		
		$("#listener_info").addClass("load");
		$("#listener_info .menu_obj").html("&nbsp;");
		$("#listener_info fieldset").addClass("foot");
		$("#listener_info fieldset").removeClass("top");

		$("#listener_info").css("top", ( $(this).offset().top - $("#listener_info").height() )  + "px");
		$("#listener_info").css("left", ( $(this).offset().left - ($("#listener_info").width() / 2) + ($(this).width() / 2)) + "px");
		$("#listener_info").show();
		
		$("#listener_info").removeAttr("data-albumid");
		$("#listener_info").removeAttr("data-musicid");
		$("#listener_info").attr("data-albumid", $(this).attr("data-albumid"));
		$("#listener_info").attr("data-musicid", $(this).attr("data-musicid"));

		var albumId = $(this).attr("data-albumid");
		var musicId = $(this).attr("data-musicid");
		
		if(!albumId && !musicId)
		{
			$(this).attr("data-disabled", "true");
			$(this).unbind("mouseenter");
			$(this).unbind("mouseleave");
		}
		
		var sendData = {};
		
		if(albumId) sendData.albumId = albumId;
		else if(musicId) sendData.musicId = musicId;

		var $T = $(this);
		if ($T.data("listener_info"))
		{
			$("#listener_info").removeClass("load");
			$("#listener_info .menu_obj").html($T.data("listener_info"));
			$("#listener_info").css("top", ( $T.offset().top - $("#listener_info").height() )  + "px");
		}
		else
		{
			$.ajax({
				url: context_path + "listener/image.ajax",
				type: "POST",
				data: sendData,
				success:function (data){
					var data2 = data.replace(/\s/gi, '');
					if(data2 != "")
					{
						$T.data("listener_info", data);
						$("#listener_info").removeClass("load");
						$("#listener_info .menu_obj").html(data);
						$("#listener_info").css("top", ( $T.offset().top - $("#listener_info").height() )  + "px");
					}
					else
					{
						$T.attr("data-disabled", "true");
						$T.unbind("mouseenter");
						$T.unbind("mouseleave");
						$("#listener_info").hide();
					}
				},
				error: function (){
					$("#listener_info").hide();
				}
			});
			
		}
		
	},

	objLeave:function (e){

		$(this).attr("data-listener-hover", "N");
		
		var $T = this;
		
		setTimeout(function (){
			if (!MXB.listener.isLockMode && $("#listener_info").data("target") == $T)
			{
				$("#listener_info").hide();
				$("#listener_info").data("target", undefined);
				MXB.listener.isLockMode = false;
			}
		}, 100);

	}
	
};
