var app_message = {
	not_signed : "로그인이 되어있지않습니다.\n로그인 후 재시도 해주세요.",
	not_connected : "서버와 연결이 실패하였습니다.\n다시 시도해 주세요.",
	duplicate_username : "아이디가 이미 사용중입니다. 다른 아이디로 시도해 주세요.",
	duplicate_mailaddress : "메일주소가 이미 사용중입니다. 다른 메일주소로 시도해 주세요.",
	not_value : "'{%s}'필드는 필수항목입니다.",
	not_compare : "'{%s}'필드의 값이 올바르지 않습니다.",
	not_exposed_music : "권리사의 사정으로 음악 사용 못함!!!",
	not_exposed_music_is_part: "일부곡이 궈어언리사의 사정으로 음악 사용 못함!!!"
};

String.prototype.parse = function (){
	if (app_message[this])
		return app_message[this];
	else
		return this;
};