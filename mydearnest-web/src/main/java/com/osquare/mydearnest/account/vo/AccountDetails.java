package com.osquare.mydearnest.account.vo;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class AccountDetails {

	private long id;
	private String username;
	private String region;
	private String bio;
	private Long imageSourceId;
	private CommonsMultipartFile thumbnail;
	private String agree;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public Long getImageSourceId() {
		return imageSourceId;
	}

	public void setImageSourceId(Long imageSourceId) {
		this.imageSourceId = imageSourceId;
	}

	public CommonsMultipartFile getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(CommonsMultipartFile thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getAgree() {
		return agree;
	}

	public void setAgree(String agree) {
		this.agree = agree;
	}

}
