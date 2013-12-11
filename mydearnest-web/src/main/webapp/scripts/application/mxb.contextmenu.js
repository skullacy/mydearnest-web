
// 컨텍스트메뉴 통합 자바스크립트
// MXB.contextMenu.init();
// MXB.contextMenu.addHandle(obj);
MXB.contextMenu = {
		
	init: function (){
		
		$("#overmenu").bind("mouseenter", MXB.contextMenu.lock);
		$("#overmenu").bind("mouseleave", MXB.contextMenu.unlock);

		$("#overmenu a").unbind("click");
		$("#overmenu a").bind("click", MXB.contextMenu.itemClick);
		
	},
	
	itemClick: function (e){
		
		var $hf = $(this).attr("href");
		if($hf && $hf.indexOf("#act:") >= 0)
		{
			$hf = $hf.substring($hf.indexOf("#act:"));
			MXB.handle.procAction($hf.substring(5), $("#overmenu").data("target"));
		}
		else
			MXB.handle.procLink($hf);		
		
		e.stopPropagation();
		e.preventDefault();
		return false;
	},
	
	addHandle: function (obj){
		
		obj.bind("mouseenter", MXB.contextMenu.objEnter);
		obj.bind("mouseleave", MXB.contextMenu.objLeave);
		
		obj.attr("data-contextmenu-hover", "N");
	},
	
	removeHandle: function (obj){

		obj.unbind("mouseenter");
		obj.unbind("mouseleave");

		obj.removeAttr("data-contextmenu-hover");
		
	},
	
	isLockMode: false,
	
	lock: function (){ MXB.contextMenu.isLockMode = true; },
	unlock: function (){ 
		
		if($("#listened_list").attr("data-in-mouse") == "N")
			$("#listened_list li").removeClass("hover");
			
		MXB.contextMenu.isLockMode = false; 
		var _T = $(this).data("target");
		if(_T && $(_T).attr("data-contextmenu-hover") != "Y")
		{
			$(this).hide();
			$("#overmenu").data("target", undefined);
		}
	},
	
	objEnter:function (e){

		$(this).attr("data-contextmenu-hover", "Y");

		$("#overmenu").data("target", this);
		MXB.contextMenu.isLockMode = false;
		
		var t_gap = 5;
		if ($(this).attr("data-gap")) t_gap = parseInt($(this).attr("data-gap"));
		
		$("#overmenu li").hide();
		var items = $(this).attr("data-item").split("|");
		$.each(items, function(){ $("#overmenu li[ref='" + this + "']").show(); });
		
		var mouseY = (e.pageY - window.pageYOffset);
		if((mouseY + $("#overmenu").height() + 50) > self.innerHeight)
		{
			$("#overmenu fieldset").addClass("foot");
			$("#overmenu fieldset").removeClass("top");

			$("#overmenu").css("top", ($(this).offset().top -($("#overmenu").height() + $(this).height() - t_gap))  + "px");
			$("#overmenu").css("left", ($(this).offset().left - ($("#overmenu").width() / 2) + ($(this).width() / 2)) + "px");
		}
		else
		{
			$("#overmenu fieldset").removeClass("foot");
			$("#overmenu fieldset").addClass("top");

			$("#overmenu").css("top", ($(this).offset().top + $(this).height() - t_gap)  + "px");
			$("#overmenu").css("left", ($(this).offset().left - ($("#overmenu").width() / 2) + ($(this).width() / 2)) + "px");
		}
		
		$("#overmenu").show();
		
	},

	objLeave:function (e){

		$(this).attr("data-contextmenu-hover", "N");
		
		var $T = this;
		
		setTimeout(function (){
			if (!MXB.contextMenu.isLockMode && $("#overmenu").data("target") == $T)
			{
				$("#overmenu").hide();
				$("#overmenu").data("target", undefined);
				MXB.contextMenu.isLockMode = false;
			}
		}, 100);

	}
	
};
