if ("function" !== typeof ffExtention) 

	var ffExtention = function () {
	
		this.targetDoc = document;
		this.targetFrame = null;
		this.mmrst_getElementsByClassName = function (d, r) {
			if ("function" !== typeof document.getElementsByClassName) {
				els = d.getElementsByTagName("*");
				for (var h = [], a = 0; a < els.length; a++) els[a].className == r && h.push(els[a]);
				return !h.length ? null : h;
			}
			return d.getElementsByClassName(r);
		};
		
		this.getProductInfo = function () {
			var d = 0, r = "KRW";
			"undefined" !== typeof memoryst_p_price && (d = parseInt(this.repairPrice(memoryst_p_price)));
			"undefined" !== typeof memoryst_p_priceunit && (r = memoryst_p_priceunit);
			if (!d) 
				if (-1 != this.targetDoc.location.hostname.indexOf("interpark.com")) 
					"object" === typeof prdOpt && "undefined" !== typeof prdOpt.saleUnitcost && (d = prdOpt.saleUnitcost);
			else if (-1 != this.targetDoc.location.hostname.indexOf("auction.co.kr")) 
				"function" === typeof GetSellingPrice && (d = parseInt(GetSellingPrice()));
			else if (-1 != this.targetDoc.location.hostname.indexOf("gmarket.co.kr")) {
				var h = d = 0;
				null != this.targetDoc.getElementById("orgin_price") && (d = this.repairPrice(this.targetDoc.getElementById("orgin_price").innerHTML));
				null != this.targetDoc.getElementById("spanCostPrice") && (h = this.repairPrice(this.targetDoc.getElementById("spanCostPrice").innerHTML));
				d = 0 <= d - h ? d - h : 0;
			} 
			else if (-1 != this.targetDoc.location.hostname.indexOf("11st.co.kr")) {
				var a = this.targetDoc.getElementsByTagName("input");
				if (0 < a.length) 
					for (var i = 0; i < a.length; i++) 
						"hidden" == a[i].getAttribute("type") && "price_checker" == a[i].getAttribute("name") && (d = this.repairPrice(a[i].value));
			} 
			else if (-1 != this.targetDoc.location.hostname.indexOf("gsshop.com")) {
				if (a = this.targetDoc.getElementsByTagName("input"), 0 < a.length) for (i = 0; i < a.length; i++) "hidden" == a[i].getAttribute("type") && "price2" == a[i].getAttribute("name") && (d = this.repairPrice(a[i].value))
			} 
			else if (-1 != this.targetDoc.location.hostname.indexOf("cjmall.com")) null != this.targetDoc.getElementById("sale_price_org2") && (d = this.repairPrice(this.targetDoc.getElementById("sale_price_org2").innerHTML));
			else if (-1 != this.targetDoc.location.hostname.indexOf("lotteimall.com")) a = this.mmrst_getElementsByClassName(this.targetDoc, "price"), 0 < a.length && (a = a[0].getElementsByTagName("strong"), a.length && (d = this.repairPrice(a[0].innerHTML)));
			else if (-1 != this.targetDoc.location.hostname.indexOf("hyundaihmall.com")) {
				if (a = this.targetDoc.getElementsByTagName("input"), 0 < a.length) for (i = 0; i < a.length; i++) "hidden" == a[i].getAttribute("type") && "SalePrice" == a[i].getAttribute("name") && (d = this.repairPrice(a[i].value))
			} else if (-1 != this.targetDoc.location.hostname.indexOf("dnshop.com")) "undefined" !== typeof salecost && (d = salecost);
			else if (-1 != this.targetDoc.location.hostname.indexOf("akmall.com")) {
				if (a = this.targetDoc.getElementsByTagName("input"), 0 < a.length) for (i = 0; i < a.length; i++) "hidden" == a[i].getAttribute("type") && "real_amt" == a[i].getAttribute("name") && (d = this.repairPrice(a[i].value))
			} else if (-1 != this.targetDoc.location.hostname.indexOf("shop.naver.com")) {
				h = d = 0;
				a = this.targetDoc.getElementsByTagName("input");
				if (0 < a.length) for (i = 0; i < a.length; i++) "hidden" == a[i].getAttribute("type") && "productSalePrice" == a[i].getAttribute("name") && (d = this.repairPrice(a[i].value));
				a = this.targetDoc.getElementsByTagName("input");
				if (0 < a.length) for (i = 0; i < a.length; i++) "hidden" == a[i].getAttribute("type") && "sellerImmediateDiscountAmount" == a[i].getAttribute("name") && (h = this.repairPrice(a[i].value));
				d = 0 <= d - h ? d - h : 0
			} else if ("undefined" !== typeof _HCmz && "undefined" !== typeof _HCmz.PS) d = _HCmz.PS;
			else if (null != this.targetDoc.getElementById("main_price")) d = this.repairPrice(this.targetDoc.getElementById("main_price").innerHTML);
			else if (null != this.targetDoc.getElementById("assign_prd_sale_price_text")) d = this.repairPrice(this.targetDoc.getElementById("assign_prd_sale_price_text").innerHTML);
			else if (null != this.targetDoc.getElementById("price")) if (a = this.targetDoc.getElementById("price"), "INPUT" == a.tagName || "input" == a.tagName) d = this.repairPrice(a.value);
			else {
				if ("SPAN" == a.tagName || "span" == a.tagName) d = this.repairPrice(a.innerHTML);
			} else "undefined" !== typeof product_price ? d = this.repairPrice(product_price) : "undefined" !== typeof _A_amt ? d = this.repairPrice(_A_amt[0]) : this.targetFrame && ("undefined" !== typeof this.targetFrame._HCmz && "undefined" !== typeof this.targetFrame._HCmz.PS ? d = this.targetFrame._HCmz.PS : "undefined" !== typeof this.targetFrame._A_amt && (d = this.repairPrice(this.targetFrame._A_amt[0])));
			return {
				price: d,
				priceUnit: r
			};
		};
		this.repairPrice = function (d) {
			d = d.replace(/[^0-9]/g, "");
			return parseInt(d);
		};
	}, ffExtention = new ffExtention;

(function () {

	function d() {
        var a;
        a = location.href;
        a = a.split("//");
        return a = a[1].substr(0, a[1].indexOf("/"));
    }
    
    function r() {
        var a = window.navigator.userAgent, c = a.indexOf("MSIE");
        return 0 < c ? parseInt(a.substring(c + 5, a.indexOf(".", c))) : 0;
    }
    
    function h() {
        return /msie/i.test(navigator.userAgent) && !/opera/i.test(navigator.userAgent);
    }
    
    function a(a) {
        var d = ["input", "iframe", "object", "embed"];
        for (n = 0; n < d.length; n++) 
        	for (var q = c.getElementsByTagName(d[n]), k = 0; k < q.length; k++) 
        		a ? "hidden" != q[k].style.visibility && (
        				q[k].style.visibility = "hidden", 
        				q[k].setAttribute("mmrst_touched", "1")
        			) : "1" == q[k].getAttribute("mmrst_touched") && (q[k].style.visibility = "visible", q[k].removeAttribute("mmrst_touched"));
    }
    
    var A;
    var $cf = {
//    	path : "http://192.168.5.50:8080",
    	path : "http://curation.mixoncorp.com",
    	re_click : "즐겨찾기에 Find it! 버튼을 다시 눌러주세요.",
    	re_find : "Re Find 기능을 사용해보세요."
    };

    
    if (location.href.match(/^https?:\/\/.*?\.?curation\.mixoncorp\.com\//)) { return window.alert($cf.re_find), false; }
    
    var p = location.href, l = document.getElementsByTagName("iframe");
    if (p.match(/^https?:\/\/mixsh\.com\/r\//))			{ return alert($cf.re_click), location.href = l[0].src, void(0); }
    if (p.match(/^https?:\/\/v\.daum\.net\/link\//))	{ return alert($cf.re_click), location.href = l[1].src, void(0); }
    
    l = document.getElementsByTagName("frame");
    if (p.match(/^https?:\/\/.*?\.?blog\.me\//))		{ return alert($cf.re_click), location.href = l[0].src, void(0); }
    if (p.match(/^https?:\/\/olpost\.com\/r\//))		{ return alert($cf.re_click), location.href = l[1].src, void(0); }
    
    if (l.length > 0 && (p = l[0].src, p.match(/^https?:\/\/blog\.naver\.com\//) && !p.match(/^https?:\/\/blog\.naver\.com\/PostView.nhn/))) { return alert($cf.re_click), location.href = l[0].src, void(0); }
    
    
    
    var x = r(),
        c = document,
        o = function () {
    	
            function a(b, c) {
                var e = new Image;
                e.src = b.src;
                return {
                    image: e,
                    width: b.width,
                    height: b.height,
                    src: b.src
                };
            }
            
            var k = [], e, h = d();
            e = document.getElementsByTagName("frame");
            
            if (0 < e.length && document.getElementsByTagName("frameset")) {
                for (var l = h = 0, g = 0; g < e.length; g++) try {
                    var o = e[g].contentWindow ? e[g].contentWindow.document : e[g].contentDocument;
                    if (o) {
                        c = o;
                        0 == g && (l = c.body.scrollHeight);
                        c.body.scrollHeight > l && 0 < g && (l = c.body.scrollHeight, h = g);
                        for (var j = 0; j < c.images.length; j++) {
                            var p = c.images[j];
                            "none" != p.style.display && (p = a(p, "image"), 80 < p.width && 80 < p.height && (109 < p.height || 109 < p.width) && k.push(p));
                        }
                    }
                } catch (r) {}
                try {
                    c = o = e[h].contentWindow ? e[h].contentWindow.document : e[h].contentDocument, ffExtention.targetDoc = c, ffExtention.targetFrame = e[h].contentWindow ? e[h].contentWindow : e[h].contentDocument, A = e[h].src, e[h].contentWindow && e[h].contentWindow.scroll(0, 0);
                } catch (s) {}
            } else {
                for (g = 0; g < c.images.length; g++) e = c.images[g], "none" != e.style.display && (e = a(e, "image"), 80 < e.width && 80 < e.height && (105 < e.height || 105 < e.width) && k.push(e));
                e = document.getElementsByTagName("iframe");
                if (0 < e.length) for (g = 0; g < e.length; g++) if (!/^https?:\/\//.exec(e[g].src) && /^\/[a-zA-Z0-9\-_]+/.exec(e[g].src) &&
                    "" != e[g].src && (e[g].src = "http://" + h + e[g].src), -1 < e[g].src.indexOf(h)) try {
                    c = o = e[g].contentWindow ? e[g].contentWindow.document : e[g].contentDocument;
                    for (j = 0; j < c.images.length; j++) p = c.images[j], "none" != p.style.display && (p = a(p, "image"), 80 < p.width && 80 < p.height && (105 < p.height || 105 < p.width) && k.push(p));
                } catch (u) {}
                c = document;
                scroll(0, 0);
            }
            return k;
        }();
        
    if (0 == o.length) window.alert("\uae30\uc5b5\uc5d0 \ub2f4\uc744 \uc218 \uc788\ub294 \uc774\ubbf8\uc9c0\ub098 \ub3d9\uc601\uc0c1\uc774 \uc5c6\uc2b5\ub2c8\ub2e4."), document.insertDoc = 0;
    else {
        
    	var s;
        hiddenOverlay = function () {
            a(!1);
            t.parentNode.removeChild(t);
            w.parentNode.removeChild(w);
            h() ? c.getElementsByTagName("head")[0].removeChild(s) : c.body.removeChild(s);
            document.insertDoc = 0;
            return !1;
        };
        
        l = '#show_it_overlay						{ position: absolute; top:0; right:0; bottom:0; left:0; width:100%; height:100%; overflow:none; background-color:#ffffff; filter:alpha(opacity=80); opacity:0.8; -moz-opacity:0.8; text-align:center; z-index:2147483647; }\n' + 
        	'#show_it_container						{ position:absolute; top:0; left:5%; margin-top:30px; width:90%; border:1px solid #7e7e7e; background:url(' + $cf.path + '/images/js/body_background.png);; z-index:2147483647; min-height:80%; text-align:center; }\n' + 
        	'#show_it_box							{ position:relative; width:auto !important; height:auto !important; }\n' + 
        	'#show_it_handler						{ padding:5px 20px 5px 15px; height:38px; margin-bottom:20px; text-align:center; border-bottom:1px solid #7e7e7e; font-size:15px; color:#fff; text-decoration:none; z-index:2147483647; background:url("' + $cf.path + '/images/js/header_background.png") repeat-x; }\n' + 
        	'#show_it_handler a.logo				{ display:inline-block; width:120px; height:40px; background:url(' + $cf.path + '/images/js/header_logo.png) left center no-repeat; }\n' + 
        	'#show_it_handler a.close 				{ display:block; position:absolute; right:10px; top:10px; width:25px; height:27px; background:url(' + $cf.path + '/images/js/close_button.png) no-repeat center center; }\n' + 
        	'.show_it_content 						{ position:relative; padding:0; margin:0; background:#fff; border:1px solid #9e9e9e; width:230px; height:220px; display:inline-block; text-align:center; margin-left:20px; margin-bottom:20px; overflow:hidden;z-index:6;}\n' + 
        	'.show_it_content .imageBox				{ display:inline-block; border:none; height:220px; width:230px;  margin:0; padding:0; }\n' + 
        	'.show_it_content .imageBox a			{ position:absolute; top:0; left:0; height:220px; width:230px; text-align:center; background-color:#FFFFFF;}\n' + 
        	'.show_it_content .imageBox a:hover 	{ background-color:#f8f8f8; filter:alpha(opacity=80); opacity:0.8; -moz-opacity:0.8; border:none; }\n' + 
        	'.show_it_content .imageBox .imageView	{ max-height:220px; max-width:230px; width:auto !important; height:auto !important;}\n' + 
        	'.show_it_img_size 						{ position:relative; margin-top:200px; line-height:16px; text-align:center; color:#f2f2f2; font-size:11px; display:block; background:#333; padding:2px 2px; z-index:9; filter:alpha(opacity=60); opacity:0.6; -moz-opacity:0.6;}\n';
        s = c.createElement("style");
        h() ? (s.type = "text/css", s.media = "screen", s.styleSheet.cssText = l, c.getElementsByTagName("head")[0].appendChild(s)) : 
    (0 < navigator.userAgent.lastIndexOf("Safari/") 
    && 
    533 > parseInt(navigator.userAgent.substr(navigator.userAgent.lastIndexOf("Safari/") + 7, 7)) ? s.innerText = "\n" + html + "\n" : s.innerHTML = "\n" + l + "\n", c.body.appendChild(s));
        a(!0);
        var t;
        h() && 9 > x ? t = c.createElement("<div id='show_it_overlay' style='height:" + c.body.scrollHeight + "px'></div>") : (t = c.createElement("div"), t.setAttribute("id",
            "show_it_overlay"), 8 < x ? t.style.cssText = "height:" + c.body.scrollHeight + "px;" : t.setAttribute("style", "height:" + c.documentElement.scrollHeight + "px"));
        t.onclick = hiddenOverlay;
        c.body.appendChild(t);
        var w;
        h() && 9 > x ? w = c.createElement("<div id='show_it_container'></div>") : (w = c.createElement("div"), w.setAttribute("id", "show_it_container"));
        c.body.appendChild(w);
        var y;
        h() && 9 > x ? y = c.createElement("<div id='show_it_box'></div>") : (y = c.createElement("div"), y.setAttribute("id", "show_it_box"));
        w.appendChild(y);
        h() && 9 > x ? l = c.createElement("<div id='show_it_handler'></div>") : (l = c.createElement("div"), l.setAttribute("id", "show_it_handler"));
        
        var f = c.createElement("a");
        f.href = "#";
        f.id = "removeLink";
        f.className = "close";
        f.appendChild(c.createTextNode(""));
        f.onclick = hiddenOverlay;
        l.appendChild(f);
        
        f = c.createElement("a");
        f.href = $cf.path;
        f.target = "_blank";
        f.className = "logo";
        f.appendChild(c.createTextNode(""));
        l.appendChild(f);
        
        y.appendChild(l);
        
        var u, B, C, D;
        m = {};
        
        for (var n = 0; n < o.length; n++) 
        	
        	m[o[n].src] || o[n].image.height && 80 > o[n].image.height || (m[o[n]] = 1, function (a) {
        		
                var d = c.createElement("div");
                h() ? d.className = "show_it_content" : d.setAttribute("class", "show_it_content");
                
                var f = c.createElement("div");
                h() ? f.className = "imageBox" : f.setAttribute("class", "imageBox");
                
                var k = c.createElement("span");
                k.innerHTML = a.width + " x " + a.height;
                h() ? k.className = "show_it_img_size" : k.setAttribute("class", "show_it_img_size");
                d.appendChild(k);
                
                y.appendChild(d).appendChild(f);
                
                d = c.createElement("a");
                d.setAttribute("href", "#");
                
                d.onclick = function () {
                    A || (A = a.src == location.href ? document.referrer || location.href : location.href);
                    (A.indexOf("http") != 0) ? A = location.href + A : A = A;
                    
                    var c = $cf.path + "/add-item?";
                    var b = ffExtention.getProductInfo(), d = "";
                    
                    if (typeof mmrst_selectedBTNID !== "undefined" && mmrst_selectedBTNID != null) {
                        var f = document.getElementById(mmrst_selectedBTNID);
                        f != null && (d = f.getAttribute("description"));
                        mmrst_selectedBTNID = null
                    }
	                    
                    b = {
                        image_url: a.src,
                        link_url: A,
                        title: document.title,
                        description: d,
                        price: b.price,
                        price_unit: b.priceUnit,
                        w:a.width,
                        h:a.height
                    };
                    
                    d = [];
                    d.push(c);
                    
                    for (var g in b) 
                    	d.push(encodeURIComponent(g) + "=" + encodeURIComponent(b[g]) + "&");
                    
                    c = d.join("");
                    navigator.userAgent.toLowerCase();
                    g = 540;
                    
                    /Safari/.test(navigator.userAgent) && !/Chrome/.test(navigator.userAgent) && (g = 540);
                    window.open(c, "pinsbox", "status=no,resizable=no,scrollbars=yes,personalbar=no,directories=no,location=no,toolbar=no,menubar=no,width=880,height=" + g + ",top=" + (screen.height - 540) / 2 + ",left=" + (screen.width - 880) / 2).focus();
                    hiddenOverlay();
                };
                f.appendChild(d);
                
                
                B = 200 / a.width;
                C = 200 / a.height;
                u = B < C ? B : C;
                u > 1 && (u = 1);
                D = (200 - parseInt(u * a.height)) * 0.5;
                if (h() && x < 9) 
                	f = c.createElement("<img src='" + a.src + "' id='" + a.src + "' width='" + parseInt(u * a.width) + "' height='" + parseInt(u * a.height) + "' style='margin-top:" + D + "px; border:0px;'>");
                else {
                    f = c.createElement("img");
                    f.setAttribute("class", "imageView");
                    f.setAttribute("style", "margin-top:" + D + "px; border:0px;");
                    f.src = a.src;
                    f.width = parseInt(u * a.width);
                    f.height = parseInt(u * a.height);
                    f.id = a.src;
                }
                d.appendChild(f);
            
        }(o[n]));
    }
})();