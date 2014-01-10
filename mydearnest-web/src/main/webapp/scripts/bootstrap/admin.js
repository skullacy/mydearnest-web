
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
	
	var color_click = 0;
	var massages = ['비중이 50%이상인 색을 선택하세요','2번째로 비중이 높은 색을 선택하세요','3번째로 비중이 높은 색을 선택하세요','4번째로 비중이 높은 색을 선택하세요','색상 입력 완료!'];
	function insertColorTag(container, color) {
		var tag = $('.tagResult').children('.tag_ori').clone();
		tag.removeClass('tag_ori').addClass('tag').data('value', color).css('background',color).css('color','#eeeeee').css('text-shadow','none');
		tag.children('.tag_label').text(color);
		tag.children('.position_hidden').removeClass('position_hidden')
		.attr('name', 'tagColor').attr('value', color+color_click);
		
		tag.find('a.delete_tag').click(function(){
			color_click = tag.parent('.color-space').data('click');
			console.log(color_click);
			$('.color-teach').text(massages[color_click]);
			$(this).parent('.tag').remove();
			return false;
			
		});
		container.append(tag);
		tag.show();
	}
	
	var color_click = 0;
	
	function findNextPosition() {
		$('.color-space').each(function() {
			
			if($(this).has('div.tag').length == 0) {
				color_click = $(this).data('click');
				$('.color-teach').text(massages[color_click]);
				return false;
			}
		});
	}
	
	
	$('#color-select-btn').click(function(){
		$(this).css('display','none');
		$('.color-teach').text(massages[color_click]);
		$("#test").ImageColorPicker({
			afterColorSelected: function(event, color){
				$('.color-space').each(function() {
					if($(this).has('div.tag').length == 0) {
						insertColorTag($(this), color);
						if(color_click == 3) {
							$('.color-teach').text(massages[color_click+1]);
						}
						else {
							findNextPosition();
						} 
							
						console.log(color_click);
						
						
						return false;
					}
					
				});
				
				
				
				
				
				if(false) {
				if(color_click < 4){
					
					console.log(color_click);
					var tag = $('.tagResult').children('.color-space').clone().children('.tag_ori');
					tag.parent('.color-space').attr('data-click', color_click );
					color_click++;
					console.log(color);
					console.log(event);
					console.log(tag);
					tag.removeClass('tag_ori').addClass('tag').data('value', color).css('background',color).css('color','#eeeeee').css('text-shadow','none');
					tag.children('.tag_label').text(color);
					$("#result_test").text(color);
					tag.children('.position_hidden').removeClass('position_hidden')
					.attr('name', 'tagColor').attr('value', color+color_click);
					tag.find('a.delete_tag').click(function(){
						color_click = tag.parent('.color-space').data('click');
						console.log(color_click);
						$('.color-teach').text(massages[color_click]);
						$(this).parent('.tag').remove();
						return false;
						
					});
					$('.color-teach').text(massages[color_click]);
					$('.tag_color').append(tag.parent('.color-space'));
					tag.show();
				}
				}
			}
		});
		$('body').mousemove(function(e){
			
			$('.tip-mousemove').css('left', e.pageX + 10).css('top', e.pageY + 10).css('display', 'block');
			
		});
	
	});	
		
		$('.feel-tooltip').popover('hide');
		

});