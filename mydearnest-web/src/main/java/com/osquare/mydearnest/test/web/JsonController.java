package com.osquare.mydearnest.test.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/json")
public class JsonController {

	@RequestMapping(value = "/test" , method = RequestMethod.GET)
	public final ModelAndView handlerRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		Map<String, List> map = new HashMap<String, List>();
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		map.put("list", list);
		modelAndView.addAllObjects(map);
		return modelAndView;
	}
	
	@RequestMapping(value = {"/admin/postList/{checkSum}/{page}/{order}"} , method = RequestMethod.GET)
	public final ModelAndView adminPostList(final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		Map<String, List> map = new HashMap<String, List>();
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		map.put("list", list);
		modelAndView.addAllObjects(map);
		return modelAndView;
	}
}
