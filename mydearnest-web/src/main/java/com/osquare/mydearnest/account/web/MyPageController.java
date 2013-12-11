package com.osquare.mydearnest.account.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.junglebird.webframe.common.PropertiesManager;
import com.junglebird.webframe.vo.SignedDetails;
import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.post.service.PostService;
import com.osquare.mydearnest.profile.service.NotifyService;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

	@Autowired private AccountService accountService;
	@Autowired private PostService postService;
	@Autowired private NotifyService notifyService;
	@Autowired private PropertiesManager pm;
	
	private Account setUser(ModelMap model) {
		return null;
	}

	
	@RequestMapping("/history")
	public String history(ModelMap model) {
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";
		
		this.setUser(model);
		return "mypage/history";
	}

	@RequestMapping("/history.ajax")
	public String history_ajax(ModelMap model,
			@RequestParam(value = "page", required = false) Integer page) {

		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";
		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		
		if (page == null) page = 1;
		
		model.addAttribute("page", page);
		model.addAttribute("items", notifyService.getHistoryByAccountId(principal.getAccountId(), page));
		model.addAttribute("layout", "./shared/layout.blank.vm");
		return "mypage/history.ajax";
	}

	@RequestMapping(value = "/complete", method = RequestMethod.GET)
	public String complete(Model model) {
		return "mypage/message_ok";
	}

}
