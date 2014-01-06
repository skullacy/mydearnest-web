package com.osquare.mydearnest.admin.web;


import java.util.Collection;
import java.util.Iterator;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.junglebird.webframe.vo.SignedDetails;
import com.osquare.mydearnest.admin.service.AdminAccountService;
import com.osquare.mydearnest.admin.service.AdminPostService;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.entity.PostGrade;

@Controller
@RequestMapping("/admin/post")
public class PostAdminController {

	@Autowired private AdminAccountService adminAccountService;
	@Autowired private AdminPostService adminPostService;
	
	
	//사진 리스트 출력용 컨트롤러
	//권한 : 모두. 접근기능은 권한별 한정하기.
	@RequestMapping("/list")
	public String index(ModelMap model, 
			@RequestParam(value="page", required = false) Integer page,
			@RequestParam(value="order", required = false) String order) {
		
		if (page == null) page = 1;
		if (order == null) order = "createdAt";
		
		model.addAttribute("order", order);
		model.addAttribute("page", page);
		model.addAttribute("pages", Math.ceil((double)adminPostService.sizeOfPost() / 20));
		
		Collection<Post> items = adminPostService.findPost(page, order);
		
		model.addAttribute("items", items);
		model.addAttribute("page_on", "post");
		
		model.addAttribute("layout", "./shared/layout.admin.vm");
		
		
		
		return "admin/post_list";
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
		
		
		Post post = adminPostService.disabledPost(id);
		model.addAttribute("success", post != null);
		model.addAttribute("redirectUri", redirectUri);
		
		return "admin/post_remove_ok";
	}
	

}
