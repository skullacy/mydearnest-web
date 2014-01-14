package com.osquare.mydearnest.util.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.junglebird.webframe.common.PropertiesManager;
import com.junglebird.webframe.vo.SignedDetails;

public class RequestInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired private PropertiesManager pm;

	private List<String> excludePathes = new ArrayList<String>();
	private Map<String, String> excludeParams = new HashMap<String, String>();

	public List<String> getExcludePathes() {
		return excludePathes;
	}

	public void setExcludePathes(List<String> excludePathes) {
		this.excludePathes = excludePathes;
	}
	
	public Map<String, String> getExcludeParams() {
		return excludeParams;
	}

	public void setExcludeParams(Map<String, String> excludeParams) {
		this.excludeParams = excludeParams;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		request.setAttribute("https_uri", pm.get("https_url"));
		request.setAttribute("web_url", pm.get("web_url"));
		
		// 확장자 있으면 일단 PASS!
		
		if (request.getPathInfo().lastIndexOf(".ajax") >= 0) return super.preHandle(request, response, handler);
		if (request.getPathInfo().lastIndexOf(".fb") >= 0) return super.preHandle(request, response, handler);
		
		boolean isExclude = false;

		if (excludePathes != null) {
			for(String excludePath : excludePathes) {
				String mappingRegexp = StringUtils.replace(excludePath, "*", ".*");
				if (request.getPathInfo().matches(mappingRegexp)) {
					isExclude = true;
					break;
				}
			}
		}
		
		if (excludeParams != null) {
			for(Map.Entry<String, String> excludeParam : excludeParams.entrySet()) {
				String mappingRegexp = StringUtils.replace(excludeParam.getValue(), "*", ".*");
				if (request.getParameter(excludeParam.getKey()) != null &&
						request.getParameter(excludeParam.getKey()).matches(mappingRegexp)) {
					isExclude = true;
					break;
				}
			}
		}
		if (isExclude) return super.preHandle(request, response, handler);
		
		String context = request.getHeader("context");
		Authentication authAccount = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		
		if(authAccount.getPrincipal() instanceof SignedDetails)
		{
			request.setAttribute("user", (SignedDetails)authAccount.getPrincipal());
			request.setAttribute("user_id", ((SignedDetails)authAccount.getPrincipal()).getAccountId());
		}
		
		request.setCharacterEncoding("UTF-8");
		if(!request.getMethod().equals("GET")) 
			return super.preHandle(request, response, handler);
		
		if (context != null && context.contains("application/mxb-xhtml")) {
			request.setAttribute("layout", "shared/layout.blank.vm");
			return super.preHandle(request, response, handler); //프레임 씨워져서 왔으면 조건 제외
		}
		
		response.setStatus(301);
		response.setHeader("Location", request.getContextPath() + "/#" + request.getContextPath() + request.getPathInfo());
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Expires", "0");
		
		return false;
	}
}
