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

import com.osquare.mydearnest.entity.Category;
import com.osquare.mydearnest.post.service.CategoryService;

@Controller
@RequestMapping("/api/category")
public class APICategoryController {
	
	@Autowired private CategoryService categoryService;

	@RequestMapping("/list.json")
	public ResponseEntity<String> list() {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type",	"application/json; charset=UTF-8");
		
		Collection<Category> categories = categoryService.getRootCategories();

		JSONObject document = new JSONObject();
		JSONArray array = new JSONArray();
		
		for(Category category : categories) {
			JSONObject object = new JSONObject();
			object.put("id", category.getId());
			object.put("name", category.getContent());
			array.add(object);
		}
		
		document.put("success", categories != null);
		document.put("data", array);

		return new ResponseEntity<String>(document.toString(), responseHeaders, HttpStatus.OK);
	}

	
}
