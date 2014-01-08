
$(function(){
	$('#accessory_search').autocomplete({
		focus: function(){
			return false;	
		},
		source: function(request, response){
			$.ajax({
				url: $('#accessory_search').attr('action'),
				dataType: "Json",
				data: $('#accessory_search').serialize(),
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
			console.log(ui.item.label);	
			$('#accessory_search').val('');
			$('#accessory_search').val(ui.item.label).attr('data-value', ui.item.value).attr('disabled','disabled');
			console.log($('#accessory_search').data('value'));
			return false;
		}
			
	});
	$('#linksubmit_button').click(function(){
		var label = $('#accessory_search').val();
		var value = $('#accessory_search').data('value');
		var title = $('#title_info').val();
		var info = $('#dummy_info').val();
		
		var tag = $('.photoTag_ori').clone();
		tag.removeClass('photoTag_ori');
		tag.find('input').each(function(){
			$(this).attr('name', $(this).attr('class'));
		});
		tag.find('.accessory_label').text(label);
		tag.find('.accessory_title').text(' ('+title+') ');
		tag.find('input[name=postTagId]').val(value);
		tag.find('input[name=title]').val(title);
		tag.find('input[name=info]').val(info);
		
		tag.click(function(){
			$(this).remove();
			return false;
		});
		console.log(tag);
		$('.tag_list').append(tag);
		tag.show();
		
		$('.link_input').css('display','none');
		
		$('#accessory_search').removeAttr("disabled");
		$('#accessory_search').data('');
		$('#accessory_search').val('');
		$('#title_info').val('');
		$('#dummy_info').val('');
		
		
		return false;
	});
});