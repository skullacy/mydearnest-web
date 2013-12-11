package com.osquare.mydearnest.account.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.api.vo.ResultObject;
import com.osquare.mydearnest.entity.Account;

@Controller
@RequestMapping("")
public class LoginController {
	@Autowired private AccountService accountService;
	@Autowired private MessageSource messageSource;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");

		response.addHeader("mxb-popup-width", "100%");
		model.addAttribute("layout", "shared/layout.blank.vm");
		
		return "account/login_frame";
	}
	
	
	@RequestMapping(value="/auth/login", method=RequestMethod.GET)
	public String login(ModelMap model, HttpServletRequest request, @RequestParam(value="error", required = false) String error)
	{
		model.addAttribute("error", error);
		if(error != null && !error.equals(""))
		{
			Cookie[] cookies = request.getCookies();
			for(Cookie cookie : cookies)
			{
				if (!cookie.getName().equals("fail_username")) continue;
				model.addAttribute("j_username", cookie.getValue());
			}
		}
		return "auth/login";
	}

	
	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public String help(Model model, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");

		response.addHeader("mxb-popup-width", "100%");
		model.addAttribute("layout", "shared/layout.blank.vm");
		
		return "account/help_frame";
	}
	
	
	@RequestMapping(value="/auth/help", method=RequestMethod.GET)
	public String help(ModelMap model, @RequestParam(value = "error", required = false) String error, @RequestParam(value = "success", required = false) String success)
	{
		model.addAttribute("error", error);
		model.addAttribute("success", success);
		return "auth/help";
	}
	
	@RequestMapping(value="/auth/help", method=RequestMethod.POST)
	public String help(ModelMap model, HttpServletRequest request, @RequestParam("mailAddress") String mailAddress) throws UnsupportedEncodingException
	{
		Account account = accountService.refreshAccountPassword(mailAddress);
		if(account == null)
			return "redirect:/auth/help?error=" + URLEncoder.encode(mailAddress, "UTF-8");
		else {
			return "redirect:/auth/help_complete?mail=" + URLEncoder.encode(mailAddress, "UTF-8");
		}
		
	}

	@RequestMapping(value="/auth/help_complete", method=RequestMethod.GET)
	public String help(ModelMap model, @RequestParam("mail") String mailAddress)
	{
		model.addAttribute("mailAddress", mailAddress);
		return "auth/help_ok";
	}
	

	@RequestMapping(value="/auth/help.ajax", method=RequestMethod.POST)
	public ResponseEntity<String> help(@RequestParam("mailAddress") String mailAddress)
	{
		ResultObject result = new ResultObject();
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		
		Pattern p = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");
		if( !p.matcher(mailAddress).matches() ) {
			result.setSuccess(false);
			result.setMessage(messageSource.getMessage("error.valid.mail_address", null, Locale.getDefault()));

			return new ResponseEntity<String>(JSONObject.fromObject(result).toString(), responseHeaders, HttpStatus.CREATED);
		}
		
		Account account = accountService.refreshAccountPassword(mailAddress);
		result.setSuccess((account != null));
		result.setMessage((account != null) ? null : messageSource.getMessage("error.notfound.account", null, Locale.getDefault()));

		return new ResponseEntity<String>(JSONObject.fromObject(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	
	@RequestMapping("/auth/success-to-index")
	public String successToIndex(ModelMap model) { return "auth/success_to_index"; }

	@RequestMapping("/auth/success-to-redirect")
	public String successToRedirect(ModelMap model) { return "auth/success_to_redirect"; }

	@RequestMapping("/auth/success-to-alert")
	public String successToAlert(ModelMap model) { return "auth/success_to_alert"; }

	@RequestMapping("/auth/failure-to-login")
	public String failureToLogin(ModelMap model) { return "auth/failure_to_login"; }

}