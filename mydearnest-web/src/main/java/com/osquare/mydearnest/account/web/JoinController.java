package com.osquare.mydearnest.account.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.junglebird.webframe.CookieHelper;
import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.account.validator.AccountValidator;
import com.osquare.mydearnest.account.vo.AccountDetails;
import com.osquare.mydearnest.account.vo.JoinDefault;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.post.service.FileService;

@Controller
public class JoinController {

	@Autowired private AccountService accountService;
	@Autowired private MessageSource messageSource;
	@Autowired private AuthenticationSuccessHandler authorizeSuccessHandler;
	@Autowired private AuthenticationManager authenticationManager;
	@Autowired private FileService fileService;

	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join(Model model, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		response.addHeader("mxb-popup-width", "100%");
		model.addAttribute("layout", "shared/layout.blank.vm");
		
		return "account/join_frame";
	}
	
	
	@RequestMapping(value = "/auth/join", method = RequestMethod.GET)
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
	
		JoinDefault vo = new JoinDefault();
		CookieHelper cookie = CookieHelper.sync(request, response);
		if (cookie.getCookie("social_temp") != null) {
			JSONObject data = JSONObject.fromObject(cookie.getCookie("social_temp"));
			if (data.get("email") != null) vo.setMailAddress(data.getString("email"));
			if (data.getString("type").equals("facebook")) {
				vo.setFacebookId(data.getString("udata"));
				vo.setFacebookAccessToken(data.getString("data1"));
			}
		}
		
		model.addAttribute("command", vo);
		return "account/join";
	}

	@RequestMapping(value = "/auth/join", method = RequestMethod.POST)
	public String joinConfirm(Model model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("command") JoinDefault vo, BindingResult result) {

		new AccountValidator().validate(vo, result);
		if (!accountService.isNotFoundUser(vo.getMailAddress())) result.addError(new ObjectError("mailAddress", "이미 사용중인 아이디입니다."));
		if (result.hasErrors()) {
			model.addAttribute("errors", result.getAllErrors());
			model.addAttribute("command", vo);
			return "account/join";
		}
		
		try {
			Account account = accountService.createAccount(vo);
			return "redirect:/auth/join_detail?id=" + account.getId();
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errors", result.getAllErrors());
			model.addAttribute("command", vo);
			return "account/join";
		}
	}

	@RequestMapping(value = "/auth/join_detail", method = RequestMethod.GET)
	public String join_detail(Model model, 
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = false) Long id) {
				
		Account account = accountService.findAccountById(id);
		if (account == null || account.isEnabled()) return "redirect:/auth/join";

		AccountDetails vo = new AccountDetails();
		vo.setId(id);
		
		CookieHelper cookie = CookieHelper.sync(request, response);
		if (cookie.getCookie("social_temp") != null) {
			JSONObject data = JSONObject.fromObject(cookie.getCookie("social_temp"));
			if (data.get("name") != null) vo.setUsername(data.getString("name"));
		}
		
		model.addAttribute("command", vo);
		return "account/join_detail";
	}

	@RequestMapping(value = "/auth/join_detail", method = RequestMethod.POST)
	public String join_detail(Model model, 
			HttpServletRequest request, HttpServletResponse response, 
			@ModelAttribute("command") AccountDetails vo, BindingResult result) {
		
		model.addAttribute("success", false);
		
		new AccountValidator().validate(vo, result);
		if (result.hasErrors()) {
			model.addAttribute("errors", result.getAllErrors());
		}
		else {
			ImageSource img = null;
			if (vo.getThumbnail() != null && vo.getThumbnail().getSize() > 0) {
				img = fileService.createImageSourceForData(vo.getThumbnail());
			}
			Account account = accountService.enabledAccount(vo, img);
			if (account == null) {
				model.addAttribute("success", false);
			}
			else {
				model.addAttribute("success", true);
				model.addAttribute("redirect_uri", request.getContextPath() + "/auth/join/complete");
			}
		}

		return "account/join_detail_result";
	}
	
	@RequestMapping(value = "/auth/join/complete", method = RequestMethod.GET)
	public String complete(Model model) {
		
		
		return "account/join_ok";

	}
	
}
