package com.osquare.mydearnest.test.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.post.service.PostService;
import com.osquare.mydearnest.test.service.ImageService;
import com.osquare.mydearnest.util.image.dominant.DominantColor;
import com.osquare.mydearnest.util.image.dominant.DominantColors;

@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired private PostService postService;
	//@Autowired private AccountService accountService;
	//@Autowired private AdminTagCateService adminTagCateService;
	//@Autowired private AdminPostService adminPostService;
	@Autowired private ImageService imageService;
	
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

	@RequestMapping(value = "/updateAvgColor" , method = RequestMethod.GET)
	public void updateAvgColor(Model model, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("========= start updateAvgColor() ============================");
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		List<ImageSource> imageSourceList =  imageService.getImageSourceList();
		
		Iterator<ImageSource> iterator = imageSourceList.iterator();
		
		while (iterator.hasNext()) {
			imageService.updateAvgColor(iterator.next().getId());			
		}
		
		System.out.println("========= end of updateAvgColor() ============================");
	}
	
	@RequestMapping(value = "/deleteThumbs" , method = RequestMethod.GET)
	public void deleteThumbs(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("========= start deleteThumbs() ============================");
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		imageService.deleteThumbs();

		System.out.println("========= end of deleteThumbs() ============================");
	}
	
	@RequestMapping(value = "/deleteThumbs/{postId}" , method = RequestMethod.GET)
	public void deleteThumbs(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("postId") Long postId) {
		
		System.out.println("========= start deleteThumbs() ============================");
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		imageService.deleteThumbs(postId);

		System.out.println("========= end of deleteThumbs() ============================");
	}
	
	@RequestMapping(value = "/copySources" , method = RequestMethod.GET)
	public void copySources(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("========= start copySources() ============================");
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		imageService.copySources();

		System.out.println("========= end of copySources() ============================");
	}	
}
