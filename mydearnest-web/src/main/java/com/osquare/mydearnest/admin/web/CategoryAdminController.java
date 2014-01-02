package com.osquare.mydearnest.admin.web;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.osquare.mydearnest.admin.service.AdminCategoryService;
import com.osquare.mydearnest.entity.Account;

@Controller
@RequestMapping("/admin/category")
public class CategoryAdminController {
	
	@Autowired private AdminCategoryService adminCategoryService;
	
	@RequestMapping("/list")
	public String list(ModelMap model, @RequestParam(value="page", required = false) Integer page) {
		
		if (page == null) page = 1;
		model.addAttribute("page", page);
		model.addAttribute("pages", Math.ceil((double)adminCategoryService.sizeOfCategory() / 20));
		model.addAttribute("items", adminCategoryService.findCategory(page));
		model.addAttribute("page_on", "member");
		
		return "admin/category_index";
	}
	
	

	
	
}
