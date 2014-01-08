package com.osquare.mydearnest.post.web;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.post.service.FileService;
import com.osquare.mydearnest.post.vo.ImageSourceFile;
import com.osquare.mydearnest.post.vo.ProfileImageQue;
import com.osquare.mydearnest.post.vo.UploadBean;

@Controller
@RequestMapping("/mdn-image/")
public class ImageController {
	
	@Autowired private FileService fileService;
	@Autowired private MessageSource messageSource;
	
	@RequestMapping("/get/{image_id}")
	public ModelAndView get(@PathVariable("image_id") long imageId, HttpServletResponse response) throws IOException {
		
		ImageSource imageSource = fileService.getImageSource(imageId);
		ImageSourceFile file = fileService.getSourceFile(imageSource);
		
		if (file != null && response != null) {
	        response.setHeader("Content-disposition", "inline;filename=" + imageId + ".jpg" );
	        response.setHeader("Content-Length", String.valueOf(file.getFileLength()));
			FileCopyUtils.copy(file.getInputStream(), response.getOutputStream());
		}
		
		return null;
	}

	@RequestMapping("/thumb/{image_id}")
	public ModelAndView thumb(@PathVariable("image_id") long imageId, 
			@RequestParam(value = "w", required = false) Long width, 
			@RequestParam(value = "h", required = false) Long height, 
			@RequestParam(value = "t", required = false) String type, 
			@RequestParam(value = "p", required = false) Long postId, 
			HttpServletResponse response) {
		
		if (width == null) width = 0L;
		if (height == null) height = 0L;
		if (type == null || type.isEmpty()) type = "ratio";
		
		ImageSource imageSource = fileService.getImageSource(imageId);
		ImageSourceFile file = fileService.getSourceFile(imageSource, width, height, type, postId);
		
		if (file != null && response != null) {
			try {
				
				response.setContentType("image/jpeg"); 
		        response.setHeader("Content-disposition", "inline;filename=" + imageId + ".jpg" );
		        response.setHeader("Content-Length", String.valueOf(file.getFileLength()));
				FileCopyUtils.copy(file.getInputStream(), response.getOutputStream());
			}
			catch(Exception ex) { }
		}
		
		return null;
	}

	@RequestMapping("/create.ajax")
	public ResponseEntity<String> create(UploadBean uploadBean, BindingResult result) throws IOException {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/json; charset=UTF-8");

		ProfileImageQue resultObject = new ProfileImageQue();
		if (uploadBean.getFiledata() == null || uploadBean.getFiledata().isEmpty())
		{
			System.out.println("file not found");
			//파일 찾을 수 없음.
			resultObject.setSuccess(false);
			resultObject.setMessage(messageSource.getMessage("error.notfound.file", null, Locale.getDefault()));
			return new ResponseEntity<String>(JSONObject.fromObject(result).toString(), responseHeaders, HttpStatus.OK);
		}

		ImageSource imageSource = fileService.createImageSourceForData(uploadBean.getFiledata());
		System.out.println(imageSource);

		resultObject.setImageId(imageSource.getId());
		return new ResponseEntity<String>(JSONObject.fromObject(resultObject).toString(), responseHeaders, HttpStatus.CREATED);
	}
}
