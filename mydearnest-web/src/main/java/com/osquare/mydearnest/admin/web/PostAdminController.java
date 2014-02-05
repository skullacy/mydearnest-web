package com.osquare.mydearnest.admin.web;


import java.util.Collection;
import java.util.HashMap;
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
import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.admin.service.AdminAccountService;
import com.osquare.mydearnest.admin.service.AdminPostService;
import com.osquare.mydearnest.admin.service.AdminTagCateService;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.entity.PostGrade;
import com.osquare.mydearnest.entity.TagCategory;
import com.osquare.mydearnest.util.DetailModifyStatus;

@Controller
@RequestMapping("/admin/post")
public class PostAdminController {

	@Autowired private AdminAccountService adminAccountService;
	@Autowired private AdminPostService adminPostService;
	@Autowired private AdminTagCateService adminTagCateService;
	@Autowired private AccountService accountService;
	
	
	//사진 리스트 출력용 컨트롤러
	//권한 : 모두. 접근기능은 권한별 한정하기.
	@RequestMapping("/list")
	public String index(ModelMap model,
			@RequestParam(value="checksum", required = false) Integer checksum,
			@RequestParam(value="page", required = false) Integer page,
			@RequestParam(value="order", required = false) String order) {
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		Account account = ((SignedDetails) authentication.getPrincipal()).getAccount();
		
		model.addAttribute("account", account);
		
		if (page == null) page = 1;
		
		//1 : 모든 조건 충족된 포스트, 0: 아직 게시조건에 충족안되는 포스트, 2: 모든 포스
		if (checksum == null) checksum = 2;
		
		model.addAttribute("order", order);
		model.addAttribute("page", page);
		model.addAttribute("pages", Math.ceil((double)adminPostService.sizeOfPost() / 10));
		
		Collection<Post> items = adminPostService.findPost(page, order, checksum, account);
		
		Collection<TagCategory> tagCate = adminTagCateService.getTagCategories();
		HashMap<String, TagCategory> tagCateHashMap = new HashMap<String, TagCategory>();
		
		for(TagCategory tc : tagCate) {
			tagCateHashMap.put(String.valueOf(tc.getId()), tc);
		}
		
		
		DetailModifyStatus.viewCurrentStatus();
		
		
		model.addAttribute("detailModifyStatus", DetailModifyStatus.getCurrentModify());
		model.addAttribute("detailModifyStatusName", DetailModifyStatus.getCurrentModifyName());
		model.addAttribute("tagcate", tagCateHashMap);
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
