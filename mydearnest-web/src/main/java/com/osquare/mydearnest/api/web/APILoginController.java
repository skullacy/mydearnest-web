package com.osquare.mydearnest.api.web;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junglebird.webframe.vo.SignedDetails;
import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.entity.Account;

@Controller
@RequestMapping("/api/login")
public class APILoginController {
	
	@Autowired private SessionFactory sessionFactory;
	@Autowired private AccountService accountService;

	//deprecate
	@RequestMapping("/login.check")
	public ResponseEntity<String> loginCheck() {
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type",	"application/json; charset=UTF-8");

		JSONObject document = new JSONObject();
		document.put("success", false);
		
		//10 : 로그인안됨.
		//11 : 로그인성공.
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) {
			document.put("success", true);
			document.put("message", "10");
		}
		else {
			SignedDetails principal = (SignedDetails) authentication.getPrincipal();
			Account account = accountService.findAccountById(principal.getAccountId());
			
			document.put("success", true);
			document.put("user_name", account.getName());
			document.put("user_email", account.getEmail());
		}

		

		return new ResponseEntity<String>(document.toString(), responseHeaders, HttpStatus.OK);
	}
	
}
