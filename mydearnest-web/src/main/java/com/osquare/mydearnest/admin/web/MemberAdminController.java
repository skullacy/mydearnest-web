package com.osquare.mydearnest.admin.web;

import javax.servlet.http.HttpServletResponse;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.osquare.mydearnest.admin.service.AdminAccountService;
import com.osquare.mydearnest.entity.Account;

@Controller
@RequestMapping("/admin/management/member")
public class MemberAdminController {
	
	@Autowired private AdminAccountService adminAccountService;
	
	@RequestMapping("")
	public String index(ModelMap model, @RequestParam(value="page", required = false) Integer page) {
		
		if (page == null) page = 1;
		model.addAttribute("page", page);
		model.addAttribute("pages", Math.ceil((double)adminAccountService.sizeOfAccount() / 20));
		model.addAttribute("items", adminAccountService.findAccount(page));
		model.addAttribute("page_on", "member");
		
		return "admin/member";
	}

	@RequestMapping("/updateRole")
	public void updateRole(ModelMap model, HttpServletResponse response, @RequestParam(value="userId") String userId, @RequestParam(value="role") String role) {
		Account account = adminAccountService.updateRole(userId, role);
		if (account == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
}
