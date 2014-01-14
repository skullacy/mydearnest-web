
$(function(){
	
	//악세사리 태그 검색 자동완성 코드
	$('#accessory_search').autocomplete({
		autoFocus: true,
		delay: 0,
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
			$('#accessory_search').val('');
			$('#accessory_search').val(ui.item.label).attr('data-value', ui.item.value).attr('disabled','disabled');
			return false;
		}
			
	});
	
	
	
	//이미지 태그 선택하러 마우스 오버시 오버레이 태그 캔버스 안보이게
	$('#overlayContainer').mouseover(function() {
		$(this).fadeOut(100);
	});
	
	//이미지 밖으로 나가면 오버레이 태그 캔버스 보이게
	$('#photo').mouseout(function(){
		$('#overlayContainer').fadeIn(100);
	});
	
	
	
	var imgSelectFunction = {
		selectHandler: function(img, selection) {
			if (!selection.width || !selection.height)
		        return;

			$('#select_posX1').val(selection.x1);
			$('#select_posY1').val(selection.y1);
			$('#select_posX2').val(selection.x2);
			$('#select_posY2').val(selection.y2);
		},
		
		dataInit: function() {
			//재입력 가능하게 초기화
			$('.link_input').css('display','none');
			$('#accessory_search').removeAttr("disabled");
			$('#accessory_search').data('');
			$('#accessory_search').val('');
			$('#title_info').val('');  
			$('#dummy_info').val('');
			$('.link_input input.posX1').val('');
			$('.link_input input.posY1').val('');
			$('.link_input input.posX2').val('');
			$('.link_input input.posY2').val('');
		},
		
		insertTag: function(data) {
			//오리지널 태그 복사후 세팅(클래스, 수치)
			var tag = $('.photoTag_ori').clone();
			tag.removeClass('photoTag_ori').addClass('photoTag');
			tag.find('input').each(function(){
				$(this).attr('name', $(this).attr('class')).removeClass($(this).attr('class'));
			});
			tag.find('.accessory_label').text(data.label);
			tag.find('.accessory_title').text(' ('+data.title+') ');
			tag.find('input[name=postTagId]').val(data.value);
			tag.find('input[name=title]').val(data.title);
			tag.find('input[name=info]').val(data.info);
			tag.find('input[name=posX1]').val(data.posX1);
			tag.find('input[name=posY1]').val(data.posY1);
			tag.find('input[name=posX2]').val(data.posX2);
			tag.find('input[name=posY2]').val(data.posY2);
			
			
			//태그 삭제이벤트 설정
			tag.click(function() {
				imgSelectFunction.deleteTag(tag);
			});
			
			//태그 컨테이너에 추가후 보여주기
			$('.tag_list').append(tag);
			tag.show();
		},
		
		//오버레이태그 추가
		insertOverlayTag: function(data) {
			var tag = $('.overlayTag_ori').clone();
			tag.removeClass('overlayTag_ori').addClass('overlayTag');
			tag.attr('data-id', data.value);
			tag.attr('data-title', data.title);
			
			//위치 지정하기
			tag.css('top', data.posY1 + 'px').css('left', data.posX1 + 'px');
			
			//크기 지정하기
			tag.css('width', Math.abs(data.posX2 - data.posX1) + 'px').css('height', Math.abs(data.posY2 - data.posY1) + 'px')
			
			$('#tagContainer').append(tag);
			tag.show();
		},
		
		//태그 지우기(텍스트 태그, 오버레이태그 둘다 지움)
		deleteTag: function(tag) {
			var tagId = tag.find('input[name=postTagId]').val();
			tag.remove();
			if(!tagId) return;
			
			var tagTitle = tag.find('input[name=title]').val();
			if(tagTitle) {
				$('#tagContainer').find('div.overlayTag[data-id=' + tagId + '][data-title=' + tagTitle +']').remove();
			}
			else {
				$('#tagContainer').find('div.overlayTag[data-id=' + tagId + ']').remove();
			}
		},
		
		//오버레이 태그 활성화(테두리 하이라이트)
		activeOverlayTag: function(tag) {
			var tagId = tag.find('input[name=postTagId]').val();
			var tagTitle = tag.find('input[name=title]').val();
			if(!tagId) return;
			if(tagTitle) {
				$('#tagContainer').find('div.overlayTag[data-id=' + tagId + '][data-title=' + tagTitle +']').addClass('active');
			}
			else {
				$('#tagContainer').find('div.overlayTag[data-id=' + tagId + ']').addClass('active');
			}
		},
		
		//오버레이 태그 비활성화(테두리 하이라이트)
		deActiveOverlayTag: function(tag) {
			var tagId = tag.find('input[name=postTagId]').val();
			var tagTitle = tag.find('input[name=title]').val();
			if(!tagId) return;
			if(tagTitle) {
				$('#tagContainer').find('div.overlayTag[data-id=' + tagId + '][data-title=' + tagTitle +']').removeClass('active');
			}
			else {
				$('#tagContainer').find('div.overlayTag[data-id=' + tagId + ']').removeClass('active');
			}
		}
	};

	
	
	$('#linksubmit_button').click(function(){
		//태그 관련값 변수에 저장
		var data = {
			label: $('#accessory_search').val(),
			value: $('#accessory_search').data('value'),
			title: $('#title_info').val(),
			info: $('#dummy_info').val(),
			posX1: $('#select_posX1').val(),
			posY1: $('#select_posY1').val(),
			posX2: $('#select_posX2').val(),
			posY2: $('#select_posY2').val()
		};
		
		//태그 추가하기
		imgSelectFunction.insertTag(data);
		
		//사진위에 오버레이태그 보여주기
		imgSelectFunction.insertOverlayTag(data);
		
		//재입력 가능하게 초기화
		imgSelectFunction.dataInit();
		
		
		//초기화 : 이미지선택영역 초기화
		photo_imgSelect.cancelSelection();
		//초기화 : 오버레이태그 다시 보이게 설정
		$('#overlayContainer').fadeIn(100);
		
		return false;
	});
	
	//태그 삭제이벤트 설정, 마우스오버시 오버레이태그 활성화
	$('.photoTag').on('click', function(){
		imgSelectFunction.deleteTag($(this));
		return false;
	}).hover(function(){
		imgSelectFunction.activeOverlayTag($(this));
	}, function(){
		imgSelectFunction.deActiveOverlayTag($(this));
	});
	
	
	//이미지 선택 플러그인 로드
	var photo_imgSelect = $('#photo').imgAreaSelect({ 
		aspectRatio: '1:1', handles: false,
		
	    fadeSpeed: 200, onSelectChange: imgSelectFunction.selectHandler, 
	    onSelectEnd: function (img, selection) {
			$('.link_input').css('display','block');
	    	
		},
		//해당 플러그인을 인스턴스화 시켜 다른 함수에서도 호출가능하게 설정.
		instance : true

	});
});