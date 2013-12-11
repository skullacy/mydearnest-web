
$(document).ready(function(){
	
	$("form[data-validate='true']").find("*[data-unique-url]").change(function (){
		
		var $obj = $(this);
		var url = $(this).attr("data-unique-url").replace(/\{%s\}/gi, $(this).val());
		
		$.ajax({
			url : url,
			type : "POST",
			dataType: "JSON",
			success: function (data){
				if (!data.success)
				{
					$obj.addClass("error");
					$("p.validate[from='" + $obj.attr("name") + "']").html(data.message).show();
				}
				else
				{
					$obj.removeClass("error");
					$("p.validate[from='" + $obj.attr("name") + "']").html("").hide();
				}
				$obj.attr("data-unique-result", data.result);
				
			},
			error: function (){
				alert("error");
			}
		});
		
	});
	
	$("form[data-validate='true']").find("*[data-unique-url]").change();
	
	$(this).find("*[data-required]").change(function (){
		$(this).removeClass("error");
		$("p.validate[from='" + $(this).attr("name") + "']").html("").hide();		
	});
	
	$("form[data-validate='true']").submit(function (e){
		
		if ($("#agree_check", this).size() > 0  && $("#agree_check:checked", this).size() == 0) {
			alert("이용약관 및 개인정보보호정책에 동의하셔야 합니다.");
			e.preventDefault();
			return false;
		}
		
		var result = true;
		
		$(this).find("*[data-required]").each(function (){
			if (!$(this).val())
			{
				//빈값체크
				$(this).addClass("error");
				var msg = "not_value".parse().replace(/\{%s\}/gi, $(this).attr("data-fieldname"));
				$("p.validate[from='" + $(this).attr("name") + "']").html(msg).show();
				result = false;
			}
			
			if ($(this).attr("data-compare"))
			{
				//중복여부여야 통과
				var $target = $("#" + $(this).attr("data-compare")).val();
				if($target != $(this).val())
				{
					$(this).addClass("error");
					var msg = "not_compare".parse().replace(/\{%s\}/gi, $(this).attr("data-fieldname"));
					$("p.validate[from='" + $(this).attr("name") + "']").html(msg).show();
					result = false;				
				}
			}
			
			if ($(this).attr("data-unique-url"))
			{
				//중복체크했던 애들 확인된거면 통과
				if ($(this).attr("data-unique-result") != "true")
					result = false;
			}
		});
		
		if(!result)
		{
			e.preventDefault();
			return false;		
		}
		else
		{
			$(this).find(".send_button").hide();
		}
		
	});
	
});



