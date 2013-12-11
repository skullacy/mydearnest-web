package com.osquare.mydearnest.profile.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;







import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.junglebird.webframe.vo.SignedDetails;
import com.osquare.mydearnest.account.service.AccountService;
import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.Folder;
import com.osquare.mydearnest.post.service.FileService;
import com.osquare.mydearnest.post.service.PostService;
import com.osquare.mydearnest.profile.vo.AccountSummary;
import com.osquare.mydearnest.profile.vo.FolderItem;

@Controller
@RequestMapping(value = "/profile/{account_name}", method = RequestMethod.GET)
public class ProfileController {

	@Autowired private AccountService accountService;
	@Autowired private MessageSource messageSource;
	@Autowired private PostService postService;
	@Autowired private FileService fileService;
	
	private Account setUser(ModelMap model, long accountId, HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		SignedDetails principal = null;

		if (authentication.getPrincipal() instanceof SignedDetails)
			principal = (SignedDetails) authentication.getPrincipal();

		AccountSummary owner = accountService.findAccountSummaryById(accountId, (principal != null ? principal.getAccountId() : -1));
		if (owner != null) {
			model.addAttribute("user", owner.getAccount());
			if (principal != null) model.addAttribute("profile_owner", principal.getAccountId().equals(owner.getAccount().getId()));
			return owner.getAccount();
		}
		else
			return null;
	}
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(ModelMap model, @PathVariable("account_name") Long accountId, HttpServletRequest request, HttpServletResponse response)
	{	
		if (!request.getRequestURI().substring(request.getRequestURI().length() - 1).equals("/")) {
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.setHeader("location", request.getRequestURI() + "/");
			return null;
		}
		this.setUser(model, accountId, request, response);
		model.addAttribute("snb_code", "index");
		return "profile/index";
	}
	
	@RequestMapping(value="/index.ajax", method=RequestMethod.POST)
	public String index_ajax(ModelMap model, @PathVariable("account_name") Long accountId,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "page", required = false) Integer page)
	{
		if (page == null) page = 1;
		
		Account account = this.setUser(model, accountId, request, response);
		System.out.println(account);
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		SignedDetails principal = null;

		if (authentication.getPrincipal() instanceof SignedDetails) {
			principal = (SignedDetails) authentication.getPrincipal();
			model.addAttribute("owner_xid", principal.getAccountId());
		}
		
		model.addAttribute("deletable", true);
		model.addAttribute("edit_mode", "post");
		
		model.addAttribute("layout", "./shared/layout.blank.vm");
		model.addAttribute("posts", postService.getPostsByAccount(account, page));
		return "post/list.ajax";
	}
	

	@RequestMapping(value="/drawer", method=RequestMethod.GET)
	public String drawer(ModelMap model, @PathVariable("account_name") Long accountId, HttpServletRequest request, HttpServletResponse response)
	{	
		if (!request.getRequestURI().substring(request.getRequestURI().length() - 1).equals("/")) {
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.setHeader("location", request.getRequestURI() + "/");
			return null;
		}
		
		Account account = this.setUser(model, accountId, request, response);
		List<FolderItem> items = new ArrayList<FolderItem>();
		for(Folder folder : postService.getUserFolders(account)) {
			FolderItem folderItem = new FolderItem();
			folderItem.setFolder(folder);
			folderItem.setImageSources(fileService.getImageSourceByFolder(folder, 6));
			items.add(folderItem);
		}
		
		model.addAttribute("folderItems", items);
		model.addAttribute("snb_code", "drawer");
		return "profile/drawer";
	}

	@RequestMapping(value="/drawer/{folder_id}", method=RequestMethod.GET)
	public String index(ModelMap model, @PathVariable("account_name") Long accountId,
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("folder_id") Long folder_id)
	{
		Account account = this.setUser(model, accountId, request, response);
		
		FolderItem currentFolder = new FolderItem();
		currentFolder.setFolder(postService.getFolder(folder_id));
		currentFolder.setImageSources(fileService.getImageSourceByFolder(currentFolder.getFolder(), 9));
		model.addAttribute("currentFolder", currentFolder);
		
		List<FolderItem> items = new ArrayList<FolderItem>();
		for(Folder folder : postService.getUserFolders(account, folder_id)) {
			FolderItem folderItem = new FolderItem();
			folderItem.setFolder(folder);
			folderItem.setImageSources(fileService.getImageSourceByFolder(folder, 6));
			items.add(folderItem);
		}

		model.addAttribute("account", account);
		model.addAttribute("folderItems", items);
		model.addAttribute("folder_id", folder_id);
		model.addAttribute("snb_code", "drawer");
		return "profile/folder_image";
	}
	
	@RequestMapping(value="/drawer/{folder_id}.ajax", method=RequestMethod.POST)
	public String folder_ajax(ModelMap model, @PathVariable("account_name") String account_name,
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("folder_id") Long folder_id,
			@RequestParam(value = "page", required = false) Integer page)
	{
		Folder folder = new Folder();
		folder.setId(folder_id);
		
		if (page == null) page = 1;
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		SignedDetails principal = null;

		if (authentication.getPrincipal() instanceof SignedDetails) {
			principal = (SignedDetails) authentication.getPrincipal();
			model.addAttribute("owner_xid", principal.getAccountId());
		}

		model.addAttribute("deletable", true);
		model.addAttribute("edit_mode", "folder");
		model.addAttribute("drawer_id", folder_id);
		
		model.addAttribute("layout", "./shared/layout.blank.vm");
		model.addAttribute("posts", postService.getPostsByFolder(folder, page));
		return "post/list.ajax";
	}

	@RequestMapping(value="/love", method=RequestMethod.GET)
	public String love(ModelMap model, @PathVariable("account_name") Long accountId, HttpServletRequest request, HttpServletResponse response)
	{	

		if (!request.getRequestURI().substring(request.getRequestURI().length() - 1).equals("/")) {
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.setHeader("location", request.getRequestURI() + "/");
			return null;
		}
		
		this.setUser(model, accountId, request, response);
		model.addAttribute("snb_code", "love");
		return "profile/love";
	}

	@RequestMapping(value="/love.ajax", method=RequestMethod.POST)
	public String love_ajax(ModelMap model, @PathVariable("account_name") Long accountId,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "page", required = false) Integer page)
	{
		if (page == null) page = 1;
		
		Account account = this.setUser(model, accountId, request, response);
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		SignedDetails principal = null;

		if (authentication.getPrincipal() instanceof SignedDetails) {
			principal = (SignedDetails) authentication.getPrincipal();
			model.addAttribute("owner_xid", principal.getAccountId());
		}

		model.addAttribute("deletable", true);
		model.addAttribute("edit_mode", "love");
		
		model.addAttribute("layout", "./shared/layout.blank.vm");
		model.addAttribute("posts", postService.getPostsByLove(account, page));
		return "post/list.ajax";
	}
	
	
	@RequestMapping(value="/folder/delete/{folder_id}.ajax", method=RequestMethod.POST)
	public void folder_delete(ModelMap model, @PathVariable("account_name") String account_name,
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("folder_id") Long folder_id) throws Exception
	{
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		SignedDetails principal = (SignedDetails)authentication.getPrincipal();
		postService.removeFolder(folder_id, principal.getAccountId());
	}

}
