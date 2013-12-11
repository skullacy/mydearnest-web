package com.osquare.mydearnest.account.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.junglebird.webframe.vo.SignedDetails;
import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.account.validator.AccountValidator;
import com.osquare.mydearnest.account.vo.AccountDetails;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.post.service.FileService;

@Controller
public class ModifyController {
	
	
	@Autowired private AccountService accountService;
	@Autowired private FileService fileService;
	@Autowired private ShaPasswordEncoder passwordEncoder;
	@Autowired private MessageSource messageSource;

	@RequestMapping(value = "/mypage/modify", method = RequestMethod.GET)
	public String join(Model model, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		response.addHeader("mxb-popup-width", "95%");
		model.addAttribute("layout", "shared/layout.blank.vm");
		
		return "account/modify_frame";
	}
	
	@RequestMapping(value = "/auth/modify", method = RequestMethod.GET)
	public String modify(ModelMap model, HttpSession session) {

		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";

		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		Account account = accountService.findAccountById(principal.getAccountId());
		
		AccountDetails vo = new AccountDetails();
		vo.setUsername(account.getName());
		vo.setImageSourceId(account.getImageId());
		vo.setRegion(account.getRegion());
		vo.setBio(account.getBio());
				
		model.addAttribute("account", account);
		model.addAttribute("command", vo);
		return "account/modify";
	}

	/***
	 * 정보수정(POST)
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/auth/modify", method = RequestMethod.POST)
	public String modify(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("command") AccountDetails vo,
			BindingResult result) {

		vo.setAgree("true");
		model.addAttribute("success", false);
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";

		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		Account account = accountService.findAccountById(principal.getAccountId());

		model.addAttribute("account", account);
		model.addAttribute("command", vo);
		
		new AccountValidator().validate(vo, result);

		if (result.hasErrors()) {
			model.addAttribute("errors", result.getAllErrors());
		}
		else {
			ImageSource imageSource = null;
			if (vo.getThumbnail() != null && vo.getThumbnail().getSize() > 0) {
				imageSource = fileService.createImageSourceForData(vo.getThumbnail());
			}
			
			Account account1 = accountService.updateAccount(account.getId(), vo, imageSource);
			if (account1 == null) {
				model.addAttribute("success", false);
			}
			else {
				model.addAttribute("success", true);
				model.addAttribute("redirect_uri", request.getContextPath() + "/auth/modify/complete");
			}
		}

		return "account/join_detail_result";
	}

	@RequestMapping(value = "/auth/modify/complete", method = RequestMethod.GET)
	public String complete(Model model) {

		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";

		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		model.addAttribute("userId", principal.getAccountId());
		return "account/modify_ok";
	}
	
}
