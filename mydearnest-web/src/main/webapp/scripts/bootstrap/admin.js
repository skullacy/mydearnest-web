
$(function(){
	$('#position_search').autocomplete({
		focus: function(){
			return false;	
		},
		source: function(request, response){
			$.ajax({
				url: $('#position_search').attr('action'),
				dataType: "Json",
				data: $('#position_search').serialize(),
				success: function(result){
					response($.map(result.data, function(item){
						return{
							label: item.title,
							value: item.id,
							type: item.type
						}
					}));
				}
			});
		},
		select: function(event, ui) {
			console.log(event);
			console.log(ui);
			$('#position_search').val('');
			
			
			
			var tag = $('.tagResult').children('.tag_ori').clone();
			console.log(tag);
			tag.removeClass('tag_ori').addClass('tag').data('value', ui.item.value);
			tag.children('.tag_label').text(ui.item.label);
			
			
			tag.children('.position_hidden').removeClass('position_hidden')
				.attr('name', 'tag'+ui.item.type.substring(0, 1).toUpperCase() + ui.item.type.substring(1, ui.item.type.length).toLowerCase()).val(ui.item.value);
			
			tag.find('a.delete_tag').click(function(){
				$(this).parent('.tag').remove();
				return false;
			});
			
			$('.tag_'+ui.item.type).append(tag);
			tag.show();
			return false;
		}

	});
	$("#test").ImageColorPicker({
		afterColorSelected: function(event, color){
			var tag = $('.tagResult').children('.tag_ori').clone();
			console.log(color);
			console.log(event);
			console.log(tag);
			tag.removeClass('tag_ori').addClass('tag').data('value', color).css('background',color).css('color','#eeeeee').css('text-shadow','none');
			
			tag.children('.tag_label').text(color);
			$("#result_test").text(color);
			tag.children('.position_hidden').removeClass('position_hidden')
			.attr('name', 'tagColor').attr('value', color);
		
			
			tag.find('a.delete_tag').click(function(){
				$(this).parent('.tag').remove();
				return false;
			});
			$('.tag_color').append(tag);
			tag.show();
			
			}
	});
	
	
		
		$('.feel-tooltip').popover('hide');
	
	
	


});