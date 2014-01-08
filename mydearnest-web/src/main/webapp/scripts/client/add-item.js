var contextPath = "";

String.prototype.trim = function() {
	return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
};

var printObj = typeof JSON != "undefined" ? JSON.stringify : function(obj) {
	var arr = [];
  $.each(obj, function(key, val) {
    var next = key + ": ";
    next += $.isPlainObject(val) ? printObj(val) : val;
    arr.push( next );
  });
  return "{ " +  arr.join(", ") + " }";
};
 
$(document).ready(function (){
	
	$("#dialog_folder form").submit(function (e) {
		
		
		if(!$("input[name='name']", this).val()) {
			alert("폴더이름을 입력해주세요.");
			e.preventDefault();
			return false;
		}

		$("#dialog_folder .btn_set .btn_pack").hide();
		$("#dialog_folder .btn_set .loading").show();
		
		$.ajax({
			url: $(this).attr("action"),
			type: $(this).attr("method"),
			dataType: "json",
			data: $(this).serialize(),
			success:function (data) {
				$("#folder_view").html("");
				$("#folder_view").html($("#folderTpl").val());
				$("#folder_view select option:eq(0)").after("<option value=\"" + data.folder_id + "\" selected=\"selected\">" + data.folder_name + "</option>");
				$("#folder_view select").selectbox().change();

				$("#dialog_folder .btn_set .btn_pack").show();
				$("#dialog_folder .btn_set .loading").hide();
				$("#dialog_folder").dialog("destroy");
				
				$("#dialog_folder input[type='text']").val("");
				$("#dialog_folder textarea").val("");
			}
		});
		
		e.preventDefault();
		return false;
	});
	
	$("#thumbnail").change(function (){
		if (!$(this).val()) return;
		var ext = $(this).val().substring($(this).val().lastIndexOf(".") + 1).toLowerCase();
		if (ext != "jpg" && ext != "gif" && ext != "png" && ext != "jpeg") {
			alert("이미지 파일이 아닙니다.");
			return;
		}
		
		var reader = new FileReader();
		console.log(reader);
        reader.onload = function (e) {
            $("#thumb_view").attr("src", e.target.result);
			$("#thumb_view").load();
        }
		reader.readAsDataURL(this.files[0]);			
		
		$("#thumb_button").hide();
	});
	
	$("#thumb_view").load(function () {
		console.log('onLoad');
		$("#thumb_view").css({"maxWidth" : "auto", "maxHeight" : "auto"});
		if ($("#thumb_view").width() > $("#thumb_view").height()) {
			$("#thumb_view").css({"maxWidth" : "100%"});
		}
		else {
			$("#thumb_view").css({"maxHeight" : "100%"});
		}
		$("#thumb_view").css({
			marginTop: -($("#thumb_view").height() / 2) + "px",
			marginLeft: -($("#thumb_view").width() / 2) + "px"
		});
		$("#thumb_view").css("display", "block");
	});
	
	$("#thumbnail").change();
	$(".selectbox").selectbox({ effect: "slide", speed: 200 });
	
	$('#position_search').autocomplete({
		focus: function() {
			return false;
		},
		source: function(request, response) {
			$.ajax({
				url: $('#position_search').attr('action'),
				dataType: "json",
				data: $('#position_search').serialize(),
				success: function (result) {
					response( $.map(result.data, function(item) {
						return {
							label: item.name,
							value: item.id
						}
					}));
				}
			});
		},
		select: function(event, ui) {
			console.log(event);
			console.log(ui);
			$('#position_search').val('');
			var tag = $('.tagList').children('.tag_ori').clone();
			console.log(tag);
			tag.removeClass('tag_ori').addClass('tag').data('value', ui.item.value);
			tag.children('.tag_label').text(ui.item.label);
			tag.children('.position_hidden').removeClass('position_hidden')
				.attr('name', 'position').val(ui.item.value);
			
			$('.tagList').append(tag);
			tag.show();
			return false;
		}
	});
	
	$('a.delete_tag').live('click', function() {
		$(this).parent('.tag').remove();
		return false;
	});
	
	
	
	$(window).resize(function () {
		if ($("#thumb_case").height() > 0)
			$(".thumb_container").css({ "height" : $("#thumb_case").height() });
		$("#thumb_view").load();
	});
	$(window).resize();
});

