// 앱 페이지 핸들링 관련 클래스
var appHandle = {
	//초기화.. 여기저기서 호출할때 쓰는부분.
	set:function (jObject){	
		$(jObject).find("a").unbind("click");
		$(jObject).find("a").bind("click", MXB.handle.onClick);
		
		if (MXB.listener)		MXB.listener.addHandle($(jObject).find("*[data-listener]"));
		if (MXB.contextMenu)	MXB.contextMenu.addHandle($(jObject).find("span[data-menu='true']"));

		$(jObject).find("iframe").bind("load", function (e){
			$(this).animate({"height": $(this.contentWindow.document.body).height() + "px"}, 200);
		});
		$(jObject).find("iframe").bind("load");
		
		$(jObject).find("*[data-post-delete]").bind("click", function (e){
			if (!confirm("삭제하시겠습니까?")) return;

			var elem = $(this);
			elem.parents(".find_item").css("opacity", "0.5");
			$.ajax({
				url : context_path + "/post/" + elem.attr("data-post-delete") + "/delete",
				data : { drawer_id : elem.attr("data-post-drawer"), mode : elem.attr("data-edit-mode") },
				type : "POST",
				dataType: "json",
				success: function (data){
					alert("삭제되었습니다.");
					elem.parents(".find_item").remove();
					if(inAppScript) inAppScript.item_loaded();
				},
				error: function () {
					alert("서버와 연결이 원할하지 않습니다.\n잠시 후 다시 시도해주세요.");
					elem.parents(".find_item").css("opacity", "1");
				}
			});
			e.preventDefault();
			return false;
		});
		
		$(jObject).find("*[data-mode='love'] a").bind("click", function (e){
			if (!MXB.isSigned()) {
				e.preventDefault();
				return false;
			}
			var elem = $(this).parent("*[data-mode='love']");
			var type = elem.attr("data-value");
			elem.hide();
			$.ajax({
				url : context_path + "/show/" + elem.attr("data-heart") + "/love",
				data : { "mode" : elem.attr("data-value") },
				type : "POST",
				dataType: "json",
				success: function (data){

					elem.parent().find("*[data-mode='love']").show();
					elem.hide();
				},
				error: function () {
					alert("다시 시도해 주세요.");
					elem.parent().find("*[data-mode='love']").hide();
					elem.show();
				}
			});
			e.preventDefault();
			return false;
		});

		$(jObject).find("*[data-mode='warn'] a").bind("click", function (e){
			if (!MXB.isSigned()) {
				e.preventDefault();
				return false;
			}
			var elem = $(this).parent("*[data-mode='warn']");
			var type = elem.attr("data-value");
			elem.hide();
			$.ajax({
				url : context_path + "/show/" + elem.attr("data-heart") + "/warning",
				data : { "mode" : elem.attr("data-value") },
				type : "POST",
				dataType: "json",
				success: function (data){
					if (elem.attr("data-value") == "yes") 
						alert("신고되었습니다.");
					else 
						alert("취소되었습니다.")
					elem.parent().find("*[data-mode='warn']").show();
					elem.hide();
				},
				error: function () {
					alert("다시 시도해 주세요.");
					elem.parent().find("*[data-mode='warn']").hide();
					elem.show();
				}
			});
			e.preventDefault();
			return false;
		});
		
		$(jObject).find("*[data-mode='follow'] a").bind("click", function (e){
			if (!MXB.isSigned()) {
				e.preventDefault();
				return false;
			}
			var elem = $(this).parents("*[data-mode='follow']");
			var type = elem.attr("data-value");
			elem.hide();
			$.ajax({
				url : context_path + "/profile/" + elem.attr("data-user") + "/follow.ajax",
				data : { "mode" : elem.attr("data-value") },
				type : "POST",
				dataType: "json",
				success: function (data){
					elem.parent().find("*[data-mode='follow']").show();
					elem.hide();
				},
				error: function () {
					alert("다시 시도해 주세요.");
					elem.parent().find("*[data-mode='follow']").hide();
					elem.show();
				}
			});
			e.preventDefault();
			return false;
		});
		
	},
	
	unset:function (jObject){	
		$(jObject).find("a").unbind("click");
		$(jObject).find("*[data-listener]").unbind("mouseenter");
		$(jObject).find("*[data-listener]").unbind("mouseleave");
		if (MXB.contextMenu) MXB.contextMenu.removeHandle($(jObject).find("span[data-menu='true']"));

		$(jObject).find("[data-mode='love'] a").unbind("click");
		
	}
};

var inAppScript;
var refresh_mode = false;
var MXB = {
	lastPath: "",
	events: undefined,
	bind: function (id, func)
	{ 
		if (func) 
		{ 
			if (!MXB.events) MXB.events = {}; 
			MXB.events[id] = func; 
		} 
		else 
			MXB.events[id](); 
	},
	run: function (id, args)
	{
		if (MXB.events[id]) MXB.events[id](args); 
	},
	unbind: function (id)
	{ 
		MXB.events[id] = undefined; 
		delete MXB.events[id]; 
	},
		
	init: function (){
		$.address.init().change(this.changeURI);

		if (this.contextMenu) this.contextMenu.init();
		if (this.listener) this.listener.init();
		if (this.loop)
		{
			this.loop.init();
			this.loop.addQue("dateResolver", function (){

				$("*[data-created-at]").each(function (){
					var dt = {};
					dt.y = parseInt($(this).attr("data-created-at").substring(0, 4), 10);
					dt.m = parseInt($(this).attr("data-created-at").substring(5, 7), 10) - 1;
					dt.d = parseInt($(this).attr("data-created-at").substring(8, 10), 10);
					dt.h = parseInt($(this).attr("data-created-at").substring(11, 13), 10);
					dt.i = parseInt($(this).attr("data-created-at").substring(14, 16), 10);
					dt.s = parseInt($(this).attr("data-created-at").substring(17, 19), 10);
					
					var myDate = new Date(dt.y, dt.m, dt.d, dt.h, dt.i, dt.s);
					$(this).find(".realtime").html(myDate.shortFormat("-"));
				});
				
			}, 30);
		}
	},
	
	isSigned:function ()
	{
		if (is_signed) return true;
		return MXB.modal.login();
	},
	
	procScript: function (href){

		if (inAppScript)
		{
			if (inAppScript.dispose) inAppScript.dispose();
			inAppScript = undefined;
			$("#mxb-jssdk").remove();
		}
		
		var d = document;
		var js, id = 'mxb-jssdk'; if (d.getElementById(id)) {return;}
		js = d.createElement('script'); js.id = id; js.async = true;
		js.src = context_path + "/scripts/inapps/" + href;
		d.getElementsByTagName('head')[0].appendChild(js);
	},
	
	resizeWindow: function (){
		$("#dialog_popup").dialog({ position: "center" });

		var mObject = $("#dialog_popup .move_object");
		if (mObject.size() > 0) {
			if (parseInt($("#dialog_popup").height()) < parseInt($("body").height()))
				mObject.height($("#dialog_popup").height());
			else
				mObject.height(parseInt($("body").height()) - 20);
		}
	},
	
	reload: function () {
		
	},
	
	changeURI: function (e, p_ign){
		if (refresh_mode) {
			location.reload();
			return;
		}
		
		var $address_val = (e != null) ? e.path : $.address.value();
		var $address_data = { "dummy" : Math.floor(Math.random() * 9999999999) };
		if (e != null)
			$.extend($address_data, e.parameters);
		
		$.ajax({
			type: "GET",
			headers: { "context" : "application/mxb-xhtml" },
			data: $address_data,
			url: $address_val,
			traditional: true,
			beforeSend: function (xhr){
				$("#loading").show();
			},
			success: function (data, textStatus, jqXHR){
				if ($address_val != this.url.substring(0, this.url.indexOf("dummy=") - 1) && !MXB.reloadMode) return;
				try {
					if (getHeader(jqXHR, "mxb-popup-width")) {

						if (!MXB.lastPath) {
							MXB.lastPath = context_path + "/index";
							MXB.changeURI({"path": context_path + "/index"}, true);
						}
						
						var p_pos = "center";
						if ($("#body").width() < 780) p_pos = "center top";
						
						$("#body").css("top", "-" + $(document).scrollTop() + "px").addClass("scroll_lock");
						$("#dialog_popup").html(data).data("beforePage", MXB.lastPath).dialog({
							width: getHeader(jqXHR, "mxb-popup-width"),
							modal: true,
							position: p_pos,
							resizable: false,
							open: function( event, ui ) {
								if ($("#body").width() < 780) $(event.target).parent().css("top", "0px");
							},
							beforeClose: function (){
								$.address.value($(this).data("beforePage"));
								$(this).removeData("beforePage");
							}
						});
						
						$("#dialog_popup").addHandle();

						$(".ui-widget-mxb-close").bind('click', function(){
						   $(".ui-dialog-titlebar-close").trigger('click');
						});
						$(".ui-widget-overlay").bind('click', function(){
						   $(".ui-dialog-titlebar-close").trigger('click');
						});

						$(window).bind("resize", MXB.resizeWindow);
						$("#dialog_popup .thumb img.valignimg").load(function (){
							$(this).show();
							$(this).parent().removeClass("min_image");
//							$("div.ui-widget-overlay.ui-front").height($(document).height());
							$(window).scrollTop(0);
						});
						
						var mObject = $("#dialog_popup .move_object");
						if (mObject.size() > 0) {
							mObject.width(mObject.parent().outerWidth() - 1);
							if (parseInt($("#dialog_popup").height()) < parseInt($("body").height())) {
								var comm_height = ($("#dialog_popup").height() - mObject.height() - 5);
								if ($(".comment_area", mObject).size() > 0) {
									$(".comment_area", mObject).height(comm_height).show();
								}
								mObject.height($("#dialog_popup").height());
							}
							else {
								var comm_height = (parseInt($("body").height()) - 20 - mObject.height());
								if ($(".comment_area", mObject).size() > 0) {
									$(".comment_area", mObject).height(comm_height).show();
								}
								mObject.height(parseInt($("body").height()) - 20);
							}
						}
						
						$("#dialog_popup form").submit(function (e){
							if (!$("#dialog_popup form textarea").val()) {
								alert('내용을 입력하세요');
								e.preventDefault();
								return false;
							}
							
							$.ajax({
								url: $(this).attr("action"),
								type: $(this).attr("method"),
								data: $(this).serialize(),
								success: function (data, textStatus, jqXHR){
									$("#dialog_popup form textarea").val("");
									$("#dialog_popup .comment_area ul").prepend(data);
								}
							});
							
							e.preventDefault();
							return false;
						});
						
						if ($("#dialog_popup .comment_area").size() > 0) {
							var comm = $("#dialog_popup .comment_area");
							$(".comment_pager", comm).click(function (){
								$(this).hide();
								$.ajax({
									url: comm.attr("data-url"),
									type: "POST",
									data: {"page" : comm.attr("data-page")},
									success:function (data) {
										var pg = parseInt(comm.attr("data-page") + 1);
										comm.attr("data-page", pg);
										$("ul .comment_pager").before(data);
										if ($("ul .comment_notfound").size() <= 0) 
											$(".comment_pager", comm).show();
									}
								})
							});
							$(".comment_pager", comm).click();
						}
						
						popup_find();
						
					}
					else if ($("#content_body").data("currentPage") != $address_val || MXB.reloadMode) {
						MXB.reloadMode = undefined;

						if (inAppScript)
						{
							if (inAppScript.dispose) inAppScript.dispose();
							inAppScript = undefined;
							$("#mxb-jssdk").remove();
						}
						
						if(!p_ign) {
							if ($("#dialog_popup").css("display") != "none")
								$("#dialog_popup").html("").dialog("destroy");
							var t = $("#body").css("top");
							$(".ui-widget-overlay").unbind('click');
							$("#body").css("top", "").removeClass("scroll_lock");
							$(document).scrollTop(t.substring(1).replace("px", ""));
							$(window).unbind("resize", MXB.resizeWindow);
						}

						$("#content_body").data("currentPage", $address_val);
						$(window).scrollTop(0);
						$("#content_body").html(data);
						$("#content_body").addHandle(data);
						
						$("#content_body .scrollable").scrollable();

						if ($("#content_body").find("*[data-paging]").size() > 0)
							MXB.pager.init($("#content_body").find("*[data-paging]"));
						else
							MXB.pager.dispose();
	
						$("#content_body th input[type='checkbox']").click(function (){
							if($(this).attr("checked") == undefined)
								$(this).parents("table").find("td input[type='checkbox']:enabled").attr("checked", false);
							else
								$(this).parents("table").find("td input[type='checkbox']:enabled").attr("checked", $(this).attr("checked"));
						});

						MXB.lastPath = $address_val;
				
					}
					else if(!p_ign) {
						$(".ui-widget-overlay").unbind('click');
						$("#dialog_popup").html("").dialog("destroy");
						var t = $("#body").css("top");
						$("#body").css("top", "").removeClass("scroll_lock");
						$(document).scrollTop(t.substring(1).replace("px", ""));
						$(window).unbind("resize", MXB.resizeWindow);

						MXB.lastPath = $address_val;
					}
					
					MXB.run("page_ready", jqXHR);
				
				}
				catch(ex)
				{
					console.log(ex);
				}
				
				$("#loading").hide();
				
			},
			error: function (jqXHR, textStatus, errorThrown){
//				alert("서버와 통신이 올바르지 않습니다. 잠시 후 다시 시도해주세요.");
//				history.back();
			}
		});
		
	}
	
};

function popup_find() {
	var curr_id = $("#dialog_popup *[data-post-id-value]").attr("data-post-id-value");
	if (!curr_id) return;
	
	if (!$("#container *[data-post-id='" + curr_id + "']").size()) {
		setTimeout(popup_find, 100);
		return;
	}
	
	var $T = $("#container *[data-post-id='" + curr_id + "']:eq(0)");
	if ($T.prev().get(0)) {
		$("#dialog_popup *[data-view-button='left']").show();
		$("#dialog_popup *[data-view-button='left'] a").click(function () {
			$.address.value(context_path + "/show/" + $T.prev().attr("data-post-id"));
		});
	}
	if ($T.next().get(0)) {
		if(!$T.next().attr("data-post-id")) {
			$T.next().bind("click");
			setTimeout(popup_find, 2000);
		}
		else {
			$("#dialog_popup *[data-view-button='right']").show();
			$("#dialog_popup *[data-view-button='right'] a").click(function () {
				$.address.value(context_path + "/show/" + $T.next().attr("data-post-id"));
			});
		}
	}
	
}


(function ($){
	
	$(document).bind("ready", function (){
		MXB.init();	
		$(document).unbind("ready");
	});

	$.fn.addHandle = function(html) { appHandle.set(this); }; 
	$.fn.removeHandle = function(html) { appHandle.unset(this); }; 
	$.error = console.error;

})(jQuery);

