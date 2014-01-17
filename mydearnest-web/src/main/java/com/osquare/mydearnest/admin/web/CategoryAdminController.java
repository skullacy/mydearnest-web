package com.osquare.mydearnest.admin.web;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.osquare.mydearnest.admin.service.AdminAccountService;
import com.osquare.mydearnest.admin.service.AdminTagCateService;
import com.osquare.mydearnest.entity.TagCategory;
import com.osquare.mydearnest.post.vo.TagCategoryVO;

@Controller
@RequestMapping("/admin/category")
public class CategoryAdminController {

	@Autowired private AdminAccountService adminAccountService;
	@Autowired private AdminTagCateService adminTagCateService;
	
	
	//카테고리 리스트 출력용 컨트롤
	//권한 : 관리자(ROLE_ADMIN)
	@RequestMapping("/list")
	public String index(ModelMap model, 
			@RequestParam(value="type", required = false) String type,
			@RequestParam(value="page", required = false) Integer page,
			@RequestParam(value="order", required = false) String order) {
		
		if (type == null) type = "";
		if (page == null) page = 1;
		if (order == null) order = "id";
		
		model.addAttribute("order", order);
		model.addAttribute("page", page);
		model.addAttribute("pages", Math.ceil((double)adminTagCateService.sizeOfTag(type) / 20));
		model.addAttribute("items", adminTagCateService.findTag(page, type, order));
		model.addAttribute("page_on", "post");
		
		return "admin/tag_list";
	}
	
	
	//카테고리 입력 화면
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String insertTag(Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("inputType", "create");
		return "admin/tag_create";
	}
	
	//카테고리 입력 처리
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String insertTagSubmit(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("command") TagCategoryVO tagCateVO,
			@RequestParam(value = "submitType", required = false) String submitType,
			@RequestParam(value = "submitId", required = false) Long tagCateId,
			BindingResult result) {
		
		model.addAttribute("success", false);
		
		if(result.hasErrors()) {
			System.out.println(result.getAllErrors());
			model.addAttribute("errors", result.getAllErrors());
		}
		else {
			
			TagCategory tagCate;
			if("update".equals(submitType)) {
				tagCate = adminTagCateService.updateTagCategory(tagCateVO, tagCateId);	
			}
			else {
				tagCate = adminTagCateService.createTagCategory(tagCateVO);	
			}
			
			if(tagCate == null) {
				model.addAttribute("success", false);
			}
			else {
				model.addAttribute("success", true);
				//성공했을 시 리다이렉트 설정해주기 (return에 있는 view파일에서 redirect_uri로 넘겨준다.) 아직 설정하지 않았으므로 다른방법 찾아도 됨.
//				model.addAttribute("redirect_uri", request.getContextPath() + "slagjsdlfasjf");
			}
		}
		
		return "redirect:/admin/category/list";
	}
	
	
	//카테고리 수정 화면
	@RequestMapping(value = "/update/{cateId}", method = RequestMethod.GET)
	public String updateTag(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("cateId") Long cateId) {
		
		TagCategory tagCate = adminTagCateService.getTagInfo(cateId);
		
		model.addAttribute("inputType", "update");
		model.addAttribute("tagInfo", tagCate);
		
		return "admin/tag_create";
	}
	
	//카테고리 삭제 처리
	@RequestMapping(value = "/remove/{cateId}", method = RequestMethod.GET)
	public ResponseEntity<String> removeTag(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("cateId") Long cateId) {
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type",	"application/json; charset=UTF-8");
		
		JSONObject document = new JSONObject();
		
		Boolean result = adminTagCateService.deleteTagCategory(cateId);
		
		document.put("success", result);

		return new ResponseEntity<String>(document.toString(), responseHeaders, HttpStatus.OK);
		
	}


	
	/**
	 * @brief
	 * 기존 포스트 삭제용 메소드, 리팩토링 예
	 * @param model
	 * @param id
	 * @param redirectUri
	 * @return
	 * @deprecated
	 */
	@RequestMapping("/remove")
	public String index(ModelMap model, 
			@RequestParam(value="id", required = true) long id,
			@RequestParam(value="redirectUri", required = false) String redirectUri) {
		
		
//		Post post = adminPostService.disabledPost(id);
//		model.addAttribute("success", post != null);
		model.addAttribute("redirectUri", redirectUri);
		
		return "admin/post_remove_ok";
	}
	

}
