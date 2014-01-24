package com.osquare.mydearnest.test.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

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
import com.osquare.mydearnest.admin.service.AdminTagCateService;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.entity.PostTag;
import com.osquare.mydearnest.entity.TagCategory;
import com.osquare.mydearnest.post.service.PostService;
import com.osquare.mydearnest.post.vo.PostVO;
import com.osquare.mydearnest.test.service.ColorTagUpdate;
import com.osquare.mydearnest.util.DetailModifyStatus;
import com.osquare.mydearnest.util.image.dominant.DominantColor;
import com.osquare.mydearnest.util.image.dominant.DominantColors;

@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired private PostService postService;
	@Autowired private AccountService accountService;
	@Autowired private ColorTagUpdate colorTagUpdate;
	@Autowired private AdminTagCateService adminTagCateService;
	@Autowired private PropertiesManager pm;
	
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
	
	/**
	 * @brief
	 * 사진 평가 입력 처리
	 */
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

	@RequestMapping(value = "/posts" , method = RequestMethod.GET)
	public String getWrongColorTags(HttpServletRequest request, HttpServletResponse response) {
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		List<PostTag> list = colorTagUpdate.getWrongColorTags();
		Set<Long> postIdSet = new HashSet<Long>();
		
		int listSize = list.size();
		for (int i = 0; i < listSize-1 ; i++) {
			long postId = list.get(i).getPost().getId();
			if (postId == list.get(i+1).getPost().getId()) postIdSet.add(postId);
		}
		
		System.out.println("Wrong post's id: " + postIdSet);
		
		if (postIdSet.isEmpty()) {
			return "redirect:/";
		}
		else {
			return "redirect:/test/detail/"+postIdSet.iterator().next();			
		}
	}
	
	@RequestMapping(value = "/detail/{postId}" , method = RequestMethod.GET)
	public String insertPostDetail(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("postId") Long postId) {
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		Post post = postService.getPostById(postId);
		
		Collection<TagCategory> tagCate = adminTagCateService.getTagCategories();
		
		model.addAttribute("tagcate", tagCate);
		model.addAttribute("post", post);
		
		Iterator<PostTag> postTags = post.getPostTag().iterator();
		boolean postTagFlag = true;
		
		while (postTags.hasNext()) {
			String value = postTags.next().getValue();
			if (value != null && value.trim() != "") postTagFlag = false;
		}
		
		if (postTagFlag) {
			BufferedImage bufferedImage = null;
			
			try {
				bufferedImage = ImageIO.read(new URL(pm.get("web_url")+"/mdn-image/thumb/"+post.getImageSource().getId()+"?w=200&t=ratio"));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			DominantColor[] colors = DominantColors.getDominantColor(bufferedImage, 3, 0.1);
			
			TreeMap<Float, String> hexTree = new TreeMap<Float, String>();
			List<String> hexes = new ArrayList<String>();
			int colorsLength = colors.length;
			for (int i = 0; i < colorsLength; i++) {
				hexTree.put(colors[i].getPercentage(), colors[i].getRGBHex());
			}
			for (int i = 0; i < colorsLength; i++) {
				hexes.add(hexTree.pollLastEntry().getValue());
			}
			
			model.addAttribute("hexes", hexes);
		}
		
		model.addAttribute("layout", "./shared/layout.admin.vm");
		
		return "test/post_detail";
	}
	
	@RequestMapping(value = "/detail/{postId}" , method = RequestMethod.POST)
	public String updateWorngColorTags(Model model, HttpServletRequest request, HttpServletResponse response,
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
		
		if (result.hasErrors()) {
			System.out.println("Write ERRORaslgdk;jasdl;fkasf");
			System.out.println(result.getAllErrors());
			model.addAttribute("errors", result.getAllErrors());
		}
		else {
			
			Post postResult = postService.createPostDetail(post, account, postVO);
			if ( postResult == null) {
				model.addAttribute("success", false);
			}
			else {
				model.addAttribute("success", true);
			}
		}
		
		return "redirect:/test/posts";
	}
}
