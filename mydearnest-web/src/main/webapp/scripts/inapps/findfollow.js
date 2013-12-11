
inAppScript = {
	run : (function (){
		$("*[data-mode='follow_find'] > a").click(inAppScript.clickTabs);
	}),

	dispose: (function (){
		
	}),

	clickTabs: (function (){
		var $p = $(this).parent("*[data-mode='follow_find']");
		$("*[data-mode='follow_find']").removeClass("on");
		$p.addClass("on");
		$("#paging_content").attr("data-url", $p.attr("data-url"));
		$("#paging_content .paging_list").html("");
		MXB.pager.init($("#paging_content"));
	}),
	
	clickSocial: (function (o){
		var width = $(o).attr("data-width");
		var height = $(o).attr("data-height");
		var left = (window.screen.width - width) / 2;
		var top = (window.screen.height - height) / 2;
		var popup = window.open($(o).attr("data-url"), "_social", 'width=' + width + ', height=' + height +', left=' + left + ', top=' + top + ', toolbar=no, location=no, directories=no, status=no, menubar=no, resizable=yes, scrollbars=no', false);
		popup.focus();
	})
};

function socialResult() {
	MXB.pager.init($("#paging_content"));
}
if (inAppScript.run) inAppScript.run();