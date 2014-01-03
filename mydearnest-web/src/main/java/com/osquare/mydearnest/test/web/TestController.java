package com.osquare.mydearnest.test.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/testAdmin")
public class TestController {
	
	@RequestMapping("/index")
	public String adminIndex(Model model, HttpServletRequest request){
		
		model.addAttribute("layout", "./shared/layout.mdn.admin.vm");
		return "testadmin/index";
	}
	@RequestMapping("/management")
	public String adminManagement(Model model, HttpServletRequest request){
		model.addAttribute("layout", "./shared/layout.mdn.admin.vm");
		return "testadmin/management";
	}
	@RequestMapping("/category/list")
	public String tagList(Model model, HttpServletRequest request){
		model.addAttribute("layout", "./shared/layout.mdn.admin.vm");
		return "testadmin/taglist";
	}
	@RequestMapping("/category/create")
	public String tagCreate(Model model, HttpServletRequest request){
		model.addAttribute("layout", "./shared/layout.mdn.admin.vm");
		return "testadmin/tagcreate";
	}
	
	@RequestMapping("/write/upload")
	public String photoUpload(Model model, HttpServletRequest request){
		model.addAttribute("layout", "./shared/layout.mdn.admin.vm");
		return "testadmin/photoupload";
	}
	
	@RequestMapping("/management/photo")
	public String photoManagement(Model model, HttpServletRequest request){
		model.addAttribute("layout", "./shared/layout.mdn.admin.vm");
		return "testadmin/photo";
	}
	
	@RequestMapping("/management/member")
	public String memberManagement(Model model, HttpServletRequest request){
		model.addAttribute("layout", "./shared/layout.mdn.admin.vm");
		return "testadmin/member";
	}
	
	@RequestMapping("/write/grade")
	public String feelUpload(Model model, HttpServletRequest request){
		model.addAttribute("layout", "./shared/layout.mdn.admin.vm");
		return "testadmin/grade";
	}
	
	@RequestMapping("/write/detail")
	public String writeDetail(Model model, HttpServletRequest request){
		model.addAttribute("layout", "./shared/layout.mdn.admin.vm");
		return "testadmin/detail";
	}
}
