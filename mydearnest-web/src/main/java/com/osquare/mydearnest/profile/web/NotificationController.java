package com.osquare.mydearnest.profile.web;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.junglebird.webframe.vo.SignedDetails;
import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.Notification;
import com.osquare.mydearnest.profile.service.NotifyService;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

	@Autowired private AccountService accountService;
	@Autowired private MessageSource messageSource;
	@Autowired private NotifyService notificationService;
	@Autowired private VelocityEngine velocityEngine;

	@RequestMapping("raw.ajax")
	public ResponseEntity<String> index(ModelMap map, HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "last_id", required = false) Integer last_id,
			@RequestParam(value = "m", required = false) String mode) {

		response.setHeader("Cache-Control","no-cache");
		response.setHeader("Pragma","no-cache"); 
		response.setDateHeader("Expires",0);
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		SignedDetails principal = null;

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		if (mode == null) mode = "all";
		
		JSONObject result = new JSONObject();
		
		if (authentication.getPrincipal() instanceof SignedDetails) {
			
			principal = (SignedDetails) authentication.getPrincipal();
			
			response.setHeader("Cache-Control","no-cache");
			response.setHeader("Pragma","no-cache"); 
			response.setDateHeader("Expires",0);
			
			Account account = accountService.findAccountById(principal.getAccountId());
			result.put("history_count", notificationService.getNotifyHistoryCount(account));
			result.put("message_count", notificationService.getNotifyMessageCount(account));
			
			if (mode.equals("follow")) {

				Collection<Notification> items = notificationService.getNotificationsByLastIdAndFollower(last_id, account.getId());
				
				ModelMap subMap = new ModelMap();
				subMap.addAttribute("items", items);
				subMap.addAttribute("request", request);

				String htmlString = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "home/notifications.vm", "UTF-8", subMap);
				result.put("notify_wall", htmlString);
			}
			
		}


		if (last_id != null && last_id > 0 && !mode.equals("follow")) {

			Collection<Notification> items = notificationService.getNotificationsByLastId(last_id);
			
			ModelMap subMap = new ModelMap();
			subMap.addAttribute("items", items);
			subMap.addAttribute("request", request);

			String htmlString = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "home/notifications.vm", "UTF-8", subMap);
			result.put("notify_wall", htmlString);
		}
		
		return new ResponseEntity<String>(result.toString(), responseHeaders, HttpStatus.OK);
		
	}

	@RequestMapping("/all.ajax")
	public String all(ModelMap map, HttpServletResponse response) {
		
		response.setHeader("Cache-Control","no-cache");
		response.setHeader("Pragma","no-cache"); 
		response.setDateHeader("Expires",0);
		
		map.addAttribute("layout", "./shared/layout.blank.vm");
		
		Collection<Notification> items = notificationService.getNotificationsByLastId(-1);
		if (items != null) {
			map.addAttribute("items", items);
			map.addAttribute("no_data", (items.size() <= 0 && -1 <= 0));
		}
		
		return "home/notifications";
		
	}

	@RequestMapping("/follow.ajax")
	public String follower(ModelMap map, HttpServletResponse response) {
		
		response.setHeader("Cache-Control","no-cache");
		response.setHeader("Pragma","no-cache"); 
		response.setDateHeader("Expires",0);
		
		map.addAttribute("layout", "./shared/layout.blank.vm");

		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		SignedDetails principal = null;

		if (authentication.getPrincipal() instanceof SignedDetails) {
			principal = (SignedDetails) authentication.getPrincipal();
			Collection<Notification> items = notificationService.getNotificationsByLastIdAndFollower(-1, principal.getAccountId());
			if (items != null) {
				map.addAttribute("items", items);
				map.addAttribute("no_data", (items.size() <= 0 && -1 <= 0));
			}
		}
		
		return "home/notifications";
		
	}


}
