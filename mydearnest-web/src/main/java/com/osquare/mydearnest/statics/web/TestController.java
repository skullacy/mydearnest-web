package com.osquare.mydearnest.statics.web;

import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.osquare.mydearnest.entity.TestPost;
import com.osquare.mydearnest.entity.itf.TestRepository; 

@Controller
@RequestMapping("/test22")
public class TestController {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@RequestMapping("/index")
	public String index(HttpServletResponse response) {
		
		TestPost tp = new TestPost("김기준", 29);
		mongoTemplate.save(tp);
		
		ObjectId id = new ObjectId(tp.getId());
		
		
		return "test/index";
	}
}
