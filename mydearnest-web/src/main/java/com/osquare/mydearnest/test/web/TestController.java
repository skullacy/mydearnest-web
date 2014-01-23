package com.osquare.mydearnest.test.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.post.service.PostService;
import com.osquare.mydearnest.util.image.dominant.DominantColor;
import com.osquare.mydearnest.util.image.dominant.DominantColors;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@Autowired private PostService postService;
	
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
	
	@RequestMapping("/thumbtest/{postId}")
	public String thumbTest(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("postId") long postId) {
		
		
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		//포스트 정보 가져오기
		Post post = postService.getPostById(postId);
		
		model.addAttribute("post", post);
		
		
		return "test/thumbtest";
	}
	
	
}
