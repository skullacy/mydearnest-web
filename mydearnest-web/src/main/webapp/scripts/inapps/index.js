
inAppScript = {
		
	is_follwable: null,
	run : (function (){
		$('#container.masonry').masonry({ isFitWidth: true });
	}),

	dispose: (function (){
	}),
	
	item_loaded: (function (){
		$('#container.masonry').masonry("reload");
	}),
};

if (inAppScript.run) inAppScript.run();