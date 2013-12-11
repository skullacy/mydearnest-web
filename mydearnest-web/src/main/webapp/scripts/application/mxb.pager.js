// 페이지네이트 관련 통합 자바스크립트.
// MXB.pager.init(오브젝트);
MXB.pager = {
	param: {},
	target: undefined,
	scrollmode: false,
	page: 1,
	isLoading: false,
	ajax: undefined,
	init: function (target){
		
		if (this.target) this.dispose();
		
		this.page = 1;
		this.param = {};
		
		this.target = target;
		
		if (this.target.find(".paging_list .pager").length > 0) {
			this.target.find(".paging_list").data("hasPager", true);
			this.target.find(".paging_list > *").each(function (key, item){
				if (item != target.find(".paging_list .pager")[0]) 
					$(item).remove();
			});
		}
		
		this.target.find(".pager #more").bind("click", this.pressEvent);
		this.target.find(".pager").show();
		if (this.target.attr("data-scrollable") == "true")
		{
			var $T = MXB.pager;
			$(window).bind("scroll", function () { 
				var ct = $T.target.find(".pager").offset().top - $(this).scrollTop();
				if ($(this).innerHeight() > ct && !$T.isLoading) $T.pressEvent();
			});
		}
		if (this.target.attr("data-init-param"))
		{
			var $T = MXB.pager;
			var params = $T.target.attr("data-init-param").split('&');
			for(var param in params)
			{
				var val = params[param].split('=');
				$T.param[val[0]] = val[1];
			}
		}
		//
		this.append();
		
	},
	
	dispose: function (){
		if (!this.target) return;
		$(window).unbind("scroll");
		if (this.target.attr("data-scrollable") == "true")
			$(window).unbind("scroll");
		this.target.find(".paging_list").html("");
		this.target.find(".pager #more").unbind("click");
		delete this.target;
		delete this.param;
	},
	
	putParam: function (key, value){

		var $T = this;
		
		this.page = 1;
		if((typeof key).toString().toLowerCase() == "object")
		{
			$.each(key, function (key_2, value_2){
				$T.param[key_2] = value_2;
			});
		}
		else
			this.param[key] = value;
		
		this.target.find(".paging_list").html("");
		this.target.find(".pager").show();
		this.append();
	},
	
	getParam: function (key){
		return this.param[key];
	},
	
	
	clearParam: function (){
		this.param = {};
	},
	
	pressEvent: function (e){
		var $T = MXB.pager;
		$T.page++;
		$T.append();
		if (e) e.preventDefault();
		return false;
	},
	
	append: function (){
		var $T = MXB.pager;
		$T.param.page = $T.page;
		$T.ajax = $.ajax({
			url : $T.target.attr("data-url"),
			data : $T.param,
			type : "POST",
			beforeSend: function (xhr){
				$T.isLoading = true;
				$T.target.find(".pager").addClass("loading");
			},
			success: function (data){
				var temp = "";
				$.each($T.param, function (key, val){
					if(temp != "") temp += "&";
					var enc_val = encodeURIComponent(val);
					enc_val = enc_val.replace(/%20/gi, "+");
					temp += key + "=" + enc_val;
				});
				if (temp != this.data) return;

				if($T.target)
				{
					if ($T.target.find(".paging_list").data("hasPager"))
						$(".pager").before(data);
					else 
						$T.target.find(".paging_list").append(data);
					
					$T.target.find(".paging_list").addHandle();
					$T.target.find(".pager").removeClass("loading");

					MXB.run("pager_appended", data);
					
					if ($T.target.attr("data-complete-function"))
					{
						try
						{
							eval("inAppScript." + $T.target.attr("data-complete-function") + "();");								
						}
						catch(ex)
						{
							console.log("inAppScript." + $T.target.attr("data-complete-function") + "();");
							console.log(ex);
						}
					}

					$T.isLoading = false;
					if ($T.target.find(".paging_list .paging_close").size() > 0)
					{
						$T.target.find(".pager").hide();
						$(window).unbind("scroll");
					}
				}
			}
		});
	}
	
};