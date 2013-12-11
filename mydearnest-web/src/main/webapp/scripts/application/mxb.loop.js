
// 반복관련 통합 자바스크립트.
// MXB.loop.addQue("키값", function (){ }, 3 (초 단위));
MXB.loop = {
	init: function () { setTimeout(MXB.loop.async, 1000); },
	methods: {},
	timeoutKey: undefined,
	addQue: function (key, func, time){
		if (this.methods[key]) return;
		this.methods[key] = {
			func: func,
			time: time,
			point: time
		};
	},
	removeQue: function (key){
		delete this.methods[key];
	},
	async: function (){
		var $T = MXB.loop;
		$.each($T.methods, function (){
			if (this.time > this.point)
				this.point++;
			else
			{
				this.point = 0;
				this.func();
			}
		});
		setTimeout($T.async, 1000);
	}
};