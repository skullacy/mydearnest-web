package com.osquare.mydearnest.admin.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@RequestMapping("")
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		return "redirect:/admin/post/list";
	}
	
	@RequestMapping("/management")
	public String managementIndex(Model model, HttpServletRequest request, HttpServletResponse response) {
		return "redirect:/admin/management/member";
	}
	
	
	
	
	
	
}
