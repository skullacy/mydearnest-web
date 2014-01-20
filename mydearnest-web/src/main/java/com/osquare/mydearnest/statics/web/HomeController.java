package com.osquare.mydearnest.statics.web;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.junglebird.webframe.common.StringUtils;
import com.junglebird.webframe.vo.SignedDetails;
import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.post.service.CategoryService;
import com.osquare.mydearnest.post.service.PostService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired private AccountService accountService;
	@Autowired private PostService postService;
	@Autowired private CategoryService categoryService;
	
	@RequestMapping("")
	public String index(ModelMap model, @RequestParam(value = "p", required = false) String p, 
			HttpServletRequest request, HttpServletResponse response, Locale locale) {
		
		logger.info("Welcome home! The client locale is {}.", locale);
		logger.info(model.toString());
		logger.info(request.toString());
		logger.info(response.toString());
		logger.info(request.getRequestURL().toString());
		logger.info(p);
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (authentication.getPrincipal() instanceof SignedDetails) {
			logger.info("1234");
			SignedDetails details = (SignedDetails) authentication.getPrincipal();
			Account account = accountService.findAccountById(details.getAccountId());
			model.addAttribute("user_image_id", account.getImageId());
		}
		logger.info("2345");
		
		
		
		model.addAttribute("p", p);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		model.addAttribute("p", p);
		model.addAttribute("categories", categoryService.getRootCategories());
		
		return "home/index";
	}
	
	@RequestMapping("/blank")
	public String blank(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		return "home/blank";
		
	}
	
	@RequestMapping("/self-close")
	public String tos(HttpServletResponse response) {
		return "self_close";
	}
	
}
