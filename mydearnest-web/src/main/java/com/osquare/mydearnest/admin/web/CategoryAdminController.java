package com.osquare.mydearnest.admin.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.osquare.mydearnest.admin.service.AdminAccountService;
import com.osquare.mydearnest.admin.service.AdminPostService;
import com.osquare.mydearnest.entity.Post;

@Controller
@RequestMapping("/admin/category")
public class CategoryAdminController {

	@Autowired private AdminAccountService adminAccountService;
//	@Autowired private AdminPostService adminPostService;
	
	
	//카테고리 리스트 출력용 컨트롤
	//권한 : 관리자(ROLE_ADMIN)
	@RequestMapping("/list")
	public String index(ModelMap model, 
			@RequestParam(value="type", required = false) String type,
			@RequestParam(value="page", required = false) Integer page,
			@RequestParam(value="order", required = false) String order) {
		
		if (type == null) type = "";
		if (page == null) page = 1;
		if (order == null) order = "createdAt";
		
		model.addAttribute("order", order);
		model.addAttribute("page", page);
//		model.addAttribute("pages", Math.ceil((double)adminPostService.sizeOfPost() / 20));
//		model.addAttribute("items", adminPostService.findPost(page, order));
		model.addAttribute("page_on", "post");
		
		return "admin/post_index";
	}
	
	
	//카테고리 입력 화면
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String insertTag(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		return null;
	}
	
	//카테고리 입력 처리
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String insertTagSubmit(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		
		return null;
	}
	
	
	//카테고리 수정 화면
	@RequestMapping(value = "/update/{cateId}", method = RequestMethod.GET)
	public String updateTag(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("cateId") Long cateId) {
		
		return null;
	}
	

	
	/**
	 * @brief
	 * 기존 포스트 삭제용 메소드, 리팩토링 예
	 * @param model
	 * @param id
	 * @param redirectUri
	 * @return
	 * @deprecated
	 */
	@RequestMapping("/remove")
	public String index(ModelMap model, 
			@RequestParam(value="id", required = true) long id,
			@RequestParam(value="redirectUri", required = false) String redirectUri) {
		
		
//		Post post = adminPostService.disabledPost(id);
//		model.addAttribute("success", post != null);
		model.addAttribute("redirectUri", redirectUri);
		
		return "admin/post_remove_ok";
	}
	

}
