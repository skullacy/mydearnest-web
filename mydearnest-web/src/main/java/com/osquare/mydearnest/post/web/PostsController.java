package com.osquare.mydearnest.post.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookLink;
import org.springframework.social.facebook.connect.FacebookServiceProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.junglebird.webframe.common.PropertiesManager;
import com.junglebird.webframe.vo.SignedDetails;
import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.api.vo.ResultObject;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.entity.PostComment;
import com.osquare.mydearnest.entity.PostLove;
import com.osquare.mydearnest.post.service.PostService;

@Controller
public class PostsController {
	
	@Autowired private PostService postService;
	@Autowired private AccountService accountService;
	@Autowired private PropertiesManager pm;

	@RequestMapping("/index")
	public String index(Model model, HttpServletResponse response) {

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		response.addHeader("mxb-menu_status", "1");
		model.addAttribute("layout", "shared/layout.blank.vm");

		return "post/list";
	}

	@RequestMapping("/index/{c}")
	public String index(Model model, HttpServletResponse response, @PathVariable("c") long category) {

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		response.addHeader("mxb-menu_status", "1");
		model.addAttribute("layout", "shared/layout.blank.vm");
		model.addAttribute("category", category);

		return "post/list";
	}

	@RequestMapping("/index.ajax")
	public String index(Model model, HttpServletResponse response,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "c", required = false) Long category) {

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		if(page == null) page = 1;
		model.addAttribute("layout", "shared/layout.blank.vm");
		model.addAttribute("posts", postService.getPostsOrderByLatest(page, category));
		model.addAttribute("ref", "index");

		return "post/list.ajax";
	}


	@RequestMapping("/show/{post_id}")
	public String show(Model model, 
			@PathVariable("post_id") Long postId, 
			@RequestParam(value = "ref", required = false) String ref,
			HttpServletResponse response) {

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		response.addHeader("mxb-popup-width", "100%");
		model.addAttribute("layout", "shared/layout.blank.vm");
		
		if (ref == null) ref = "index";
		
		Post post = postService.getPostById(postId);
		model.addAttribute("post", post);
		model.addAttribute("refindUsers", postService.getPostRefindUsers(postId, 5));
		model.addAttribute("refindCount", postService.getPostRefindCount(postId));
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (authentication.getPrincipal() instanceof SignedDetails) {
			SignedDetails principal = (SignedDetails) authentication.getPrincipal();
			Account account = accountService.findAccountById(principal.getAccountId());
			postService.createPostView(account, post);
			model.addAttribute("love_me", postService.findPostLoveByAccount(account, post) != null);
		}
		
		return "post/show";
	}

	@RequestMapping("/show/{post_id}.fb")
	public String showFB(Model model, @PathVariable("post_id") Long postId, HttpServletResponse response) {

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		Post post = postService.getPostById(postId);
		model.addAttribute("post", post);		
		return "post/show.fb";
	}

	@RequestMapping("/show/{post_id}/love")
	public ResponseEntity<String> loveControl(@PathVariable("post_id") Long postId, HttpServletResponse response,
			@RequestParam(value = "mode", required = true) String mode) {

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return null;
		
		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		Account account = accountService.findAccountById(principal.getAccountId());

		ResultObject result = new ResultObject();
		
		PostLove postLove;
		if (mode.toLowerCase().equals("yes")) {
			Post post = postService.getPostById(postId);
			postLove = postService.createPostLove(account, postId);
			JSONObject jsonObject = JSONObject.fromObject(account.getSocialTokens());
			
			if (account.getFacebookId() != null && !account.getFacebookId().isEmpty()) {
				String url = "http://findfashion.kr/show/" + postId + ".fb";
				String postMessage = post.getDescription() + "를 좋아합니다.";
				
				FacebookServiceProvider facebookProvider = new FacebookServiceProvider(pm.get("social.facebook.appId"), pm.get("social.facebook.appSecret"));
				Facebook facebook = facebookProvider.getApi(jsonObject.getString("facebook_accessToken"));
				FacebookLink fbLink = new FacebookLink(url, post.getTitle(), "", post.getDescription());
				facebook.feedOperations().postLink(postMessage, fbLink);
			}

		}
		else {
			Post post = new Post();
			post.setId(postId);
			
			postLove = postService.findPostLoveByAccount(account, post);
			postService.deletePostLove(postLove);
		}
		
		result.setSuccess(postService != null);
		return new ResponseEntity<String>(JSONObject.fromObject(result).toString(), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping("/show/{post_id}/comments.ajax")
	public String comments(Model model, HttpServletResponse response, 
			@PathVariable("post_id") Long postId,
			@RequestParam(value = "page", required = false) Integer page) {

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
				
		Post post = new Post();
		post.setId(postId);

		if(page == null) page = 1;
		Collection<PostComment> comments = postService.getCommentsByPost(post, page);
		model.addAttribute("layout", "shared/layout.blank.vm");
		model.addAttribute("comments", comments);

		return "post/show_comments";
	}

	@RequestMapping("/post/{post_id}/delete")
	public ResponseEntity<String> delete(Model model, HttpServletResponse response, 
			@PathVariable("post_id") Long postId, 
			@RequestParam(value = "drawer_id", required = false) Long drawerId, 
			@RequestParam(value = "mode", required = false) String editMode) {

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		Post post = postService.removePostByMode(postId, editMode, drawerId);
		
		ResultObject result = new ResultObject();
		result.setSuccess(post != null);
		return new ResponseEntity<String>(JSONObject.fromObject(result).toString(), responseHeaders, result.getSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT);
	}
	
	@RequestMapping("/post/comment.ajax")
	public String comment(Model model, HttpServletResponse response, @RequestParam("text") String text, @RequestParam("post_id") long postId) {
		

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";
		
		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		Account account = accountService.findAccountById(principal.getAccountId());
		
		Post post = new Post();
		post.setId(postId);

		PostComment postComment = postService.createPostComment(post, account, text);
		
		List<PostComment> comments = new ArrayList<PostComment>();
		comments.add(postComment);
		
		model.addAttribute("layout", "shared/layout.blank.vm");
		model.addAttribute("comments", comments);

		return "post/show_comments";
	}

}
