package com.osquare.mydearnest.statics.web;

import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.osquare.mydearnest.entity.TestPost;
import com.osquare.mydearnest.entity.itf.TestRepository; 

@Controller
@RequestMapping("/test2")
public class TestController {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@RequestMapping("/chart")
	public String index(Model model, HttpServletResponse response) {
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		response.addHeader("mxb-menu_status", "1");
		
//		TestPost tp = new TestPost("김기준", 29);
//		mongoTemplate.save(tp);
		
		model.addAttribute("layout", "shared/layout.blank.vm");
		
		return "test/index";
	}
}
