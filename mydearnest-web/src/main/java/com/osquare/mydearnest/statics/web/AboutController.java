package com.osquare.mydearnest.statics.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junglebird.webframe.common.StringUtils;

@Controller
@RequestMapping("/about")
public class AboutController {

	@RequestMapping("")
	public String index(HttpServletResponse response) {
		return "about/about";
	}
	
	@RequestMapping("/button")
	public String button(ModelMap model, HttpServletResponse response, HttpServletRequest request) {
		model.addAttribute("isChrome", request.getHeader("user-agent").indexOf("Chrome/") > 0);
		return "about/button";
	}

	@RequestMapping("/help")
	public String help(HttpServletResponse response) {
		return "about/help";
	}

	@RequestMapping("/privacy")
	public String privacy(HttpServletResponse response) {
		return "about/privacy";
	}

	@RequestMapping("/tos")
	public String tos(HttpServletResponse response) {
		
		return "about/tos";
	}

		
}
