package com.osquare.mydearnest.test.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

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

import com.junglebird.webframe.vo.SignedDetails;
import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.entity.PostGrade;
import com.osquare.mydearnest.entity.PostUserGrade;
import com.osquare.mydearnest.post.service.PostService;
import com.osquare.mydearnest.post.validator.PostGradeValidator;
import com.osquare.mydearnest.post.vo.PostVO;
import com.osquare.mydearnest.util.image.dominant.DominantColor;
import com.osquare.mydearnest.util.image.dominant.DominantColors;

@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired private PostService postService;
	@Autowired private AccountService accountService;
	
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

	/**
	 * @brief
	 * 사진 평가 입력 화면
	 */
	@RequestMapping(value = "/grade/{postId}", method = RequestMethod.GET)
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
	}
	
	/**
	 * @brief
	 * 사진 평가 입력 처리
	 */
	@RequestMapping(value = "/grade/{postId}", method = RequestMethod.POST)
	public String insertPostUserGrade(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "pagetype", required = false) String pageType,
			@RequestParam(value = "redirectType", required = false) String redirectType,
			@PathVariable("postId") Long postId,
			@ModelAttribute("command") PostVO postVO,
			BindingResult result) {
		
		model.addAttribute("success", false);
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";

		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		Account account = accountService.findAccountById(principal.getAccountId());
		
		Post post = postService.getPostById(postId);

		model.addAttribute("account", account);
		model.addAttribute("command", postVO);

		new PostGradeValidator().validate(postVO, result);
		
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			model.addAttribute("errors", result.getAllErrors());
		}
		else {
			PostGrade postGrade = null;
			if("update".equals(pageType)) {
				postGrade = postService.updatePostGrade(post, account, postVO);
			}
			else {
				postGrade = postService.createPostGrade(post, account, postVO);
			}
			
			if (postGrade == null) {
				model.addAttribute("success", false);
			}
			else {
				model.addAttribute("success", true);
//				model.addAttribute("redirect_uri", request.getContextPath() + "/write/complete.do");
			}
		}
		

		//Submit 후 리다이렉트 지정되지 않은경우.
		if(redirectType == null) {
			
			//미완료된 포스트중 랜덤 호출.
			Post redirectPost = postService.getPostByRandom(0, "grade", account);
			
			if(redirectPost == null) {
				return "redirect:/admin";
			}
			else {
				return "redirect:/admin/write/grade/"+redirectPost.getId();
			}
			
		}
		else {
			return "redirect:/admin/write/" + redirectType + "/" +  post.getId();
		}
		
	}
	
}
