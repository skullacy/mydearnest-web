package com.osquare.mydearnest.api.web;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.osquare.mydearnest.admin.service.AdminTagCateService;
import com.osquare.mydearnest.entity.Category;
import com.osquare.mydearnest.entity.TagCategory;
import com.osquare.mydearnest.post.service.CategoryService;

@Controller
@RequestMapping("/api/category")
public class APICategoryController {
	
	//deprecate
	@Autowired private CategoryService categoryService;
	
	@Autowired private AdminTagCateService adminTagCateService;

	//deprecate
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
	
	@RequestMapping("/search.ajax")
	public ResponseEntity<String> searchKeyword(Model model, HttpServletResponse response,
			@RequestParam(value = "keyword", required = false) String keyword) {
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type",	"application/json; charset=UTF-8");
		
		Collection<TagCategory> tagCategories = adminTagCateService.getMatchedTagCategories(keyword);
		
		JSONObject document = new JSONObject();
		try {
			if(tagCategories.size() > 0) {
				JSONArray array = new JSONArray();
				
				for(TagCategory category : tagCategories) {
					JSONObject object = new JSONObject();
					object.put("id", category.getId());
					object.put("title", category.getTitle());
					object.put("type", category.getType());
					array.add(object);
				}
				
				document.put("data", array);
				
			}
			document.put("success", true);
		}
		catch (Exception e) {
			document.put("success", false);
		}
		
		
		
		
		

		return new ResponseEntity<String>(document.toString(), responseHeaders, HttpStatus.OK);
	}

	
}
