package com.osquare.mydearnest.profile.vo;

import java.math.BigInteger;
import java.util.Date;

public class CommentVO {

	private long id;
	private long imageId;
	private String userId;
	private String name;
	private String title;
	private String text;
	private Date createdAt;
	private long count;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public long getCount() {
		return count;
	}

	public void setCount(BigInteger count) {
		this.count = count.longValue();
	}
	
	

}
