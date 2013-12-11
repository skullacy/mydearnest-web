package com.osquare.mydearnest.post.vo;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadBean {
	private String fileid;
	private CommonsMultipartFile filedata;

	public String getFileid() { return fileid; }
	public void setFileid(String fileid) { this.fileid = fileid; }
	
	public CommonsMultipartFile getFiledata() { return filedata; }
	public void setFiledata(CommonsMultipartFile filedata) { this.filedata = filedata; }

}
