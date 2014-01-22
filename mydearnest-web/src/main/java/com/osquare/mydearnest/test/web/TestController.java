package com.osquare.mydearnest.test.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.osquare.mydearnest.util.image.dominant.DominantColor;
import com.osquare.mydearnest.util.image.dominant.DominantColors;

@Controller
@RequestMapping("/test")
public class TestController {
	
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
	
}
