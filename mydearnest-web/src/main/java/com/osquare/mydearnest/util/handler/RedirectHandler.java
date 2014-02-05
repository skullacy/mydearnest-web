package com.osquare.mydearnest.util.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.post.service.PostService;

public class RedirectHandler {
	
	@Autowired private PostService postService;
	
	public String getRedirectUrlForUploadProcess(String process, String redirectType, Post post, Account account) {
		
		//Submit 후 리다이렉트 지정되지 않은경우.
		if(redirectType == null) {
			
			//미완료된 포스트중 랜덤 호출.
			Post redirectPost = postService.getPostByRandom(0, process, account);
			
			if(redirectPost == null) {
				return "redirect:/admin";
			}
			else {
				return "redirect:/admin/write/"+ process +"/"+redirectPost.getId();
			}
			
		}
		else {
			return "redirect:/admin/write/" + redirectType + "/" +  post.getId();
		}
	}
}
