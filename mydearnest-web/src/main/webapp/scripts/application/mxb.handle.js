
// 핸들링관련 통합 자바스크립트.
// MXB.handle.addHandle(obj);
// MXB.handle.removeHandle(obj);
MXB.handle = {
		
	msg: {
		"no_check_music" : "선택된 곡이 없습니다.",
		"error" : "올바르지 않는 서버요청입니다. 잠시 후 다시 시도해주세요.",
		"adult_only" : "본 정보내용은 청소년유해매체물로서 정보통신망이용촉진 및 정보보호 등에 관한 법률 및 청소년보호법의 규정에 의하여 19세미만의 청소년이 이용할 수 없습니다."
	},
	
	init: function (){
		
	},
	
	onHoverElem: function(e){
		$(this).addClass($(this).attr("data-hoverclass"));
	},

	onHoverHide: function(e){
		$(this).removeClass($(this).attr("data-hoverclass"));
	},

	onClick:function (e){

		if ($(this).attr("data-init-handle") == "false") return;
		if ($(this).attr("target")) return true;
		
		var $hf = $(this).attr("href");
		if($hf && $hf.indexOf("#act:") >= 0)
		{
			$hf = $hf.substring($hf.indexOf("#act:"));
			MXB.handle.procAction($hf.substring(5), this);
		}
		else
			MXB.handle.procLink($hf);		
		
		e.stopPropagation();
		e.preventDefault();
		return false;
	},
	
	procLink:function (_h)
	{
		if ($(this).attr("target") == "_blank") return true;
		if(!_h || _h.substring(0, 1) == "#") return false;
		if(_h.length > 10 && _h.substring(0, 11) == "javascript:") return true;
		if(_h.substring(0, 7) == "http://" || _h.substring(0, 8) == "https://") 
			_h = _h.substring(_h.indexOf("/", 9));
		$.address.path(_h);
		return false;
	},

	procAction:function (actionName, obj){

		if ($(obj).attr("data-disabled") == "true") return false;
		
		try
		{
			switch(actionName){

			case "add-facebook": //페이스북에 올리기(한곡만)
				
				var dt = $(obj).parents("*[data-musicinfo]");
				var temp = undefined;
				if(dt.size() > 0) eval("temp = " + dt.attr("data-musicinfo") + ";");
				
				var url = "https://www.facebook.com/sharer/sharer.php?u=";
				url += encodeURIComponent("http://" + location.host + context_path);
				url += encodeURIComponent("music/" + temp.songId + ".fb");
				window.open(url, "facebook", "width=660, height=360, scrollbars=no, resizeable=no");
				
				break;
				

			case "download": // 다운로드 클릭시 (한곡만)
				if(!MXB.isSigned()) break;
				var dt = $(obj).parents("*[data-musicinfo]");
				if(dt.size() > 0)
				{
					eval("var obj = [" + dt.attr("data-musicinfo") + "];");
					MXB.downer.async(obj);
				}

				break;

			case "download-for-checked": // 다운로드 클릭시 (선택된 항목 가져다가)
				if(!MXB.isSigned()) break;
				var dt = $("fieldset.checklist").find("td input[type='checkbox']:checked");
				var dt2 = $("fieldset.checklist").find("li input[type='checkbox']:checked");
				if(dt.size() < dt2.size()) dt = dt2;
				if(dt.size() == 0)
					alert(MXB.handle.msg.no_check_music);
				else
				{
					var music_array = [];
					dt.each(function (){ eval("music_array.push(" + $(this).parents("*[data-musicinfo]").attr("data-musicinfo") + ");"); });
					MXB.downer.async(music_array);
				}

				break;
				
			default :
				MXB.run(actionName, null);
				
			}
			
		}
		catch(e)
		{
			alert(MXB.handle.msg.error);
			console.log(e);
		}
	}
	
};