package com.junglebird.webframe;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHelper {

	private static CookieHelper _instance;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public static CookieHelper sync(HttpServletRequest request, HttpServletResponse response) {
		_instance = new CookieHelper(request, response);
		return _instance;
	}
	
	private CookieHelper(HttpServletRequest request, HttpServletResponse response){
		this.request = request;
		this.response = response;
	}

	public String getCookie(String key) {
		Cookie cookie = null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null)
			for(Cookie item : cookies)
			{
				if (!item.getName().equals(key)) continue;
				cookie = item;			
			}

		if(cookie == null) return null;
		try
		{
			return URLDecoder.decode(cookie.getValue(), "UTF-8");
		}
		catch(Exception ex) {
			return null;
		}
	}
	public void setCookie(String key, String value) throws UnsupportedEncodingException {
		Cookie cookie = null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null)
			for(Cookie item : cookies)
			{
				if (!item.getName().equals(key)) continue;
				cookie = item;			
			}

		if(cookie == null) 
		{
			cookie = new Cookie(key, URLEncoder.encode(value, "UTF-8"));
			cookie.setPath("/");
		}
		else
		{
			cookie.setValue(value);
			cookie.setPath("/");
		}

		response.addCookie(cookie);
	}
}
