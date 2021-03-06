package com.osquare.mydearnest.admin.web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.entity.PostGrade;
import com.osquare.mydearnest.entity.PostTag;
import com.osquare.mydearnest.entity.TagCategory;
import com.osquare.mydearnest.post.service.FileService;
import com.osquare.mydearnest.post.service.PostService;
import com.osquare.mydearnest.post.validator.PostGradeValidator;
import com.osquare.mydearnest.post.validator.PostUploadValidator;
import com.osquare.mydearnest.post.vo.PostVO;
import com.osquare.mydearnest.util.DetailModifyStatus;
import com.osquare.mydearnest.util.image.dominant.DominantColor;
import com.osquare.mydearnest.util.image.dominant.DominantColors;

@Controller
@RequestMapping("/admin/write")
public class WriteAdminController {

	@Autowired private PostService postService;
	@Autowired private FileService fileService;
	@Autowired private AccountService accountService;
	@Autowired private AdminTagCateService adminTagCateService;
	@Autowired private PropertiesManager pm;
	
	/**
	 * @brief
	 * 사진 업로드 화면
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String uploadPost(Model model, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		return "admin/post_photoupload";
	}
	
	/**
	 * @brief
	 * 사진 업로드 처리 
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadPostSubmit(Model model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("command") PostVO postVO,
			BindingResult result) {
		
		model.addAttribute("success", false);
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";

		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		Account account = accountService.findAccountById(principal.getAccountId());

		model.addAttribute("account", account);
		model.addAttribute("command", postVO);

		new PostUploadValidator().validate(postVO, result);
		
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			model.addAttribute("errors", result.getAllErrors());
		}
		else {
			ImageSource imageSource = null;
			if (postVO.getThumbnail() != null && postVO.getThumbnail().getSize() > 0) {
				imageSource = fileService.createImageSourceForData(postVO.getThumbnail());
			}
			
			Post post = postService.createPostUpload(account, imageSource, postVO);
			if (post == null) {
				model.addAttribute("success", false);
			}
			else {
				model.addAttribute("success", true);
				model.addAttribute("redirect_uri", request.getContextPath() + "/admin/write/submit.do");
			}
		}

		return "redirect:/admin/write/upload";
	}
	
	/**
	 * @brief
	 * 사진 업로드 취소처리 (Chrome Extension API)
	 * @memo 
	 * api컨트롤러에 올려야 정상이지만, 권한설정을 위해 해당 컨트롤러로 이동.
	 */
	@RequestMapping(value = "/upload/delete/{postId}", method = RequestMethod.POST)
	public ResponseEntity<String> uploadPostDelete(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("postId") long postId) {
		
		
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type",	"application/json; charset=UTF-8");

		JSONObject document = new JSONObject();
		document.put("success", false);
		
		//1 : 성공
		//10 : 로그인안됨.
		//12 : 본인이 올린 사진이 아님.
		//13 : 서버에서 사진 삭제중 에러발생함.
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) {
			document.put("message", "10");
			
			return new ResponseEntity<String>(document.toString(), responseHeaders, HttpStatus.FORBIDDEN);
		}
		
		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		Account account = accountService.findAccountById(principal.getAccountId());
		
		//포스트 정보 가져오기
		Post post = postService.getPostById(postId);
		
		//본인이 올린 사진이 아닐경우 해당 메세지 리턴.
		//skullacy modified : 계정이 어드민인 경우는 그냥 진행 가능하게 코드 변동.
		if( (post.getAccount().getId() == account.getId()) || "ROLE_ADMIN".equals(account.getRole()) ) {
			//포스트 삭제시 한번 더 유효성 체크(권한검사)를 실행한다.
			Boolean result = postService.deletePost(postId);
			
			if(result) {
				document.put("success", true);
				document.put("message", "1");
			}
			else {
				document.put("message", 13);
				return new ResponseEntity<String>(document.toString(), responseHeaders, HttpStatus.FORBIDDEN);
			}
		}
		else {
			document.put("message", "12");
			return new ResponseEntity<String>(document.toString(), responseHeaders, HttpStatus.FORBIDDEN);
		}
		
		
		
		return new ResponseEntity<String>(document.toString(), responseHeaders, HttpStatus.OK);
	}
	
	
	/**
	 * @brief
	 * 사진 태그 입력 화면
	 */
	@RequestMapping(value = "/phototag/{postId}", method = RequestMethod.GET)
	public String insertPhotoTag(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "from", required = false) String from,
			@PathVariable("postId") long postId) {
		
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		//구글 확장프로그램을 통해서 해당 뷰에 접근 했을경우.
		if("chromiumextension".equals(from)) {
			model.addAttribute("fromClipping", true);
		}
		
		//포스트 정보 가져오기
		Post post = postService.getPostById(postId);
		Collection<TagCategory> tagCate = adminTagCateService.getTagCategories();
		HashMap<String, TagCategory> tagCateHashMap = new HashMap<String, TagCategory>();
		
		for(TagCategory tc : tagCate) {
			tagCateHashMap.put(String.valueOf(tc.getId()), tc);
		}
		
		model.addAttribute("tagcate", tagCateHashMap);
		model.addAttribute("photoTags", post.getPhotoTags());
		
		model.addAttribute("post", post);
		
		model.addAttribute("layout", "./shared/layout.admin.vm");
		
		return "admin/post_phototag";
	}
	
	/**
	 * @brief
	 * 사진 태그 입력 처리
	 */
	@RequestMapping(value = "/phototag/{postId}", method = RequestMethod.POST)
	public String insertPhotoTagSubmit(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "redirectType", required = false) String redirectType,
			@PathVariable("postId") long postId,
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

//		new PostGradeValidator().validate(postVO, result);
//		
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			model.addAttribute("errors", result.getAllErrors());
		}
		else {
			
			Post postResult = postService.createPostPhotoTag(post, account, postVO);
			if ( postResult == null) {
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
			Post redirectPost = postService.getPostByRandom(0, "phototag", account);
			
			if(redirectPost == null) {
				return "redirect:/admin";
			}
			else {
				return "redirect:/admin/write/phototag/"+redirectPost.getId();
			}
			
		}
		else {
			return "redirect:/admin/write/" + redirectType + "/" +  post.getId();
		}
		
		

		
	}
	
	
	
	
	/**
	 * @brief
	 * 사진 상세정보 입력 화면
	 */
	@RequestMapping(value = "/detail/{postId}" , method = RequestMethod.GET)
	public String insertPostDetail(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("postId") Long postId) {
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		//포스트 정보 가져오기
		Post post = postService.getPostById(postId);
		
		Collection<TagCategory> tagCate = adminTagCateService.getTagCategories();
		
		model.addAttribute("tagcate", tagCate);
		model.addAttribute("post", post);
		
		// getDominantColor() 실행
		Iterator<PostTag> postTags = post.getPostTag().iterator();
		boolean postTagFlag = true;
		
		while (postTags.hasNext()) {
			String value = postTags.next().getValue();
			if (value != null && value.trim() != "") postTagFlag = false;
		}
		
		if (postTagFlag) {
			BufferedImage bufferedImage = null;
			
			try {
				System.out.println(new URL(pm.get("web_url")+"/mdn-image/thumb/"+post.getImageSource().getId()+"?w=200&t=ratio"));
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
		
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";

		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		Account account = accountService.findAccountById(principal.getAccountId());
		
		
		if(!DetailModifyStatus.updateModifyStatus(account, post)) return "redirect:/admin";
		
		return "admin/post_detail";
	}
	
	/**
	 * @brief
	 * 사진 상세정보 입력 처리 
	 */
	@RequestMapping(value = "/detail/{postId}", method = RequestMethod.POST)
	public String insertPostDetailSubmit(Model model, HttpServletRequest request, HttpServletResponse response,
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

//		new PostGradeValidator().validate(postVO, result);
//		
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
//				model.addAttribute("redirect_uri", request.getContextPath() + "/write/complete.do");
			}
		}
		
		
		
		//Submit 후 리다이렉트 지정되지 않은경우.
		if(redirectType == null) {
			
			//미완료된 포스트중 랜덤 호출.
			Post redirectPost = postService.getPostByRandom(0, "detail", account);
			
			DetailModifyStatus.releaseModifyStatus(account);
			
			if(redirectPost == null) {
				return "redirect:/admin";
			}
			else {
				return "redirect:/admin/write/detail/"+redirectPost.getId();
			}
			
		}
		else {
			return "redirect:/admin/write/" + redirectType + "/" +  post.getId();
		}
				
		
		
				
	}
	
	/**
	 * @brief
	 * 사진 평가 입력 화면
	 */
	@RequestMapping(value = "/grade/{postId}", method = RequestMethod.GET)
	public String insertPostGrade(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("postId") Long postId,
			@RequestParam(value = "pagetype", required = false) String pagetype) {
		
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";

		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		Account account = accountService.findAccountById(principal.getAccountId());
		
//		Post checkPost = postService.getPostByRandom(0, "grade", account);
//		if(checkPost == null) {
//			System.out.println("더할거없음");
//		}
//		else {
//			System.out.println(checkPost.getId());
//		}
		
		
		Post post = postService.getPostById(postId);
		model.addAttribute("post", post);
		
		if("update".equals(pagetype)) {
			PostGrade postGrade = postService.getMyPostGradeByPost(account, post);
			model.addAttribute("command", postGrade);
			model.addAttribute("pagetype", pagetype);
		}
		else {
			model.addAttribute("command", new PostGrade());
		}
		
		model.addAttribute("layout", "./shared/layout.admin.vm");
		
		return "admin/post_grade";
	}
	
	/**
	 * @brief
	 * 사진 평가 입력 처리
	 */
	@RequestMapping(value = "/grade/{postId}", method = RequestMethod.POST)
	public String insertPostGrade(Model model, HttpServletRequest request, HttpServletResponse response,
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
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/browse", method = RequestMethod.GET)
	public String browseFrame(Model model, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		response.addHeader("mxb-popup-width", "95%");
		model.addAttribute("layout", "shared/layout.blank.vm"); 
		
		return "write/browse_frame";
		
		
	}
	

//	@RequestMapping(value = "/browse.do", method = RequestMethod.GET)
//	public String browse(Model model, HttpServletRequest request, HttpServletResponse response) {
//		PostVO postVO = new PostVO();
//		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
//
//		if (authentication.getPrincipal() instanceof SignedDetails) {
//			SignedDetails principal = (SignedDetails) authentication.getPrincipal();
//			Account account = accountService.findAccountById(principal.getAccountId());
//
//			model.addAttribute("rootCategories", categoryService.getRootCategories());
//			model.addAttribute("haveFolders", postService.getUserFolders(account));
//			
//		}
//
//		model.addAttribute("command", postVO);
//		model.addAttribute("layout", "./shared/layout.default.vm");
//		return "write/browse.html";
//		
//		
//	}
//
//	@RequestMapping(value = "/submit", method = RequestMethod.POST)
//	public String modify(ModelMap model, HttpServletRequest request,
//			HttpServletResponse response,
//			@ModelAttribute("command") PostVO postVO,
//			BindingResult result) {
//
//		model.addAttribute("success", false);
//		
//		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
//		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";
//
//		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
//		Account account = accountService.findAccountById(principal.getAccountId());
//
//		model.addAttribute("account", account);
//		model.addAttribute("command", postVO);
//
//		new PostValidator().validate(postVO, result);
//		if (result.hasErrors()) {
//			System.out.println(result.getAllErrors());
//			model.addAttribute("errors", result.getAllErrors());
//		}
//		else {
//			ImageSource imageSource = null;
//			if (postVO.getThumbnail() != null && postVO.getThumbnail().getSize() > 0) {
//				imageSource = fileService.createImageSourceForData(postVO.getThumbnail());
//			}
//			
//			Post post = postService.createPost(account, imageSource, postVO);
//			if (post == null) {
//				model.addAttribute("success", false);
//			}
//			else {
//				model.addAttribute("success", true);
//				model.addAttribute("redirect_uri", request.getContextPath() + "/write/complete.do");
//			}
//		}
//
//		return "account/join_detail_result";
//	}
//
	@RequestMapping(value = "/submit.do", method = RequestMethod.GET)
	public String complete(Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("layout", "shared/layout.blank.vm");
		return "admin/submit";
	}

}
