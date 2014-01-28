package com.osquare.mydearnest.test.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.junglebird.webframe.common.PropertiesManager;
import com.junglebird.webframe.vo.SignedDetails;
import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.admin.service.AdminPostService;
import com.osquare.mydearnest.admin.service.AdminTagCateService;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.entity.PostTag;
import com.osquare.mydearnest.post.service.FileServiceImpl;
import com.osquare.mydearnest.post.service.PostService;
import com.osquare.mydearnest.post.vo.PostVO;
import com.osquare.mydearnest.util.image.dominant.DominantColor;
import com.osquare.mydearnest.util.image.dominant.DominantColors;

@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired private PostService postService;
	@Autowired private AccountService accountService;
	@Autowired private AdminTagCateService adminTagCateService;
	@Autowired private PropertiesManager pm;
	@Autowired private AdminPostService adminPostService;
	
	public static final double minDiff1 = 0.1;
	public static final double minDiff2 = 0.9;
	
	@RequestMapping("/index/{testId}")
	public String adminIndex(Model model, HttpServletRequest request,
			@PathVariable("testId") String testFilename){
		
		File fileDir = new File("/Users/skullacy/git/mydearnest-web/mydearnest-web/src/main/webapp/images/test");
		File aFile = new File(fileDir, testFilename + ".jpg");
		
		BufferedImage img;
		try {
			img = ImageIO.read(aFile);
			DominantColor[] test = DominantColors.getDominantColor(img, 4, minDiff1);
			DominantColor[] test2 = DominantColors.getDominantColor(img, 4, minDiff2);
			model.addAttribute("colors", test);
			model.addAttribute("colors2", test2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		model.addAttribute("layout", "./shared/layout.blank.vm");
		model.addAttribute("file", "/images/test/"+aFile.getName());
		
		
		return "test/index";
	}

// post user grade
/*	@RequestMapping(value = "/grade/{postId}", method = RequestMethod.GET)
	public String insertPostUserGrade(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("postId") long postId) {
		
		// POST 공통처리 파트
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";

		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		Account account = accountService.findAccountById(principal.getAccountId());
		
		Post post = postService.getPostById(postId);
		model.addAttribute("post", post);

		// Grade 가져오는 파트
		Collection<PostGrade> postGrades = postService.getPostGradeByPost(post);
		Collection<PostUserGrade> postUserGrades = postService.getPostUserGradeByPost(post);
		
		PostUserGrade myPostUserGrade = null;
		
		if (postUserGrades != null) {
			Iterator<PostUserGrade> iterator = postUserGrades.iterator();
			
			while (iterator.hasNext()) {
				myPostUserGrade = iterator.next();
			
				if (myPostUserGrade.getAccount().getId() == account.getId()) break;
			}
		}
		
		model.addAttribute("postGrades", postGrades);
		model.addAttribute("postUserGrades", postUserGrades);
		model.addAttribute("myPostUserGrade", myPostUserGrade);

		model.addAttribute("layout", "./shared/layout.admin.vm");
		
		return "test/grade";
	}*/
	
// 미작성 파트
/*	@RequestMapping(value = "/grade/{postId}", method = RequestMethod.POST)
	public String insertPostUserGrade(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("postId") Long postId,
			@RequestBody List<Integer> postUserGradeList,
			BindingResult result) {
		
		model.addAttribute("success", false);
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";

		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		Account account = accountService.findAccountById(principal.getAccountId());
		
		Post post = postService.getPostById(postId);

		model.addAttribute("account", account);

		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			model.addAttribute("errors", result.getAllErrors());
		}
		else {
			int listSize = postUserGradeList.size();
			for (int i = 0; i < listSize; i++) {
				postUserGradeList.get(0)
			}
			
			PostUserGrade postUserGrade = postService.updatePostUserGrade(post, account, postUserGrade1);
			
			if (postUserGrade == null) {
				model.addAttribute("success", false);
			}
			else {
				model.addAttribute("success", true);
			}
		}

		return "redirect:/grade/" +  post.getId();
	}*/

	@RequestMapping(value = "/update/avgColor" , method = RequestMethod.GET)
	public String updateAvgColor(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		FileServiceImpl fileServiceImpl = new FileServiceImpl();
		//fileServiceImpl.getAveColor(img);
		
		return null;
	}
	
}
