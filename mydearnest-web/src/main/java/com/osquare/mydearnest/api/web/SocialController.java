package com.osquare.mydearnest.api.web;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.junglebird.webframe.CookieHelper;
import com.junglebird.webframe.common.PropertiesManager;
import com.junglebird.webframe.vo.HttpConnectionResult;
import com.junglebird.webframe.vo.SignedDetails;
import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.api.helper.HttpConnection;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.post.service.FileService;

@Controller
@RequestMapping("/social")
public class SocialController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private FileService fileService;
	@Autowired
	private PropertiesManager pm;

	@RequestMapping("/facebook")
	public void facebook(HttpServletResponse response,
			HttpServletRequest request, HttpSession session,
			@RequestParam("mode") String mode,
			@RequestParam(value = "reUrl", required = false) String reUrl)
			throws UnsupportedEncodingException {

		String prefix = "www";
		String display = "popup";

		StringBuilder sb = new StringBuilder();
		sb.append("https://" + prefix
				+ ".facebook.com/dialog/permissions.request");
		sb.append("?app_id=" + pm.get("social.facebook.appId"));
		sb.append("&next="
				+ URLEncoder.encode(request.getRequestURL() + "/result",
						"UTF-8"));
		sb.append("&display="
				+ display
				+ "&response_type=code&perms=email,user_birthday,publish_stream,publish_actions,offline_access,read_friendlists&fbconnect=1");

		session.setAttribute("ff_social_mode", mode);
		session.setAttribute("ff_reUrl", reUrl);

		response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
		response.setHeader("location", sb.toString());

		response.setHeader("Cache-Control",
				"max-age=0, private, must-revalidate");
		response.setHeader("Pragma", "no-cache");

	}

	@RequestMapping("/facebook/result")
	public String facebookResult(ModelMap model, HttpServletResponse response,
			HttpServletRequest request, HttpSession session)
			throws UnsupportedEncodingException {

		response.setHeader("Cache-Control",
				"max-age=0, private, must-revalidate");
		response.setHeader("Pragma", "no-cache");

		URI url = URI.create(request.getRequestURL().toString());

		StringBuilder sb = new StringBuilder();
		sb.append("https://graph.facebook.com/oauth/access_token");
		sb.append("?client_id=" + pm.get("social.facebook.appId"));
		sb.append("&client_secret=" + pm.get("social.facebook.appSecret"));
		sb.append("&redirect_uri="
				+ URLEncoder.encode("http://" + url.getAuthority()
						+ "/social/facebook/result", "UTF-8"));
		sb.append("&code=" + request.getParameter("code"));

		HttpConnectionResult result = HttpConnection.get(sb.toString());
		if (!result.isSuccess()) {
			model.addAttribute("failure", true);
			model.addAttribute("message", "서버에 연결할 수 없습니다.\\n잠시후 다시 시도해주세요.");
		} else {

			String meInfo = "https://graph.facebook.com/me?"
					+ result.getResult();
			HttpConnectionResult info = HttpConnection.get(meInfo);
			JSONObject object = JSONObject.fromObject(info.getResult());

			String accessToken = result.getResult().substring(
					result.getResult().indexOf("=") + 1,
					result.getResult().indexOf("&"));
			String mode = session.getAttribute("ff_social_mode").toString();
			model.addAttribute("mode", mode);

			Account accountOwner = accountService.findAccountBySocial(
					"facebook", object.getString("id"));
			if (accountOwner != null) {
				JSONObject json = JSONObject.fromObject(accountOwner
						.getSocialTokens());
				json.put("facebook_accessToken", accessToken);
				accountService.updateSocialTokens(accountOwner.getId(),
						json.toString());
			}

			if (mode.equals("modify")) {
				if (accountOwner != null) {
					model.addAttribute("failure", true);
					model.addAttribute("message", "이미 다른사용자에게 연결되어있습니다.");
				} else {
					Authentication authentication = ((SecurityContext) SecurityContextHolder
							.getContext()).getAuthentication();
					SignedDetails principal = null;

					if (authentication.getPrincipal() instanceof SignedDetails) {
						principal = (SignedDetails) authentication
								.getPrincipal();

						Account me = accountService.findAccountById(principal
								.getAccountId());

						JSONObject data = JSONObject.fromObject(me
								.getSocialTokens());
						data.put("facebook_accessToken",
								String.valueOf(accessToken));
						data.put("facebook_secretToken", "");

						me.setFacebookId(object.getString("id"));
						me.setSocialTokens(data.toString());

						accountService.updateAccount(me);
					}

					model.addAttribute("type", "facebook");
					model.addAttribute("udata", object.getString("id"));
					model.addAttribute("data1", accessToken);
					model.addAttribute("data2", "");
					model.addAttribute(
							"url",
							"http://www.facebook.com/profile.php?id="
									+ object.getString("id"));
				}
			} else if (mode.substring(0, 5).equals("login")) {
				Account account = accountService.findAccountBySocial(
						"facebook", object.getString("id"));
				if (account == null) {

					String query = "SELECT pic_big FROM user WHERE uid='"
							+ object.getString("id") + "'";
					HttpConnectionResult fqlHttp = HttpConnection
							.get("https://graph.facebook.com/fql?q="
									+ URLEncoder.encode(query, "UTF-8")
									+ "&accessToken=" + accessToken);
					JSONObject fqlResult = JSONObject.fromObject(fqlHttp
							.getResult());
					CookieHelper cookie = CookieHelper.sync(request, response);

					JSONObject data = new JSONObject();
					data.put("type", "facebook");
					if (object.containsKey("name"))
						data.put("name", object.getString("name"));
					if (object.containsKey("email"))
						data.put("email", object.getString("email"));
					if (!object.getJSONObject("location").isNullObject())
						data.put("country", object.getJSONObject("location")
								.getString("name"));

					data.put("type", "facebook");
					data.put("udata", object.getString("id"));
					data.put("data1", accessToken);
					data.put("data2", "");

					String imageUrl = fqlResult.getJSONArray("data")
							.getJSONObject(0).getString("pic_big");
					if (imageUrl != null && !imageUrl.isEmpty())
						data.put("imageUrl", imageUrl);

					cookie.setCookie("social_temp", data.toString());

					if (mode.equals("login"))
						model.addAttribute("redirect_uri",
								"#/join?social=facebook");
				} else {
					UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
							account.getEmail(), account.getPassword());
					Authentication authentication = authenticationManager
							.authenticate(authRequest);

					SecurityContext securityContext = new SecurityContextImpl();
					securityContext.setAuthentication(authentication);
					SecurityContextHolder.setContext(securityContext);
					request.getSession()
							.setAttribute(
									HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
									securityContext);

					accountService.updateSignInfo(account,
							request.getRemoteAddr());

					Object reUrl = session.getAttribute("ff_reUrl");
					if (reUrl != null && !reUrl.toString().isEmpty())
						model.addAttribute("redirect_uri", reUrl);
				}
				//
			} else if (mode.equals("join")) {
				if (accountOwner != null) {
					model.addAttribute("failure", true);
					model.addAttribute("message", "이미 다른사용자에게 연결되어있습니다.");
				} else {
					String query = "SELECT pic_big FROM user WHERE uid='"
							+ object.getString("id") + "'";
					HttpConnectionResult fqlHttp = HttpConnection
							.get("https://graph.facebook.com/fql?q="
									+ URLEncoder.encode(query, "UTF-8")
									+ "&accessToken=" + accessToken);
					JSONObject fqlResult = JSONObject.fromObject(fqlHttp
							.getResult());

					model.addAttribute("type", "facebook");
					if (object.containsKey("name")) model.addAttribute("name", object.getString("name"));
					if (object.containsKey("email")) model.addAttribute("email", object.getString("email"));
					if (!object.getJSONObject("location").isNullObject())
						model.addAttribute("country", object.getJSONObject("location").getString( "name"));

					model.addAttribute("type", "facebook");
					model.addAttribute("udata", object.getString("id"));
					model.addAttribute("data1", accessToken);
					model.addAttribute("data2", "");

					String imageUrl = fqlResult.getJSONArray("data")
							.getJSONObject(0).getString("pic_big");
					if (imageUrl != null && !imageUrl.isEmpty()) {
						ImageSource imageSource = fileService
								.createImageSourceForURL(imageUrl);
						model.addAttribute("imageSourceId", imageSource.getId());
					}
				}
			}
			return "api/result";

		}

		return "api/result";
	}

}
