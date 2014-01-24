var color_click = 0;
	var massages = ['비중이 50%이상인 색을 선택하세요','2번째로 비중이 높은 색을 선택하세요','3번째로 비중이 높은 색을 선택하세요','4번째로 비중이 높은 색을 선택하세요','색상 입력 완료!'];
	function insertColorTag(container, color) {
		var dataClick = container.attr('data-click');
		var tag = $('.tagResult').children('.tag_ori').clone();
		tag.removeClass('tag_ori').addClass('tag').data('value', color).css('background',color).css('color','#eeeeee').css('text-shadow','none');
		tag.children('.tag_label').text(color);
		tag.children('.position_hidden').removeClass('position_hidden')
		.attr('name', 'tagColor').attr('value', color + dataClick);
		
		tag.find('a.delete_tag').click(function(){
			$(this).parent('.tag').remove();
			findNextPosition();
			return false;
			
		});
		container.append(tag);
		tag.show();
	}
	
	
	function findNextPosition() {
		$('.color-space').each(function() {
			console.log($(this).has('div.tag').length);
			if($(this).has('div.tag').length == 0) {
				color_click = $(this).data('click');
				$('.color-teach').text(massages[color_click]);
				return false;
			}else {
				color_click++;
				$('.color-teach').text(massages[color_click]);
			}
		});
	}




$(function(){
	$('#position_search').autocomplete({
		delay: 0,
		autoFocus: true,
		focus: function(){
			return false;	
		},
		source: function(request, response){
			$.ajax({
				url: $('#position_search').attr('action'),
				dataType: "Json",
				data: $('#position_search').serialize(),
				success: function(result){
					console.log(result);
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
			$('.color-space').each(function() {
				if($(this).has('div.tag').length == 0) {
					insertColorTag($(this), color);
					findNextPosition();
					return false;
				}
			});
		}
	});
	
	var view_hint = 0;
	$('#color-select-btn').click(function(){
		if(view_hint == 0){  
			view_hint = 1; 
			$(this).text('색상선택 힌트끄기').removeClass('btn-success').addClass('btn-info');
			findNextPosition();
			$('body').mousemove(function(e){
				$('.tip-mousemove').css('left', e.pageX + 10).css('top', e.pageY + 10).css('display', 'block');
			
			});
		}else{
			view_hint = 0;
			$(this).text('색상 선택 힌트 보기').removeClass('btn-info').addClass('btn-success');
			$('body').mousemove(function(e){
				$('.tip-mousemove').css('left', e.pageX + 10).css('top', e.pageY + 10).css('display', 'none');
			
			});
		}
	});	
	
	
	$('.question a').popover({
		html : true,
		content: function(){
			var hint = $(this).attr('rel');
			return $('#popover_'+hint).html();
		}
	});	
	
		
		
		
		

});