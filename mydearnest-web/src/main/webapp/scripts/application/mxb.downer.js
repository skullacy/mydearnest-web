
// 다운로드 호출 관련 통합 자바스크립트.
// MXB.downer.async(songJsonArray);
MXB.downer = {
	obj: undefined,
	
	async: function (array)
	{
		var $T = MXB.downer;
		var $temp;
		try {
			$temp = $T.obj.css;
			$temp = true;
		}
		catch(ex) { $temp = false; }
		if(!$temp) $T.obj = undefined;

		delete $temp;
		console.log($T.obj);
		if(!$T.obj) // 창 안켜졌을때
			$("#downloader_async").html("");
		else if($T.obj.innerWidth <= 10)
			$("#downloader_async").html("");
		else if($T.obj.isDownloading)
		{
			alert("현재 다운로드 진행중입니다.\n완료 후 재시도해주세요.");
			$T.obj.focus();
			return;
		}
		
		for(var item in array)
			$("#downloader_async").append("<input type=\"text\" name=\"music_id\" value=\"" + array[item].songId + "\" />");
		
		$T.obj = window.open("about:blank", "downloader", "width=850, height=360, resizable=false, scrollbars=no");
		$("#downloader_async").submit();
		$T.obj.focus();
	}
	
};