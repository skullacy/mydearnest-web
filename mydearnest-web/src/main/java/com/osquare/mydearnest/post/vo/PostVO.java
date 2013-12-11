package com.osquare.mydearnest.post.vo;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class PostVO {

	private CommonsMultipartFile thumbnail;
	private Long folderId;
	private Long category;
	private String title;
	private String desc;

	public CommonsMultipartFile getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(CommonsMultipartFile thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
