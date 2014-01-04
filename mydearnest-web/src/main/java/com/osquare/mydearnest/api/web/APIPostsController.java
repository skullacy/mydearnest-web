package com.osquare.mydearnest.api.web;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.junglebird.webframe.vo.SignedDetails;
import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.post.service.FileService;
import com.osquare.mydearnest.post.service.PostService;
import com.osquare.mydearnest.post.vo.PostVO;

@Controller
@RequestMapping("/api/posts")
public class APIPostsController {

	@Autowired private PostService postService;
	@Autowired private FileService fileService;
	@Autowired private SessionFactory sessionFactory;
	@Autowired private AccountService accountService;

	//현재 사용안함 (App 개발시 사용할 예정)
	@RequestMapping("/list.json")
	public ResponseEntity<String> list(@RequestParam(value = "category", required = false) Long category, 
			@RequestParam(value = "page", required = false) Integer page) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type",	"application/json; charset=UTF-8");
		
		if (page == null) page = 1;
		
		Collection<Post> posts = postService.getPostsOrderByLatest(page, category);

		JSONObject document = new JSONObject();
		JSONArray array = new JSONArray();
		
		for(Post post : posts) {
			JSONObject object = new JSONObject();
			object.put("id", post.getId());
			//기존 방식 에러제거위해 주석처
//			object.put("title", post.getTitle());
			object.put("title", "1");
			object.put("image", post.getImageSource().getId());
			array.add(object);
		}
		
		document.put("success", posts != null);
		document.put("data", array);

		return new ResponseEntity<String>(document.toString(), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/upload.ge", method = RequestMethod.POST)
	public ResponseEntity<String> uploadGE(MultipartHttpServletRequest request, HttpServletResponse response) {
		
		PostVO postVO = new PostVO();
		postVO.setThumbnail((CommonsMultipartFile) request.getFile("thumbnail"));
		postVO.setSource(request.getParameter("source"));
		
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type",	"application/json; charset=UTF-8");
		
		JSONObject document = new JSONObject();
		
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) {
			document.put("success", false);
			return new ResponseEntity<String>(document.toString(), responseHeaders, HttpStatus.OK);
		}

		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		System.out.println(principal.getUsername());
		Account account = accountService.findAccountById(principal.getAccountId());
		
		ImageSource imageSource = null;
		imageSource = fileService.createImageSourceForData(postVO.getThumbnail());
		
		Post post = postService.createPostUpload(account, imageSource, postVO);
		
		if(post == null) {
			document.put("success", false);
		}
		else {
			document.put("success", true);
		}
		
		
		
		return new ResponseEntity<String>(document.toString(), responseHeaders, HttpStatus.OK);
	}

}
