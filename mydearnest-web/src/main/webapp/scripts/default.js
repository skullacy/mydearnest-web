var context_path,is_signed,account_id,is_certified,session_id;if(!console)var console={log:function(){}, error:function(){}};
String.prototype.string=function(l){var s='',i=0;while(i++<l){s+=this;}return s;};String.prototype.zf=function(l,m){return '0'.string(l-this.length)+this;};Number.prototype.zf=function(l,m){return this.toString().zf(l);};Date.prototype.shortFormat=function(s){if(!this.valueOf())return " ";var timeString="";var now=new Date();var min=(now-this)/60000;if(Math.floor(min)<=0){timeString="방금 전";}else if(Math.floor(min/60)>=24){timeString=this.format("yyyy. MM. dd");}else if(Math.floor(min)>=60){timeString=Math.floor(min/60)+"시간 전";}else{timeString=Math.floor(min)+"분 전";}return (s?s+" ":"")+timeString;};Date.prototype.format=function(f){if(!this.valueOf())return " ";var weekName=["일요일","월요일","화요일","수요일","목요일","금요일","토요일"];var d=this;return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi,function($1){switch($1){case "yyyy":return d.getFullYear();case "yy":return (d.getFullYear() % 1000).zf(2);case "MM":return (d.getMonth() + 1).zf(2);case "dd":return d.getDate().zf(2);case "E":return weekName[d.getDay()];case "HH":return d.getHours().zf(2);case "hh":return ((h=d.getHours()%12)?h:12).zf(2);case "mm":return d.getMinutes().zf(2);case "ss":return d.getSeconds().zf(2);case "a/p":return d.getHours()<12?"오전":"오후";default:return $1;}});};String.prototype.trim=function(){return this.replace(/^\s+|\s+$/g,'');};
var getHeader=function(jqXHR, key){var header=jqXHR.getAllResponseHeaders().split("\n");var result="";for(var i=0;i<header.length;i++){if(header[i].indexOf(key+":")==0){result=header[i].substring((key+":").length).replace(/^\s\s*/,'').replace(/\s\s*$/,'');break;}}return result;}
if($){
	$(document).ready(function(){
		$("input.txt.focus").focus(function(){
			$(this).parent().addClass("focused");
		});
		$("input.txt.focus").focusout(function(){
			if($(this).val()){
				$(this).parent().addClass("focused");
			}else{
				$(this).parent().removeClass("focused");
			}
		});
		$("input.txt.focus").focusout();
		$("*[data-submenu]").mouseenter(function(){
			$(this).addClass("hover");
			$(".submenu",this).clearQueue();
			$(".submenu",this).css("height", "auto");
			$(".submenu",this).slideDown(250);
		});
		$("*[data-submenu]").mouseleave(function(){
			var $t = $(this)
			$(".submenu", this).clearQueue();
			$(".submenu", this).slideUp(200, function () {
				$t.removeClass("hover");
			});
		});
		
	});
	function socialLogin(obj){
		var width=$(obj).attr("data-width");
		var height=$(obj).attr("data-height");
		var left=(window.screen.width-width)/2;
		var top=(window.screen.height-height)/2;
		location.href = $(obj).attr("data-url");
		//,"_social",'width='+width+',height='+height+',left='+left+',top='+top+',toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=yes,scrollbars=no',false);popup.focus();
	//	setTimeout(popup.focus, 1000);
	}
	function popup(obj){
		var width=$(obj).attr("data-width");
		var height=$(obj).attr("data-height");
		var left=(window.screen.width-width)/2;
		var top=(window.screen.height-height)/2;
		var popup = window.open($(obj).attr("href"), "_social",'width='+width+',height='+height+',left='+left+',top='+top+',toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=yes,scrollbars=no',false);popup.focus();
		setTimeout(function () { popup.focus(); }, 1000);
	}
}