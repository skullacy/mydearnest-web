package com.osquare.mydearnest.admin.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.junglebird.webframe.vo.SignedDetails;
import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.admin.service.AdminAccountService;
import com.osquare.mydearnest.admin.vo.AdminAccountStatusVO;
import com.osquare.mydearnest.entity.Account;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired private AccountService accountService;
	@Autowired private AdminAccountService adminAccountService;
	
	@RequestMapping("")
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";

		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		Account account = accountService.findAccountById(principal.getAccountId());
		
		request.getSession(true).setAttribute("accountStatus", adminAccountService.getAccountStatus(account));
		
		return "redirect:/admin/post/list";
	}
	
	@RequestMapping("/management")
	public String managementIndex(Model model, HttpServletRequest request, HttpServletResponse response) {
		return "redirect:/admin/management/member";
	}
	
	
	
	
	
	
}
