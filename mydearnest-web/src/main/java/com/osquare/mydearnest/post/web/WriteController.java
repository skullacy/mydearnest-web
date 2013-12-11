package com.osquare.mydearnest.post.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.post.service.CategoryService;
import com.osquare.mydearnest.post.service.FileService;
import com.osquare.mydearnest.post.service.PostService;
import com.osquare.mydearnest.post.validator.PostValidator;
import com.osquare.mydearnest.post.vo.PostVO;

@Controller
@RequestMapping("/write")
public class WriteController {

	@Autowired private CategoryService categoryService;
	@Autowired private PostService postService;
	@Autowired private FileService fileService;
	@Autowired private AccountService accountService;

	@RequestMapping(value = "/browse", method = RequestMethod.GET)
	public String browseFrame(Model model, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		response.addHeader("mxb-popup-width", "95%");
		model.addAttribute("layout", "shared/layout.blank.vm");
		
		return "write/browse_frame";
	}
	

	@RequestMapping(value = "/browse.do", method = RequestMethod.GET)
	public String browse(Model model, HttpServletRequest request, HttpServletResponse response) {

		PostVO postVO = new PostVO();
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();

		if (authentication.getPrincipal() instanceof SignedDetails) {
			SignedDetails principal = (SignedDetails) authentication.getPrincipal();
			Account account = accountService.findAccountById(principal.getAccountId());

			model.addAttribute("rootCategories", categoryService.getRootCategories());
			model.addAttribute("haveFolders", postService.getUserFolders(account));
			
		}

		model.addAttribute("command", postVO);
		model.addAttribute("layout", "./shared/layout.default.vm");
		return "write/browse.html";
		
	}

	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String modify(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("command") PostVO postVO,
			BindingResult result) {

		model.addAttribute("success", false);
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";

		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		Account account = accountService.findAccountById(principal.getAccountId());

		model.addAttribute("account", account);
		model.addAttribute("command", postVO);

		new PostValidator().validate(postVO, result);
		if (result.hasErrors()) {
			model.addAttribute("errors", result.getAllErrors());
		}
		else {
			ImageSource imageSource = null;
			if (postVO.getThumbnail() != null && postVO.getThumbnail().getSize() > 0) {
				imageSource = fileService.createImageSourceForData(postVO.getThumbnail());
			}
			
			Post post = postService.createPost(account, imageSource, postVO);
			if (post == null) {
				model.addAttribute("success", false);
			}
			else {
				model.addAttribute("success", true);
				model.addAttribute("redirect_uri", request.getContextPath() + "/write/complete.do");
			}
		}

		return "account/join_detail_result";
	}

	@RequestMapping(value = "/complete.do", method = RequestMethod.GET)
	public String complete(Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("layout", "shared/layout.blank.vm");
		return "write/complete";
	}

}
