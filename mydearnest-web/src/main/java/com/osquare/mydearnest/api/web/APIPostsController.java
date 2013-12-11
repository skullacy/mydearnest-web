package com.osquare.mydearnest.api.web;

import java.util.Collection;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.post.service.PostService;

@Controller
@RequestMapping("/api/posts")
public class APIPostsController {

	@Autowired private PostService postService;

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
			object.put("title", post.getTitle());
			object.put("image", post.getImageSource().getId());
			array.add(object);
		}
		
		document.put("success", posts != null);
		document.put("data", array);

		return new ResponseEntity<String>(document.toString(), responseHeaders, HttpStatus.OK);
	}

}
