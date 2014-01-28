package com.osquare.mydearnest.test.web;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.junglebird.webframe.common.PropertiesManager;
import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;
import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.util.image.dominant.DominantColor;
import com.osquare.mydearnest.util.image.dominant.DominantColors;

@Controller
@RequestMapping("/test")
public class TestController {

	//@Autowired private PostService postService;
	//@Autowired private AccountService accountService;
	//@Autowired private AdminTagCateService adminTagCateService;
	//@Autowired private AdminPostService adminPostService;
	@Resource private SessionFactory sessionFactory;
	@Autowired private PropertiesManager pm;
	
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

// post user grade
/*	@RequestMapping(value = "/grade/{postId}", method = RequestMethod.GET)
	public String insertPostUserGrade(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("postId") long postId) {
		
		// POST 공통처리 파트
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";

		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		Account account = accountService.findAccountById(principal.getAccountId());
		
		Post post = postService.getPostById(postId);
		model.addAttribute("post", post);

		// Grade 가져오는 파트
		Collection<PostGrade> postGrades = postService.getPostGradeByPost(post);
		Collection<PostUserGrade> postUserGrades = postService.getPostUserGradeByPost(post);
		
		PostUserGrade myPostUserGrade = null;
		
		if (postUserGrades != null) {
			Iterator<PostUserGrade> iterator = postUserGrades.iterator();
			
			while (iterator.hasNext()) {
				myPostUserGrade = iterator.next();
			
				if (myPostUserGrade.getAccount().getId() == account.getId()) break;
			}
		}
		
		model.addAttribute("postGrades", postGrades);
		model.addAttribute("postUserGrades", postUserGrades);
		model.addAttribute("myPostUserGrade", myPostUserGrade);

		model.addAttribute("layout", "./shared/layout.admin.vm");
		
		return "test/grade";
	}*/
	
// 미작성 파트
/*	@RequestMapping(value = "/grade/{postId}", method = RequestMethod.POST)
	public String insertPostUserGrade(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable("postId") Long postId,
			@RequestBody List<Integer> postUserGradeList,
			BindingResult result) {
		
		model.addAttribute("success", false);
		
		Authentication authentication = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (!(authentication.getPrincipal() instanceof SignedDetails)) return "shared/required.login";

		SignedDetails principal = (SignedDetails) authentication.getPrincipal();
		Account account = accountService.findAccountById(principal.getAccountId());
		
		Post post = postService.getPostById(postId);

		model.addAttribute("account", account);

		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			model.addAttribute("errors", result.getAllErrors());
		}
		else {
			int listSize = postUserGradeList.size();
			for (int i = 0; i < listSize; i++) {
				postUserGradeList.get(0)
			}
			
			PostUserGrade postUserGrade = postService.updatePostUserGrade(post, account, postUserGrade1);
			
			if (postUserGrade == null) {
				model.addAttribute("success", false);
			}
			else {
				model.addAttribute("success", true);
			}
		}

		return "redirect:/grade/" +  post.getId();
	}*/

	@RequestMapping(value = "/updateAvgColor" , method = RequestMethod.GET)
	public void updateAvgColor(Model model, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("========= start updateAvgColor() ============================");
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		List<Long> imageIdList =  getImageIdList();
		
		Iterator<Long> iterator = imageIdList.iterator();
		
		while (iterator.hasNext()) {
			updateAvgColor(iterator.next());			
		}
		
		System.out.println("========= end of updateAvgColor() ============================");
	}
	
	public List<Long> getImageIdList() {
		List<Post> postList = null; 
		List<Long> imageIdList = new ArrayList<Long>();
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			Criteria criteria = session.createCriteria(Post.class);
			
			postList = criteria.list();
			
			Iterator<Post> iterator = postList.iterator();
			
			while (iterator.hasNext()) {
				imageIdList.add(iterator.next().getImageSource().getId());
			}
			
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return imageIdList;
	}
	
	public void updateAvgColor(long imageId) {
		TestController testController = new TestController();
		StringBuilder builder = new StringBuilder();
		ImageSource imageSource = null;
		
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			Criteria criteria = session.createCriteria(ImageSource.class).add(Restrictions.eq("id", imageId));
			imageSource = (ImageSource) criteria.uniqueResult();
			
			builder.append(pm.get("amazon.fs.serverUrl")).append("/").append(imageSource.getStoragePath())
			.append("/").append(imageSource.getId()).append("/source");
			
			System.out.println("img URL: " + builder);
			
			imageSource.setAveColor(testController.getAveColor(new URL(builder.toString())));
			session.update(imageSource);

			session.getTransaction().commit();
			
		} catch(Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	public String getAveColor(URL img) {
		String rgbHex = null;
		
		try {
			ResampleOp resampleOp = new ResampleOp(3, 3);
			resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal);
			BufferedImage tdestImg = resampleOp.filter(ImageIO.read(img), null);
			
			int red = 0;
			int green = 0;
			int blue = 0;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					int rgbColor = tdestImg.getRGB(i, j);
					Color color = new Color(rgbColor);
					red += color.getRed();
					green += color.getGreen();
					blue += color.getBlue();
				}
			}
			
	        rgbHex = String.format("#%02x%02x%02x", red/9, green/9, blue/9);
		}
		catch(Exception e) {
			e.printStackTrace();
			
			rgbHex = "#FFFFFF";
		}
		
		return rgbHex;
        
	}
}
